package dynamictreesbop.trees;

import java.util.List;

import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.DynamicTree;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CompatHelper;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLog;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
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
			
			setBasicGrowingParameters(0.2f, 8.0f, 4, 4, 0.9f);
			
			setDynamicSapling(new BlockDynamicSapling("palmsapling").getDefaultState());
			
			envFactor(Type.COLD, 0.25f);
			
			addAcceptableSoil(Blocks.SAND, BOPBlocks.grass, BOPBlocks.dirt, BOPBlocks.white_sand);
			
			generateSeed();
			
			setupStandardSeedDropping();
			
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return CompatHelper.biomeHasType(biome, Type.BEACH);
		}
		
		@Override
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
			EnumFacing originDir = signal.dir.getOpposite();
			
			//Alter probability map for direction change
			probMap[0] = 0;//Down is always disallowed for palm
			probMap[1] = 10;
			probMap[2] = probMap[3] = probMap[4] = probMap[5] =  0;
			probMap[originDir.ordinal()] = 0;//Disable the direction we came from
			
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
		
		//Palm trees are so similar that it makes sense to randomize their height for a little variation
		//but we don't want the trees to always be the same height all the time when planted in the same location
		//so we feed the hash function the in-game month
		@Override
		public float getEnergy(World world, BlockPos pos) {
			long day = world.getTotalWorldTime() / 24000L;
			int month = (int)day / 30;//Change the hashs every in-game month
			
			return super.getEnergy(world, pos) * biomeSuitability(world, pos) + (coordHashCode(pos.up(month)) % 6);//Vary the height energy by a psuedorandom hash function
		}
		
		public int coordHashCode(BlockPos pos) {
			int hash = (pos.getX() * 9973 ^ pos.getY() * 8287 ^ pos.getZ() * 9721) >> 1;
			return hash & 0xFFFF;
		}
		
		@Override
		public void postGeneration(World world, BlockPos rootPos, Biome biome, int radius, List<BlockPos> endPoints, boolean worldGen) {
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
