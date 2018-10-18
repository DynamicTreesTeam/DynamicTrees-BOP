package dynamictreesbop.trees;

import java.util.List;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.SpeciesRare;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockBOPLog;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.dropcreators.DropCreatorInvoluntarySeed;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

public class TreeEbony extends TreeFamily {

	public class SpeciesEbony extends Species {
		
		public SpeciesEbony(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.ebonyLeavesProperties);
			
			setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 1.0f);
			
			setDynamicSapling(new BlockDynamicSapling("ebonysapling").getDefaultState());
			
			envFactor(Type.WET, 0.5f);
			envFactor(Type.COLD, 0.5f);
			envFactor(Type.SAVANNA, 1.05f);
			
			addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
			
			generateSeed();
			
			setupStandardSeedDropping();
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, BOPBiomes.brushland.orNull());
		}
		
	}
	
	public class SpeciesEbonyTwiglet extends SpeciesRare {
		
		public SpeciesEbonyTwiglet(TreeFamily treeFamily) {
			super(new ResourceLocation(DynamicTreesBOP.MODID, treeFamily.getName().getResourcePath() + "twiglet"), treeFamily, ModContent.ebonyLeavesProperties);
			
			setBasicGrowingParameters(0.3f, 2.5f, 1, 2, 1.0f);
			
			envFactor(Type.WET, 0.5f);
			envFactor(Type.COLD, 0.5f);
			envFactor(Type.SAVANNA, 1.05f);
			
			addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
			
			addDropCreator(new DropCreatorInvoluntarySeed());
			remDropCreator(new ResourceLocation(ModConstants.MODID, "logs"));
		}
		
		@Override
		public LogsAndSticks getLogsAndSticks(float volume) {
			return super.getLogsAndSticks(volume * 16);
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, BOPBiomes.brushland.orNull());
		}
		
		@Override
		public ItemStack getSeedStack(int qty) {
			return getFamily().getCommonSpecies().getSeedStack(qty);
		}
		
		@Override
		public Seed getSeed() {
			return getFamily().getCommonSpecies().getSeed();
		}
		
	}
	
	
	Species twigletSpecies;
	
	public TreeEbony() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "ebony"));
		
		IBlockState primLog = BlockBOPLog.paging.getVariantState(BOPWoods.EBONY);
		setPrimitiveLog(primLog, BlockBOPLog.paging.getVariantItem(BOPWoods.EBONY));
		
		ModContent.ebonyLeavesProperties.setTree(this);
		
		this.addConnectableVanillaLeaves((state) -> {
			return state.getBlock() instanceof BlockBOPLeaves && state.getValue(((BlockBOPLeaves) state.getBlock()).variantProperty) == BOPTrees.EBONY;
		});
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesEbony(this));
		twigletSpecies = new SpeciesEbonyTwiglet(this);
	}
	
	@Override
	public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
		super.registerSpecies(speciesRegistry);
		speciesRegistry.register(twigletSpecies);
	}
	
	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(getCommonSpecies().getDynamicSapling().getBlock());
		return super.getRegisterableBlocks(blockList);
	}
	
}
