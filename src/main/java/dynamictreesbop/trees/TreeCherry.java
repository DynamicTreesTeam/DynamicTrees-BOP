package dynamictreesbop.trees;

import java.util.List;
import java.util.Random;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockBOPLog;
import biomesoplenty.common.block.BlockBOPMushroom;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

public class TreeCherry extends TreeFamily {
	
	public class SpeciesPinkCherry extends Species {
		
		SpeciesPinkCherry(TreeFamily treeFamily) {
			super(new ResourceLocation(treeFamily.getName().getResourceDomain(), ModContent.PINKCHERRY), treeFamily, ModContent.leaves.get(ModContent.PINKCHERRY));
			
			setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.8f);
						
			envFactor(Type.COLD, 0.75f);
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);
			
			generateSeed();
			
			setupStandardSeedDropping();
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, BOPBiomes.cherry_blossom_grove.orNull());
		}
		
		@Override
		public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random, boolean rapid) {
			if(super.rot(world, pos, neighborCount, radius, random, rapid)) {
				if(radius > 4 && TreeHelper.isRooty(world.getBlockState(pos.down())) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
					world.setBlockState(pos, random.nextInt(3) == 0 ? BOPBlocks.mushroom.getDefaultState().withProperty(BlockBOPMushroom.VARIANT, BlockBOPMushroom.MushroomType.TOADSTOOL) : BOPBlocks.mushroom.getDefaultState().withProperty(BlockBOPMushroom.VARIANT, BlockBOPMushroom.MushroomType.PORTOBELLO));//Change branch to a mushroom
					world.setBlockState(pos.down(), ModBlocks.blockStates.podzol);//Change rooty dirt to Podzol
				}
				return true;
			}
			
			return false;
		}
		
	}
	
	public class SpeciesWhiteCherry extends Species {
		
		SpeciesWhiteCherry(TreeFamily treeFamily) {
			super(new ResourceLocation(treeFamily.getName().getResourceDomain(), ModContent.WHITECHERRY), treeFamily, ModContent.leaves.get(ModContent.WHITECHERRY));
			
			setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.8f);
						
			envFactor(Type.COLD, 0.75f);
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);
			
			generateSeed();
			
			setupStandardSeedDropping();
			
			setRequiresTileEntity(true);
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, BOPBiomes.cherry_blossom_grove.orNull());
		}
		
		@Override
		public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random, boolean rapid) {
			if(super.rot(world, pos, neighborCount, radius, random, rapid)) {
				if(radius > 4 && TreeHelper.isRooty(world.getBlockState(pos.down())) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
					world.setBlockState(pos, random.nextInt(3) == 0 ? BOPBlocks.mushroom.getDefaultState().withProperty(BlockBOPMushroom.VARIANT, BlockBOPMushroom.MushroomType.TOADSTOOL) : BOPBlocks.mushroom.getDefaultState().withProperty(BlockBOPMushroom.VARIANT, BlockBOPMushroom.MushroomType.PORTOBELLO));//Change branch to a mushroom
					world.setBlockState(pos.down(), ModBlocks.blockStates.podzol);//Change rooty dirt to Podzol
				}
				return true;
			}
			
			return false;
		}
		
	}
	
	Species whiteSpecies;
	
	public TreeCherry() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.CHERRY));
		
		IBlockState primLog = BlockBOPLog.paging.getVariantState(BOPWoods.CHERRY);
		setPrimitiveLog(primLog, BlockBOPLog.paging.getVariantItem(BOPWoods.CHERRY));
		
		ModContent.leaves.get(ModContent.PINKCHERRY).setTree(this);
		ModContent.leaves.get(ModContent.WHITECHERRY).setTree(this);
		
		this.addConnectableVanillaLeaves((state) -> {
			if (state.getBlock() instanceof BlockBOPLeaves) {
				BOPTrees treeType = (BOPTrees) state.getValue(((BlockBOPLeaves) state.getBlock()).variantProperty);
				return treeType == BOPTrees.PINK_CHERRY || treeType == BOPTrees.WHITE_CHERRY;
			}
			return false;
		});
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesPinkCherry(this));
		whiteSpecies = new SpeciesWhiteCherry(this);
	}
	
	@Override
	public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
		super.registerSpecies(speciesRegistry);
		speciesRegistry.register(whiteSpecies);
	}
	
	@Override
	public List<Item> getRegisterableItems(List<Item> itemList) {
		itemList.add(whiteSpecies.getSeed());
		return super.getRegisterableItems(itemList);
	}
	
}
