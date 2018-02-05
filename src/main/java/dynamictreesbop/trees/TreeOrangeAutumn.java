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
import biomesoplenty.api.item.BOPItems;
import biomesoplenty.common.block.BlockBOPLeaves;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.dropcreators.DropCreatorFruit;
import dynamictreesbop.trees.TreeYellowAutumn.SpeciesYellowAutumn;
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
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreeOrangeAutumn extends DynamicTree {
	
	public class SpeciesOrangeAutumn extends Species {
		
		SpeciesOrangeAutumn(DynamicTree treeFamily) {
			super(treeFamily.getName(), treeFamily);
			
			setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.8f);
			
			setDynamicSapling(new BlockDynamicSapling("orangeautumnsapling").getDefaultState());
			
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);
			
			addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
			
			addDropCreator(new DropCreatorFruit(BOPItems.persimmon));
			setupStandardSeedDropping();
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, BOPBiomes.seasonal_forest.get());
		}
		
	}
	
	public TreeOrangeAutumn(int seq) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "orangeautumn"), seq);
		
		IBlockState primLog = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);		
		setPrimitiveLog(primLog, new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.OAK.ordinal() & 3));
		
		IBlockState primLeaves = BlockBOPLeaves.paging.getVariantState(BOPTrees.ORANGE_AUTUMN);
		setPrimitiveLeaves(primLeaves, BlockBOPLeaves.paging.getVariantItem(BOPTrees.ORANGE_AUTUMN));
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesOrangeAutumn(this));
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
