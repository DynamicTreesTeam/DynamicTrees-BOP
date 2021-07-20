package therealeststu.dtbop.dropcreators;

import com.ferreusveritas.dynamictrees.api.registry.IRegistry;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreator;
import net.minecraft.util.ResourceLocation;
import therealeststu.dtbop.DynamicTreesBOP;

public class DTBOPDropCreators {

    public static final DropCreator LEAVES_STRING = new StringDropCreator(new ResourceLocation(DynamicTreesBOP.MOD_ID, "leaves_string"));

    public static void register(final IRegistry<DropCreator> registry) {
        registry.registerAll(LEAVES_STRING);
    }


}
