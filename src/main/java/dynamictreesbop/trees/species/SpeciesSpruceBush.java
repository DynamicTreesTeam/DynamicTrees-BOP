package dynamictreesbop.trees.species;

import com.ferreusveritas.dynamictrees.api.IGenFeature;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenBush;
import com.ferreusveritas.dynamictrees.trees.Species;

import biomesoplenty.api.block.BOPBlocks;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

public class SpeciesSpruceBush extends Species {
		
	public SpeciesSpruceBush() {
		super();
		setRegistryName(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.SPRUCEBUSH));
		
		setStandardSoils();
		addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		
		addGenFeature(
			new FeatureGenBush()
				.setLeavesState(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE))
				.setLogState(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE)), IGenFeature.FULLGEN);
	}
	
}
