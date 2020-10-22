package dynamictreesbop.trees.species;

import java.util.Random;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenBush;
import com.ferreusveritas.dynamictrees.trees.SpeciesRare;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;

import biomesoplenty.api.biome.BOPBiomes;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.dropcreators.DropCreatorInvoluntarySeed;
import net.minecraft.block.BlockGrass;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class SpeciesAcaciaTwiglet extends SpeciesRare {
	
	FeatureGenBush bushGen;
	
	public SpeciesAcaciaTwiglet(TreeFamily treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.ACACIATWIGLET), treeFamily, ModContent.leaves.get(ModContent.ACACIATWIGLET));
		
		setBasicGrowingParameters(0.3f, 2.5f, 1, 2, 1.0f);
		
		envFactor(Type.SNOWY, 0.25f);
		envFactor(Type.DRY, 0.75f);
		envFactor(Type.HOT, 1.05f);
		
		addDropCreator(new DropCreatorInvoluntarySeed());
		remDropCreator(new ResourceLocation(ModConstants.MODID, "logs"));
		
		bushGen = new FeatureGenBush();
		
		leavesProperties.setTree(treeFamily);
	}
	
	@Override
	protected void setStandardSoils() {
		addAcceptableSoils(DirtHelper.DIRTLIKE, DirtHelper.SANDLIKE);
	}
	
	@Override
	public LogsAndSticks getLogsAndSticks(float volume) {
		return super.getLogsAndSticks(volume * 16);
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
	public boolean generate(World world, BlockPos rootPos, Biome biome, Random random, int radius, SafeChunkBounds safeBounds) {
		if (biome == BOPBiomes.xeric_shrubland.orNull() && world.getBlockState(rootPos).getBlock() instanceof BlockGrass) {
			return bushGen.generate(world, rootPos, this, biome, random, radius, safeBounds);
		}
		return super.generate(world, rootPos, biome, random, radius, safeBounds);
	}
	
}
