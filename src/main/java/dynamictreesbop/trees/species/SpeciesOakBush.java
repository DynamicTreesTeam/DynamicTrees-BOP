package dynamictreesbop.trees.species;

import java.util.ArrayList;
import java.util.Random;

import com.ferreusveritas.dynamictrees.trees.Species;

import biomesoplenty.api.block.BOPBlocks;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.featuregen.FeatureGenBush;
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
	public boolean generate(World world, BlockPos pos, Biome biome, Random random, int radius) {
		bushGen.setRadius(radius).gen(world, pos, new ArrayList<BlockPos>());
		return true;
	}

}
