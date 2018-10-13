package dynamictreesbop.trees.species;

import java.util.Random;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.SpeciesRare;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.dropcreators.DropCreatorInvoluntarySeed;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class SpeciesJungleTwiglet extends SpeciesRare {
	
	public SpeciesJungleTwiglet(TreeFamily treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, treeFamily.getName().getResourcePath() + "twiglet"), treeFamily, ModContent.jungleTwigletLeavesProperties);
		
		setBasicGrowingParameters(0.3f, 2.5f, 1, 2, 1.0f);
		
		envFactor(Type.SNOWY, 0.25f);
		envFactor(Type.DRY, 0.75f);
		envFactor(Type.HOT, 1.05f);
		
		addAcceptableSoil(Blocks.GRASS, BOPBlocks.grass, BOPBlocks.dirt, Blocks.SAND);
		
		addDropCreator(new DropCreatorInvoluntarySeed());
		remDropCreator(new ResourceLocation(ModConstants.MODID, "logs"));
		
		leavesProperties.setTree(treeFamily);
	}
	
	@Override
	public LogsAndSticks getLogsAndSticks(int volume) {
		return new LogsAndSticks(volume / 256, (volume % 256) / 64);
	}
	
	@Override
	public boolean isBiomePerfect(Biome biome) {
		return isOneOfBiomes(biome, BOPBiomes.overgrown_cliffs.orNull(), BOPBiomes.tropical_island.orNull(),
				BOPBiomes.brushland.orNull(), BOPBiomes.oasis.orNull());
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
	public boolean generate(World world, BlockPos pos, Biome biome, Random random, int radius, SafeChunkBounds safeBounds) {
		if (world.getBlockState(pos).getBlock() == Blocks.SAND) {
			if (biome == BOPBiomes.oasis.orNull()) return TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "cactus")).generate(world, pos, biome, random, radius, safeBounds);
			return false;
		}
		return super.generate(world, pos, biome, random, radius, safeBounds);
	}

}
