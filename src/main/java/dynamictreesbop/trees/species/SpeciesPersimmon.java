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

public class SpeciesPersimmon extends Species {

	public static float fruitingOffset = 1.5f; //autumn-winter

	public SpeciesPersimmon(TreeFamily treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.PERSIMMONOAK), treeFamily, ModContent.leaves.get(ModContent.OAKDYING));
		
		setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.65f);
				
		envFactor(Type.LUSH, 0.75f);
		envFactor(Type.SPOOKY, 1.05f);
		envFactor(Type.DEAD, 1.05f);
		
		generateSeed();

		setFlowerSeasonHold(fruitingOffset - 0.5f, fruitingOffset + 0.5f);

		addDropCreator(new DropCreatorFruit(BOPItems.persimmon));

		//this causes fruit trees to turn back to oak if they are disabled
		setRequiresTileEntity(ModConfigs.enablePersimmonTrees);
		
		leavesProperties.setTree(treeFamily);
		
		treeFamily.addConnectableVanillaLeaves((state) -> state.getBlock() instanceof BlockBOPLeaves && state.getValue(((BlockBOPLeaves) state.getBlock()).variantProperty) == BOPTrees.DEAD);

		//TODO: replace with persimmon
		addGenFeature(new FeatureGenFruit(ModContent.persimmonFruit){
			@Override
			public int getQuantity(boolean worldGen) {
				return 10;
			}
		}.setRayDistance(4));
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
		return BiomeDictionary.hasType(biome, Type.DEAD);
	}
	
	@Override
	public int maxBranchRadius() {
		return 8;
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
