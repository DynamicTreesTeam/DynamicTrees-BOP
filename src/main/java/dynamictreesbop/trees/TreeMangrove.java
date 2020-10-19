package dynamictreesbop.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockBOPLog;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreeMangrove extends TreeFamily {
	
	public class SpeciesMangrove extends Species {
		
		public SpeciesMangrove(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.leaves.get(ModContent.MANGROVE));
			
			setBasicGrowingParameters(0.15f, 12.0f, 2, 2, 0.8f);
						
			envFactor(Type.COLD, 0.15f);
			envFactor(Type.DRY,  0.20f);
			envFactor(Type.HOT, 1.1f);
			envFactor(Type.WET, 1.1f);
			
			addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
			
			generateSeed();
			
			setupStandardSeedDropping();
			
			//Add species features
			//addGenFeature(new FeatureGenCocoa());
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, BOPBiomes.mangrove.orNull());
		};
		
		@Override
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
			return super.customDirectionManipulation(world, pos, radius, signal, probMap);
		}
		
		@Override
		protected EnumFacing newDirectionSelected(EnumFacing newDir, GrowSignal signal) {
			if (newDir != EnumFacing.UP) {
				signal.energy += 0.75f;
			}
			if (newDir == EnumFacing.UP && signal.dir != EnumFacing.UP) {
				signal.energy += (Math.max(Math.abs(signal.delta.getX()), Math.abs(signal.delta.getZ())) - 2f) * 1.5f;
			}
			return newDir;
		}
		
	}
	
	public TreeMangrove() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.MANGROVE));
		
		IBlockState primLog = BlockBOPLog.paging.getVariantState(BOPWoods.MANGROVE);
		setPrimitiveLog(primLog, BlockBOPLog.paging.getVariantItem(BOPWoods.MANGROVE));
		
		ModContent.leaves.get(ModContent.MANGROVE).setTree(this);
		
		this.addConnectableVanillaLeaves((state) -> {
			return state.getBlock() instanceof BlockBOPLeaves && state.getValue(((BlockBOPLeaves) state.getBlock()).variantProperty) == BOPTrees.MANGROVE;
		});
		
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesMangrove(this));
	}
	
	@Override
	public BlockBranch createBranch() {
		String branchName = getName() + "branch";
		return new BlockBranchBasic(branchName) {
			@Override
			public int getMaxRadius() {
				return 4;
			}
			
			@Override
			public BlockRenderLayer getBlockLayer() {
				return BlockRenderLayer.CUTOUT_MIPPED;
			}
		};
	}
	
}
