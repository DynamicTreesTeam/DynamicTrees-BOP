package dynamictreesbop.trees.species;

import java.util.Random;

import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenBush;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;

import biomesoplenty.api.block.BOPBlocks;
import dynamictreesbop.DynamicTreesBOP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class SpeciesOakBush extends Species {
	
FeatureGenBush bushGen;
	
	public SpeciesOakBush() {
		super();
		setRegistryName(new ResourceLocation(DynamicTreesBOP.MODID, "oakbush"));
		
		setStandardSoils();
		addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		
		this.bushGen = new FeatureGenBush(this);
	}
	
	@Override
	public boolean generate(World world, BlockPos rootPos, Biome biome, Random random, int radius, SafeChunkBounds safeBounds) {
		bushGen.gen(world, rootPos, biome, random, radius, safeBounds);
		return true;
	}

}
