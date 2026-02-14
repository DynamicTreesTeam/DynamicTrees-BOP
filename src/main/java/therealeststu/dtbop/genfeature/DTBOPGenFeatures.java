package therealeststu.dtbop.genfeature;

import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictrees.systems.genfeature.*;
import net.minecraft.resources.ResourceLocation;
import therealeststu.dtbop.DynamicTreesBOP;

public class DTBOPGenFeatures {

    //public static final GenFeature ALT_LEAVES = new AlternativeLeavesGenFeature(new ResourceLocation(DynamicTreesBOP.MOD_ID, "alt_leaves"));
    public static final GenFeature EXTRA_FLARE_BOTTOM = new ExtraBottomFlareGenFeature(DynamicTreesBOP.location("extra_bottom_flare"));
    public static final GenFeature FALLEN_LEAVES = new FallenLeavesGenFeature(DynamicTreesBOP.location("fallen_leaves"));

    public static void register(final Registry<GenFeature> registry) {
        registry.registerAll(EXTRA_FLARE_BOTTOM, FALLEN_LEAVES);
    }

}
