package dynamictreesbop.trees.species;

import com.ferreusveritas.dynamictrees.api.IGenFeature;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenBush;
import com.ferreusveritas.dynamictrees.trees.Species;

import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

public class SpeciesAcaciaBush extends Species {
	
	public SpeciesAcaciaBush() {
		super();
		setRegistryName(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.ACACIABUSH));
		
		addAcceptableSoils(DirtHelper.DIRTLIKE, DirtHelper.SANDLIKE);
		
		addGenFeature(new FeatureGenBush()
			.setLeavesState(Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.ACACIA))
			.setLogState(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA)), IGenFeature.FULLGEN);
	}
	
}
