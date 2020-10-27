package dynamictreesbop.trees;

import java.util.List;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenMudHole;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockBOPLog;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.items.ItemMangroveSeed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreeMangrove extends TreeFamily {
	
	public class SpeciesMangrove extends Species {
		
		protected int deepSoilTypeFlags;
		
		public SpeciesMangrove(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.leaves.get(ModContent.MANGROVE));
			
			setBasicGrowingParameters(0.13f, 10.0f, 1, 2, 0.8f);
			
			envFactor(Type.COLD, 0.15f);
			envFactor(Type.DRY,  0.20f);
			envFactor(Type.HOT, 1.1f);
			envFactor(Type.WET, 1.1f);
			
			Seed seed = new ItemMangroveSeed(getRegistryName().getResourcePath() + "seed");
			setSeedStack(new ItemStack(seed));
			
			setupStandardSeedDropping();
			
			//Add species features
			addGenFeature(new FeatureGenMudHole(BOPBlocks.mud.getDefaultState()));
			addGenFeature(new FeatureGenMangrovelings());
		}
		
		@Override
		public LogsAndSticks getLogsAndSticks(float volume) {
			return super.getLogsAndSticks(1.0f + volume);//Guarantee at least one log is produced
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
		protected void setStandardSoils() {
			addAcceptableSoils(DirtHelper.WATERLIKE);
			deepSoilTypeFlags = DirtHelper.getSoilFlags(DirtHelper.DIRTLIKE, DirtHelper.SANDLIKE, DirtHelper.MUDLIKE);
		}
		
		@Override
		public boolean isAcceptableSoil(World world, BlockPos pos, IBlockState soilBlockState) {
			return super.isAcceptableSoil(world, pos, soilBlockState) && DirtHelper.isSoilAcceptable(world.getBlockState(pos.down()).getBlock(), deepSoilTypeFlags);
		}
		
		@Override
		public boolean isAcceptableSoilForWorldgen(World world, BlockPos pos, IBlockState soilBlockState) {
			if(world.getSeaLevel() - 1 != pos.getY()) {
				return false;
			}
			
			return soilBlockState.getBlock() == BOPBlocks.mud || super.isAcceptableSoilForWorldgen(world, pos, soilBlockState);
		}
		
		@Override
		public BlockRooty getRootyBlock(World world, BlockPos rootPos) {
			return ModContent.rootyWater;
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
	
	public static class FeatureGenMangrovelings implements IPostGenFeature {

		@Override
		public boolean postGeneration(World world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
			
			int[] angles = new int[2]; 
			angles[0] = angles[1] = world.rand.nextInt(6);
			while(angles[0] == angles[1]) {
				angles[1] = world.rand.nextInt(6);
			}
			
			anglesLoop:
			for(int a : angles) {
				double angle = Math.toRadians(a * 60.0f);
				float distance = Math.min(3.0f + world.rand.nextFloat() * 2.0f, radius);
				BlockPos offPos = rootPos.add(new Vec3i(Math.sin(angle) * distance, 0, Math.cos(angle) * distance));
				
				if(safeBounds.inBounds(offPos, true)) {
					if(species.isAcceptableSoil(world, offPos, world.getBlockState(offPos))) {
						if( !(world.isAirBlock(offPos.up(1)) && world.isAirBlock(offPos.up(2))) ) {
							continue anglesLoop;
						}
						for(EnumFacing hor : EnumFacing.HORIZONTALS) {
							BlockPos offPos2 = offPos.offset(hor);
							if( !(world.isAirBlock(offPos2.up(1)) && world.isAirBlock(offPos2.up(2))) ) {
								continue anglesLoop;
							}
						}
						
						world.setBlockState(offPos, species.getRootyBlock(world, offPos).getDefaultState().withProperty(BlockRooty.LIFE, 0));
						species.getFamily().getDynamicBranch().setRadius(world, offPos.up(1), 1, EnumFacing.DOWN, 0);
						if(world.rand.nextInt(2) == 0) {
							world.setBlockState(offPos.up(2), species.getLeavesProperties().getDynamicLeavesState(1));
						} else {
							species.getFamily().getDynamicBranch().setRadius(world, offPos.up(2), 1, EnumFacing.DOWN, 0);
							if(world.isAirBlock(offPos.up(3))) {
								world.setBlockState(offPos.up(3), species.getLeavesProperties().getDynamicLeavesState(1));
							}
						}
					}
				}
			}
			
			return false;
		}
		
	}
	
}
