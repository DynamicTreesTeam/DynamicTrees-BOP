package dynamictreesbop.trees.species;

import java.util.Random;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.trees.SpeciesRare;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.item.BOPItems;
import biomesoplenty.common.block.BlockBOPLeaves;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.dropcreators.DropCreatorFruit;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class SpeciesDyingOak extends SpeciesRare {
	
	public SpeciesDyingOak(TreeFamily treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "oakdying"), treeFamily, ModContent.leaves.get("dyingoak"));
		
		setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.65f);
				
		envFactor(Type.LUSH, 0.75f);
		envFactor(Type.SPOOKY, 1.05f);
		envFactor(Type.DEAD, 1.05f);
		
		addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		
		generateSeed();
		
		addDropCreator(new DropCreatorFruit(BOPItems.persimmon));
		setupStandardSeedDropping();
		
		leavesProperties.setTree(treeFamily);
		
		treeFamily.addConnectableVanillaLeaves((state) -> {
			return state.getBlock() instanceof BlockBOPLeaves && state.getValue(((BlockBOPLeaves) state.getBlock()).variantProperty) == BOPTrees.DEAD;
		});
	}
	
	@Override
	public boolean isBiomePerfect(Biome biome) {
		return BiomeDictionary.hasType(biome, Type.DEAD);
	}
	
	@Override
	public int maxBranchRadius() {
		return 8;
	}
	
	@Override
	public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random, boolean rapid) {
		if(super.rot(world, pos, neighborCount, radius, random, rapid)) {
			if(radius > 4 && TreeHelper.isRooty(world.getBlockState(pos.down())) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
				world.setBlockState(pos, random.nextInt(3) == 0 ? ModBlocks.blockStates.redMushroom : ModBlocks.blockStates.brownMushroom);//Change branch to a mushroom
				world.setBlockState(pos.down(), ModBlocks.blockStates.podzol);//Change rooty dirt to Podzol
			}
			return true;
		}
		
		return false;
	}
	
}
