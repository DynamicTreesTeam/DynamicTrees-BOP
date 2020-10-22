package dynamictreesbop.trees;

import com.ferreusveritas.dynamictrees.api.IGenFeature;
import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenBush;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenConiferTopper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockBOPLog;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreePine extends TreeFamily {
	
	public class SpeciesPine extends Species {
				
		SpeciesPine(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.leaves.get(ModContent.PINE));
			
			setBasicGrowingParameters(0.3f, 16.0f, 4, 4, 0.9f);
			setGrowthLogicKit(new ConiferLogic(6.0f).setHorizontalLimiter(1.8f).setHeightVariation(6));
			
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.25f);
			envFactor(Type.WET, 0.75f);
			
			generateSeed();
			
			setupStandardSeedDropping();
			
			addGenFeature(new FeatureGenConiferTopper(getLeavesProperties()));//Make a topper for this conifer tree
			addGenFeature(new FeatureGenBush().setBiomePredicate(b -> b == BOPBiomes.shield.orNull()), IGenFeature.POSTGEN);//Generate undergrowth
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return BiomeDictionary.hasType(biome, Type.CONIFEROUS);
		}
				
	}
	
	public TreePine() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.PINE));
		
		IBlockState primLog = BlockBOPLog.paging.getVariantState(BOPWoods.PINE);
		setPrimitiveLog(primLog, BlockBOPLog.paging.getVariantItem(BOPWoods.PINE));
		
		ModContent.leaves.get(ModContent.PINE).setTree(this);
		hasConiferVariants = true;
		
		this.addConnectableVanillaLeaves((state) -> {
			return state.getBlock() instanceof BlockBOPLeaves && state.getValue(((BlockBOPLeaves) state.getBlock()).variantProperty) == BOPTrees.PINE;
		});
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesPine(this));
	}
	
}
