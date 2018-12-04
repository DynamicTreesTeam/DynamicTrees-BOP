package dynamictreesbop.trees.species;

import com.ferreusveritas.dynamictrees.api.IGenFeature;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.network.INodeInspector;
import com.ferreusveritas.dynamictrees.api.treedata.ITreePart;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenBush;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.SpeciesRare;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.common.block.BlockBOPLeaves;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.cells.DTBOPLeafClusters;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class SpeciesPoplar extends SpeciesRare {
		
	public SpeciesPoplar(TreeFamily treeFamily, String name) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, name), treeFamily, ModContent.leaves.get(name));
		
		setBasicGrowingParameters(0.25f, 11.0f, 5, 4, 0.85f);
		
		envFactor(Type.HOT, 0.50f);
		envFactor(Type.DRY, 0.50f);
		envFactor(Type.FOREST, 1.05f);
		
		addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt, BOPBlocks.mud);
		
		setupStandardSeedDropping();
		
		//Generate undergrowth
		addGenFeature(
				new FeatureGenBush()
					.setBiomePredicate(b -> b == BOPBiomes.grove.orNull())
					.setSecondaryLeavesState(BlockBOPLeaves.paging.getVariantState(BOPTrees.FLOWERING)
				), IGenFeature.POSTGEN
			);
		
		leavesProperties.setTree(treeFamily);
	}
	
	@Override
	public boolean isBiomePerfect(Biome biome) {
		return biome == BOPBiomes.grove.orNull() || biome == BOPBiomes.bog.orNull();
	}
	
	@Override
	protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
		EnumFacing originDir = signal.dir.getOpposite();
		
		// Alter probability map for direction change
		probMap[0] = signal.isInTrunk() ? 0 : 1;
		probMap[1] = signal.isInTrunk() ? 4 : 1;
		probMap[2] = probMap[3] = probMap[4] = probMap[5] = (((signal.isInTrunk() && signal.numSteps % 2 == 0) || !signal.isInTrunk()) && signal.energy < 8) ? 2 : 0;
		probMap[originDir.ordinal()] = 0; // Disable the direction we came from
		probMap[signal.dir.ordinal()] += (signal.isInTrunk() ? 0 : signal.numTurns == 1 ? 2 : 1); // Favor current travel direction 
		
		return probMap;
	}
	
	@Override
	public INodeInspector getNodeInflator(SimpleVoxmap leafMap) {
		return new NodeInflatorPoplar(this, leafMap);
	}
		
	@Override
	protected EnumFacing newDirectionSelected(EnumFacing newDir, GrowSignal signal) {
		if (signal.isInTrunk() && newDir != EnumFacing.UP) { // Turned out of trunk
			if (signal.energy >= 4f) {
				signal.energy = 1.8f; // don't grow branches more than 1 block out from the trunk
			} else if (signal.energy < 5) {
				signal.energy = 0.8f; // don't grow branches, only leaves
			} else {
				signal.energy = 0; // don't grow branches or leaves
			}
		}
		return newDir;
	}
	
	// Poplar trees are so similar that it makes sense to randomize their height for a little variation
	@Override
	public float getEnergy(World world, BlockPos pos) {
		return super.getEnergy(world, pos) * biomeSuitability(world, pos) + (coordHashCode(pos) % 3); // Vary the height energy by a psuedorandom hash function
	}
	
	@Override
	public int getLowestBranchHeight(World world, BlockPos pos) {
		return getLowestBranchHeight() + (coordHashCode(pos) % 3);
	}
	
	public int coordHashCode(BlockPos pos) {
		int hash = (pos.getX() * 9973 ^ pos.getY() * 8287 ^ pos.getZ() * 9721) >> 1;
		return hash & 0xFFFF;
	}
	
	@Override
	public ItemStack getSeedStack(int qty) {
		return getFamily().getCommonSpecies().getSeedStack(qty);
	}
	
	@Override
	public Seed getSeed() {
		return getFamily().getCommonSpecies().getSeed();
	}
	
	@Override
	public int maxBranchRadius() {
		return 8;
	}
	
	public class NodeInflatorPoplar implements INodeInspector {
		
		private float radius;
		private BlockPos last;

		Species species;
		SimpleVoxmap leafMap;
		
		public NodeInflatorPoplar(Species species, SimpleVoxmap leafMap) {
			this.species = species;
			this.leafMap = leafMap;
			last = BlockPos.ORIGIN;
		}
		
		@Override
		public boolean run(IBlockState blockState, World world, BlockPos pos, EnumFacing fromDir) {
			BlockBranch branch = TreeHelper.getBranch(blockState);
			
			if (branch != null) {
				radius = species.getFamily().getPrimaryThickness();
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
					if(!dir.equals(fromDir)) { // Don't count where the signal originated from
						
						BlockPos dPos = pos.offset(dir);
						
						if (dPos.equals(last)) { // or the branch we just came back from
							isTwig = false; // on the return journey if the block we just came from is a branch we are obviously not the endpoint(twig)
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
					SimpleVoxmap leafCluster = DTBOPLeafClusters.poplarTop;
					leafMap.blitMax(pos, leafCluster);
				} else if (isTwig) {
					// Handle branch leaves here
					leafMap.setVoxel(pos, (byte) 16); // 16(bit 5) is code for a twig
					SimpleVoxmap leafCluster = species.getLeavesProperties().getCellKit().getLeafCluster();
					leafMap.blitMax(pos, leafCluster);
				} else {
					// The new branch should be the square root of all of the sums of the areas of the branches coming into it.
					radius = (float) Math.sqrt(areaAccum) + (species.getTapering() * species.getWorldGenTaperingFactor());
					
					//Ensure the branch is never inflated past it's species maximum
					int maxRadius = species.maxBranchRadius();
					if(radius > maxRadius) {
						radius = maxRadius;
					}
					
					// Make sure that non-twig branches are at least radius 2
					float secondaryThickness = species.getFamily().getSecondaryThickness();
					if (radius < secondaryThickness) {
						radius = secondaryThickness;
					}
					
					branch.setRadius(world, pos, (int) Math.floor(radius), null);
					leafMap.setVoxel(pos, (byte) 32); // 32(bit 6) is code for a branch
					if (Math.floor(radius) < 3) {
						SimpleVoxmap leafCluster = species.getLeavesProperties().getCellKit().getLeafCluster();
						leafMap.blitMax(pos, leafCluster);
					}
				}
				last = pos;
			}
			return false;
		}
		
	}

}
