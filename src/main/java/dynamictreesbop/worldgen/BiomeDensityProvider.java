package dynamictreesbop.worldgen;

import java.util.HashMap;
import java.util.Random;

import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDensityProvider;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.TreeGenerator;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.common.biome.BOPBiome;
import dynamictreesbop.DynamicTreesBOP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public class BiomeDensityProvider implements IBiomeDensityProvider {
	
	HashMap<Integer, DensityData> fastDensityDataLookup = new HashMap<Integer, DensityData>();
	int lastBiomeDensityId = -1;
	DensityData lastBiomeDensity = null;
	
	public void processBiomeDensityInjections () {
		Biome.REGISTRY.forEach(biome -> {
			if (biome instanceof BOPBiome) {
				TreeGenerator.getTreeGenerator().biomeTreeHandler.defaultDensityProvider.injectDensityData(biome, computeDensityData(biome));
			}
		});
	}
	
	@Override
	public ResourceLocation getName() {
		return new ResourceLocation(DynamicTreesBOP.MODID, "default");
	}
	
	@Override
	public int getPriority() {
		return 1;
	}
	
	public DensityData getDensityData(Biome biome) {
		int biomeId = Biome.getIdForBiome(biome);
		
		//The idea here is that the next biome is usually the same as the last
		if(biomeId != lastBiomeDensityId) {
			lastBiomeDensityId = biomeId;
			lastBiomeDensity = fastDensityDataLookup.computeIfAbsent(biomeId, k -> computeDensityData(biome));
		}
		
		return lastBiomeDensity;
	}
	
	public DensityData computeDensityData(Biome biome) {
		return new DensityData(computeChance(biome), computeDensity(biome));
	}
	
	@Override
	public double getDensity(Biome biome, double noiseDensity, Random random) {
		return getDensityData(biome).getDensity().getDensity(random, noiseDensity);
	}
	
	@Override
	public EnumChance chance(Biome biome, Species species, int radius, Random random) {
		return getDensityData(biome).getChance().getChance(random, species, radius);
	}
	
	public IBiomeDensityProvider.IChance computeChance(Biome biome) {
		if (biome == BOPBiomes.alps_foothills.orNull()) {
			return (rnd, spc, rad) -> { return rnd.nextFloat() < 0.5f ? EnumChance.OK : EnumChance.CANCEL; };
		}
		if (biome == BOPBiomes.bamboo_forest.orNull()) {
			return (rnd, spc, rad) -> { return EnumChance.OK; };
		}
		if (biome == BOPBiomes.brushland.orNull()) {
			return (rnd, spc, rad) -> { return EnumChance.OK; };
		}
		if (biome == BOPBiomes.bog.orNull()) {
			return (rnd, spc, rad) -> { return EnumChance.OK; };
		}
		if (biome == BOPBiomes.chaparral.orNull()) {
			return (rnd, spc, rad) -> { return rnd.nextFloat() < 0.7f ? EnumChance.OK : EnumChance.CANCEL; };
		}
		if (biome == BOPBiomes.dead_swamp.orNull()) {
			return (rnd, spc, rad) -> { return rnd.nextFloat() < 0.6f ? EnumChance.OK : EnumChance.CANCEL; };
		}
		if (biome == BOPBiomes.eucalyptus_forest.orNull()) {
			return (rnd, spc, rad) -> { return EnumChance.OK; };
		}
		if (biome == BOPBiomes.lavender_fields.orNull()) {
			return (rnd, spc, rad) -> { return rnd.nextFloat() < 0.3f ? EnumChance.OK : EnumChance.CANCEL; };
		}
		if (biome == BOPBiomes.lush_desert.orNull()) {
			return (rnd, spc, rad) -> { return rnd.nextFloat() < 0.4f ? EnumChance.OK : EnumChance.CANCEL; };
		}
		if (biome == BOPBiomes.meadow.orNull()) {
			return (rnd, spc, rad) -> { return rnd.nextFloat() < 0.6f ? EnumChance.OK : EnumChance.CANCEL; };
		}
		if (biome == BOPBiomes.oasis.orNull()) {
			return (rnd, spc, rad) -> { 
				if (spc.getRegistryName().getResourcePath().equals("palm")) {
					return rnd.nextFloat() < 0.5f ? EnumChance.OK : EnumChance.CANCEL;
				}
				return EnumChance.OK;
			};
		}
		if (biome == BOPBiomes.outback.orNull()) {
			return (rnd, spc, rad) -> { return EnumChance.OK; };
		}
		if (biome == BOPBiomes.overgrown_cliffs.orNull()) {
			return (rnd, spc, rad) -> { return EnumChance.OK; };
		}
		if (biome == BOPBiomes.prairie.orNull()) {
			return (rnd, spc, rad) -> { return rnd.nextFloat() < 0.15f ? EnumChance.OK : EnumChance.CANCEL; };
		}
		if (biome == BOPBiomes.quagmire.orNull()) {
			return (rnd, spc, rad) -> { return rnd.nextFloat() < 0.02f ? EnumChance.OK : EnumChance.CANCEL; };
		}
		if (biome == BOPBiomes.rainforest.orNull()) {
			return (rnd, spc, rad) -> { return EnumChance.OK; };
		}
		if (biome == BOPBiomes.tropical_island.orNull()) {
			return (rnd, spc, rad) -> { return EnumChance.OK; };
		}
		if (biome == BOPBiomes.tropical_rainforest.orNull()) {
			return (rnd, spc, rad) -> { return EnumChance.OK; };
		}
		if (biome == BOPBiomes.wasteland.orNull()) {
			return (rnd, spc, rad) -> { return rnd.nextFloat() < 0.3f ? EnumChance.OK : EnumChance.CANCEL; };
		}
		if (biome == BOPBiomes.xeric_shrubland.orNull()) {
			return (rnd, spc, rad) -> { return rnd.nextFloat() < 0.7f ? EnumChance.OK : EnumChance.CANCEL; };
		}

		if (   biome == BOPBiomes.cold_desert.orNull()
			|| biome == BOPBiomes.crag.orNull()
			|| biome == BOPBiomes.flower_field.orNull()
			|| biome == BOPBiomes.flower_island.orNull()
			|| biome == BOPBiomes.glacier.orNull() 
			|| biome == BOPBiomes.grassland.orNull()
			|| biome == BOPBiomes.gravel_beach.orNull() 
			|| biome == BOPBiomes.highland.orNull()
			|| biome == BOPBiomes.mangrove.orNull()
			|| biome == BOPBiomes.marsh.orNull()
			|| biome == BOPBiomes.moor.orNull()
			|| biome == BOPBiomes.origin_beach.orNull()
			|| biome == BOPBiomes.origin_island.orNull()
			|| biome == BOPBiomes.pasture.orNull()
			|| biome == BOPBiomes.redwood_forest.orNull()
			|| biome == BOPBiomes.sacred_springs.orNull()
			|| biome == BOPBiomes.shrubland.orNull()
			|| biome == BOPBiomes.steppe.orNull()
			|| biome == BOPBiomes.tundra.orNull()
			|| biome == BOPBiomes.volcanic_island.orNull()) {
			return (rnd, spc, rad) -> { return EnumChance.CANCEL; };
		}
		
		return (rnd, spc, rad) -> { return EnumChance.UNHANDLED; };
	}
	
	public IDensity computeDensity(Biome biome) {
		if (biome == BOPBiomes.alps_foothills.orNull()) {
			return (rnd, nd) -> { return nd * 0.05; };
		}
		if (biome == BOPBiomes.bamboo_forest.orNull()) {
			return (rnd, nd) -> { return (nd * 0.25) + 0.75; };
		}
		if (biome == BOPBiomes.bayou.orNull()) {
			return (rnd, nd) -> { return nd * 0.8; };
		}
		if (biome == BOPBiomes.bog.orNull()) {
			return (rnd, nd) -> { return ((nd * 0.75) + 0.25) * 0.7; };
		}
		if (biome == BOPBiomes.boreal_forest.orNull()) {
			return (rnd, nd) -> { return nd; };
		}
		if (biome == BOPBiomes.brushland.orNull()) {
			return (rnd, nd) -> { return ((nd * 0.75) + 0.25) * 0.4; };
		}
		if (biome == BOPBiomes.chaparral.orNull()) {
			return (rnd, nd) -> { return nd * 0.7; };
		}
		if (biome == BOPBiomes.cherry_blossom_grove.orNull()) {
			return (rnd, nd) -> { return nd * 0.3; };
		}
		if (biome == BOPBiomes.coniferous_forest.orNull()) {
			return (rnd, nd) -> { return ((nd * 0.5) + 0.5) * 0.95; };
		}
		if (biome == BOPBiomes.dead_forest.orNull()) {
			return (rnd, nd) -> { return nd * 0.3; };
		}
		if (biome == BOPBiomes.dead_swamp.orNull()) {
			return (rnd, nd) -> { return nd * 0.06; };
		}
		if (biome == BOPBiomes.eucalyptus_forest.orNull()) {
			return (rnd, nd) -> { return (nd * 0.5) + 0.5; };
		}
		if (biome == BOPBiomes.fen.orNull()) {
			return (rnd, nd) -> { return nd * 0.9; };
		}
		if (biome == BOPBiomes.grove.orNull()) {
			return (rnd, nd) -> { return nd * 0.25; };
		}
		if (biome == BOPBiomes.lavender_fields.orNull()) {
			return (rnd, nd) -> { return nd * 0.1; };
		}
		if (biome == BOPBiomes.lush_desert.orNull()) {
			return (rnd, nd) -> { return nd * 0.4; };
		}
		if (biome == BOPBiomes.lush_swamp.orNull()) {
			return (rnd, nd) -> { return nd * 0.8; };
		}
		if (biome == BOPBiomes.maple_woods.orNull()) {
			return (rnd, nd) -> { return nd; };
		}
		if (biome == BOPBiomes.meadow.orNull()) {
			return (rnd, nd) -> { return nd * 0.25; };
		}
		if (biome == BOPBiomes.mountain.orNull()) {
			return (rnd, nd) -> { return nd * 0.3; };
		}
		if (biome == BOPBiomes.mountain_foothills.orNull()) {
			return (rnd, nd) -> { return nd * 0.3; };
		}
		if (biome == BOPBiomes.mystic_grove.orNull()) {
			return (rnd, nd) -> { return nd; };
		}
		if (biome == BOPBiomes.oasis.orNull()) {
			return (rnd, nd) -> { return ((nd * 0.25) + 0.75) * 0.7; };
		}
		if (biome == BOPBiomes.ominous_woods.orNull()) {
			return (rnd, nd) -> { return nd; };
		}
		if (biome == BOPBiomes.orchard.orNull()) {
			return (rnd, nd) -> { return nd * 0.5; };
		}
		if (biome == BOPBiomes.outback.orNull()) {
			return (rnd, nd) -> { return nd * 0.45; };
		}
		if (biome == BOPBiomes.overgrown_cliffs.orNull()) {
			return (rnd, nd) -> { return (nd * 0.75) + 0.25; };
		}
		if (biome == BOPBiomes.prairie.orNull()) {
			return (rnd, nd) -> { return nd * 0.1; };
		}
		if (biome == BOPBiomes.rainforest.orNull()) {
			return (rnd, nd) -> { return nd; };
		}
		if (biome == BOPBiomes.seasonal_forest.orNull()) {
			return (rnd, nd) -> { return nd * 0.9; };
		}
		if (biome == BOPBiomes.shield.orNull()) {
			return (rnd, nd) -> { return nd * 0.9; };
		}
		if (biome == BOPBiomes.snowy_coniferous_forest.orNull()) {
			return (rnd, nd) -> { return nd * 0.8; };
		}
		if (biome == BOPBiomes.snowy_forest.orNull()) {
			return (rnd, nd) -> { return nd * 0.3; };
		}
		if (biome == BOPBiomes.temperate_rainforest.orNull()) {
			return (rnd, nd) -> { return nd; };
		}
		if (biome == BOPBiomes.tropical_island.orNull()) {
			return (rnd, nd) -> { return (nd * 0.5) + 0.5; };
		}
		if (biome == BOPBiomes.tropical_rainforest.orNull()) {
			return (rnd, nd) -> { return (nd * 0.5) + 0.5; };
		}
		if (biome == BOPBiomes.wasteland.orNull()) {
			return (rnd, nd) -> { return nd * 0.03; };
		}
		if (biome == BOPBiomes.wetland.orNull()) {
			return (rnd, nd) -> { return nd; };
		}
		if (biome == BOPBiomes.woodland.orNull()) {
			return (rnd, nd) -> { return nd; };
		}
		if (biome == BOPBiomes.xeric_shrubland.orNull()) {
			return (rnd, nd) -> { return nd * 0.4; };
		}
		return (rnd, nd) -> { return -1; };
	}
	
}
