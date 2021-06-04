package therealeststu.dtbop.genfeature;

import com.ferreusveritas.dynamictrees.api.registry.IRegistry;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import net.minecraft.util.ResourceLocation;
import therealeststu.dtbop.DynamicTreesBOP;

public class DTBOPGenFeatures {

    public static final GenFeature ALT_LEAVES = new AlternativeLeavesGenFeature(new ResourceLocation(DynamicTreesBOP.MOD_ID, "alt_leaves"));

    public static void register(final IRegistry<GenFeature> registry) {
        registry.registerAll(ALT_LEAVES);
    }

}
