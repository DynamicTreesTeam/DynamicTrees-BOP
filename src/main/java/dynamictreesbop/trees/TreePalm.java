package dynamictreesbop.trees;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorSeed;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeFindEnds;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.BranchDestructionData;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.CoordUtils.Surround;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockBOPLog;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.blocks.BlockDynamicLeavesPalm;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.property.IExtendedBlockState;

public class TreePalm extends TreeFamily {
	
	public class SpeciesPalm extends Species {
				
		SpeciesPalm(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.palmLeavesProperties);
			
			setBasicGrowingParameters(0.4f, 9.0f, 4, 3, 0.9f);
						
			envFactor(Type.COLD, 0.25f);
			
			generateSeed();
			
			addDropCreator(new DropCreatorSeed(3.0f) {
				@Override
				public List<ItemStack> getHarvestDrop(World world, Species species, BlockPos leafPos, Random random, List<ItemStack> dropList, int soilLife, int fortune) {
					if(random.nextInt(4) == 0) { // 1 in 4 chance to drop a seed on destruction..	
						dropList.add(species.getSeedStack(1));
					}
					return dropList;
				}
				
				@Override
				public List<ItemStack> getLeavesDrop(IBlockAccess access, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, int fortune) {
					int chance = 4;
					if (fortune > 0) {
						chance -= fortune;
						if (chance < 3) { 
							chance = 3;
						}
					}
					if (random.nextInt(chance) == 0) {
						dropList.add(species.getSeedStack(1));
					}
					return dropList;
				}
			});
		}
		
		@Override
		protected void setStandardSoils() {
			addAcceptableSoils(DirtHelper.DIRTLIKE, DirtHelper.SANDLIKE);
		}
		
		@Override
		public LogsAndSticks getLogsAndSticks(float volume) {
			return super.getLogsAndSticks(volume * 5);
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, BOPBiomes.oasis.orNull(), BOPBiomes.tropical_island.orNull()) || BiomeDictionary.hasType(biome, Type.BEACH);
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
				
		// Palm trees are so similar that it makes sense to randomize their height for a little variation
		// but we don't want the trees to always be the same height all the time when planted in the same location
		// so we feed the hash function the in-game month
		@Override
		public float getEnergy(World world, BlockPos pos) {
			long day = world.getWorldTime() / 24000L;
			int month = (int) day / 30; // Change the hashs every in-game month
			
			return super.getEnergy(world, pos) * biomeSuitability(world, pos) + (CoordUtils.coordHashCode(pos.up(month), 3) % 3); // Vary the height energy by a psuedorandom hash function
		}
		
		@Override
		public boolean postGrow(World world, BlockPos rootPos, BlockPos treePos, int soilLife, boolean natural) {
			IBlockState trunkBlockState = world.getBlockState(treePos);
			BlockBranch branch = TreeHelper.getBranch(trunkBlockState);
			NodeFindEnds endFinder = new NodeFindEnds();
			MapSignal signal = new MapSignal(endFinder);
			branch.analyse(trunkBlockState, world, treePos, EnumFacing.DOWN, signal);
			List<BlockPos> endPoints = endFinder.getEnds();
			
			for (BlockPos endPoint: endPoints) {
				TreeHelper.ageVolume(world, endPoint, 2, 3, 3, SafeChunkBounds.ANY);
			}
			
			// Make sure the bottom block is always just a little thicker that the block above it.
			int radius = branch.getRadius(world.getBlockState(treePos.up()));
			if (radius != 0) {
				branch.setRadius(world, treePos, radius + 1, null);
			}
			
			return super.postGrow(world, rootPos, treePos, soilLife, natural);
		}
		
		@Override
		public void postGeneration(World world, BlockPos rootPos, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
			for (BlockPos endPoint : endPoints) {
				TreeHelper.ageVolume(world, endPoint, 1, 2, 3, safeBounds);
			}
		}
		
		@Override
		public boolean placeRootyDirtBlock(World world, BlockPos rootPos, int life) {
			if (world.getBlockState(rootPos).getMaterial() == Material.SAND) {
				world.setBlockState(rootPos, ModBlocks.blockRootySand.getDefaultState().withProperty(BlockRooty.LIFE, life));
			} else {
				world.setBlockState(rootPos, getRootyBlock(world, rootPos).getDefaultState().withProperty(BlockRooty.LIFE, life));
			}
			return true;
		}
		
	}
	
	public TreePalm() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.PALM));
		
		IBlockState primLog = BlockBOPLog.paging.getVariantState(BOPWoods.PALM);
		setPrimitiveLog(primLog, BlockBOPLog.paging.getVariantItem(BOPWoods.PALM));
		
		ModContent.palmLeavesProperties.setTree(this);
		
		this.addConnectableVanillaLeaves((state) -> {
			return state.getBlock() instanceof BlockBOPLeaves && state.getValue(((BlockBOPLeaves) state.getBlock()).variantProperty) == BOPTrees.PALM;
		});
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesPalm(this));
	}

	@Override
	public float getPrimaryThickness() {
		return 3.0f;
	}

	@Override
	public float getSecondaryThickness() {
		return 3.0f;
	}
	
	@Override
	public HashMap<BlockPos, IBlockState> getFellingLeavesClusters(BranchDestructionData destructionData) {
		
		if(destructionData.getNumEndpoints() < 1) {
			return null;
		}
		
		HashMap<BlockPos, IBlockState> leaves = new HashMap<>();
		BlockPos relPos = destructionData.getEndPointRelPos(0).up();//A palm tree is only supposed to have one endpoint at it's top.
		ILeavesProperties leavesProperties = getCommonSpecies().getLeavesProperties();
		
		leaves.put(relPos, leavesProperties.getDynamicLeavesState(4));//The barky overlapping part of the palm frond cluster
		leaves.put(relPos.up(), leavesProperties.getDynamicLeavesState(3));//The leafy top of the palm frond cluster
		
		//The 4 corners and 4 sides of the palm frond cluster
		for(int hydro = 1; hydro <= 2; hydro++) {
			IExtendedBlockState extState = (IExtendedBlockState) leavesProperties.getDynamicLeavesState(hydro);
			for(Surround surr : BlockDynamicLeavesPalm.hydroSurroundMap[hydro]) {
				leaves.put(relPos.add(surr.getOpposite().getOffset()), extState.withProperty(BlockDynamicLeavesPalm.CONNECTIONS[surr.ordinal()], true));
			}
		}
		
		return leaves;
	}
	
}
