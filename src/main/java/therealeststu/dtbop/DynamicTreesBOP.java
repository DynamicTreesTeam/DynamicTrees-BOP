package therealeststu.dtbop;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DynamicTreesBOP.MOD_ID)
public class DynamicTreesBOP
{
    public static final String MOD_ID = "dtbop";

    public DynamicTreesBOP() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);

        RegistryHandler.setup(MOD_ID);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        final BlockColors blockColors = Minecraft.getInstance().getBlockColors();

        blockColors.register((state, world, pos, tintIndex) -> DTBOPRegistries.largeRootyWater.colorMultiplier(blockColors, state, world, pos, tintIndex), DTBOPRegistries.largeRootyWater);
    }

}
