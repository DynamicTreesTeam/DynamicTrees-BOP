package dynamictreesbop.trees.species;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockRootyDirt;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.DynamicTree;
import com.ferreusveritas.dynamictrees.trees.Species;

import biomesoplenty.api.block.BOPBlocks;
import dynamictreesbop.DynamicTreesBOP;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class SpeciesOakLarge extends Species {
	
	Species baseSpecies;
	
	public SpeciesOakLarge(DynamicTree treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "oaklarge"), treeFamily);
		
		baseSpecies = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oak"));
		
		// Large oaks grow more similarly to dark oaks
		setBasicGrowingParameters(0.35f, 18.0f, 6, 6, 0.85f);
		
		setSoilLongevity(12);
		
		envFactor(Type.COLD, 0.50f);
		envFactor(Type.DRY, 0.50f);
		
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
	
	@Override
	public ItemStack getSeedStack(int qty) {
		return baseSpecies.getSeedStack(qty);
	}
	
	@Override
	public Seed getSeed() {
		return baseSpecies.getSeed();
	}

}