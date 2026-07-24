package com.dtteam.dtbop;

import com.dtteam.dynamictrees.api.DynamicTreesAddonEntrypoint;
import net.minecraft.resources.Identifier;

/**
 * Main entrypoint for the Dynamic Trees BOP addon on Fabric.
 *
 * <p>Dynamic Trees invokes {@link #onDynamicTreesPreSetup()} from its own {@code ModInitializer},
 * before it registers its own content and loads tree packs, so all addon registration happens
 * here.</p>
 */
public class DynamicTreesBOP implements DynamicTreesAddonEntrypoint {

    public static final String MOD_ID = "dtbop";

    @Override
    public void onDynamicTreesPreSetup() {
        DynamicTreesAddonEntrypoint.setupAddon(MOD_ID);
        DTBOPRegistries.setup();
    }

    public static Identifier location(String name) {
        return Identifier.fromNamespaceAndPath(MOD_ID, name);
    }

}
