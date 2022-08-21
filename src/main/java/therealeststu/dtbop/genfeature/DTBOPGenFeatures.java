package therealeststu.dtbop.genfeature;

import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import net.minecraft.util.ResourceLocation;
import therealeststu.dtbop.DynamicTreesBOP;

public class DTBOPGenFeatures {

    public static final GenFeature ALT_LEAVES = new AlternativeLeavesGenFeature(new ResourceLocation(DynamicTreesBOP.MOD_ID, "alt_leaves"));
    public static final GenFeature EXTRA_FLARE_BOTTOM = new ExtraBottomFlareGenFeature(new ResourceLocation(DynamicTreesBOP.MOD_ID, "extra_bottom_flare"));

    public static void register(final Registry<GenFeature> registry) {
        registry.registerAll(ALT_LEAVES, EXTRA_FLARE_BOTTOM);
    }

}
