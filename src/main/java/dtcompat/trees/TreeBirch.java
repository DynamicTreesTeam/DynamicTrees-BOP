package dtcompat.trees;

import java.util.List;
import java.util.Random;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockRootyDirt;
import com.ferreusveritas.dynamictrees.trees.DynamicTree;
import com.ferreusveritas.dynamictrees.trees.Species;

import biomesoplenty.api.block.BOPBlocks;
import dtcompat.DynamicTreesCompat;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TreeBirch extends DynamicTree {
	
	public class SpeciesBirch extends Species {
		
		Species baseSpecies = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "birch"));

		SpeciesBirch(DynamicTree treeFamily) {
			super(treeFamily.getName(), treeFamily);
			
			//Birch are tall, skinny, fast growing trees
			setBasicGrowingParameters(0.1f, 14.0f, 4, 4, 1.25f);
			
			setDynamicSapling(baseSpecies.getDynamicSapling());
			setSeedStack(baseSpecies.getSeedStack(1));
			
			envFactor(Type.COLD, 0.75f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);
			
			setupStandardSeedDropping();
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return BiomeDictionary.hasType(biome, Type.FOREST);
		}
		
		@Override
		public boolean isAcceptableSoil(World world, BlockPos pos, IBlockState soilBlockState) {
			Block soilBlock = soilBlockState.getBlock();
			return soilBlock == Blocks.DIRT || soilBlock == Blocks.GRASS || soilBlock == Blocks.MYCELIUM || soilBlock instanceof BlockRootyDirt || soilBlock == BOPBlocks.grass || soilBlock == BOPBlocks.dirt;
		}
		
	}
		
	public TreeBirch(int seq) {
		super(new ResourceLocation(DynamicTreesCompat.MODID, "birch"), seq);
		
		BlockPlanks.EnumType type = BlockPlanks.EnumType.BIRCH;
		
		IBlockState primLeaves = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, type);
		IBlockState primLog = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, type);
		setPrimitiveLeaves(primLeaves, new ItemStack(primLeaves.getBlock(), 1, primLeaves.getValue(BlockOldLeaf.VARIANT).getMetadata() & 3));
		setPrimitiveLog(primLog, new ItemStack(primLog.getBlock(), 1, primLog.getValue(BlockOldLog.VARIANT).getMetadata() & 3));
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesBirch(this));
	}
	
	@Override
	public List<Item> getRegisterableItems(List<Item> itemList) {
		//Register an itemBlock for the branch block
		itemList.add(new ItemBlock(getDynamicBranch()).setRegistryName(getDynamicBranch().getRegistryName()));
		
		return itemList;
	}
	
	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		return super.getRegisterableBlocks(blockList);
	}
	
	@Override
	public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random) {
		if(super.rot(world, pos, neighborCount, radius, random)) {
			if(radius > 4 && TreeHelper.isRootyDirt(world, pos.down()) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
				world.setBlockState(pos, Blocks.BROWN_MUSHROOM.getDefaultState());//Change branch to a brown mushroom
				world.setBlockState(pos.down(), Blocks.DIRT.getDefaultState(), 3);//Change rooty dirt to dirt
			}
			return true;
		}
		
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
		return ColorizerFoliage.getFoliageColorBirch();
	}
	
}
