package com.dtteam.dtbop.genfeature;

import com.dtteam.dynamictrees.api.registry.Registry;
import com.dtteam.dynamictrees.systems.genfeature.GenFeature;
import net.minecraft.resources.ResourceLocation;
import com.dtteam.dtbop.DynamicTreesBOP;

public class DTBOPGenFeatures {

    public static final GenFeature ALT_LEAVES = new AlternativeLeavesGenFeature(ResourceLocation.fromNamespaceAndPath(DynamicTreesBOP.MOD_ID, "alt_leaves"));
    public static final GenFeature EXTRA_FLARE_BOTTOM = new ExtraBottomFlareGenFeature(ResourceLocation.fromNamespaceAndPath(DynamicTreesBOP.MOD_ID, "extra_bottom_flare"));

    public static void register(final Registry<GenFeature> registry) {
        registry.registerAll(EXTRA_FLARE_BOTTOM);
    }

}
