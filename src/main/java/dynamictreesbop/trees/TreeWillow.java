package dynamictreesbop.trees;

import java.util.List;
import java.util.Random;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.trees.DynamicTree;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CompatHelper;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLog;
import biomesoplenty.common.block.BlockBOPMushroom;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.featuregen.FeatureGenVine;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreeWillow extends DynamicTree {
	
	public class SpeciesWillow extends Species {
		
		FeatureGenVine vineGen;
		
		SpeciesWillow(DynamicTree treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.willowLeavesProperties);
			
			setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.8f);
			
			setDynamicSapling(new BlockDynamicSapling("willowsapling").getDefaultState());
			
			envFactor(Type.COLD, 0.50f);
			envFactor(Type.DRY, 0.50f);
			
			addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt, BOPBlocks.mud);
			
			generateSeed();
			
			setupStandardSeedDropping();
			
			vineGen = new FeatureGenVine(this).setQuantity(16).setMaxLength(8).setRayDistance(8).setVineBlock(BOPBlocks.willow_vine);
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return BiomeDictionary.hasType(biome, Type.SWAMP);
		}
		
		@Override
		public boolean isAcceptableSoilForWorldgen(World world, BlockPos pos, IBlockState soilBlockState) {
			if (soilBlockState.getBlock() == Blocks.WATER) {
				Biome biome = world.getBiome(pos);
				if (CompatHelper.biomeHasType(biome, Type.SWAMP)) {
					BlockPos down = pos.down();
					if (isAcceptableSoil(world, down, world.getBlockState(down))) {
						return true;
					}
				}
			}
			return super.isAcceptableSoilForWorldgen(world, pos, soilBlockState);
		}
		
		@Override
		public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random) {
			if(super.rot(world, pos, neighborCount, radius, random)) {
				if(radius > 4 && TreeHelper.isRooty(world, pos.down()) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
					world.setBlockState(pos, random.nextInt(3) == 0 ? ModBlocks.blockStates.redMushroom : BOPBlocks.mushroom.getDefaultState().withProperty(BlockBOPMushroom.VARIANT, BlockBOPMushroom.MushroomType.BLUE_MILK_CAP)); // Change branch to a mushroom
				}
				return true;
			}
			return false;
		}
		
		@Override
		public void postGeneration(World world, BlockPos rootPos, Biome biome, int radius, List<BlockPos> endPoints, boolean worldGen) {
			super.postGeneration(world, rootPos, biome, radius, endPoints, worldGen);
			
			//Generate Vines
			vineGen.gen(world, rootPos.up(), endPoints);
		}
	
	}
	
	public TreeWillow() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "willow"));
		
		IBlockState primLog = BlockBOPLog.paging.getVariantState(BOPWoods.WILLOW);
		setPrimitiveLog(primLog, BlockBOPLog.paging.getVariantItem(BOPWoods.WILLOW));
		
		ModContent.willowLeavesProperties.setTree(this);
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesWillow(this));
	}
	
	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(getCommonSpecies().getDynamicSapling().getBlock());
		return super.getRegisterableBlocks(blockList);
	}

}
