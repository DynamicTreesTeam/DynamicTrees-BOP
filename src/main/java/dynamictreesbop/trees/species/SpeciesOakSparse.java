package dynamictreesbop.trees.species;

import java.util.Random;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.SpeciesRare;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class SpeciesOakSparse extends SpeciesRare {
	
	public SpeciesOakSparse(TreeFamily treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, treeFamily.getName().getResourcePath() + "sparse"), treeFamily, ModContent.leaves.get("oaksparse"));
		
		setBasicGrowingParameters(0.3f, 2.5f, 1, 2, 1.0f);
		
		envFactor(Type.SNOWY, 0.25f);
		envFactor(Type.DRY, 0.75f);
		envFactor(Type.PLAINS, 1.05f);
		
		addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		
		leavesProperties.setTree(treeFamily);
	}
	
	@Override
	public boolean isBiomePerfect(Biome biome) {
		return biome == BOPBiomes.prairie.orNull();
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
	public int maxBranchRadius() {
		return 8;
	}
	
	@Override
	public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random, boolean rapid) {
		if (super.rot(world, pos, neighborCount, radius, random, rapid)) {
			if (radius > 4 && TreeHelper.isRooty(world.getBlockState(pos.down())) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
				world.setBlockState(pos, random.nextInt(3) == 0 ? ModBlocks.blockStates.redMushroom : ModBlocks.blockStates.brownMushroom); // Change branch to a mushroom
			}
			return true;
		}
		
		return false;
	}

}
