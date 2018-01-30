package dynamictreesbop.trees.species;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockRootyDirt;
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

public class SpeciesBirch extends Species {
	
	Species baseSpecies;

	public SpeciesBirch(DynamicTree treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "birch"), treeFamily);
		
		baseSpecies = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "birch"));
		
		//Birch are tall, skinny, fast growing trees
		setBasicGrowingParameters(0.1f, 14.0f, 4, 4, 1.25f);
		
		setDynamicSapling(baseSpecies.getDynamicSapling());
		setSeedStack(baseSpecies.getSeedStack(1));
		
		envFactor(Type.COLD, 0.875f);
		envFactor(Type.HOT, 0.75f);
		envFactor(Type.DRY, 0.50f);
		envFactor(Type.FOREST, 1.05f);
		
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
