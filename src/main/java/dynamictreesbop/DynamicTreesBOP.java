package dynamictreesbop;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeSpeciesSelector;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.biome.IExtendedBiome;
import biomesoplenty.api.generation.BOPGeneratorBase;
import biomesoplenty.api.generation.IGenerator;
import biomesoplenty.common.biome.BOPBiome;
import biomesoplenty.common.biome.vanilla.ExtendedBiomeWrapper;
import biomesoplenty.common.init.ModBiomes;
import biomesoplenty.common.world.generator.GeneratorWeighted;
import biomesoplenty.common.world.generator.tree.GeneratorTreeBase;
import dynamictreesbop.blocks.BlockDynamicLeavesDTBOP;
import dynamictreesbop.proxy.CommonProxy;
import dynamictreesbop.worldgen.BiomeDensityProvider;
import dynamictreesbop.worldgen.BiomeSpeciesSelector;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=DynamicTreesBOP.MODID, name = DynamicTreesBOP.NAME, version = DynamicTreesBOP.VERSION, dependencies = DynamicTreesBOP.DEPENDENCIES)
public class DynamicTreesBOP {
	
	public static final String MODID = "dynamictreesbop";
	public static final String NAME = "Dynamic Trees BOP";
	public static final String VERSION = "1.0";
	public static final String DEPENDENCIES = "required-after:dynamictrees;after:biomesoplenty";
	
	@Mod.Instance
	public static DynamicTreesBOP instance;
	
	@SidedProxy(clientSide = "dynamictreesbop.proxy.ClientProxy", serverSide = "dynamictreesbop.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		registerBiomeHandlers();
		
		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		proxy.postInit();
		
		//Set<String> trees = new HashSet<String>();
		for (Biome b : ModBiomes.presentBiomes) {
			if (!(b instanceof BOPBiome)) continue;
			BOPBiome biome = (BOPBiome) b;
			
			IGenerator treeGen = biome.getGenerator("trees");
			if (treeGen != null && treeGen instanceof GeneratorWeighted) {
				GeneratorWeighted gen = (GeneratorWeighted) treeGen;
				
				Field genField = gen.getClass().getDeclaredField("generators");
				genField.setAccessible(true);
				//((HashMap<String, IGenerator>) genField.get(gen)).forEach((name, generator) -> {System.out.println(biome.getBiomeName() + ": " + name);});
				//((HashMap<String, IGenerator>) genField.get(gen)).forEach((name, generator) -> {trees.add(name);});
			} else if (treeGen != null && treeGen instanceof GeneratorTreeBase) {
				String name = biome.getBiomeName().toLowerCase().replace(' ', '_') + "_" + "tree";
				
				//System.out.println(biome.getBiomeName() + ": " + name);
				//trees.add(name);
			}
		}
		//trees.forEach(tree -> {System.out.println(tree);});
		
	}
	
	public void registerBiomeHandlers() {
		
		if(WorldGenRegistry.isWorldGenEnabled()) {
			IBiomeSpeciesSelector biomeSpeciesSelector = new BiomeSpeciesSelector();
			WorldGenRegistry.registerBiomeTreeSelector(biomeSpeciesSelector);
			WorldGenRegistry.registerBiomeDensityProvider(new BiomeDensityProvider());
			
			biomeSpeciesSelector.init();
		}
		
	}
	
	public static BlockDynamicLeaves getLeavesBlockForSequence(String modid, int seq) {
		int key = seq / 4;
		String regname = "leaves" + key;
		
		BlockDynamicLeaves leaves = null;
		
		try {
			Method getLeavesMapForModId = TreeHelper.class.getDeclaredMethod("getLeavesMapForModId", String.class);
			getLeavesMapForModId.setAccessible(true);
			HashMap<Integer, BlockDynamicLeaves> modLeaves = (HashMap<Integer, BlockDynamicLeaves>) getLeavesMapForModId.invoke(null, modid);
			leaves = modLeaves.computeIfAbsent(key, k -> (BlockDynamicLeaves)new BlockDynamicLeavesDTBOP().setRegistryName(modid, regname).setUnlocalizedName(regname));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
		return leaves;
		//return getLeavesMapForModId(modid).computeIfAbsent(key, k -> (BlockDynamicLeaves)new BlockDynamicLeaves().setRegistryName(modid, regname).setUnlocalizedName(regname));
	}
	
}
