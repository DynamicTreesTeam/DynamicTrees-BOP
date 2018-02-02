package dynamictreesbop.trees;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.treedata.ITreePart;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.blocks.BlockRootyDirt;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeFindEnds;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeInflator;
import com.ferreusveritas.dynamictrees.trees.DynamicTree;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.MathHelper;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap.Cell;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.worldgen.JoCode;
import com.ferreusveritas.dynamictrees.worldgen.TreeCodeStore;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockBOPLog;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.trees.TreeMagic.SpeciesMagic;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreeDecayed extends DynamicTree {
	
	public class SpeciesDecayed extends Species {
		
		SpeciesDecayed(DynamicTree treeFamily) {
			super(treeFamily.getName(), treeFamily);
			
			setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 1.0f);
		}
		
		@Override
		public boolean isAcceptableSoil(World world, BlockPos pos, IBlockState soilBlockState) {
			Block soilBlock = soilBlockState.getBlock();
			return soilBlock == Blocks.DIRT || soilBlock == Blocks.GRASS || soilBlock == Blocks.MYCELIUM || soilBlock instanceof BlockRootyDirt || soilBlock == BOPBlocks.grass || soilBlock == BOPBlocks.dirt || soilBlock == BOPBlocks.dried_sand;
		}
		
		@Override
		public boolean grow(World world, BlockRootyDirt rootyDirt, BlockPos rootPos, int soilLife, ITreePart treeBase, BlockPos treePos, Random random, boolean rapid) {
			return false;
		}
		
		@Override
		public boolean canGrowWithBoneMeal(World world, BlockPos pos) {
			return false;
		}
		
		@Override
		public boolean canUseBoneMealNow(World world, Random rand, BlockPos pos) {
			return false;
		}
		
		@Override
		public boolean generate(World world, BlockPos pos, Biome biome, Random random, int radius) {
			EnumFacing facing = CoordUtils.getRandomDir(random);
			if(getJoCodeStore() != null) {
				JoCode code = getJoCodeStore().getRandomCode(radius, random);
				if(code != null) {
					code.generate(world, this, pos, biome, facing, radius);
					return true;
				}
			}
			
			return false;
		}
		
	}
	
	public TreeDecayed() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "decayed"), -1);
		
		IBlockState primLog = BlockBOPLog.paging.getVariantState(BOPWoods.DEAD);
		setPrimitiveLog(primLog, BlockBOPLog.paging.getVariantItem(BOPWoods.DEAD));
		
		IBlockState primLeaves = BlockBOPLeaves.paging.getVariantState(BOPTrees.DEAD);
		setPrimitiveLeaves(primLeaves, BlockBOPLeaves.paging.getVariantItem(BOPTrees.DEAD));
		
		setCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "bare"));
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesDecayed(this));
	}
	
	@Override
	public BlockDynamicLeaves getDynamicLeaves() {
		return null;
	}
	
	@Override
	public IBlockState getDynamicLeavesState() {
		return Blocks.AIR.getDefaultState();
	}
	
	@Override
	public IBlockState getDynamicLeavesState(int hydro) {
		return getDynamicLeavesState();
	}
	
	@Override
	public boolean isCompatibleDynamicLeaves(IBlockAccess blockAccess, BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean isCompatibleDynamicLeaves(Block leaves, int sub) {
		return false;
	}
	
	@Override
	public boolean isCompatibleVanillaLeaves(IBlockAccess blockAccess, BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean isCompatibleGenericLeaves(IBlockAccess blockAccess, BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random) {
		return false;
	}

}
