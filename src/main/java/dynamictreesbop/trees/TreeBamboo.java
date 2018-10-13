package dynamictreesbop.trees;

import java.util.List;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.network.INodeInspector;
import com.ferreusveritas.dynamictrees.api.treedata.ITreePart;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.common.block.BlockBOPLeaves;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.cells.DTBOPLeafClusters;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TreeBamboo extends TreeFamily {

	public class SpeciesBamboo extends Species {
		
		SpeciesBamboo(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.bambooLeavesProperties);
			
			setBasicGrowingParameters(0.125f, 11.0f, 4, 4, 1.5f); // Fastest growing "tree"
			
			setDynamicSapling(new BlockDynamicSapling("bamboosapling").getDefaultState());
			
			envFactor(Type.COLD, 0.25f);
			
			addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
			
			generateSeed();
			
			setupStandardSeedDropping();
			
		}
		
		@Override
		public LogsAndSticks getLogsAndSticks(int volume) {
			return new LogsAndSticks(volume / 768, 0);
		}
		
		@Override
		public INodeInspector getNodeInflator(SimpleVoxmap leafMap) {
			return new NodeInflatorBamboo(this, leafMap);
		}
		
		@Override
		public int getWorldGenAgeIterations() {
			return 6;
		}
		
		@Override
		public int getWorldGenLeafMapHeight() {
			return 36;
		}
		
		@Override
		public BlockRooty getRootyBlock() {
			return ModBlocks.blockRootyDirt;
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, BOPBiomes.bamboo_forest.orNull());
		}
		
		@Override
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
			EnumFacing originDir = signal.dir.getOpposite();
			
			// Alter probability map for direction change
			probMap[0] = 0; // Down is always disallowed for bamboo
			probMap[1] = 10;
			probMap[2] = probMap[3] = probMap[4] = probMap[5] = signal.energy <= 1 ? 9 : 0;
			probMap[originDir.ordinal()] = 0; // Disable the direction we came from
			
			return probMap;
		}
		
		@Override
		protected EnumFacing newDirectionSelected(EnumFacing newDir, GrowSignal signal) {
			if (signal.isInTrunk() && newDir != EnumFacing.UP) { // Turned out of trunk
				signal.energy = 0.2f;
			}
			return newDir;
		}
		
		// Bamboo trees are so similar that it makes sense to randomize their height for a little variation
		// but we don't want the trees to always be the same height all the time when planted in the same location
		// so we feed the hash function the in-game month
		@Override
		public float getEnergy(World world, BlockPos pos) {
			long day = world.getTotalWorldTime() / 24000L;
			int month = (int) day / 30; // Change the hashs every in-game month
			
			return super.getEnergy(world, pos) * biomeSuitability(world, pos) + (CoordUtils.coordHashCode(pos.up(month), 3) % 11); // Vary the height energy by a psuedorandom hash function
		}
		
	}
	
	public TreeBamboo() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "bamboo"));
		
		setPrimitiveLog(Block.REGISTRY.getObject(new ResourceLocation("biomesoplenty", "bamboo")).getDefaultState());
		setStick(ItemStack.EMPTY);
		
		ModContent.bambooLeavesProperties.setTree(this);
		
		this.addConnectableVanillaLeaves((state) -> {
			return state.getBlock() instanceof BlockBOPLeaves && state.getValue(((BlockBOPLeaves) state.getBlock()).variantProperty) == BOPTrees.BAMBOO;
		});
	}
	
	@Override
	public BlockBranch createBranch() {
		return new BlockBranchBasic(getName() + "branch") {
			
			@Override
			public void setRadius(World world, BlockPos pos, int radius, EnumFacing dir, int flags) {
				super.setRadius(world, pos, MathHelper.clamp(radius, 1, 3), dir, flags);
			}
			
			@Override
			@SideOnly(Side.CLIENT)
			public BlockRenderLayer getBlockLayer() {
				return BlockRenderLayer.CUTOUT_MIPPED;
			}

		};
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesBamboo(this));
	}
	
	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(getCommonSpecies().getDynamicSapling().getBlock());
		return super.getRegisterableBlocks(blockList);
	}
	
	@Override
	public int getRadiusForCellKit(IBlockAccess blockAccess, BlockPos pos, IBlockState blockState, EnumFacing dir, BlockBranch branch) {
		int radius = branch.getRadius(blockState);
		if (radius == 1) {
			if (blockAccess.getBlockState(pos.down()).getBlock() == branch) {
				return 128;
			}
		}
		return radius;
	}
	
	public class NodeInflatorBamboo implements INodeInspector {
		
		private float radius;
		private BlockPos last;

		Species species;
		SimpleVoxmap leafMap;
		
		public NodeInflatorBamboo(Species species, SimpleVoxmap leafMap) {
			this.species = species;
			this.leafMap = leafMap;
			last = BlockPos.ORIGIN;
		}
		
		@Override
		public boolean run(IBlockState blockState, World world, BlockPos pos, EnumFacing fromDir) {
			BlockBranch branch = TreeHelper.getBranch(blockState);
			
			if (branch != null) {
				radius = 1.0f;
			}
			return false;
		}
		
		@Override
		public boolean returnRun(IBlockState blockState, World world, BlockPos pos, EnumFacing fromDir) {
			// Calculate Branch Thickness based on neighboring branches
			BlockBranch branch = TreeHelper.getBranch(blockState);
			
			if (branch != null) {
				float areaAccum = radius * radius; // Start by accumulating the branch we just came from
				boolean isTop = (world.getBlockState(pos.down()).getBlock() == branch);
				
				for (EnumFacing dir: EnumFacing.VALUES) {
					if (!dir.equals(fromDir)) { // Don't count where the signal originated from
						
						BlockPos dPos = pos.offset(dir);
						
						if (dPos.equals(last)) { // or the branch we just came back from
							isTop = false;
							continue;
						}
						
						IBlockState deltaBlockState = world.getBlockState(dPos);
						ITreePart treepart = TreeHelper.getTreePart(deltaBlockState);
						if (branch.isSameTree(treepart)) {
							int branchRadius = treepart.getRadius(deltaBlockState);
							areaAccum += branchRadius * branchRadius;
						}
					}
				}
				
				if (isTop) {
					// Handle top leaves here
					leafMap.setVoxel(pos, (byte) 16); // 16(bit 5) is code for a twig
					SimpleVoxmap leafCluster = DTBOPLeafClusters.eucalyptusTop;
					leafMap.blitMax(pos, leafCluster);
				} else {
					// The new branch should be the square root of all of the sums of the areas of the branches coming into it.
					radius = (float) Math.sqrt(areaAccum) + (species.getTapering() * species.getWorldGenTaperingFactor());
					
					// Make sure that non-twig branches are at least radius 2
					float secondaryThickness = species.getFamily().getSecondaryThickness();
					if (radius < secondaryThickness) {
						radius = secondaryThickness;
					}
					
					branch.setRadius(world, pos, (int) Math.floor(radius), null);
					leafMap.setVoxel(pos, (byte) 32); // 32(bit 6) is code for a branch
					
					if (Math.floor(radius) <= 2 && pos.getY() > 5) {
						SimpleVoxmap leafCluster = DTBOPLeafClusters.eucalyptus;
						leafMap.blitMax(pos, leafCluster);
					}
				}
				last = pos;
			}
			return false;
		}
		
	}
	
}
