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
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.api.item.BOPItems;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockBOPLog;
import biomesoplenty.common.block.BlockBOPMushroom;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.dropcreators.DropCreatorFruit;
import dynamictreesbop.trees.TreeMagic.SpeciesMagic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreeDying extends DynamicTree {
	
	public class SpeciesDying extends Species {
		
		SpeciesDying(DynamicTree treeFamily) {
			super(treeFamily.getName(), treeFamily);
			
			setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.65f);
			
			setDynamicSapling(new BlockDynamicSapling("dyingsapling").getDefaultState());
			
			envFactor(Type.LUSH, 0.75f);
			envFactor(Type.SPOOKY, 1.05f);
			envFactor(Type.DEAD, 1.05f);
			
			addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
			
			addDropCreator(new DropCreatorFruit(BOPItems.persimmon));
			setupStandardSeedDropping();
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return BiomeDictionary.hasType(biome, Type.DEAD);
		}
		
	}
	
	public TreeDying(int seq) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "dying"), seq);
		
		IBlockState primLog = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);		
		setPrimitiveLog(primLog, new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.OAK.ordinal() & 3));
		
		IBlockState primLeaves = BlockBOPLeaves.paging.getVariantState(BOPTrees.DEAD);
		setPrimitiveLeaves(primLeaves, BlockBOPLeaves.paging.getVariantItem(BOPTrees.DEAD));
		
		setCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "sparse"));
		setSmotherLeavesMax(1);
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesDying(this));
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
				world.setBlockState(pos.down(), ModBlocks.blockStates.podzol);//Change rooty dirt to Podzol
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
