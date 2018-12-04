package dynamictreesbop.trees;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.api.item.BOPItems;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockBOPLog;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.dropcreators.DropCreatorFruit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreeJacaranda extends TreeFamily {
	
	public class SpeciesJacaranda extends Species {
		
		SpeciesJacaranda(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.leaves.get("jacaranda"));
			
			setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.9f);
						
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			
			addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
			
			generateSeed();
			
			addDropCreator(new DropCreatorFruit(BOPItems.peach));
			setupStandardSeedDropping();
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, BOPBiomes.lavender_fields.orNull(), BOPBiomes.mystic_grove.orNull(), Biomes.EXTREME_HILLS, Biomes.EXTREME_HILLS_EDGE, Biomes.EXTREME_HILLS_WITH_TREES);
		}
		
	}
	
	public TreeJacaranda() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "jacaranda"));
		
		IBlockState primLog = BlockBOPLog.paging.getVariantState(BOPWoods.JACARANDA);
		setPrimitiveLog(primLog, BlockBOPLog.paging.getVariantItem(BOPWoods.JACARANDA));
		
		ModContent.leaves.get("jacaranda").setTree(this);
		
		this.addConnectableVanillaLeaves((state) -> {
			return state.getBlock() instanceof BlockBOPLeaves && state.getValue(((BlockBOPLeaves) state.getBlock()).variantProperty) == BOPTrees.JACARANDA;
		});
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesJacaranda(this));
	}
	
}
