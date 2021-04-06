package dynamictreesbop.trees.species;

import com.ferreusveritas.dynamictrees.api.IGenFeature;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.featuregen.FeatureGenBrushBush;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

public class SpeciesAcaciaBrushBush extends Species {

    public SpeciesAcaciaBrushBush() {
        super();
        setRegistryName(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.ACACIABRUSHBUSH));

        addAcceptableSoils(DirtHelper.DIRTLIKE, DirtHelper.SANDLIKE);

        addGenFeature(new FeatureGenBrushBush()
                .setBlockState(Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockNewLeaf.DECAYABLE, false).withProperty(BlockNewLeaf.CHECK_DECAY, false)),
                IGenFeature.FULLGEN);
    }
}
