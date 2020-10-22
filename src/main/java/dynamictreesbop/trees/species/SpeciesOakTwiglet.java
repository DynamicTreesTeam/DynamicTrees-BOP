package dynamictreesbop.trees.species;

import java.util.Random;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.trees.SpeciesRare;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import biomesoplenty.api.biome.BOPBiomes;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.dropcreators.DropCreatorInvoluntarySeed;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class SpeciesOakTwiglet extends SpeciesRare {
	
	public SpeciesOakTwiglet(TreeFamily treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.OAKTWIGLET), treeFamily, ModContent.leaves.get(ModContent.OAKSPARSE));
		
		setBasicGrowingParameters(0.3f, 2.5f, 1, 2, 1.0f);
		
		envFactor(Type.SNOWY, 0.25f);
		envFactor(Type.DRY, 0.75f);
		envFactor(Type.PLAINS, 1.05f);
		
		addDropCreator(new DropCreatorInvoluntarySeed());
		remDropCreator(new ResourceLocation(ModConstants.MODID, "logs"));
		
		leavesProperties.setTree(treeFamily);
	}
	
	@Override
	protected void setStandardSoils() {
		addAcceptableSoils(DirtHelper.DIRTLIKE, DirtHelper.HARDCLAYLIKE);
	}
	
	@Override
	public LogsAndSticks getLogsAndSticks(float volume) {
		return super.getLogsAndSticks(volume * 16);
	}
	
	@Override
	public boolean isBiomePerfect(Biome biome) {
		return isOneOfBiomes(biome, BOPBiomes.chaparral.orNull(), BOPBiomes.lush_desert.orNull());
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
	public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random, boolean rapid) {
		if (super.rot(world, pos, neighborCount, radius, random, rapid)) {
			if (TreeHelper.isRooty(world.getBlockState(pos.down())) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
				world.setBlockState(pos, random.nextInt(3) == 0 ? ModBlocks.blockStates.redMushroom : ModBlocks.blockStates.brownMushroom);//Change branch to a mushroom
			}
			return true;
		}
		
		return false;
	}

}
