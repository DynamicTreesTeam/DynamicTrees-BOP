package dynamictreesbop.trees;

import java.util.List;
import java.util.Random;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.ModTrees;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenClearVolume;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenConiferTopper;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFlareBottom;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenMound;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenVine;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPDirt;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockBOPLog;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

public class TreeUmbran extends TreeFamily {
	
	public class SpeciesUmbran extends Species {
				
		SpeciesUmbran(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.leaves.get(ModContent.UMBRAN));
			
			setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.7f);
						
			envFactor(Type.COLD, 0.75f);
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);
			envFactor(Type.SPOOKY, 1.1f);
			envFactor(Type.DEAD, 1.1f);
			envFactor(Type.MAGICAL, 1.1f);
			
			generateSeed();
			
			setupStandardSeedDropping();
			
			addGenFeature(new FeatureGenVine().setQuantity(7).setMaxLength(6).setRayDistance(6).setVineBlock(BOPBlocks.willow_vine));
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, BOPBiomes.ominous_woods.orNull());
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
					world.setBlockState(pos.down(), BOPBlocks.dirt.getDefaultState().withProperty(BlockBOPDirt.VARIANT, BlockBOPDirt.BOPDirtType.LOAMY));//Change rooty dirt to loam
				}
				return true;
			}
			
			return false;
		}
		
	}
	
	public class SpeciesUmbranConifer extends Species {
		
		SpeciesUmbranConifer(TreeFamily treeFamily) {
			super(new ResourceLocation(treeFamily.getName().getResourceDomain(), ModContent.UMBRANCONIFER), treeFamily, ModContent.leaves.get(ModContent.UMBRANCONIFER));
			
			setBasicGrowingParameters(0.25f, 16.0f, 3, 3, 0.8f);
			setGrowthLogicKit(TreeRegistry.findGrowthLogicKit(ModTrees.CONIFER));
			
			envFactor(Type.COLD, 0.75f);
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);
			envFactor(Type.SPOOKY, 1.1f);
			envFactor(Type.DEAD, 1.1f);
			envFactor(Type.MAGICAL, 1.1f);
			
			generateSeed();
			
			setupStandardSeedDropping();
			
			setRequiresTileEntity(true);
			
			//Add species features
			addGenFeature(new FeatureGenConiferTopper(getLeavesProperties()));//Make a topper for this conifer tree
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, BOPBiomes.ominous_woods.orNull());
		}
		
		@Override
		public int maxBranchRadius() {
			return 8;
		}
		
		@Override
		public boolean isThick() {
			return false;
		}
		
		@Override
		public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random, boolean rapid) {
			if(super.rot(world, pos, neighborCount, radius, random, rapid)) {
				if(radius > 4 && TreeHelper.isRooty(world.getBlockState(pos.down())) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
					world.setBlockState(pos, random.nextInt(3) == 0 ? ModBlocks.blockStates.redMushroom : ModBlocks.blockStates.brownMushroom);//Change branch to a mushroom
					world.setBlockState(pos.down(), BOPBlocks.dirt.getDefaultState().withProperty(BlockBOPDirt.VARIANT, BlockBOPDirt.BOPDirtType.LOAMY));//Change rooty dirt to loam
				}
				return true;
			}
			
			return false;
		}
		
	}
	
	public class SpeciesMegaUmbranConifer extends Species {
		
		SpeciesMegaUmbranConifer(TreeFamily treeFamily) {
			super(new ResourceLocation(treeFamily.getName().getResourceDomain(), ModContent.UMBRANCONIFERMEGA), treeFamily, ModContent.leaves.get(ModContent.UMBRANCONIFER));
			
			setBasicGrowingParameters(0.3f, 32.0f, 7, 7, 1.0f);
			setGrowthLogicKit(new ConiferLogic().setHeightVariation(8));
			
			envFactor(Type.COLD, 0.75f);
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);
			envFactor(Type.SPOOKY, 1.1f);
			envFactor(Type.DEAD, 1.1f);
			envFactor(Type.MAGICAL, 1.1f);
			
			setupStandardSeedDropping();
			
			setRequiresTileEntity(true);
			
			//Add species features
			addGenFeature(new FeatureGenClearVolume(6));//Clear a spot for the thick tree trunk
			addGenFeature(new FeatureGenConiferTopper(getLeavesProperties()));//Make a topper for this conifer tree
			addGenFeature(new FeatureGenMound(999));//Establish mounds
			addGenFeature(new FeatureGenFlareBottom());//Flare the bottom
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, BOPBiomes.ominous_woods.orNull());
		}
		
		@Override
		public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random, boolean rapid) {
			if(super.rot(world, pos, neighborCount, radius, random, rapid)) {
				if(radius > 4 && TreeHelper.isRooty(world.getBlockState(pos.down())) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
					world.setBlockState(pos, random.nextInt(3) == 0 ? ModBlocks.blockStates.redMushroom : ModBlocks.blockStates.brownMushroom);//Change branch to a mushroom
					world.setBlockState(pos.down(), BOPBlocks.dirt.getDefaultState().withProperty(BlockBOPDirt.VARIANT, BlockBOPDirt.BOPDirtType.LOAMY));//Change rooty dirt to loam
				}
				return true;
			}
			
			return false;
		}
		
		@Override
		public ItemStack getSeedStack(int qty) {
			return coniferSpecies.getSeedStack(qty);
		}
		
		@Override
		public Seed getSeed() {
			return coniferSpecies.getSeed();
		}
		
		@Override
		public boolean isThick() {
			return true;
		}
		
	}
	
	public Species coniferSpecies;
	public Species megaConiferSpecies;

	public TreeUmbran() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.UMBRAN));
		
		IBlockState primLog = BlockBOPLog.paging.getVariantState(BOPWoods.UMBRAN);
		setPrimitiveLog(primLog, BlockBOPLog.paging.getVariantItem(BOPWoods.UMBRAN));
		
		ModContent.leaves.get(ModContent.UMBRAN).setTree(this);
		ModContent.leaves.get(ModContent.UMBRANCONIFER).setTree(this);
		hasConiferVariants = true;
		
		this.addConnectableVanillaLeaves((state) -> {
			return state.getBlock() instanceof BlockBOPLeaves && state.getValue(((BlockBOPLeaves) state.getBlock()).variantProperty) == BOPTrees.UMBRAN;
		});
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesUmbran(this));
		coniferSpecies = new SpeciesUmbranConifer(this);
		megaConiferSpecies = new SpeciesMegaUmbranConifer(this);
	}
	
	@Override
	public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
		super.registerSpecies(speciesRegistry);
		speciesRegistry.register(coniferSpecies);
		speciesRegistry.register(megaConiferSpecies);
	}
	
	@Override
	public boolean isThick() {
		return true;
	}
	
	@Override
	public List<Item> getRegisterableItems(List<Item> itemList) {
		itemList.add(coniferSpecies.getSeed());
		return super.getRegisterableItems(itemList);
	}
	
}
