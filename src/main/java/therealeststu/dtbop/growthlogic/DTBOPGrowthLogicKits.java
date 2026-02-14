package therealeststu.dtbop.growthlogic;

import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import net.minecraft.resources.ResourceLocation;
import therealeststu.dtbop.DynamicTreesBOP;

public class DTBOPGrowthLogicKits {

    public static final GrowthLogicKit POPLAR = new PoplarLogic(DynamicTreesBOP.location("poplar"));
    public static final GrowthLogicKit CYPRESS = new CypressLogic(DynamicTreesBOP.location("cypress"));
    public static final GrowthLogicKit REDWOOD = new RedwoodLogic(DynamicTreesBOP.location("redwood"));
    public static final GrowthLogicKit SMALL_REDWOOD = new SmallRedwoodLogic(DynamicTreesBOP.location("small_redwood"));
    public static final GrowthLogicKit MAHOGANY = new MahoganyLogic(DynamicTreesBOP.location("mahogany"));

    public static void register(final Registry<GrowthLogicKit> registry) {
        registry.registerAll(POPLAR, CYPRESS, REDWOOD, SMALL_REDWOOD, MAHOGANY);
    }

}
