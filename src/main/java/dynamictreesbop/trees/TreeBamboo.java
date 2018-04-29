package dynamictreesbop.trees;

import java.util.List;
import java.util.Random;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.network.INodeInspector;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.api.treedata.ITreePart;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorLogs;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeFindEnds;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.MathHelper;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap.Cell;
import com.ferreusveritas.dynamictrees.worldgen.JoCode;

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

			addDropCreator(new DropCreatorLogs() {
				@Override
				public List<ItemStack> getLogsDrop(World world, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, int volume) {
					int numLogs = volume / 768;
					while(numLogs > 64) {
						dropList.add(species.getFamily().getPrimitiveLogItemStack(numLogs >= 64 ? 64 : numLogs));
						numLogs -= 64;
					}
					return dropList;
				}
			});
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
		
		@Override
		public JoCode getJoCode(String joCodeString) {
			return new JoCodeBamboo(joCodeString);
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
		int radius = branch.getRadius(blockState, blockAccess, pos);
		if (radius == 1) {
			if (blockAccess.getBlockState(pos.down()).getBlock() == branch) {
				return 128;
			}
		}
		return radius;
	}
	
	
	public class JoCodeBamboo extends JoCode {
		
		public JoCodeBamboo(String code) {
			super(code);
		}
		
		@Override
		public void generate(World world, Species species, BlockPos rootPos, Biome biome, EnumFacing facing, int radius) {
			IBlockState initialState = world.getBlockState(rootPos); // Save the initial state of the dirt in case this fails
			species.placeRootyDirtBlock(world, rootPos, 0); // Set to unfertilized rooty dirt

			// A Tree generation boundary radius is at least 2 and at most 8
			radius = MathHelper.clamp(radius, 2, 8);
			BlockPos treePos = rootPos.up();
			
			// Create tree
			setFacing(facing);
			generateFork(world, species, 0, rootPos, false);

			// Fix branch thicknesses and map out leaf locations
			IBlockState branchState = world.getBlockState(treePos);
			BlockBranch branch = TreeHelper.getBranch(branchState);
			if (branch != null) { // If a branch exists then the growth was successful
				ILeavesProperties leavesProperties = species.getLeavesProperties();
				SimpleVoxmap leafMap = new SimpleVoxmap(radius * 2 + 1, 36, radius * 2 + 1).setMapAndCenter(treePos, new BlockPos(radius, 0, radius));
				NodeInflatorBamboo inflator = new NodeInflatorBamboo(species, leafMap); // This is responsible for thickening the branches
				NodeFindEnds endFinder = new NodeFindEnds(); // This is responsible for gathering a list of branch end points
				MapSignal signal = new MapSignal(inflator, endFinder); // The inflator signal will "paint" a temporary voxmap of all of the leaves and branches.
				branch.analyse(branchState, world, treePos, EnumFacing.DOWN, signal);
				List<BlockPos> endPoints = endFinder.getEnds();
				
				// Establish a zone where we can place leaves without hitting ungenerated chunks.
				SafeChunkBounds safeBounds = new SafeChunkBounds(world, rootPos); // Area that is safe to place leaves during worldgen
				
				// Place Growing Leaves Blocks from voxmap
				for (Cell cell: leafMap.getAllNonZeroCells((byte) 0x0F)) { // Iterate through all of the cells that are leaves(not air or branches)
					BlockPos cellPos = cell.getPos();
					if(safeBounds.inBounds(cellPos)) {
						IBlockState testBlockState = world.getBlockState(cellPos);
						Block testBlock = testBlockState.getBlock();
						if(testBlock.isReplaceable(world, cellPos)) {
							world.setBlockState(cellPos, leavesProperties.getDynamicLeavesState(cell.getValue()), careful ? 2 : 0);
						}
					} else {
						leafMap.setVoxel(cellPos, (byte) 0);
					}
				}

				// Shrink the safeBounds down by 1 so that the aging process won't look for neighbors outside of the bounds.
				safeBounds.setShrink(1);
				for (Cell cell: leafMap.getAllNonZeroCells((byte) 0x0F)) {
					BlockPos cellPos = cell.getPos();
					if (!safeBounds.inBounds(cellPos)) {
						leafMap.setVoxel(cellPos, (byte) 0);
					}
				}
				
				// Age volume for 6 cycles using a leafmap
				TreeHelper.ageVolume(world, treePos, radius, 32, leafMap, 6);
				
				// Rot the unsupported branches
				species.handleRot(world, endPoints, rootPos, treePos, 0, true);
				
				// Allow for special decorations by the tree itself
				species.postGeneration(world, rootPos, biome, radius, endPoints, !careful);
			
			} else { // The growth failed.. turn the soil back to what it was
				world.setBlockState(rootPos, initialState, careful ? 3 : 2);
			}
		}
		
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
							int branchRadius = treepart.getRadius(deltaBlockState, world, dPos);
							areaAccum += branchRadius * branchRadius;
						}
					}
				}
				
				if (isTop) {
					// Handle top leaves here
					leafMap.setVoxel(pos, (byte) 16); // 16(bit 5) is code for a twig
					SimpleVoxmap leafCluster = DTBOPLeafClusters.eucalyptusTop;
					leafMap.BlitMax(pos, leafCluster);
				} else {
					// The new branch should be the square root of all of the sums of the areas of the branches coming into it.
					radius = (float) Math.sqrt(areaAccum) + (species.getTapering() * species.getWorldGenTaperingFactor());
					
					// Make sure that non-twig branches are at least radius 2
					float secondaryThickness = species.getSecondaryThickness();
					if (radius < secondaryThickness) {
						radius = secondaryThickness;
					}
					
					branch.setRadius(world, pos, (int) Math.floor(radius), null);
					leafMap.setVoxel(pos, (byte) 32); // 32(bit 6) is code for a branch
					
					if (Math.floor(radius) <= 2 && pos.getY() > 5) {
						SimpleVoxmap leafCluster = DTBOPLeafClusters.eucalyptus;
						leafMap.BlitMax(pos, leafCluster);
					}
				}
				last = pos;
			}
			return false;
		}
		
	}
	
}
