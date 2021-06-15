package therealeststu.dtbop.growthlogic;

import com.ferreusveritas.dynamictrees.api.registry.IRegistry;
import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import net.minecraft.util.ResourceLocation;
import therealeststu.dtbop.DynamicTreesBOP;

public class DTBOPGrowthLogicKits {

    public static final GrowthLogicKit POPLAR = new PoplarLogic(new ResourceLocation(DynamicTreesBOP.MOD_ID, "poplar"), false);
    public static final GrowthLogicKit LARGE_POPLAR = new PoplarLogic(new ResourceLocation(DynamicTreesBOP.MOD_ID, "large_poplar"), true);
    public static final GrowthLogicKit CYPRESS = new CypressLogic(new ResourceLocation(DynamicTreesBOP.MOD_ID, "cypress"));
    public static final GrowthLogicKit FIR = new ConiferLogic(new ResourceLocation(DynamicTreesBOP.MOD_ID, "fir"),4.5f).setHeightVariation(8);
    public static final GrowthLogicKit SMALL_CONIFER = new ConiferLogic(new ResourceLocation(DynamicTreesBOP.MOD_ID, "small_conifer"),4.0f);
    public static final GrowthLogicKit REDWOOD = new RedwoodLogic(new ResourceLocation(DynamicTreesBOP.MOD_ID, "redwood"));
    public static final GrowthLogicKit SMALL_REDWOOD = new SmallRedwoodLogic(new ResourceLocation(DynamicTreesBOP.MOD_ID, "small_redwood"));

    public static void register(final IRegistry<GrowthLogicKit> registry) {
        registry.registerAll(POPLAR, LARGE_POPLAR, CYPRESS, FIR, SMALL_CONIFER, REDWOOD, SMALL_REDWOOD);
    }

}
