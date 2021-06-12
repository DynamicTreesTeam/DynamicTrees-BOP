package therealeststu.dtbop.genfeature;

import com.ferreusveritas.dynamictrees.api.registry.IRegistry;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.VinesGenFeature;
import net.minecraft.util.ResourceLocation;
import therealeststu.dtbop.DynamicTreesBOP;

public class DTBOPGenFeatures {

    public static final GenFeature ALT_LEAVES = new AlternativeLeavesGenFeature(new ResourceLocation(DynamicTreesBOP.MOD_ID, "alt_leaves"));
    public static final GenFeature EXTRA_FLARE_BOTTOM = new ExtraBottomFlareGenFeature(new ResourceLocation(DynamicTreesBOP.MOD_ID, "extra_bottom_flare"));
    public static final GenFeature VINES_2 = new VinesGenFeature(new ResourceLocation(DynamicTreesBOP.MOD_ID, "vines_2")); //a second vines gen feature so we can have multiple vine types in the same tree

    public static void register(final IRegistry<GenFeature> registry) {
        registry.registerAll(ALT_LEAVES, EXTRA_FLARE_BOTTOM, VINES_2);
    }

}
