package dynamictreesbop.trees;

import java.util.List;
import java.util.Random;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
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
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.blocks.BlockBranchDTBOP;
import dynamictreesbop.dropcreators.DropCreatorFruit;
import dynamictreesbop.trees.TreeMagic.SpeciesMagic;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreeJacaranda extends DynamicTree {
	
	public class SpeciesJacaranda extends Species {
		
		SpeciesJacaranda(DynamicTree treeFamily) {
			super(treeFamily.getName(), treeFamily);
			
			setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.9f);
			
			setDynamicSapling(new BlockDynamicSapling("jacarandasapling").getDefaultState());
			
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			
			addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
			
			addDropCreator(new DropCreatorFruit(BOPItems.peach));
			setupStandardSeedDropping();
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, BOPBiomes.lavender_fields.get(), BOPBiomes.mystic_grove.get(), Biomes.EXTREME_HILLS, Biomes.EXTREME_HILLS_EDGE, Biomes.EXTREME_HILLS_WITH_TREES);
		}
		
	}
	
	public TreeJacaranda(int seq) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "jacaranda"), seq);
		
		IBlockState primLog = BlockBOPLog.paging.getVariantState(BOPWoods.JACARANDA);
		setPrimitiveLog(primLog, BlockBOPLog.paging.getVariantItem(BOPWoods.JACARANDA));
		
		IBlockState primLeaves = BlockBOPLeaves.paging.getVariantState(BOPTrees.JACARANDA);
		setPrimitiveLeaves(primLeaves, BlockBOPLeaves.paging.getVariantItem(BOPTrees.JACARANDA));
		
		setDynamicBranch(new BlockBranchDTBOP("jacaranda" + "branch"));
	}
	
	@Override
	protected DynamicTree setDynamicLeaves(String modid, int seq) {
		return setDynamicLeaves(DynamicTreesBOP.getLeavesBlockForSequence(modid, seq), seq & 3);
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesJacaranda(this));
		getCommonSpecies().generateSeed();
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