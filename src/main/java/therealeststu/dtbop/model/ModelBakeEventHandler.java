package therealeststu.dtbop.model;


import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import therealeststu.dtbop.DynamicTreesBOP;

@Mod.EventBusSubscriber(modid = DynamicTreesBOP.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModelBakeEventHandler {

    @SubscribeEvent
    public static void onModelRegistryEvent(ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(DynamicTreesBOP.MOD_ID, "palm_fronds"), new PalmLeavesModelLoader());
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        // Setup fronds models
        PalmLeavesBakedModel.INSTANCES.forEach(PalmLeavesBakedModel::setupModels);
    }

}
