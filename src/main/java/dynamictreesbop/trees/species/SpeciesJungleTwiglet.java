package dynamictreesbop.trees.species;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.trees.SpeciesRare;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import biomesoplenty.api.biome.BOPBiomes;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.dropcreators.DropCreatorInvoluntarySeed;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class SpeciesJungleTwiglet extends SpeciesRare {
	
	public SpeciesJungleTwiglet(TreeFamily treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.JUNGLETWIGLET), treeFamily, ModContent.leaves.get(ModContent.JUNGLETWIGLET));
		
		setBasicGrowingParameters(0.3f, 2.5f, 1, 2, 1.0f);
		
		envFactor(Type.SNOWY, 0.25f);
		envFactor(Type.DRY, 0.75f);
		envFactor(Type.HOT, 1.05f);
		
		addDropCreator(new DropCreatorInvoluntarySeed());
		remDropCreator(new ResourceLocation(ModConstants.MODID, "logs"));
		
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
	public boolean placeRootyDirtBlock(World world, BlockPos rootPos, int life) {
		if (world.getBlockState(rootPos).getMaterial() == Material.SAND) {
			world.setBlockState(rootPos, ModBlocks.blockRootySand.getDefaultState().withProperty(BlockRooty.LIFE, life));
		} else {
			world.setBlockState(rootPos, getRootyBlock().getDefaultState().withProperty(BlockRooty.LIFE, life));
		}
		return true;
	}

}
