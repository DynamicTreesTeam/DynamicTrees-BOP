package dynamictreesbop.trees;

import java.util.List;
import java.util.Random;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.trees.Species;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLog;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.dropcreators.DropCreatorTwigletLogs;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreeHellbark extends TreeFamily {
	
	public class SpeciesHellbark extends Species {
		
		SpeciesHellbark(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.hellbarkLeavesProperties);
			
			setBasicGrowingParameters(0.3f, 2.5f, 1, 2, 1.0f);
			
			setDynamicSapling(new BlockDynamicSapling("hellbarksapling").getDefaultState());
			
			envFactor(Type.COLD, 0.25f);
			envFactor(Type.NETHER, 1.05f);
			
			addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
			
			generateSeed();
			
			setupStandardSeedDropping();
			remDropCreator(new ResourceLocation(ModConstants.MODID, "logs"));
			addDropCreator(new DropCreatorTwigletLogs());
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, BOPBiomes.undergarden.get());
		}
		
		@Override
		public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random) {
			if(super.rot(world, pos, neighborCount, radius, random)) {
				if(radius > 4 && TreeHelper.isRooty(world.getBlockState(pos.down())) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
					world.setBlockState(pos, random.nextInt(3) == 0 ? ModBlocks.blockStates.redMushroom : ModBlocks.blockStates.brownMushroom); // Change branch to a mushroom
				}
				return true;
			}
			return false;
		}
		
	}
	
	public TreeHellbark() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "hellbark"));
		
		IBlockState primLog = BlockBOPLog.paging.getVariantState(BOPWoods.HELLBARK);
		setPrimitiveLog(primLog, BlockBOPLog.paging.getVariantItem(BOPWoods.HELLBARK));
		
		ModContent.hellbarkLeavesProperties.setTree(this);
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesHellbark(this));
	}
	
	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(getCommonSpecies().getDynamicSapling().getBlock());
		return super.getRegisterableBlocks(blockList);
	}

}
