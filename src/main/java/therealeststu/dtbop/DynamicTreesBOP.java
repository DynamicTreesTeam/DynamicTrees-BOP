package therealeststu.dtbop;

import com.ferreusveritas.dynamictrees.api.GatherDataHelper;
import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.block.rooty.SoilProperties;
import com.ferreusveritas.dynamictrees.resources.Resources;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.data.event.GatherDataEvent;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DynamicTreesBOP.MOD_ID)
public class DynamicTreesBOP {
    public static final String MOD_ID = "dtbop";

    public DynamicTreesBOP() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
//        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::gatherData);

        MinecraftForge.EVENT_BUS.register(this);

        RegistryHandler.setup(MOD_ID);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

//    @OnlyIn(Dist.CLIENT)
//    private void clientSetup(final FMLClientSetupEvent event) {
//        final BlockColors blockColors = Minecraft.getInstance().getBlockColors();
//
//        blockColors.register((state, world, pos, tintIndex) -> DTBOPRegistries.largeRootyWater.colorMultiplier(blockColors, state, world, pos, tintIndex), DTBOPRegistries.largeRootyWater);
//    }

    private void gatherData(final GatherDataEvent event) {
        GatherDataHelper.gatherAllData(MOD_ID, event,
                SoilProperties.REGISTRY,
                Family.REGISTRY,
                Species.REGISTRY,
                LeavesProperties.REGISTRY
        );
    }

}
