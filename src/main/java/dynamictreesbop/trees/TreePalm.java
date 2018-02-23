package dynamictreesbop.trees;

import java.util.List;
import java.util.Random;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorLogs;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeFindEnds;
import com.ferreusveritas.dynamictrees.trees.DynamicTree;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CompatHelper;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLog;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreePalm extends DynamicTree {
	
	public class SpeciesPalm extends Species {
				
		SpeciesPalm(DynamicTree treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.palmLeavesProperties);
			
			setBasicGrowingParameters(0.4f, 7.0f, 4, 4, 0.9f);
			
			setDynamicSapling(new BlockDynamicSapling("palmsapling").getDefaultState());
			
			envFactor(Type.COLD, 0.25f);
			
			clearAcceptableSoils();
			addAcceptableSoil(Blocks.SAND, BOPBlocks.white_sand);
			
			generateSeed();
			
			setupStandardSeedDropping();
			remDropCreator(new ResourceLocation(ModConstants.MODID, "logs"));
			addDropCreator(new DropCreatorLogs() {
				@Override
				public List<ItemStack> getLogsDrop(World world, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, int volume) {
					dropList.add(species.getTree().getPrimitiveLogItemStack(volume / 768)); // A log contains 4096 voxels of wood material(16x16x16 pixels) Drop vanilla logs or whatever
					dropList.add(species.getTree().getStick((volume % 768) / 96)); // A stick contains 512 voxels of wood (1/8th log) (1 log = 4 planks, 2 planks = 4 sticks) Give him the stick!
					return dropList;
				}
			});
		}
		
		@Override
		public BlockRooty getRootyBlock() {
			return ModBlocks.blockRootySand;
		}

		//Let the worldgen plant on dirt blocks.
		@Override
		public boolean isAcceptableSoilForWorldgen(World world, BlockPos pos, IBlockState soilBlockState) {
			return soilBlockState.getBlock() instanceof BlockGrass ? true : super.isAcceptableSoilForWorldgen(world, pos, soilBlockState);
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return CompatHelper.biomeHasType(biome, Type.BEACH);
		}
		
		@Override
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
			EnumFacing originDir = signal.dir.getOpposite();
			
			// Alter probability map for direction change
			probMap[0] = 0; // Down is always disallowed for palm
			probMap[1] = 10;
			probMap[2] = probMap[3] = probMap[4] = probMap[5] =  0;	
			probMap[originDir.ordinal()] = 0; // Disable the direction we came from
			
			return probMap;
		}
		
		@Override
		protected EnumFacing newDirectionSelected(EnumFacing newDir, GrowSignal signal) {
			if (signal.isInTrunk() && newDir != EnumFacing.UP) { // Turned out of trunk
				signal.energy /= 6.0f;
				if (signal.energy > 1.8f) signal.energy = 1.8f;
			}
			return newDir;
		}
		
		// Palm trees are so similar that it makes sense to randomize their height for a little variation
		// but we don't want the trees to always be the same height all the time when planted in the same location
		// so we feed the hash function the in-game month
		@Override
		public float getEnergy(World world, BlockPos pos) {
			long day = world.getTotalWorldTime() / 24000L;
			int month = (int) day / 30; // Change the hashs every in-game month
			
			return super.getEnergy(world, pos) * biomeSuitability(world, pos) + (coordHashCode(pos.up(month)) % 3); // Vary the height energy by a psuedorandom hash function
		}
		
		public int coordHashCode(BlockPos pos) {
			int hash = (pos.getX() * 9973 ^ pos.getY() * 8287 ^ pos.getZ() * 9721) >> 1;
			return hash & 0xFFFF;
		}
		
		@Override
		public boolean generate(World world, BlockPos pos, Biome biome, Random random, int radius) {
			return super.generate(world, pos, biome, random, radius);
		}
		
		@Override
		public boolean postGrow(World world, BlockPos rootPos, BlockPos treePos, int soilLife, boolean rapid) {
			IBlockState trunkBlockState = world.getBlockState(treePos); 
			BlockBranch branch = TreeHelper.getBranch(trunkBlockState);
			NodeFindEnds endFinder = new NodeFindEnds();
			MapSignal signal = new MapSignal(endFinder);
			branch.analyse(trunkBlockState, world, treePos, EnumFacing.DOWN, signal);
			List<BlockPos> endPoints = endFinder.getEnds();
			
			for (BlockPos endPoint: endPoints) {
				TreeHelper.ageVolume(world, endPoint, 1, 2, null, 3);
			}
			
			// Make sure the bottom block is always just a little thicker that the block above it.
			int radius = branch.getRadius(world.getBlockState(treePos.up()), world, treePos.up());
			if (radius != 0) {
				branch.setRadius(world, treePos, radius + 1, null);
			}
			
			return super.postGrow(world, rootPos, treePos, soilLife, rapid);
		}
		
		@Override
		public void postGeneration(World world, BlockPos rootPos, Biome biome, int radius, List<BlockPos> endPoints, boolean worldGen) {
			for (BlockPos endPoint: endPoints) {
				TreeHelper.ageVolume(world, endPoint, 1, 2, null, 3);
			}
		}

		@Override
		public float getPrimaryThickness() {
			return 3.0f;
		}

		@Override
		public float getSecondaryThickness() {
			return 3.0f;
		}
		
	}
	
	public TreePalm() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "palm"));
		
		IBlockState primLog = BlockBOPLog.paging.getVariantState(BOPWoods.PALM);
		setPrimitiveLog(primLog, BlockBOPLog.paging.getVariantItem(BOPWoods.PALM));
		
		ModContent.palmLeavesProperties.setTree(this);
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesPalm(this));
	}
	
	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(getCommonSpecies().getDynamicSapling().getBlock());
		return super.getRegisterableBlocks(blockList);
	}
	
	
}
