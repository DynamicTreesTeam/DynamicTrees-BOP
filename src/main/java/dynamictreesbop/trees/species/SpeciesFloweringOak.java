package dynamictreesbop.trees.species;

import java.util.Random;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.entities.EntityFallingTree;
import com.ferreusveritas.dynamictrees.models.ModelEntityFallingTree;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.item.BOPItems;
import biomesoplenty.common.block.BlockBOPLeaves;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModConfigs;
import dynamictreesbop.ModContent;
import dynamictreesbop.dropcreators.DropCreatorFruit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import javax.annotation.Nullable;

public class SpeciesFloweringOak extends Species {

	public SpeciesFloweringOak(ResourceLocation name, TreeFamily treeFamily, ILeavesProperties leavesProperties) {
		super(name, treeFamily, leavesProperties);
		setDefaultGrowingParameters();

		setRequiresTileEntity(true);

		treeFamily.addConnectableVanillaLeaves((state) -> state.getBlock() instanceof BlockBOPLeaves && state.getValue(((BlockBOPLeaves) state.getBlock()).variantProperty) == BOPTrees.FLOWERING);
	}

	protected void setDefaultGrowingParameters (){
		setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.85f);

		setupStandardSeedDropping();

		envFactor(Type.COLD, 0.75f);
		envFactor(Type.HOT, 0.50f);
		envFactor(Type.DRY, 0.50f);
		envFactor(Type.FOREST, 1.05f);
	}

	public SpeciesFloweringOak(TreeFamily treeFamily) {
		this(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.FLOWERINGOAK), treeFamily, ModContent.floweringOakLeavesProperties[0]);

		addValidLeavesBlocks(ModContent.floweringOakLeavesProperties);
		ModContent.floweringOakLeavesProperties[0].setTree(treeFamily);
		ModContent.floweringOakLeavesProperties[1].setTree(treeFamily);

		generateSeed();

		if (!ModConfigs.enablePeachTrees)
			addDropCreator(new DropCreatorFruit(BOPItems.peach));
	}

	@Override
	public int colorTreeQuads(int defaultColor, ModelEntityFallingTree.TreeQuadData treeQuad, @Nullable EntityFallingTree entity) {
		return treeQuad.bakedQuad.getTintIndex() != 0 ? 0xffffff : defaultColor;
	}

	@Override
	public boolean isBiomePerfect(Biome biome) {
		return BiomeDictionary.hasType(biome, Type.FOREST);
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
	
	@Override
	public int saplingColorMultiplier(IBlockState state, IBlockAccess access, BlockPos pos, int tintIndex) {
		return tintIndex != 0 ? 0xffffff : getLeavesProperties().foliageColorMultiplier(state, access, pos);
	}
}
