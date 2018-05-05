package dynamictreesbop.trees;

import java.util.List;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.network.INodeInspector;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.api.treedata.ITreePart;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeFindEnds;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.MathHelper;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap.Cell;
import com.ferreusveritas.dynamictrees.worldgen.JoCode;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockBOPLog;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.cells.DTBOPLeafClusters;
import dynamictreesbop.featuregen.FeatureGenBush;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreeEucalyptus extends TreeFamily {
	
	public class SpeciesEucalyptus extends Species {
		
		FeatureGenBush bushGen;
		
		public SpeciesEucalyptus(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.eucalyptusLeavesProperties);
			
			setBasicGrowingParameters(0.25f, 15.0f, 5, 11, 0.9f);
			
			setDynamicSapling(new BlockDynamicSapling("eucalyptussapling").getDefaultState());
			
			envFactor(Type.COLD, 0.15f);
			envFactor(Type.DRY,  0.20f);
			envFactor(Type.HOT, 1.1f);
			envFactor(Type.WET, 1.1f);
			
			addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
			
			generateSeed();
			
			setupStandardSeedDropping();
			
			bushGen = new FeatureGenBush(this);
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return biome == BOPBiomes.eucalyptus_forest.orNull();
		}
		
		@Override
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
			EnumFacing originDir = signal.dir.getOpposite();
			
			// Alter probability map for direction change
			probMap[0] = signal.isInTrunk() ? 0 : 1;
			probMap[1] = signal.isInTrunk() ? 5 : 1;
			probMap[2] = probMap[3] = probMap[4] = probMap[5] = ((signal.isInTrunk() && signal.numSteps % 2 == 0 && signal.energy > 2) || !signal.isInTrunk()) && signal.energy < 12 ? 1 : 0;
			probMap[originDir.ordinal()] = 0; // Disable the direction we came from
			if (!signal.isInTrunk()) probMap[signal.dir.ordinal()] = 0;
			
			return probMap;
		}
		
		@Override
		protected EnumFacing newDirectionSelected(EnumFacing newDir, GrowSignal signal) {
			if (signal.isInTrunk() && newDir != EnumFacing.UP) { // Turned out of trunk
				signal.energy = 1.2f;
			} else if (!signal.isInTrunk()) {
				if (signal.delta.getX() > 0 && newDir == EnumFacing.EAST) signal.energy = 0;
				else if (signal.delta.getX() < 0 && newDir == EnumFacing.WEST) signal.energy = 0;
				else if (signal.delta.getZ() > 0 && newDir == EnumFacing.SOUTH) signal.energy = 0;
				else if (signal.delta.getZ() < 0 && newDir == EnumFacing.NORTH) signal.energy = 0;
			}
			return newDir;
		}
		
		@Override
		public float getEnergy(World world, BlockPos pos) {
			return super.getEnergy(world, pos) * biomeSuitability(world, pos) + (coordHashCode(pos) % 16); // Vary the height energy by a psuedorandom hash function
		}
		
		@Override
		public int getLowestBranchHeight(World world, BlockPos pos) {
			return getLowestBranchHeight() + (int) ((coordHashCode(pos) % 16) * 0.625f);
		}
		
		public int coordHashCode(BlockPos pos) {
			int hash = (pos.getX() * 9973 ^ pos.getY() * 8287 ^ pos.getZ() * 9721) >> 1;
			return hash & 0xFFFF;
		}
		
		@Override
		public JoCode getJoCode(String joCodeString) {
			return new JoCodeEucalyptus(joCodeString);
		}
		
		@Override
		public void postGeneration(World world, BlockPos rootPos, Biome biome, int radius, List<BlockPos> endPoints, boolean worldGen) {
			if (worldGen) {
				//Generate undergrowth
				bushGen.setRadius(radius).gen(world, rootPos.up(), endPoints);
			}
		}
		
	}
	
	public TreeEucalyptus() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "eucalyptus"));
		
		IBlockState primLog = BlockBOPLog.paging.getVariantState(BOPWoods.EUCALYPTUS);
		setPrimitiveLog(primLog, BlockBOPLog.paging.getVariantItem(BOPWoods.EUCALYPTUS));
		
		ModContent.eucalyptusLeavesProperties.setTree(this);
		
		this.addConnectableVanillaLeaves((state) -> {
			return state.getBlock() instanceof BlockBOPLeaves && state.getValue(((BlockBOPLeaves) state.getBlock()).variantProperty) == BOPTrees.EUCALYPTUS;
		});
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesEucalyptus(this));
	}
	
	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(getCommonSpecies().getDynamicSapling().getBlock());
		return super.getRegisterableBlocks(blockList);
	}
	
	@Override
	public BlockBranch createBranch() {
		return new BlockBranchBasic(getName() + "branch") {
			@Override
			public GrowSignal growSignal(World world, BlockPos pos, GrowSignal signal) {
				if (signal.step()) { // This is always placed at the beginning of every growSignal function
					Species species = signal.getSpecies();
					
					EnumFacing originDir = signal.dir.getOpposite(); // Direction this signal originated from
					EnumFacing prevDir = signal.dir;
					EnumFacing targetDir = species.selectNewDirection(world, pos, this, signal); // This must be cached on the stack for proper recursion
					signal.doTurn(targetDir);
					
					{
						BlockPos deltaPos = pos.offset(targetDir);
						IBlockState blockState = world.getBlockState(deltaPos);
						
						// Pass grow signal to next block in path
						ITreePart treepart = TreeHelper.getTreePart(blockState);
						if (treepart != TreeHelper.nullTreePart) {
							if ((signal.numTurns < 2 || treepart.getTreePartType() == TreePartType.BRANCH)) {
								signal = treepart.growSignal(world, deltaPos, signal); // Recurse
							} else {
								signal.success = false;
								return signal;
							}
						} else if (world.isAirBlock(deltaPos)) {
							if (signal.isInTrunk() || (prevDir != targetDir)) {
							//if (signal.isInTrunk() || (signal.delta.getX() <= 1 && signal.delta.getX() >= -1 && signal.delta.getZ() <= 1 && signal.delta.getZ() >= -1)) {
								signal = growIntoAir(world, deltaPos, signal, getRadius(blockState, world, pos));
							} else {
								signal.success = false;
								return signal;
							}
						}
					}
					
					if (signal.isInTrunk()) {	
						// Calculate Branch Thickness based on neighboring branches
						float areaAccum = signal.radius * signal.radius; // Start by accumulating the branch we just came from
						
						for (EnumFacing dir : EnumFacing.VALUES) {
							if (!dir.equals(originDir) && !dir.equals(targetDir)) { // Don't count where the signal originated from or the branch we just came back from
								BlockPos deltaPos = pos.offset(dir);
								
								// If it is decided to implement a special block(like a squirrel hole, tree
								// swing, rotting, burned or infested branch, etc) then this new block could be
								// derived from BlockBranch and this works perfectly. Should even work with
								// tileEntity blocks derived from BlockBranch.
								IBlockState blockState = world.getBlockState(deltaPos);
								ITreePart treepart = TreeHelper.getTreePart(blockState);
								if (isSameTree(treepart)) {
									int branchRadius = treepart.getRadius(blockState, world, deltaPos);
									areaAccum += branchRadius * branchRadius;
								}
							}
						}
						IBlockState currBlockState = world.getBlockState(pos);
						
						// The new branch should be the square root of all of the sums of the areas of the branches coming into it.
						// But it shouldn't be smaller than it's current size(prevents the instant slimming effect when chopping off branches)
						signal.radius = MathHelper.clamp((float) Math.sqrt(areaAccum) + species.getTapering(), getRadius(currBlockState, world, pos), 8);// WOW!
						setRadius(world, pos, (int) Math.floor(signal.radius), null);
					}
				}
				return signal;
			}
			@Override
			public GrowSignal growIntoAir(World world, BlockPos pos, GrowSignal signal, int fromRadius) {
				Species species = signal.getSpecies();
				
				BlockDynamicLeaves leaves = TreeHelper.getLeaves(species.getLeavesProperties().getDynamicLeavesState());
				if (leaves != null && signal.energy > 0) {
					if (fromRadius == 1) { // If we came from a twig then just make some leaves
						signal.success = leaves.growLeavesIfLocationIsSuitable(world, species.getLeavesProperties(), pos, 0);
					} else { // Otherwise make a proper branch
						if (signal.numTurns < 2) {
							return leaves.branchOut(world, pos, signal);
						} else {
							signal.success = leaves.growLeavesIfLocationIsSuitable(world, species.getLeavesProperties(), pos, 0);
						}
					}
				}
				return signal;
			}
		};
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
	
	
	public class JoCodeEucalyptus extends JoCode {
		
		public JoCodeEucalyptus(String code) {
			super(code);
		}
		
		@Override
		public void generate(World world, Species species, BlockPos rootPos, Biome biome, EnumFacing facing, int radius, SafeChunkBounds safeBounds) {
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
				NodeInflatorEucalyptus inflator = new NodeInflatorEucalyptus(species, leafMap); // This is responsible for thickening the branches
				NodeFindEnds endFinder = new NodeFindEnds(); // This is responsible for gathering a list of branch end points
				MapSignal signal = new MapSignal(inflator, endFinder); // The inflator signal will "paint" a temporary voxmap of all of the leaves and branches.
				branch.analyse(branchState, world, treePos, EnumFacing.DOWN, signal);
				List<BlockPos> endPoints = endFinder.getEnds();
				
				// Place Growing Leaves Blocks from voxmap
				for (Cell cell: leafMap.getAllNonZeroCells((byte) 0x0F)) { // Iterate through all of the cells that are leaves(not air or branches)
					MutableBlockPos cellPos = cell.getPos();
					if(safeBounds.inBounds(cellPos, false)) {
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
				for (Cell cell: leafMap.getAllNonZeroCells((byte) 0x0F)) {
					MutableBlockPos cellPos = cell.getPos();
					if (!safeBounds.inBounds(cellPos, true)) {
						leafMap.setVoxel(cellPos, (byte) 0);
					}
				}
				
				// Age volume for 3 cycles using a leafmap
				TreeHelper.ageVolume(world, treePos, radius, 32, leafMap, 3);
				
				// Rot the unsupported branches
				species.handleRot(world, endPoints, rootPos, treePos, 0, true);
				
				// Allow for special decorations by the tree itself
				species.postGeneration(world, rootPos, biome, radius, endPoints, !careful);
			
			} else { // The growth failed.. turn the soil back to what it was
				world.setBlockState(rootPos, initialState, careful ? 3 : 2);
			}
		}
		
	}
	
	public class NodeInflatorEucalyptus implements INodeInspector {
		
		private float radius;
		private BlockPos last;

		Species species;
		SimpleVoxmap leafMap;
		
		public NodeInflatorEucalyptus(Species species, SimpleVoxmap leafMap) {
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
				boolean isTwig = true;
				boolean isTop = (world.getBlockState(pos.down()).getBlock() == branch);
				
				for (EnumFacing dir: EnumFacing.VALUES) {
					if (!dir.equals(fromDir)) { // Don't count where the signal originated from
						
						BlockPos dPos = pos.offset(dir);
						
						if (dPos.equals(last)) { // or the branch we just came back from
							isTwig = false; // on the return journey if the block we just came from is a branch we are obviously not the endpoint(twig)
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
					leafMap.blitMax(pos, leafCluster);
				} else if (isTwig) {
					leafMap.setVoxel(pos, (byte) 16); // 16(bit 5) is code for a twig
					SimpleVoxmap leafCluster = species.getLeavesProperties().getCellKit().getLeafCluster();
					leafMap.blitMax(pos, leafCluster);
					leafMap.setVoxel(pos.offset(fromDir.getOpposite()), (byte) 0);
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
					
					if (Math.floor(radius) <= 3) {
						SimpleVoxmap leafCluster = DTBOPLeafClusters.eucalyptusTrunk;
						leafMap.blitMax(pos, leafCluster);
					}
				}
				last = pos;
			}
			return false;
		}
		
	}
	
}
