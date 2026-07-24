package com.dtteam.dtbop.genfeature;

import com.dtteam.dynamictrees.api.registry.Registry;
import com.dtteam.dynamictrees.systems.genfeature.GenFeature;
import net.minecraft.resources.Identifier;
import com.dtteam.dtbop.DynamicTreesBOP;

public class DTBOPGenFeatures {

    public static final GenFeature ALT_LEAVES = new AlternativeLeavesGenFeature(Identifier.fromNamespaceAndPath(DynamicTreesBOP.MOD_ID, "alt_leaves"));
    public static final GenFeature EXTRA_FLARE_BOTTOM = new ExtraBottomFlareGenFeature(Identifier.fromNamespaceAndPath(DynamicTreesBOP.MOD_ID, "extra_bottom_flare"));

    public static void register(final Registry<GenFeature> registry) {
        registry.registerAll(EXTRA_FLARE_BOTTOM);
    }

}
