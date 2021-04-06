package dynamictreesbop.trees.species;

import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.common.block.BlockBOPLeaves;
import com.ferreusveritas.dynamictrees.api.IGenFeature;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.featuregen.FeatureGenBrushBush;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

public class SpeciesDeadBrushBush extends Species {

    public SpeciesDeadBrushBush() {
        super();
        setRegistryName(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.DEADBRUSHBUSH));

        addAcceptableSoils(DirtHelper.DIRTLIKE);

        addGenFeature(new FeatureGenBrushBush()
                .setBlockState(BlockBOPLeaves.paging.getVariantState(BOPTrees.DEAD).withProperty(BlockLeaves.DECAYABLE, false).withProperty(BlockLeaves.CHECK_DECAY, false)),
                IGenFeature.FULLGEN);
    }

}
