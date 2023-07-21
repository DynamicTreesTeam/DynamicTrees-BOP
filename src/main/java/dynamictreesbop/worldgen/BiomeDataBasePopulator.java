package dynamictreesbop.worldgen;

import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBasePopulatorJson;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.biome.IExtendedBiome;
import dynamictreesbop.ModConstants;
import net.minecraft.util.ResourceLocation;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {
	
	public static final String RESOURCEPATH = "worldgen/default.json";
	
	private final BiomeDataBasePopulatorJson jsonPopulator;
	
	public BiomeDataBasePopulator() {
		jsonPopulator = new BiomeDataBasePopulatorJson(new ResourceLocation(ModConstants.MODID, RESOURCEPATH));
	}
	
	public void populate(BiomeDataBase dbase) {
		
		//Pass the population task to the JSON populator
		jsonPopulator.populate(dbase);
		
		//Remove generators from extended biomes(No JSON support for extended biomes)
		removeTreeGen(BOPBiomes.forest_extension);
		removeTreeGen(BOPBiomes.forest_hills_extension);
		removeTreeGen(BOPBiomes.flower_forest_extension);
		removeTreeGen(BOPBiomes.extreme_hills_extension);
		removeTreeGen(BOPBiomes.extreme_hills_plus_extension);
		removeTreeGen(BOPBiomes.swampland_extension);
	}
	
	private void removeTreeGen(IExtendedBiome extendedBiome) {
		extendedBiome.getGenerationManager().removeGenerator("trees");	
	}
	
	
}
