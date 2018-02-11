package dynamictreesbop.worldgen;

import java.util.Random;

import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDensityProvider;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CompatHelper;
import com.ferreusveritas.dynamictrees.util.MathHelper;

import biomesoplenty.api.biome.BOPBiomes;
import dynamictreesbop.DynamicTreesBOP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public class BiomeDensityProvider implements IBiomeDensityProvider {

	@Override
	public EnumChance chance(Biome biome, Species species, int radius, Random rand) {
		if (biome == BOPBiomes.alps_foothills.get()) return rand.nextFloat() < 0.5f ? EnumChance.OK : EnumChance.CANCEL;
		if (biome == BOPBiomes.dead_swamp.get()) return rand.nextFloat() < 0.6f ? EnumChance.OK : EnumChance.CANCEL;
		if (biome == BOPBiomes.lavender_fields.get()) return rand.nextFloat() < 0.3f ? EnumChance.OK : EnumChance.CANCEL;
		if (biome == BOPBiomes.lush_desert.get()) return rand.nextFloat() < 0.4f ? EnumChance.OK : EnumChance.CANCEL;
		if (biome == BOPBiomes.lush_swamp.get()) return EnumChance.OK;
		if (biome == BOPBiomes.meadow.get()) return rand.nextFloat() < 0.3f ? EnumChance.OK : EnumChance.CANCEL;
		if (biome == BOPBiomes.prairie.get()) return rand.nextFloat() < 0.2f ? EnumChance.OK : EnumChance.CANCEL;
		if (biome == BOPBiomes.rainforest.get()) return EnumChance.OK;
		/*if (biome == BOPBiomes.seasonal_forest.get()) {
			if(radius > 3) {//Start dropping tree spawn opportunities when the radius gets bigger than 3
				float chance = 2.0f / radius;
				return rand.nextFloat() < Math.sqrt(chance) ? EnumChance.OK : EnumChance.CANCEL;
			}
		}*/
		if (biome == BOPBiomes.tropical_rainforest.get()) return EnumChance.OK;
		if (biome == BOPBiomes.wasteland.get()) return rand.nextFloat() < 0.3f ? EnumChance.OK : EnumChance.CANCEL;
		
		return EnumChance.UNHANDLED;
	}

	@Override
	public double getDensity(Biome biome, double noiseDensity, Random rand) {
		double naturalDensity = MathHelper.clamp((CompatHelper.getBiomeTreesPerChunk(biome)) / 10.0f, 0.0f, 1.0f);
		double base = naturalDensity * noiseDensity;
		if (biome == BOPBiomes.alps_foothills.get()) return noiseDensity * 0.05;
		
		if (biome == BOPBiomes.bayou.get()) return noiseDensity * 0.8;
		
		if (biome == BOPBiomes.boreal_forest.get()) return noiseDensity;
		
		if (biome == BOPBiomes.cherry_blossom_grove.get()) return noiseDensity * 0.3;
		
		if (biome == BOPBiomes.coniferous_forest.get()) return noiseDensity * 0.8;
		
		if (biome == BOPBiomes.dead_forest.get()) return noiseDensity * 0.3;
		
		if (biome == BOPBiomes.dead_swamp.get()) return noiseDensity * 0.06;
		
		if (biome == BOPBiomes.fen.get()) return noiseDensity * 0.9;
		
		if (biome == BOPBiomes.lavender_fields.get()) return noiseDensity * 0.1;
		
		if (biome == BOPBiomes.lush_desert.get()) return noiseDensity * 0.4;
		
		if (biome == BOPBiomes.lush_swamp.get()) return noiseDensity * 0.9;
		
		if (biome == BOPBiomes.maple_woods.get()) return noiseDensity;
		
		if (biome == BOPBiomes.mountain.get()) return noiseDensity * 0.3;
		
		if (biome == BOPBiomes.mountain_foothills.get()) return noiseDensity * 0.3;
		
		if (biome == BOPBiomes.mystic_grove.get()) return noiseDensity;
		
		if (biome == BOPBiomes.ominous_woods.get()) return noiseDensity;
		
		if (biome == BOPBiomes.orchard.get()) return noiseDensity * 0.5;
		
		if (biome == BOPBiomes.prairie.get()) return noiseDensity * 0.1;
		
		if (biome == BOPBiomes.rainforest.get()) return noiseDensity;
		
		if (biome == BOPBiomes.seasonal_forest.get()) return noiseDensity * 0.9;
		
		if (biome == BOPBiomes.snowy_coniferous_forest.get()) return noiseDensity * 0.8;
		
		if (biome == BOPBiomes.snowy_forest.get()) return noiseDensity * 0.3;
		
		if (biome == BOPBiomes.temperate_rainforest.get()) return noiseDensity;
		
		if (biome == BOPBiomes.tropical_rainforest.get()) return noiseDensity;
		
		if (biome == BOPBiomes.wasteland.get()) return noiseDensity * 0.03; // technically should be 0.03
		
		if (biome == BOPBiomes.wetland.get()) return noiseDensity;
		
		if (biome == BOPBiomes.woodland.get()) return noiseDensity;
		
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
	
	private interface IChance {
		EnumChance getChance(Random random, int radius);
	}

	private class ChanceStatic implements IChance {
		private final EnumChance chance;
		
		public ChanceStatic(EnumChance chance) {
			this.chance = chance;
		}
		
		@Override
		public EnumChance getChance(Random random, int radius) {
			return chance;
		}
	}

	private class ChanceRandom implements IChance {
		private final float value;
		
		public ChanceRandom(float value) {
			this.value = value;
		}
		
		@Override
		public EnumChance getChance(Random random, int radius) {
			return random.nextFloat() < value ? EnumChance.OK : EnumChance.CANCEL;
		}
	}
	
	private class ChanceByRadius implements IChance {
		@Override
		public EnumChance getChance(Random random, int radius) {
			float chance = 1.0f;
			
			if(radius > 3) {//Start dropping tree spawn opportunities when the radius gets bigger than 3
				chance = 2.0f / radius;
				return random.nextFloat() < chance ? EnumChance.OK : EnumChance.CANCEL;
			}

			return random.nextFloat() < chance ? EnumChance.OK : EnumChance.CANCEL;
		}
	}

}
