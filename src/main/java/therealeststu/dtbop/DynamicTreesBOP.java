package therealeststu.dtbop;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.data.provider.DTBlockTagsProvider;
import com.ferreusveritas.dynamictrees.data.provider.DTItemTagsProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DynamicTreesBOP.MOD_ID)
public class DynamicTreesBOP
{
    public static final String MOD_ID = "dtbop";

    public DynamicTreesBOP() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::gatherData);

        MinecraftForge.EVENT_BUS.register(this);

        RegistryHandler.setup(MOD_ID);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        final BlockColors blockColors = Minecraft.getInstance().getBlockColors();

        blockColors.register((state, world, pos, tintIndex) -> DTBOPRegistries.largeRootyWater.colorMultiplier(blockColors, state, world, pos, tintIndex), DTBOPRegistries.largeRootyWater);
    }

    public void gatherData(final GatherDataEvent event) {
        gatherTagGenerators(MOD_ID, event);
    }
    public static void gatherTagGenerators(final String modId, final GatherDataEvent event) {
        final BlockTagsProvider blockTagsProvider = new DTBlockTagsProvider(event.getGenerator(), modId, event.getExistingFileHelper());
        final ItemTagsProvider itemTagsProvider = new DTItemTagsProvider(event.getGenerator(), MOD_ID, blockTagsProvider, event.getExistingFileHelper());

        event.getGenerator().addProvider(blockTagsProvider);
        event.getGenerator().addProvider(itemTagsProvider);
    }

}
