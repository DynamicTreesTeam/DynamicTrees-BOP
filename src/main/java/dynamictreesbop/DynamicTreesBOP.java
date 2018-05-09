package dynamictreesbop;

import com.ferreusveritas.dynamictrees.api.WorldGenRegistry;
import com.ferreusveritas.dynamictrees.worldgen.TreeGenerator;

import dynamictreesbop.proxy.CommonProxy;
import dynamictreesbop.worldgen.BiomeDataBasePopulator;
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
	public static final String VERSION = "@VERSION@";
	public static final String DEPENDENCIES = "required-after:dynamictrees@[1.12.2-0.7.7,);required-after:biomesoplenty@[7.0.1.2344,)";
	
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
		if (WorldGenRegistry.isWorldGenEnabled()) {
			new BiomeDataBasePopulator(TreeGenerator.getTreeGenerator().biomeDataBase).populate();
		}
		
		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		proxy.postInit();
		/*
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
				//String name = biome.getBiomeName().toLowerCase().replace(' ', '_') + "_" + "tree";
				//System.out.println(biome.getBiomeName() + ": " + name);
				//trees.add(name);
			}
		}
		//trees.forEach(tree -> {System.out.println(tree);});
		*/
	}
	
}
