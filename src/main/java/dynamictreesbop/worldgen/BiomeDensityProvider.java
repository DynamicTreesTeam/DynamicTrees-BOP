package dynamictreesbop.worldgen;

import java.util.Random;

import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDensityProvider;
import com.ferreusveritas.dynamictrees.trees.Species;

import biomesoplenty.api.biome.BOPBiomes;
import dynamictreesbop.DynamicTreesBOP;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public class BiomeDensityProvider implements IBiomeDensityProvider {

	@Override
	public EnumChance chance(Biome biome, Species species, int radius, Random rand) {
		if (biome == BOPBiomes.alps_foothills.orNull()) return rand.nextFloat() < 0.5f ? EnumChance.OK : EnumChance.CANCEL;
		if (biome == BOPBiomes.bamboo_forest.orNull()) return EnumChance.OK;
		if (biome == BOPBiomes.brushland.orNull()) return EnumChance.OK;
		if (biome == BOPBiomes.bog.orNull()) return EnumChance.OK;
		if (biome == BOPBiomes.chaparral.orNull()) return rand.nextFloat() < 0.7f ? EnumChance.OK : EnumChance.CANCEL;
		if (biome == BOPBiomes.dead_swamp.orNull()) return rand.nextFloat() < 0.6f ? EnumChance.OK : EnumChance.CANCEL;
		if (biome == BOPBiomes.eucalyptus_forest.orNull()) return EnumChance.OK;
		if (biome == BOPBiomes.lavender_fields.orNull()) return rand.nextFloat() < 0.3f ? EnumChance.OK : EnumChance.CANCEL;
		if (biome == BOPBiomes.lush_desert.orNull()) return rand.nextFloat() < 0.4f ? EnumChance.OK : EnumChance.CANCEL;
		if (biome == BOPBiomes.marsh.orNull()) return EnumChance.CANCEL;
		if (biome == BOPBiomes.meadow.orNull()) return rand.nextFloat() < 0.6f ? EnumChance.OK : EnumChance.CANCEL;
		if (biome == BOPBiomes.moor.orNull()) return EnumChance.CANCEL;
		if (biome == BOPBiomes.oasis.orNull()) {
			if (species.getRegistryName().getResourcePath().equals("palm")) {
				return rand.nextFloat() < 0.5f ? EnumChance.OK : EnumChance.CANCEL;
			}
			return EnumChance.OK;
		}
		if (biome == BOPBiomes.outback.orNull()) return EnumChance.OK;
		if (biome == BOPBiomes.overgrown_cliffs.orNull()) return EnumChance.OK;
		if (biome == BOPBiomes.prairie.orNull()) return rand.nextFloat() < 0.15f ? EnumChance.OK : EnumChance.CANCEL;
		if (biome == BOPBiomes.quagmire.orNull()) return rand.nextFloat() < 0.02f ? EnumChance.OK : EnumChance.CANCEL;
		if (biome == BOPBiomes.rainforest.orNull()) return EnumChance.OK;
		if (biome == BOPBiomes.tropical_island.orNull()) return EnumChance.OK;
		if (biome == BOPBiomes.tropical_rainforest.orNull()) return EnumChance.OK;
		if (biome == BOPBiomes.wasteland.orNull()) return rand.nextFloat() < 0.3f ? EnumChance.OK : EnumChance.CANCEL;
		if (biome == BOPBiomes.xeric_shrubland.orNull()) return rand.nextFloat() < 0.7f ? EnumChance.OK : EnumChance.CANCEL;
		/*if (biome == BOPBiomes.brushland.orNull()) {
			if (radius > 3) { // Start dropping tree spawn opportunities when the radius gets bigger than 3
				float chance = 2.0f / radius;
				return rand.nextFloat() < Math.sqrt(chance) ? EnumChance.OK : EnumChance.CANCEL;
			}
			EnumChance.CANCEL;
		}*/
		if (biome == Biomes.RIVER) return EnumChance.CANCEL;
		
		if (biome == BOPBiomes.redwood_forest.orNull()) return EnumChance.CANCEL;
		
		return EnumChance.UNHANDLED;
	}

	@Override
	public double getDensity(Biome biome, double noiseDensity, Random rand) {
		//double naturalDensity = MathHelper.clamp((CompatHelper.getBiomeTreesPerChunk(biome)) / 10.0f, 0.0f, 1.0f);
		//double base = naturalDensity * noiseDensity;
		
		if (biome == BOPBiomes.alps_foothills.orNull()) return noiseDensity * 0.05;
		
		if (biome == BOPBiomes.bamboo_forest.orNull()) return (noiseDensity * 0.25) + 0.75;
		
		if (biome == BOPBiomes.bayou.orNull()) return noiseDensity * 0.8;
		
		if (biome == BOPBiomes.bog.orNull()) return ((noiseDensity * 0.75) + 0.25) * 0.7;
		
		if (biome == BOPBiomes.boreal_forest.orNull()) return noiseDensity;
		
		if (biome == BOPBiomes.brushland.orNull()) return ((noiseDensity * 0.75) + 0.25) * 0.4;
		
		if (biome == BOPBiomes.chaparral.orNull()) return noiseDensity * 0.7;
		
		if (biome == BOPBiomes.cherry_blossom_grove.orNull()) return noiseDensity * 0.3;
		
		if (biome == BOPBiomes.coniferous_forest.orNull()) return ((noiseDensity * 0.5) + 0.5) * 0.95;
		
		if (biome == BOPBiomes.dead_forest.orNull()) return noiseDensity * 0.3;
		
		if (biome == BOPBiomes.dead_swamp.orNull()) return noiseDensity * 0.06;
		
		if (biome == BOPBiomes.eucalyptus_forest.orNull()) return (noiseDensity * 0.5) + 0.5;
		
		if (biome == BOPBiomes.fen.orNull()) return noiseDensity * 0.9;
		
		if (biome == BOPBiomes.grove.orNull()) return noiseDensity * 0.25;
		
		if (biome == BOPBiomes.lavender_fields.orNull()) return noiseDensity * 0.1;
		
		if (biome == BOPBiomes.lush_desert.orNull()) return noiseDensity * 0.4;
		
		if (biome == BOPBiomes.lush_swamp.orNull()) return noiseDensity * 0.8;
		
		if (biome == BOPBiomes.maple_woods.orNull()) return noiseDensity;
		
		if (biome == BOPBiomes.meadow.orNull()) return noiseDensity * 0.25;
		
		if (biome == BOPBiomes.mountain.orNull()) return noiseDensity * 0.3;
		
		if (biome == BOPBiomes.mountain_foothills.orNull()) return noiseDensity * 0.3;
		
		if (biome == BOPBiomes.mystic_grove.orNull()) return noiseDensity;
		
		if (biome == BOPBiomes.oasis.orNull()) return ((noiseDensity * 0.25) + 0.75) * 0.7;
		
		if (biome == BOPBiomes.ominous_woods.orNull()) return noiseDensity;
		
		if (biome == BOPBiomes.orchard.orNull()) return noiseDensity * 0.5;
		
		if (biome == BOPBiomes.outback.orNull()) return noiseDensity * 0.45;
		
		if (biome == BOPBiomes.overgrown_cliffs.orNull()) return (noiseDensity * 0.75) + 0.25;
		
		if (biome == BOPBiomes.prairie.orNull()) return noiseDensity * 0.1;
		
		if (biome == BOPBiomes.rainforest.orNull()) return noiseDensity;
		
		if (biome == BOPBiomes.seasonal_forest.orNull()) return noiseDensity * 0.9;
		
		if (biome == BOPBiomes.shield.orNull()) return noiseDensity * 0.9;
		
		if (biome == BOPBiomes.snowy_coniferous_forest.orNull()) return noiseDensity * 0.8;
		
		if (biome == BOPBiomes.snowy_forest.orNull()) return noiseDensity * 0.3;
		
		if (biome == BOPBiomes.temperate_rainforest.orNull()) return noiseDensity;
		
		if (biome == BOPBiomes.tropical_island.orNull()) return (noiseDensity * 0.5) + 0.5;
		
		if (biome == BOPBiomes.tropical_rainforest.orNull()) return (noiseDensity * 0.5) + 0.5;
		
		if (biome == BOPBiomes.wasteland.orNull()) return noiseDensity * 0.03;
		
		if (biome == BOPBiomes.wetland.orNull()) return noiseDensity;
		
		if (biome == BOPBiomes.woodland.orNull()) return noiseDensity;
		
		if (biome == BOPBiomes.xeric_shrubland.orNull()) return noiseDensity * 0.4;
		
		return -1;
	}

	@Override
	public ResourceLocation getName() {
		return new ResourceLocation(DynamicTreesBOP.MODID, "default");
	}

	@Override
	public int getPriority() {
		return 1;
	}
	
}
