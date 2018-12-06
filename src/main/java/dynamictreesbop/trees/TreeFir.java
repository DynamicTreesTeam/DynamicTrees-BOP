package dynamictreesbop.trees;

import com.ferreusveritas.dynamictrees.ModTrees;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenClearVolume;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenConiferTopper;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFlareBottom;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenMound;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockBOPLog;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

public class TreeFir extends TreeFamily {
	
	public class SpeciesMegaFir extends Species {
		
		SpeciesMegaFir(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.leaves.get(ModContent.FIR));
			
			setBasicGrowingParameters(0.3f, 32.0f, 7, 7, 1.0f);
			setGrowthLogicKit(new ConiferLogic(4.5f).setHeightVariation(8));

			setSoilLongevity(14);
						
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.25f);
			envFactor(Type.WET, 0.75f);
			
			addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
			
			generateSeed();
			
			setupStandardSeedDropping();
			
			//Add species features
			addGenFeature(new FeatureGenClearVolume(6));//Clear a spot for the thick tree trunk
			addGenFeature(new FeatureGenConiferTopper(getLeavesProperties()));//Make a topper for this conifer tree
			addGenFeature(new FeatureGenMound(999));//Establish mounds
			addGenFeature(new FeatureGenFlareBottom());//Flare the bottom
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return BiomeDictionary.hasType(biome, Type.CONIFEROUS);
		}
		
	}
	
	public class SpeciesFir extends Species {
		
		SpeciesFir(TreeFamily treeFamily) {
			super(new ResourceLocation(treeFamily.getName().getResourceDomain(), treeFamily.getName().getResourcePath() + "small"), treeFamily, ModContent.leaves.get(ModContent.FIR));
			
			setBasicGrowingParameters(0.3f, 16.0f, 3, 3, 0.9f);
			setGrowthLogicKit(TreeRegistry.findGrowthLogicKit(ModTrees.CONIFER));
			
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.25f);
			envFactor(Type.WET, 0.75f);
			
			addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
			
			setupStandardSeedDropping();
			
			//Add species features
			addGenFeature(new FeatureGenConiferTopper(getLeavesProperties()));//Make a topper for this conifer tree
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return BiomeDictionary.hasType(biome, Type.CONIFEROUS);
		}
		
		@Override
		public int maxBranchRadius() {
			return 8;
		}
		
		@Override
		public boolean isThick() {
			return false;
		}
		
		@Override
		public ItemStack getSeedStack(int qty) {
			return getCommonSpecies().getSeedStack(qty);
		}
		
		@Override
		public Seed getSeed() {
			return getCommonSpecies().getSeed();
		}
		
	}
	
	Species smallSpecies;
	
	public TreeFir() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.FIR));
		
		IBlockState primLog = BlockBOPLog.paging.getVariantState(BOPWoods.FIR);
		setPrimitiveLog(primLog, BlockBOPLog.paging.getVariantItem(BOPWoods.FIR));
		hasConiferVariants = true;
		ModContent.leaves.get(ModContent.FIR).setTree(this);
		
		this.addConnectableVanillaLeaves((state) -> {
			return state.getBlock() instanceof BlockBOPLeaves && state.getValue(((BlockBOPLeaves) state.getBlock()).variantProperty) == BOPTrees.FIR;
		});
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesMegaFir(this));
		smallSpecies = new SpeciesFir(this);
	}
	
	@Override
	public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
		super.registerSpecies(speciesRegistry);
		speciesRegistry.register(smallSpecies);
	}
	
	@Override
	public boolean isThick() {
		return true;
	}
	
}
