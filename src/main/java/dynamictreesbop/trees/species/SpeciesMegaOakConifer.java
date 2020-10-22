package dynamictreesbop.trees.species;

import java.util.Random;

import com.ferreusveritas.dynamictrees.api.IGenFeature;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenBush;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenClearVolume;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenConiferTopper;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFlareBottom;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenMound;
import com.ferreusveritas.dynamictrees.trees.SpeciesRare;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.common.block.BlockBOPMushroom;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class SpeciesMegaOakConifer extends SpeciesRare {
		
	public SpeciesMegaOakConifer(TreeFamily treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.MEGAOAKCONIFER), treeFamily, ModContent.leaves.get(ModContent.OAKCONIFER));
		
		setBasicGrowingParameters(0.3f, 32.0f, 7, 7, 1.0f);
		setGrowthLogicKit(new ConiferLogic().setHeightVariation(8));
		
		setSoilLongevity(14);
		
		envFactor(Type.COLD, 0.75f);
		envFactor(Type.HOT, 0.5f);
		envFactor(Type.PLAINS, 1.05f);
		envFactor(Type.FOREST, 1.05f);
		
		setupStandardSeedDropping();
		
		//Add species features
		addGenFeature(new FeatureGenClearVolume(6));//Clear a spot for the thick tree trunk
		addGenFeature(new FeatureGenConiferTopper(getLeavesProperties()));//Make a topper for this conifer tree
		addGenFeature(new FeatureGenMound(999));//Establish mounds
		addGenFeature(new FeatureGenFlareBottom());//Flare the bottom
		addGenFeature(new FeatureGenBush(), IGenFeature.POSTGEN);//Generate undergrowth
	}
	
	@Override
	public boolean isBiomePerfect(Biome biome) {
		return biome == BOPBiomes.temperate_rainforest.orNull();
	}
	
	@Override
	public ItemStack getSeedStack(int qty) {
		return getFamily().getCommonSpecies().getSeedStack(qty);
	}
	
	@Override
	public Seed getSeed() {
		return getFamily().getCommonSpecies().getSeed();
	}
	
	@Override
	public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random, boolean rapid) {
		if(super.rot(world, pos, neighborCount, radius, random, rapid)) {
			if(radius > 4 && TreeHelper.isRooty(world.getBlockState(pos.down())) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
				world.setBlockState(pos, BOPBlocks.mushroom.getDefaultState().withProperty(BlockBOPMushroom.VARIANT, BlockBOPMushroom.MushroomType.BLUE_MILK_CAP));//Change branch to a mushroom
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isThick() {
		return true;
	}
	
}
