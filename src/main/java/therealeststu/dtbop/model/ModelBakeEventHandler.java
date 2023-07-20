package therealeststu.dtbop.model;


import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent.RegisterGeometryLoaders;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import therealeststu.dtbop.DynamicTreesBOP;

@Mod.EventBusSubscriber(modid = DynamicTreesBOP.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModelBakeEventHandler {

    @SubscribeEvent
    public static void onModelRegistryEvent(RegisterGeometryLoaders event) {
        event.register(new ResourceLocation(DynamicTreesBOP.MOD_ID, "palm_fronds").getPath(), new PalmLeavesModelLoader());
    }

}
