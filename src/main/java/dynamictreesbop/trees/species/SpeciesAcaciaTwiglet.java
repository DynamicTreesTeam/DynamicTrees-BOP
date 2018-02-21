package dynamictreesbop.trees.species;

import java.util.ArrayList;
import java.util.Random;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.DynamicTree;
import com.ferreusveritas.dynamictrees.trees.Species;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
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

public class SpeciesAcaciaTwiglet extends Species {
	
	FeatureGenBush bushGen;
	
	public SpeciesAcaciaTwiglet(DynamicTree treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, treeFamily.getName().getResourcePath() + "twiglet"), treeFamily, ModContent.acaciaTwigletLeavesProperties);
		
		setBasicGrowingParameters(0.3f, 2.5f, 1, 2, 1.0f);
		
		envFactor(Type.SNOWY, 0.25f);
		envFactor(Type.DRY, 0.75f);
		envFactor(Type.HOT, 1.05f);
		
		addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt, Blocks.SAND);
		
		setupStandardSeedDropping();
		remDropCreator(new ResourceLocation(ModConstants.MODID, "logs"));
		addDropCreator(new DropCreatorTwigletLogs());
		
		bushGen = new FeatureGenBush(this);
		
		leavesProperties.setTree(treeFamily);
	}
	
	@Override
	public boolean isBiomePerfect(Biome biome) {
		return isOneOfBiomes(biome, BOPBiomes.xeric_shrubland.get(), BOPBiomes.outback.get());
	}
	
	@Override
	public ItemStack getSeedStack(int qty) {
		return getTree().getCommonSpecies().getSeedStack(qty);
	}
	
	@Override
	public Seed getSeed() {
		return getTree().getCommonSpecies().getSeed();
	}
	
	@Override
	public boolean generate(World world, BlockPos pos, Biome biome, Random random, int radius) {
		if (biome == BOPBiomes.xeric_shrubland.get() && world.getBlockState(pos).getBlock() instanceof BlockGrass) {
			bushGen.setRadius(radius).gen(world, pos, new ArrayList<BlockPos>());
			return false;
		}
		return super.generate(world, pos, biome, random, radius);
	}
	
}
