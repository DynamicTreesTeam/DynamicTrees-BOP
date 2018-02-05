package dynamictreesbop.trees;

import java.util.List;
import java.util.Random;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.blocks.BlockRootyDirt;
import com.ferreusveritas.dynamictrees.trees.DynamicTree;
import com.ferreusveritas.dynamictrees.trees.Species;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPFlowers;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPDirt;
import biomesoplenty.common.block.BlockBOPFlower;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockBOPLog;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.blocks.BlockBranchDTBOP;
import dynamictreesbop.featuregen.FeatureGenVine;
import dynamictreesbop.trees.TreeMagic.SpeciesMagic;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreeUmbran extends DynamicTree {
	
	public class SpeciesUmbran extends Species {
		
		FeatureGenVine vineGen;
		
		SpeciesUmbran(DynamicTree treeFamily) {
			super(treeFamily.getName(), treeFamily);
			
			setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.7f);
			
			setDynamicSapling(new BlockDynamicSapling("umbransapling").getDefaultState());
			
			envFactor(Type.COLD, 0.75f);
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);
			envFactor(Type.SPOOKY, 1.1f);
			envFactor(Type.DEAD, 1.1f);
			envFactor(Type.MAGICAL, 1.1f);
			
			addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
			
			setupStandardSeedDropping();
			
			vineGen = new FeatureGenVine(this).setQuantity(7).setMaxLength(6).setRayDistance(8).setVineBlock(BOPBlocks.tree_moss);
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, BOPBiomes.ominous_woods.get());
		}
		
		@Override
		public void postGeneration(World world, BlockPos rootPos, Biome biome, int radius, List<BlockPos> endPoints, boolean worldGen) {
			super.postGeneration(world, rootPos, biome, radius, endPoints, worldGen);
			
			//Generate Vines
			vineGen.gen(world, rootPos.up(), endPoints);
		}
		
	}

	public TreeUmbran(int seq) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "umbran"), seq);
		
		IBlockState primLog = BlockBOPLog.paging.getVariantState(BOPWoods.UMBRAN);
		setPrimitiveLog(primLog, BlockBOPLog.paging.getVariantItem(BOPWoods.UMBRAN));
		
		IBlockState primLeaves = BlockBOPLeaves.paging.getVariantState(BOPTrees.UMBRAN);
		setPrimitiveLeaves(primLeaves, BlockBOPLeaves.paging.getVariantItem(BOPTrees.UMBRAN));
		
		setDynamicBranch(new BlockBranchDTBOP("umbran" + "branch"));
	}
	
	@Override
	protected DynamicTree setDynamicLeaves(String modid, int seq) {
		return setDynamicLeaves(DynamicTreesBOP.getLeavesBlockForSequence(modid, seq), seq & 3);
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesUmbran(this));
		getCommonSpecies().generateSeed();
	}
	
	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(getCommonSpecies().getDynamicSapling().getBlock());
		return super.getRegisterableBlocks(blockList);
	}
	
	@Override
	public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random) {
		if(super.rot(world, pos, neighborCount, radius, random)) {
			if(radius > 4 && TreeHelper.isRootyDirt(world, pos.down()) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
				world.setBlockState(pos, random.nextInt(3) == 0 ? ModBlocks.blockStates.redMushroom : ModBlocks.blockStates.brownMushroom);//Change branch to a mushroom
				world.setBlockState(pos.down(), BOPBlocks.dirt.getDefaultState().withProperty(BlockBOPDirt.VARIANT, BlockBOPDirt.BOPDirtType.LOAMY));//Change rooty dirt to loam
			}
			return true;
		}
		
		return false;
	}
	
	@Override
	public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
		return 0xffffff;
	}

}
