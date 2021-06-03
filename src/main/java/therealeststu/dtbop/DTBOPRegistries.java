package therealeststu.dtbop;

import biomesoplenty.api.block.BOPBlocks;
import com.ferreusveritas.dynamictrees.api.cells.CellKit;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.RootyBlock;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.RootyBlockHelper;
import com.ferreusveritas.dynamictrees.systems.dropcreators.FruitDropCreator;
import com.ferreusveritas.dynamictrees.trees.Mushroom;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import therealeststu.dtbop.cells.DTBOPCellKits;
import therealeststu.dtbop.trees.Bush;
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
    public static void registerSpecies (final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<Species> event) {
        // Registers fake species for generating bushes.
        event.getRegistry().registerAll(new Bush("flowering_oak_bush", new ResourceLocation("oak_log"), new ResourceLocation("oak_leaves"), new ResourceLocation("biomesoplenty", "flowering_oak_leaves")));
    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
        Bush.INSTANCES.forEach((Bush::setup));

        DirtHelper.registerSoil(BOPBlocks.origin_grass_block, DirtHelper.DIRT_LIKE);
        DirtHelper.registerSoil(BOPBlocks.white_sand, DirtHelper.SAND_LIKE);
        DirtHelper.registerSoil(BOPBlocks.orange_sand, DirtHelper.SAND_LIKE);
        DirtHelper.registerSoil(BOPBlocks.black_sand, DirtHelper.SAND_LIKE);
        DirtHelper.registerSoil(BOPBlocks.dried_salt, DirtHelper.SAND_LIKE);
        DirtHelper.registerSoil(BOPBlocks.mud, DirtHelper.MUD_LIKE);

        for (RootyBlock rooty : RootyBlockHelper.generateListForRegistry(true, DynamicTreesBOP.MOD_ID))
            event.getRegistry().register(rooty);
    }

}
