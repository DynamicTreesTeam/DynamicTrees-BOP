package therealeststu.dtbop;

import com.ferreusveritas.dynamictrees.api.cells.CellKit;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import therealeststu.dtbop.cells.DTBOPCellKits;
import therealeststu.dtbop.trees.PoplarSpecies;
import therealeststu.dtbop.trees.TwigletSpecies;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DTBOPRegistries {

    @SubscribeEvent
    public static void registerLeavesPropertiesTypes (final TypeRegistryEvent<LeavesProperties> event) {

    }

    @SubscribeEvent
    public static void registerSpeciesTypes (final TypeRegistryEvent<Species> event) {
        event.registerType(new ResourceLocation(DynamicTreesBOP.MOD_ID, "twiglet"), TwigletSpecies.TYPE);
        event.registerType(new ResourceLocation(DynamicTreesBOP.MOD_ID, "poplar"), PoplarSpecies.TYPE);
    }

    @SubscribeEvent
    public static void onCellKitRegistry (final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<CellKit> event) {
        event.getRegistry().registerAll(DTBOPCellKits.SPARSE, DTBOPCellKits.POPLAR,
                DTBOPCellKits.MAHOGANY, DTBOPCellKits.BRUSH, DTBOPCellKits.EUCALYPTUS);
    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {

    }

}
