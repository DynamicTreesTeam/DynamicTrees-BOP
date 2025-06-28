package com.dtteam.dtbop.growthlogic;

import com.dtteam.dynamictrees.api.registry.Registry;
import com.dtteam.dynamictrees.systems.growthlogic.GrowthLogicKit;
import net.minecraft.resources.ResourceLocation;
import com.dtteam.dtbop.DynamicTreesBOP;

public class DTBOPGrowthLogicKits {

    public static final GrowthLogicKit POPLAR = new PoplarLogic(ResourceLocation.fromNamespaceAndPath(DynamicTreesBOP.MOD_ID, "poplar"));
    public static final GrowthLogicKit CYPRESS = new CypressLogic(ResourceLocation.fromNamespaceAndPath(DynamicTreesBOP.MOD_ID, "cypress"));
    public static final GrowthLogicKit REDWOOD = new RedwoodLogic(ResourceLocation.fromNamespaceAndPath(DynamicTreesBOP.MOD_ID, "redwood"));
    public static final GrowthLogicKit SMALL_REDWOOD = new SmallRedwoodLogic(ResourceLocation.fromNamespaceAndPath(DynamicTreesBOP.MOD_ID, "small_redwood"));
    public static final GrowthLogicKit MAHOGANY = new MahoganyLogic(ResourceLocation.fromNamespaceAndPath(DynamicTreesBOP.MOD_ID, "mahogany"));

    public static void register(final Registry<GrowthLogicKit> registry) {
        registry.registerAll(POPLAR, CYPRESS, REDWOOD, SMALL_REDWOOD, MAHOGANY);
    }

}
