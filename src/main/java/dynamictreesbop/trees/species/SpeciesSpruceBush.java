package dynamictreesbop.trees.species;

import java.util.ArrayList;
import java.util.Random;

import com.ferreusveritas.dynamictrees.trees.Species;

import biomesoplenty.api.block.BOPBlocks;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.featuregen.FeatureGenBush;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class SpeciesSpruceBush extends Species {
	
	FeatureGenBush bushGen;
	
	public SpeciesSpruceBush() {
		super();
		setRegistryName(new ResourceLocation(DynamicTreesBOP.MODID, "sprucebush"));
		
		setStandardSoils();
		addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		
		this.bushGen = new FeatureGenBush(this)
				.setLeavesState(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE))
				.setLogState(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE));
	}
	
	@Override
	public boolean generate(World world, BlockPos pos, Biome biome, Random random, int radius) {
		bushGen.setRadius(radius).gen(world, pos, new ArrayList<BlockPos>());
		return true;
	}

}
