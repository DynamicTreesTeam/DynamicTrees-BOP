package dynamictreesbop.trees.species;

import biomesoplenty.api.biome.BOPBiomes;
import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenBush;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.dropcreators.DropCreatorInvoluntarySeed;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

import java.util.Random;

public class SpeciesMapleTwiglet extends Species {

	public SpeciesMapleTwiglet(TreeFamily treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.MAPLETWIGLET), treeFamily, ModContent.leaves.get(ModContent.MAPLETWIGLET));

		setBasicGrowingParameters(0.3f, 2.5f, 1, 2, 1.0f);
		
		envFactor(Type.SNOWY, 1.1f);
		envFactor(Type.DRY, 0.75f);
		envFactor(Type.HOT, 0.90f);
		
		addDropCreator(new DropCreatorInvoluntarySeed());
		
		setRequiresTileEntity(true);
		
		leavesProperties.setTree(treeFamily);
	}

	@Override
	public LogsAndSticks getLogsAndSticks(float volume) {
		return super.getLogsAndSticks(1.0f + volume);//Guarantee at least one log is produced
	}
	
	@Override
	public boolean isBiomePerfect(Biome biome) {
		return isOneOfBiomes(biome, BOPBiomes.tundra.orNull(), BOPBiomes.snowy_tundra.orNull());
	}

	private Species getMapleSpecies(){
		return TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.MAPLE));
	}

	@Override
	public ItemStack getSeedStack(int qty) {
		return getMapleSpecies().getSeedStack(qty);
	}
	
	@Override
	public Seed getSeed() {
		return getMapleSpecies().getSeed();
	}
	
}
