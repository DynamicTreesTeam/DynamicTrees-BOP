package dtcompat.worldgen;

import java.util.Random;

import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDensityProvider;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CompatHelper;
import com.ferreusveritas.dynamictrees.util.MathHelper;

import biomesoplenty.api.biome.BOPBiomes;
import dtcompat.DynamicTreesCompat;
import net.minecraft.world.biome.Biome;

public class BiomeDensityProvider implements IBiomeDensityProvider {

	@Override
	public EnumChance chance(Biome biome, Species species, int arg2, Random rand) {
		if (biome == BOPBiomes.rainforest.get()) return EnumChance.OK;
		if (biome == BOPBiomes.tropical_rainforest.get()) return EnumChance.OK;
		if (biome == BOPBiomes.lush_swamp.get()) return EnumChance.OK;
		if (biome == BOPBiomes.meadow.get()) return rand.nextFloat() < 0.3f ? EnumChance.OK : EnumChance.CANCEL;
		
		return EnumChance.UNHANDLED;
	}

	@Override
	public double getDensity(Biome biome, double noiseDensity, Random rand) {
		double naturalDensity = MathHelper.clamp((CompatHelper.getBiomeTreesPerChunk(biome)) / 10.0f, 0.0f, 1.0f);
		double base = naturalDensity * noiseDensity;
		
		if (biome == BOPBiomes.tropical_rainforest.get()) return base * 5;
		
		if (biome == BOPBiomes.lush_swamp.get()) return base * 5;
		
		if (biome == BOPBiomes.rainforest.get()) return base * 5;
		
		return -1;
	}

	@Override
	public String getName() {
		return DynamicTreesCompat.MODID + ":default";
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
