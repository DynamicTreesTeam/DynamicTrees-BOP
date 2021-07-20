package therealeststu.dtbop.genfeature;

import com.ferreusveritas.dynamictrees.api.registry.IRegistry;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.VinesGenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.config.ConfiguredGenFeature;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import therealeststu.dtbop.DynamicTreesBOP;

import java.util.List;
import java.util.Objects;

public class DTBOPGenFeatures {

    public static final GenFeature ALT_LEAVES = new AlternativeLeavesGenFeature(new ResourceLocation(DynamicTreesBOP.MOD_ID, "alt_leaves"));
    public static final GenFeature EXTRA_FLARE_BOTTOM = new ExtraBottomFlareGenFeature(new ResourceLocation(DynamicTreesBOP.MOD_ID, "extra_bottom_flare"));

    public static void register(final IRegistry<GenFeature> registry) {
        registry.registerAll(ALT_LEAVES, EXTRA_FLARE_BOTTOM);
    }

}
