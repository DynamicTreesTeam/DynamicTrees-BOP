package dtcompat;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ferreusveritas.dynamictrees.api.WorldGenRegistry;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.biome.IExtendedBiome;
import biomesoplenty.api.generation.BOPGeneratorBase;
import biomesoplenty.api.generation.IGenerator;
import biomesoplenty.common.biome.BOPBiome;
import biomesoplenty.common.biome.vanilla.ExtendedBiomeWrapper;
import biomesoplenty.common.init.ModBiomes;
import biomesoplenty.common.world.generator.GeneratorWeighted;
import biomesoplenty.common.world.generator.tree.GeneratorTreeBase;
import dtcompat.proxy.CommonProxy;
import dtcompat.worldgen.BiomeDensityProvider;
import dtcompat.worldgen.BiomeSpeciesSelector;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=DynamicTreesCompat.MODID, name = DynamicTreesCompat.NAME, version = DynamicTreesCompat.VERSION, dependencies = DynamicTreesCompat.DEPENDENCIES)
public class DynamicTreesCompat {
	
	public static final String MODID = "dtcompat";
	public static final String NAME = "Dynamic Trees Compat";
	public static final String VERSION = "1.0";
	//public static final String DEPENDENCIES = "required-after:dynamictrees;after:biomesoplenty";
	public static final String DEPENDENCIES = "";
	
	@Mod.Instance
	public static DynamicTreesCompat instance;
	
	@SidedProxy(clientSide = "dtcompat.proxy.ClientProxy", serverSide = "dtcompat.proxy.CommonProxy")
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
			WorldGenRegistry.registerBiomeTreeSelector(new BiomeSpeciesSelector());
			WorldGenRegistry.registerBiomeDensityProvider(new BiomeDensityProvider());
		}
		
	}
	
}
