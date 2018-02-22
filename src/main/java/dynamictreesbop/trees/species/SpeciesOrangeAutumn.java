package dynamictreesbop.trees.species;

import java.util.Random;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSaplingRare;
import com.ferreusveritas.dynamictrees.trees.DynamicTree;
import com.ferreusveritas.dynamictrees.trees.SpeciesRare;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.item.BOPItems;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.dropcreators.DropCreatorFruit;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class SpeciesOrangeAutumn extends SpeciesRare {
	
	public SpeciesOrangeAutumn(DynamicTree treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "orangeautumn"), treeFamily, ModContent.orangeAutumnLeavesProperties);
		
		setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.8f);
		
		setDynamicSapling(new BlockDynamicSaplingRare("orangeautumnsapling").getDefaultState());
		
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
		return isOneOfBiomes(biome, BOPBiomes.seasonal_forest.get());
	}
	
	@Override
	public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random) {
		if(super.rot(world, pos, neighborCount, radius, random)) {
			if(radius > 4 && TreeHelper.isRooty(world.getBlockState(pos.down())) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
				world.setBlockState(pos, random.nextInt(3) == 0 ? ModBlocks.blockStates.redMushroom : ModBlocks.blockStates.brownMushroom);//Change branch to a mushroom
				world.setBlockState(pos.down(), ModBlocks.blockStates.podzol);//Change rooty dirt to Podzol
			}
			return true;
		}
		
		return false;
	}

}
