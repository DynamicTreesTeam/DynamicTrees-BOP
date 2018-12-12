package dynamictreesbop.trees.species;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorApple;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenVine;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class SpeciesOakFloweringVine extends Species {
	
	Species baseSpecies;
	
	public SpeciesOakFloweringVine(TreeFamily treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.OAKFLOWERINGVINE), treeFamily);
		
		baseSpecies = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, ModContent.OAK));
		
		setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.8f);
		
		envFactor(Type.COLD, 0.75f);
		envFactor(Type.HOT, 0.75f);
		envFactor(Type.DRY, 0.625f);
		envFactor(Type.FOREST, 1.05f);
		
		addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		
		if(ModConfigs.worldGen && !ModConfigs.enableAppleTrees) {//If we've disabled apple trees we still need some way to get apples.
			addDropCreator(new DropCreatorApple());
		}
		
		setupStandardSeedDropping();
		
		//Add species features
		addGenFeature(new FeatureGenVine().setQuantity(20).setMaxLength(7).setRayDistance(6).setVineBlock(BOPBlocks.ivy));//Generate Ivy
	}
	
	@Override
	public boolean isBiomePerfect(Biome biome) {
		return isOneOfBiomes(biome, BOPBiomes.mystic_grove.orNull());
	}
	
	@Override
	public ItemStack getSeedStack(int qty) {
		return baseSpecies.getSeedStack(qty);
	}
	
	@Override
	public Seed getSeed() {
		return baseSpecies.getSeed();
	}
	
	@Override
	public int maxBranchRadius() {
		return 8;
	}
	
}
