package dynamictreesbop.trees;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenCocoa;
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
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreeMahogany extends TreeFamily {
	
	public class SpeciesMahogany extends Species {
		
		public SpeciesMahogany(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.leaves.get(ModContent.MAHOGANY));
			
			setBasicGrowingParameters(0.15f, 16.0f, 2, 7, 0.9f);
						
			envFactor(Type.COLD, 0.15f);
			envFactor(Type.DRY,  0.20f);
			envFactor(Type.HOT, 1.1f);
			envFactor(Type.WET, 1.1f);
			
			addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
			
			generateSeed();
			
			setupStandardSeedDropping();
			
			//Add species features
			addGenFeature(new FeatureGenCocoa());
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, BOPBiomes.tropical_rainforest.orNull(), BOPBiomes.overgrown_cliffs.orNull());
		};
		
		@Override
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
			EnumFacing originDir = signal.dir.getOpposite();
			
			probMap[0] = 0; // Down is always disallowed for mahogany
			
			if (signal.energy < 3.5 && signal.energy > 1) {
				probMap[1] = 1;
				probMap[2] = probMap[3] = probMap[4] = probMap[5] = 0;
			} else {
				float r = Math.max(Math.abs(signal.delta.getX()), Math.abs(signal.delta.getZ()));
				
				probMap[2] = probMap[3] = probMap[4] = probMap[5] = 3;
				probMap[1] = 1 + (int) (r * 2.5);
				
				if (signal.delta.getZ() > 0) probMap[2] = 0;
				if (signal.delta.getZ() < 0) probMap[3] = 0;
				if (signal.delta.getX() > 0) probMap[4] = 0;
				if (signal.delta.getX() < 0) probMap[5] = 0;
				
				probMap[originDir.ordinal()] = 0; // Disable the direction we came from
				probMap[signal.dir.ordinal()] += signal.dir == EnumFacing.UP ? (int) ((r - 1.75) * 1.5f) : 0;
			}
			
			return probMap;
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
	
	public TreeMahogany() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.MAHOGANY));
		
		IBlockState primLog = BlockBOPLog.paging.getVariantState(BOPWoods.MAHOGANY);
		setPrimitiveLog(primLog, BlockBOPLog.paging.getVariantItem(BOPWoods.MAHOGANY));
		
		ModContent.leaves.get(ModContent.MAHOGANY).setTree(this);
		
		this.addConnectableVanillaLeaves((state) -> {
			return state.getBlock() instanceof BlockBOPLeaves && state.getValue(((BlockBOPLeaves) state.getBlock()).variantProperty) == BOPTrees.MAHOGANY;
		});
		
		canSupportCocoa = true;
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesMahogany(this));
	}
		
	@Override
	public boolean onTreeActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		//Place Cocoa Pod if we are holding Cocoa Beans
		if (heldItem != null) {
			if (heldItem.getItem() == Items.DYE && heldItem.getItemDamage() == 3) {
				BlockBranch branch = TreeHelper.getBranch(state);
				if (branch != null && branch.getRadius(state) == 8) {
					if (side != EnumFacing.UP && side != EnumFacing.DOWN) {
						pos = pos.offset(side);
					}
					if (world.isAirBlock(pos)) {
						IBlockState cocoaState = ModBlocks.blockFruitCocoa.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, 0, player);
						EnumFacing facing = cocoaState.getValue(BlockHorizontal.FACING);
						world.setBlockState(pos, ModBlocks.blockFruitCocoa.getDefaultState().withProperty(BlockHorizontal.FACING, facing), 2);
						if (!player.capabilities.isCreativeMode) {
							heldItem.shrink(1);
						}
						return true;
					}
				}
			}
		}
		
		// Need this here to apply potions or bone meal.
		return super.onTreeActivated(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
	}

}
