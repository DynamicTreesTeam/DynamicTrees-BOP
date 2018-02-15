package dynamictreesbop.trees;

import java.util.List;

import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.trees.DynamicTree;
import com.ferreusveritas.dynamictrees.trees.Species;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.api.item.BOPItems;
import biomesoplenty.common.block.BlockBOPLog;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.dropcreators.DropCreatorFruit;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreeJacaranda extends DynamicTree {
	
	public class SpeciesJacaranda extends Species {
		
		SpeciesJacaranda(DynamicTree treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.jacarandaLeavesProperties);
			
			setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.9f);
			
			setDynamicSapling(new BlockDynamicSapling("jacarandasapling").getDefaultState());
			
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			
			addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
			
			generateSeed();
			
			addDropCreator(new DropCreatorFruit(BOPItems.peach));
			setupStandardSeedDropping();
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, BOPBiomes.lavender_fields.get(), BOPBiomes.mystic_grove.get(), Biomes.EXTREME_HILLS, Biomes.EXTREME_HILLS_EDGE, Biomes.EXTREME_HILLS_WITH_TREES);
		}
		
	}
	
	public TreeJacaranda() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "jacaranda"));
		
		IBlockState primLog = BlockBOPLog.paging.getVariantState(BOPWoods.JACARANDA);
		setPrimitiveLog(primLog, BlockBOPLog.paging.getVariantItem(BOPWoods.JACARANDA));
		
		ModContent.jacarandaLeavesProperties.setTree(this);
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesJacaranda(this));
	}
	
	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(getCommonSpecies().getDynamicSapling().getBlock());
		return super.getRegisterableBlocks(blockList);
	}

}
