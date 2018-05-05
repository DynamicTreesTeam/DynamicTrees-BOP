package dynamictreesbop.trees.species;

import java.util.ArrayList;
import java.util.Random;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.SpeciesRare;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.dropcreators.DropCreatorInvoluntarySeed;
import dynamictreesbop.dropcreators.DropCreatorTwigletLogs;
import dynamictreesbop.featuregen.FeatureGenBush;
import net.minecraft.block.BlockGrass;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class SpeciesAcaciaTwiglet extends SpeciesRare {
	
	FeatureGenBush bushGen;
	
	public SpeciesAcaciaTwiglet(TreeFamily treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, treeFamily.getName().getResourcePath() + "twiglet"), treeFamily, ModContent.acaciaTwigletLeavesProperties);
		
		setBasicGrowingParameters(0.3f, 2.5f, 1, 2, 1.0f);
		
		envFactor(Type.SNOWY, 0.25f);
		envFactor(Type.DRY, 0.75f);
		envFactor(Type.HOT, 1.05f);
		
		addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt, Blocks.SAND);
		
		addDropCreator(new DropCreatorInvoluntarySeed());
		remDropCreator(new ResourceLocation(ModConstants.MODID, "logs"));
		addDropCreator(new DropCreatorTwigletLogs());
		
		bushGen = new FeatureGenBush(this);
		
		leavesProperties.setTree(treeFamily);
	}
	
	@Override
	public boolean isBiomePerfect(Biome biome) {
		return isOneOfBiomes(biome, BOPBiomes.xeric_shrubland.orNull(), BOPBiomes.outback.orNull());
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
		if (biome == BOPBiomes.xeric_shrubland.orNull() && world.getBlockState(pos).getBlock() instanceof BlockGrass) {
			bushGen.setRadius(radius).gen(world, pos, new ArrayList<BlockPos>(), safeBounds);
			return false;
		}
		return super.generate(world, pos, biome, random, radius, safeBounds);
	}
	
}
