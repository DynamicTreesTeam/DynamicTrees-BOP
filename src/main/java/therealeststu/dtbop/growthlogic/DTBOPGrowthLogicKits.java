package therealeststu.dtbop.growthlogic;

import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import net.minecraft.util.ResourceLocation;
import therealeststu.dtbop.DynamicTreesBOP;

public class DTBOPGrowthLogicKits {

    public static final GrowthLogicKit POPLAR = new PoplarLogic(new ResourceLocation(DynamicTreesBOP.MOD_ID, "poplar"));
    public static final GrowthLogicKit CYPRESS = new CypressLogic(new ResourceLocation(DynamicTreesBOP.MOD_ID, "cypress"));
    public static final GrowthLogicKit REDWOOD = new RedwoodLogic(new ResourceLocation(DynamicTreesBOP.MOD_ID, "redwood"));
    public static final GrowthLogicKit SMALL_REDWOOD = new SmallRedwoodLogic(new ResourceLocation(DynamicTreesBOP.MOD_ID, "small_redwood"));
    public static final GrowthLogicKit MAHOGANY = new MahoganyLogic(new ResourceLocation(DynamicTreesBOP.MOD_ID, "mahogany"));

    public static void register(final Registry<GrowthLogicKit> registry) {
        registry.registerAll(POPLAR, CYPRESS, REDWOOD, SMALL_REDWOOD, MAHOGANY);
    }

}
