package dynamictreesbop.trees;

import java.util.List;
import java.util.Random;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenVine;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockBOPLog;
import biomesoplenty.common.block.BlockBOPMushroom;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
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

public class TreeWillow extends TreeFamily {
	
	public class SpeciesWillow extends Species {
		
		FeatureGenVine vineGen;
		
		SpeciesWillow(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.willowLeavesProperties);
			
			setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.8f);
			
			setDynamicSapling(new BlockDynamicSapling("willowsapling").getDefaultState());
			
			envFactor(Type.COLD, 0.50f);
			envFactor(Type.DRY, 0.50f);
			
			addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt, BOPBlocks.mud);
			
			generateSeed();
			
			setupStandardSeedDropping();
			
			vineGen = new FeatureGenVine(this).setQuantity(32).setMaxLength(8).setRayDistance(7).setVineBlock(BOPBlocks.willow_vine);
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return BiomeDictionary.hasType(biome, Type.SWAMP);
		}
		
		@Override
		public boolean isAcceptableSoilForWorldgen(World world, BlockPos pos, IBlockState soilBlockState) {
			if (soilBlockState.getBlock() == Blocks.WATER) {
				Biome biome = world.getBiome(pos);
				if (BiomeDictionary.hasType(biome, Type.SWAMP)) {
					BlockPos down = pos.down();
					if (isAcceptableSoil(world, down, world.getBlockState(down))) {
						return true;
					}
				}
			}
			return super.isAcceptableSoilForWorldgen(world, pos, soilBlockState);
		}
		
		@Override
		public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random, boolean rapid) {
			if(super.rot(world, pos, neighborCount, radius, random, rapid)) {
				if(radius > 4 && TreeHelper.isRooty(world.getBlockState(pos.down())) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
					world.setBlockState(pos, random.nextInt(3) == 0 ? ModBlocks.blockStates.redMushroom : BOPBlocks.mushroom.getDefaultState().withProperty(BlockBOPMushroom.VARIANT, BlockBOPMushroom.MushroomType.BLUE_MILK_CAP)); // Change branch to a mushroom
				}
				return true;
			}
			return false;
		}
		
		@Override
		public void postGeneration(World world, BlockPos rootPos, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
			super.postGeneration(world, rootPos, biome, radius, endPoints, safeBounds, initialDirtState);
			vineGen.postGeneration(world, rootPos, biome, radius, endPoints, safeBounds, initialDirtState);//Generate Vines
		}
	
	}
	
	public TreeWillow() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "willow"));
		
		IBlockState primLog = BlockBOPLog.paging.getVariantState(BOPWoods.WILLOW);
		setPrimitiveLog(primLog, BlockBOPLog.paging.getVariantItem(BOPWoods.WILLOW));
		
		ModContent.willowLeavesProperties.setTree(this);
		
		this.addConnectableVanillaLeaves((state) -> {
			return state.getBlock() instanceof BlockBOPLeaves && state.getValue(((BlockBOPLeaves) state.getBlock()).variantProperty) == BOPTrees.WILLOW;
		});
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
