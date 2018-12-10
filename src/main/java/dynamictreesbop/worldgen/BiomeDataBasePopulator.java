package dynamictreesbop.worldgen;

import java.util.ArrayList;
import java.util.List;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBasePopulatorJson;
import com.google.gson.JsonElement;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.biome.IExtendedBiome;
import biomesoplenty.common.biome.BOPBiome;
import dynamictreesbop.ModConstants;
import net.minecraft.util.ResourceLocation;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {
	
	public static final String REMOVEBOPGEN = "removebopgen";
	public static final String RESOURCEPATH = "worldgen/default.json";
	
	private final BiomeDataBasePopulatorJson jsonPopulator;
	
	public BiomeDataBasePopulator() {
		
		//Add a removebopgen applier to the JSON capabilities
		BiomeDataBasePopulatorJson.addJsonBiomeApplier(REMOVEBOPGEN, (database, element, biome) -> {
			if(biome instanceof BOPBiome) {
				List<String> featuresToRemove = new ArrayList<>();
				
				if(element.isJsonPrimitive()) {
					featuresToRemove.add(element.getAsString());
				}
				else if(element.isJsonArray()) {
					for(JsonElement e : element.getAsJsonArray()) {
						if(e.isJsonPrimitive()) {
							featuresToRemove.add(e.getAsString());
						}
					}
				}
				
				for(String featureName : featuresToRemove) {
					if("cactus".equals(featureName) && ModConfigs.vanillaCactusWorldGen) {
						return; //Don't allow the cancellation of the cactus generator if the DynamicTrees has dual dynamic/vanilla cactus generation enabled 
					}
					((BOPBiome) biome).removeGenerator(featureName);
				}
				
			}
		});
		
		jsonPopulator = new BiomeDataBasePopulatorJson(new ResourceLocation(ModConstants.MODID, RESOURCEPATH));
	}
	
	public void populate(BiomeDataBase dbase) {
		
		//Pass the population task to the JSON populator
		jsonPopulator.populate(dbase);
		
		//Remove generators from extended biomes(No JSON support for extended biomes)
		removeTreeGen(BOPBiomes.forest_extension);
		removeTreeGen(BOPBiomes.forest_hills_extension);
		removeTreeGen(BOPBiomes.extreme_hills_extension);
		removeTreeGen(BOPBiomes.extreme_hills_plus_extension);
		removeTreeGen(BOPBiomes.swampland_extension);
	}
	
	private void removeTreeGen(IExtendedBiome extendedBiome) {
		extendedBiome.getGenerationManager().removeGenerator("trees");	
	}
	
	
}
