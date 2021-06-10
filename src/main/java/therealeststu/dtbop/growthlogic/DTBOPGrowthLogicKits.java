package therealeststu.dtbop.growthlogic;

import com.ferreusveritas.dynamictrees.api.registry.IRegistry;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import net.minecraft.util.ResourceLocation;
import therealeststu.dtbop.DynamicTreesBOP;

public class DTBOPGrowthLogicKits {

    public static final GrowthLogicKit POPLAR = new PoplarLogic(new ResourceLocation(DynamicTreesBOP.MOD_ID, "poplar"), false);
    public static final GrowthLogicKit LARGE_POPLAR = new PoplarLogic(new ResourceLocation(DynamicTreesBOP.MOD_ID, "large_poplar"), true);

    public static void register(final IRegistry<GrowthLogicKit> registry) {
        registry.registerAll(POPLAR, LARGE_POPLAR);
    }

}
