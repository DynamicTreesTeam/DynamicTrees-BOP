package dynamictreesbop.trees;

import java.util.List;
import java.util.function.BiFunction;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.treedata.ITreePart;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchThick;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.blocks.BlockSurfaceRoot;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenClearVolume;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFlareBottom;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenMound;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenRoots;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.CoordUtils;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockBOPLog;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreeRedwood extends TreeFamily {

	public class SpeciesRedwood extends Species {
		
		SpeciesRedwood(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.redwoodLeavesProperties);
			
			setBasicGrowingParameters(0.27f, 38.0f, 24, 22, 1.33f);
			
			setSoilLongevity(53);
			
			setDynamicSapling(new BlockDynamicSapling("redwoodsapling").getDefaultState());
			
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.25f);
			envFactor(Type.FOREST, 1.05f);
			
			addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
			
			generateSeed();
			
			setupStandardSeedDropping();
			
			//Add species features
			addGenFeature(new FeatureGenClearVolume(6));//Clear a spot for the thick tree trunk
			addGenFeature(new FeatureGenMound(this, 6));//Establish mounds
			addGenFeature(new FeatureGenFlareBottom(this));//Flare the bottom
			addGenFeature(new FeatureGenRoots(this, 15).setScaler(getRootScaler()));//Finally Generate Roots
		}
		
		protected BiFunction<Integer, Integer, Integer> getRootScaler() {
			return (inRadius, trunkRadius) -> {
				float scale = MathHelper.clamp(trunkRadius >= 15 ? (trunkRadius / 24f) : 0, 0, 1);
				return (int) (inRadius * scale);
			};
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, BOPBiomes.redwood_forest.orNull());
		}
		
		@Override
		public EnumFacing selectNewDirection(World world, BlockPos pos, BlockBranch branch, GrowSignal signal) {
			EnumFacing originDir = signal.dir.getOpposite();
			int signalY = signal.delta.getY();
			
			if (signalY <= 2) return EnumFacing.UP; // prevent branches on the ground
			
			int probMap[] = new int[6]; // 6 directions possible DUNSWE
			
			// Probability taking direction into account
			probMap[EnumFacing.UP.ordinal()] = signal.dir != EnumFacing.DOWN ? getUpProbability(): 0;//Favor up
			probMap[signal.dir.ordinal()] += getReinfTravel(); //Favor current direction
			
			int radius = branch.getRadius(world.getBlockState(pos));
			
			if (signal.delta.getY() < getLowestBranchHeight() - 3) {
				long day = world.getWorldTime() / 24000L;
				int month = (int) day / 30; // Change the hashs every in-game month
				
				int treeHash = CoordUtils.coordHashCode(signal.rootPos.up(month), 3);
				int posHash = CoordUtils.coordHashCode(pos.up(month), 2);
				
				int hashMod = signalY < 7 ? 3 : 11;
				boolean sideTurn = !signal.isInTrunk() || (signal.isInTrunk() && ((signal.numSteps + treeHash) % hashMod == 0) && (radius > 1)); // Only allow turns when we aren't in the trunk(or the branch is not a twig)
				
				if (!sideTurn) return EnumFacing.UP;

				probMap[2 + (posHash % 4)] = 1;
			}
			
			// Create probability map for direction change
			for (EnumFacing dir: EnumFacing.VALUES) {
				if (!dir.equals(originDir)) {
					BlockPos deltaPos = pos.offset(dir);
					// Check probability for surrounding blocks
					// Typically Air:1, Leaves:2, Branches: 2+r
					if (signalY >= getLowestBranchHeight()) {
						IBlockState deltaBlockState = world.getBlockState(deltaPos);
						ITreePart treePart = TreeHelper.getTreePart(deltaBlockState);
					
						probMap[dir.getIndex()] += treePart.probabilityForBlock(deltaBlockState, world, deltaPos, branch);
					}
				}
			}
			
			//Do custom stuff or override probability map for various species
			probMap = customDirectionManipulation(world, pos, radius, signal, probMap);
			
			//Select a direction from the probability map
			int choice = com.ferreusveritas.dynamictrees.util.MathHelper.selectRandomFromDistribution(signal.rand, probMap);//Select a direction from the probability map
			return newDirectionSelected(EnumFacing.getFront(choice != -1 ? choice : 1), signal);//Default to up if things are screwy
		}
		
		@Override
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
			EnumFacing originDir = signal.dir.getOpposite();
			
			// Alter probability map for direction change
			probMap[0] = 0; // Down is always disallowed
			probMap[1] = signal.isInTrunk() || signal.delta.getY() >= getLowestBranchHeight() ? getUpProbability() : 1;
			//probMap[2] = probMap[3] = probMap[4] = probMap[5] = //Only allow turns when we aren't in the trunk(or the branch is not a twig and step is odd)
			//		!signal.isInTrunk() || (signal.isInTrunk() && signal.numSteps % 2 == 1 && radius > 1) ? 2 : 0;
			probMap[originDir.ordinal()] = 0; // Disable the direction we came from
			
			return probMap;
		}
		
		@Override
		protected EnumFacing newDirectionSelected(EnumFacing newDir, GrowSignal signal) {
			if (signal.isInTrunk() && newDir != EnumFacing.UP) { // Turned out of trunk
				int signalY = signal.delta.getY();
				if (signalY < getLowestBranchHeight()) {
					signal.energy = 0.9f + ((1f - ((float) signalY / getLowestBranchHeight())) * 3.7f);
				} else {
					signal.energy += 5;
					signal.energy /= 4.8f;
					if (signal.energy > 5.8f) signal.energy = 5.8f;
				}
			}
			return newDir;
		}
		
		// Redwood trees are so similar that it makes sense to randomize their height for a little variation
		// but we don't want the trees to always be the same height all the time when planted in the same location
		// so we feed the hash function the in-game month
		@Override
		public float getEnergy(World world, BlockPos pos) {
			long day = world.getWorldTime() / 24000L;
			int month = (int) day / 30; // Change the hashs every in-game month
			
			return super.getEnergy(world, pos) * biomeSuitability(world, pos) + (coordHashCode(pos.up(month)) % 16); // Vary the height energy by a psuedorandom hash function
		}
		
		@Override
		public int getLowestBranchHeight(World world, BlockPos pos) {
			long day = world.getWorldTime() / 24000L;
			int month = (int) day / 30; // Change the hashs every in-game month
		
			return (int) ((getLowestBranchHeight() + ((coordHashCode(pos.up(month)) % 16) * 0.5f)) * biomeSuitability(world, pos));
		}
		
		public int getWorldGenLeafMapHeight() {
			return 60;
		}
		
	}
	
	
	BlockSurfaceRoot surfaceRootBlock;
	
	public TreeRedwood() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "redwood"));
		
		IBlockState primLog = BlockBOPLog.paging.getVariantState(BOPWoods.REDWOOD);
		setPrimitiveLog(primLog, BlockBOPLog.paging.getVariantItem(BOPWoods.REDWOOD));
		
		ModContent.redwoodLeavesProperties.setTree(this);
		
		surfaceRootBlock = new BlockSurfaceRoot(Material.WOOD, getName() + "root");
		
		this.addConnectableVanillaLeaves((state) -> {
			return state.getBlock() instanceof BlockBOPLeaves && state.getValue(((BlockBOPLeaves) state.getBlock()).variantProperty) == BOPTrees.REDWOOD;
		});
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesRedwood(this));
	}
	
	@Override
	public boolean isThick() {
		return true;
	}
	
	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList = super.getRegisterableBlocks(blockList);
		blockList.add(getCommonSpecies().getDynamicSapling().getBlock());
		blockList.add(surfaceRootBlock);
		return blockList;
	}
	
	@Override
	public BlockSurfaceRoot getSurfaceRoots() {
		return surfaceRootBlock;
	}
	
	@Override
	public BlockBranch createBranch() {
		String branchName = getName() + "branch";
		return new BlockBranchRedwood(branchName);
	}
	
	protected class BlockBranchRedwood extends BlockBranchThick {
		
		public BlockBranchRedwood(String name) {
			this(Material.WOOD, name);
		}
		
		public BlockBranchRedwood(Material material, String name) {
			super(material, name, false);
			otherBlock = new BlockBranchRedwood(material, name + "x", true);
			otherBlock.otherBlock = this;
			
			cacheBranchThickStates();
		}
		
		protected BlockBranchRedwood(Material material, String name, boolean extended) {
			super(material, name, extended);
		}
		
		/**
		 * This is a recursive algorithm used to explore the branch network.  It calls a run() function for the signal on the way out
		 * and a returnRun() on the way back.
		 * 
		 * Okay so a little explanation here..  
		 * I've been hit up by people who claim that recursion is a bad idea.  The reason why they think this is because java has to push values
		 * on the stack for each level of recursion and then pop them off as the levels complete.  Many time this can lead to performance issues.
		 * Fine, I understand that.  The reason why it doesn't matter here is because of the object oriented nature of how the tree parts
		 * function demand that a different analyze function be called for each object type.  Even if this were rewritten to be iterative the
		 * same number of stack pushes and pops would need to be performed to run the custom function for each node in the network anyway.  The
		 * depth of recursion for this algorithm is less than 32.  So there's no real risk of a stack overflow.
		 * 
		 * The difference being that in an iterative design I would need to maintain a stack array holding all of the values and push and pop
		 * them manually or use a stack index.  This is messy and not something I would want to maintain for practically non-existent gains.
		 * Java does a pretty good job of managing the stack on its own.
		 */
		@Override
		public MapSignal analyse(IBlockState blockState, World world, BlockPos pos, EnumFacing fromDir, MapSignal signal) {
			// Note: fromDir will be null in the origin node
			if (signal.depth++ < 64) { // Prevents going too deep into large networks, or worse, being caught in a network loop
				signal.run(blockState, world, pos, fromDir); // Run the inspectors of choice
				for (EnumFacing dir : EnumFacing.VALUES) { // Spread signal in various directions
					if (dir != fromDir) { // don't count where the signal originated from
						BlockPos deltaPos = pos.offset(dir);
						
						IBlockState deltaState = world.getBlockState(deltaPos);
						ITreePart treePart = TreeHelper.getTreePart(deltaState);
						
						if (treePart.shouldAnalyse()) {
							signal = treePart.analyse(deltaState, world, deltaPos, dir.getOpposite(), signal);
						
							// This should only be true for the originating block when the root node is found
							if (signal.found && signal.localRootDir == null && fromDir == null) {
								signal.localRootDir = dir;
							}
						}
					}
				}
				signal.returnRun(blockState, world, pos, fromDir);
			} else {
				world.setBlockToAir(pos); // Destroy one of the offending nodes
				signal.overflow = true;
			}
			signal.depth--;
			
			return signal;
		}
		
	}
	
}
