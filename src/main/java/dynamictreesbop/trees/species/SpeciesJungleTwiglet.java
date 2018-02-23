package dynamictreesbop.trees.species;

import java.util.Random;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.trees.Species;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.dropcreators.DropCreatorTwigletLogs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class SpeciesJungleTwiglet extends Species {
	
	public SpeciesJungleTwiglet(TreeFamily treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, treeFamily.getName().getResourcePath() + "twiglet"), treeFamily, ModContent.jungleTwigletLeavesProperties);
		
		setBasicGrowingParameters(0.3f, 2.5f, 1, 2, 1.0f);
		
		envFactor(Type.SNOWY, 0.25f);
		envFactor(Type.DRY, 0.75f);
		envFactor(Type.HOT, 1.05f);
		
		addAcceptableSoil(Blocks.GRASS, BOPBlocks.grass, BOPBlocks.dirt, Blocks.SAND);
		
		setupStandardSeedDropping();
		remDropCreator(new ResourceLocation(ModConstants.MODID, "logs"));
		addDropCreator(new DropCreatorTwigletLogs());
		
		leavesProperties.setTree(treeFamily);
	}
	
	@Override
	public boolean isBiomePerfect(Biome biome) {
		return isOneOfBiomes(biome, BOPBiomes.overgrown_cliffs.get(), BOPBiomes.tropical_island.get(),
				BOPBiomes.brushland.get(), BOPBiomes.oasis.get());
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
	public boolean generate(World world, BlockPos pos, Biome biome, Random random, int radius) {
		if (world.getBlockState(pos).getBlock() == Blocks.SAND) {
			if (biome == BOPBiomes.oasis.get()) return TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "cactus")).generate(world, pos, biome, random, radius);
			return false;
		}
		return super.generate(world, pos, biome, random, radius);
	}

}
