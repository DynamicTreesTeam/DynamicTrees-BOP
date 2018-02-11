package dynamictreesbop.trees.species;

import java.util.Random;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.trees.DynamicTree;
import com.ferreusveritas.dynamictrees.trees.Species;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.item.BOPItems;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.dropcreators.DropCreatorFruit;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class SpeciesYellowAutumn extends Species {
	
	public SpeciesYellowAutumn(DynamicTree treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "yellowautumn"), treeFamily, ModContent.yellowAutumnLeavesProperties);
		
		setBasicGrowingParameters(0.1f, 14.0f, 4, 4, 1.25f);
		
		setDynamicSapling(new BlockDynamicSapling("yellowautumnsapling").getDefaultState());
		
		envFactor(Type.HOT, 0.50f);
		envFactor(Type.DRY, 0.50f);
		envFactor(Type.FOREST, 1.05f);
		
		addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		
		generateSeed();
		
		addDropCreator(new DropCreatorFruit(BOPItems.persimmon));
		setupStandardSeedDropping();
		
		leavesProperties.setTree(treeFamily);
	}
	
	@Override
	public boolean isBiomePerfect(Biome biome) {
		return isOneOfBiomes(biome, BOPBiomes.boreal_forest.get(), BOPBiomes.seasonal_forest.get());
	}
	
	@Override
	public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random) {
		if(super.rot(world, pos, neighborCount, radius, random)) {
			if(radius > 4 && TreeHelper.isRooty(world, pos.down()) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
				world.setBlockState(pos, Blocks.BROWN_MUSHROOM.getDefaultState());//Change branch to a brown mushroom
				world.setBlockState(pos.down(), Blocks.DIRT.getDefaultState(), 3);//Change rooty dirt to dirt
			}
			return true;
		}
		
		return false;
	}
	
}
