package dynamictreesbop.trees;

import java.util.List;
import java.util.Random;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.blocks.BlockRootyDirt;
import com.ferreusveritas.dynamictrees.trees.DynamicTree;
import com.ferreusveritas.dynamictrees.trees.Species;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.api.item.BOPItems;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockBOPLog;
import biomesoplenty.common.block.BlockBOPMushroom;
import biomesoplenty.common.block.BlockBOPPlant;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.dropcreators.DropCreatorFruit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreeMagic extends DynamicTree {
	
	public class SpeciesMagic extends Species {
		
		SpeciesMagic(DynamicTree treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.magicLeavesProperties);
			
			setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 1.0f);
			
			setDynamicSapling(new BlockDynamicSapling("magicsapling").getDefaultState());
			
			envFactor(Type.COLD, 0.75f);
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);
			
			addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
			
			generateSeed();
			
			setupStandardSeedDropping();
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, BOPBiomes.mystic_grove.get());
		}
		
		@Override
		public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random) {
			if(super.rot(world, pos, neighborCount, radius, random)) {
				if(radius > 4 && TreeHelper.isRooty(world, pos.down()) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
					world.setBlockState(pos, random.nextInt(3) == 0 ? ModBlocks.blockStates.redMushroom : BOPBlocks.mushroom.getDefaultState().withProperty(BlockBOPMushroom.VARIANT, BlockBOPMushroom.MushroomType.BLUE_MILK_CAP));//Change branch to a mushroom
					world.setBlockState(pos.down(), ModBlocks.blockStates.podzol);//Change rooty dirt to Podzol
				}
				return true;
			}
			
			return false;
		}
		
	}
	
	public TreeMagic() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "magic"));
		
		IBlockState primLog = BlockBOPLog.paging.getVariantState(BOPWoods.MAGIC);
		setPrimitiveLog(primLog, BlockBOPLog.paging.getVariantItem(BOPWoods.MAGIC));
		
		ModContent.magicLeavesProperties.setTree(this);
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesMagic(this));
	}
	
	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(getCommonSpecies().getDynamicSapling().getBlock());
		return super.getRegisterableBlocks(blockList);
	}
	
	@Override
	public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
		return 0xffffff;
	}

}
