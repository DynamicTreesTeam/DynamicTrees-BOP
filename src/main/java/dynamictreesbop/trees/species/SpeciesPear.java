package dynamictreesbop.trees.species;

import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.item.BOPItems;
import biomesoplenty.common.block.BlockBOPLeaves;
import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModConfigs;
import dynamictreesbop.ModContent;
import dynamictreesbop.dropcreators.DropCreatorFruit;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import java.util.Random;

public class SpeciesPear extends Species {

	public static float fruitingOffset = 0f; //summer

	public SpeciesPear(TreeFamily treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.PEAROAK), treeFamily);

		setBasicGrowingParameters(0.2f, 8.0f, upProbability, lowestBranchHeight, 0.8f);

		envFactor(Type.COLD, 0.50f);
		envFactor(Type.DRY, 0.50f);
		
		generateSeed();

		setFlowerSeasonHold(fruitingOffset - 0.5f, fruitingOffset + 0.5f);

		addDropCreator(new DropCreatorFruit(BOPItems.pear));

		//this causes fruit trees to turn back to oak if they are disabled
		setRequiresTileEntity(ModConfigs.enablePearTrees);
		
		treeFamily.addConnectableVanillaLeaves((state) -> state.getBlock() instanceof BlockBOPLeaves && state.getValue(((BlockBOPLeaves) state.getBlock()).variantProperty) == BOPTrees.DEAD);

		//TODO: replace with pear
		addGenFeature(new FeatureGenFruit(ModContent.pearFruit).setRayDistance(4).setFruitingRadius(4));
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

	@Override
	public boolean isBiomePerfect(Biome biome) {
		return BiomeDictionary.hasType(biome, Type.SWAMP);
	}
	
	@Override
	public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random, boolean rapid) {
		if(super.rot(world, pos, neighborCount, radius, random, rapid)) {
			if(radius > 4 && TreeHelper.isRooty(world.getBlockState(pos.down())) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
				world.setBlockState(pos, random.nextInt(3) == 0 ? ModBlocks.blockStates.redMushroom : ModBlocks.blockStates.brownMushroom);//Change branch to a mushroom
				world.setBlockState(pos.down(), ModBlocks.blockStates.podzol);//Change rooty dirt to Podzol
			}
			return true;
		}
		
		return false;
	}
	
}
