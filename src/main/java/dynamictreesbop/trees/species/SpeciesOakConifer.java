package dynamictreesbop.trees.species;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.ModTrees;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.SpeciesRare;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.common.block.BlockBOPMushroom;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class SpeciesOakConifer extends SpeciesRare {
	
	public SpeciesOakConifer(TreeFamily treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.OAKCONIFER), treeFamily, ModContent.leaves.get(ModContent.OAKCONIFER));
		
		setBasicGrowingParameters(0.3f, 16.0f, 3, 3, 0.9f);
		setGrowthLogicKit(TreeRegistry.findGrowthLogicKit(ModTrees.CONIFER));
		
		envFactor(Type.COLD, 0.75f);
		envFactor(Type.HOT, 0.5f);
		envFactor(Type.PLAINS, 1.05f);
		envFactor(Type.FOREST, 1.05f);
		
		addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		
		setupStandardSeedDropping();
		
		leavesProperties.setTree(treeFamily);
	}
	
	@Override
	public boolean isBiomePerfect(Biome biome) {
		return biome == BOPBiomes.temperate_rainforest.orNull();
	}
	
	@Override
	public void postGeneration(World world, BlockPos rootPos, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
		// Manually place the highest few blocks of the conifer since the leafCluster voxmap won't handle it
		BlockPos highest = Collections.max(endPoints, (a, b) -> a.getY() - b.getY());
		world.setBlockState(highest.up(1), leavesProperties.getDynamicLeavesState(4));
		world.setBlockState(highest.up(2), leavesProperties.getDynamicLeavesState(3));
		world.setBlockState(highest.up(3), leavesProperties.getDynamicLeavesState(1));
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
		if(super.rot(world, pos, neighborCount, radius, random, rapid)) {
			if(radius > 4 && TreeHelper.isRooty(world.getBlockState(pos.down())) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
				world.setBlockState(pos, BOPBlocks.mushroom.getDefaultState().withProperty(BlockBOPMushroom.VARIANT, BlockBOPMushroom.MushroomType.PORTOBELLO));//Change branch to a mushroom
				world.setBlockState(pos.down(), ModBlocks.blockStates.podzol);//Change rooty dirt to Podzol
			}
			return true;
		}
		return false;
	}

}
