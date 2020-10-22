package dynamictreesbop.trees;

import java.util.Random;

import com.ferreusveritas.dynamictrees.api.treedata.ITreePart;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.api.item.BOPItems;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockBOPLog;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.dropcreators.DropCreatorFruit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

public class TreeDead extends TreeFamily {
	
	public class SpeciesDead extends Species {
		
		SpeciesDead(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.leaves.get(ModContent.DEAD));
			
			setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.5f);
			
			envFactor(Type.LUSH, 0.75f);
			envFactor(Type.SPOOKY, 1.05f);
			envFactor(Type.DEAD, 1.05f);
			
			addDropCreator(new DropCreatorFruit(BOPItems.persimmon));
		}
		
		@Override
		public boolean isAcceptableSoil(IBlockState soilBlockState) {
			return super.isAcceptableSoil(soilBlockState) || soilBlockState.getBlock() == BOPBlocks.dried_sand;
		}
		
		@Override
		protected void setStandardSoils() {
			addAcceptableSoils(DirtHelper.Type.DIRTLIKE, DirtHelper.Type.SANDLIKE);
		}
		
		@Override
		public boolean grow(World world, BlockRooty rootyDirt, BlockPos rootPos, int soilLife, ITreePart treeBase, BlockPos treePos, Random random, boolean natural) {
			return true;
		}
		
		@Override
		public boolean canGrowWithBoneMeal(World world, BlockPos pos) {
			return false;
		}
		
		@Override
		public boolean canUseBoneMealNow(World world, Random rand, BlockPos pos) {
			return false;
		}
		
		@Override
		public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random, boolean rapid) {
			return false;
		}
		
	}
	
	public class SpeciesDecayed extends Species {
		
		SpeciesDecayed(TreeFamily treeFamily) {
			super(new ResourceLocation(treeFamily.getName().getResourceDomain(), ModContent.DECAYED), treeFamily, ModContent.decayedLeavesProperties);
			setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 1.0f);
		}
		
		@Override
		public boolean isAcceptableSoil(IBlockState soilBlockState) {
			return super.isAcceptableSoil(soilBlockState) || soilBlockState.getBlock() == BOPBlocks.dried_sand;
		}
		
		@Override
		protected void setStandardSoils() {
			addAcceptableSoils(DirtHelper.Type.DIRTLIKE, DirtHelper.Type.SANDLIKE, DirtHelper.Type.HARDCLAYLIKE);
		}
		
		@Override
		public boolean grow(World world, BlockRooty rootyDirt, BlockPos rootPos, int soilLife, ITreePart treeBase, BlockPos treePos, Random random, boolean natural) {
			return true;
		}
		
		@Override
		public boolean canGrowWithBoneMeal(World world, BlockPos pos) {
			return false;
		}
		
		@Override
		public boolean canUseBoneMealNow(World world, Random rand, BlockPos pos) {
			return false;
		}
		
		@Override
		public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random, boolean rapid) {
			return false;
		}
		
	}

	Species decayedSpecies;
	
	public TreeDead() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.DEAD));
		
		IBlockState primLog = BlockBOPLog.paging.getVariantState(BOPWoods.DEAD);
		setPrimitiveLog(primLog, BlockBOPLog.paging.getVariantItem(BOPWoods.DEAD));
		
		ModContent.leaves.get(ModContent.DEAD).setTree(this);
		ModContent.decayedLeavesProperties.setTree(this);
		
		this.addConnectableVanillaLeaves((state) -> {
			return state.getBlock() instanceof BlockBOPLeaves && state.getValue(((BlockBOPLeaves) state.getBlock()).variantProperty) == BOPTrees.DEAD;
		});
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesDead(this));
		decayedSpecies = new SpeciesDecayed(this);
	}
	
	@Override
	public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
		super.registerSpecies(speciesRegistry);
		speciesRegistry.register(decayedSpecies);
	}
	
}
