package dynamictreesbop.trees.species;

import biomesoplenty.api.item.BOPItems;
import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.entities.EntityFallingTree;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.models.ModelEntityFallingTree;
import com.ferreusveritas.dynamictrees.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModConfigs;
import dynamictreesbop.ModContent;
import dynamictreesbop.dropcreators.DropCreatorFruit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nullable;
import java.util.Random;

public class SpeciesFloweringApple extends SpeciesFloweringOak {

	public static final int PEACH_FLOWER_COLOR = 0xffb8ec;

	public static float fruitingOffset = 0f; //summer

	public SpeciesFloweringApple(TreeFamily treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.FLOWERINGAPPLE), treeFamily, ModContent.floweringOakLeavesProperties[0]);

		addValidLeavesBlocks(ModContent.floweringOakLeavesProperties);

		setFlowerSeasonHold(fruitingOffset - 0.5f, fruitingOffset + 0.5f);

		addDropCreator(new DropCreatorFruit(Items.APPLE));

		addGenFeature(new FeatureGenFruit(ModBlocks.blockApple).setRayDistance(4));
	}

	@Override
	protected void setDefaultGrowingParameters() {
		this.setBasicGrowingParameters(0.4F, 10.0F, 1, 4, 0.7F);

		this.envFactor(BiomeDictionary.Type.COLD, 0.75F);
		this.envFactor(BiomeDictionary.Type.HOT, 0.75F);
		this.envFactor(BiomeDictionary.Type.DRY, 0.25F);
	}

	@Override
	public float seasonalFruitProductionFactor(World world, BlockPos pos) {
		float offset = fruitingOffset;
		return SeasonHelper.globalSeasonalFruitProductionFactor(world, pos, offset);
	}

	@Override
	public boolean testFlowerSeasonHold(World world, BlockPos pos, float seasonValue) {
		return SeasonHelper.isSeasonBetween(seasonValue, flowerSeasonHoldMin, flowerSeasonHoldMax);
	}

	private static Species getApple(){
		return TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "apple"));
	}

	@Override
	public Seed getSeed() {
		return getApple().getSeed();
	}

	@Override
	public ItemStack getSeedStack(int qty) {
		return getApple().getSeedStack(qty);
	}
}
