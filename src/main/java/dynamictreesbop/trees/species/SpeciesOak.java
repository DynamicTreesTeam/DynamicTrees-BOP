package dynamictreesbop.trees.species;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockRootyDirt;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorApple;
import com.ferreusveritas.dynamictrees.trees.DynamicTree;
import com.ferreusveritas.dynamictrees.trees.Species;

import biomesoplenty.api.block.BOPBlocks;
import dynamictreesbop.DynamicTreesBOP;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class SpeciesOak extends Species {
	
	Species baseSpecies;
	
	public SpeciesOak(DynamicTree treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "oak"), treeFamily);
		
		baseSpecies = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oak"));
		
		//Oak trees are about as average as you can get
		setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.8f);
		
		setDynamicSapling(baseSpecies.getDynamicSapling());
		setSeedStack(baseSpecies.getSeedStack(1));
		
		envFactor(Type.COLD, 0.75f);
		envFactor(Type.HOT, 0.75f);
		envFactor(Type.DRY, 0.625f);
		envFactor(Type.FOREST, 1.05f);
		
		if(ModConfigs.worldGen && !ModConfigs.enableAppleTrees) {//If we've disabled apple trees we still need some way to get apples.
			addDropCreator(new DropCreatorApple());
		}
		
		setupStandardSeedDropping();
	}
	
	@Override
	public boolean isBiomePerfect(Biome biome) {
		return BiomeDictionary.hasType(biome, Type.FOREST);
	}
	
	@Override
	public boolean isAcceptableSoil(World world, BlockPos pos, IBlockState soilBlockState) {
		Block soilBlock = soilBlockState.getBlock();
		return soilBlock == Blocks.DIRT || soilBlock == Blocks.GRASS || soilBlock == Blocks.MYCELIUM || soilBlock instanceof BlockRootyDirt || soilBlock == BOPBlocks.grass || soilBlock == BOPBlocks.dirt;
	}
	
}
