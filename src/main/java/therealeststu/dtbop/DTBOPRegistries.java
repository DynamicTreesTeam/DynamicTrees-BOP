package therealeststu.dtbop;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.ferreusveritas.dynamictrees.api.cells.CellKit;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.leaves.PalmLeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.leaves.SolidLeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.leaves.WartProperties;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.RootyBlock;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.RootyBlockHelper;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreator;
import com.ferreusveritas.dynamictrees.systems.genfeatures.BeeNestGenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import therealeststu.dtbop.blocks.leaves.CobwebLeavesProperties;
import therealeststu.dtbop.cells.DTBOPCellKits;
import therealeststu.dtbop.dropcreators.StringDropCreator;
import therealeststu.dtbop.genfeature.AlternativeLeavesGenFeature;
import therealeststu.dtbop.genfeature.DTBOPGenFeatures;
import therealeststu.dtbop.growthlogic.DTBOPGrowthLogicKits;
import therealeststu.dtbop.trees.Bush;
import therealeststu.dtbop.trees.PoplarSpecies;
import therealeststu.dtbop.trees.TwigletSpecies;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DTBOPRegistries {

    @SubscribeEvent
    public static void onGenFeatureRegistry (final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<GenFeature> event) {
        DTBOPGenFeatures.register(event.getRegistry());
    }
    @SubscribeEvent
    public static void onGrowthLogicKitRegistry (final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<GrowthLogicKit> event) {
        DTBOPGrowthLogicKits.register(event.getRegistry());
    }
    @SubscribeEvent
    public static void onCellKitRegistry (final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<CellKit> event) {
        DTBOPCellKits.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerLeavesPropertiesTypes(TypeRegistryEvent<LeavesProperties> event) {
        event.registerType(new ResourceLocation(DynamicTreesBOP.MOD_ID, "cobweb"), CobwebLeavesProperties.TYPE);
    }

    @SubscribeEvent
    public static void registerSpeciesTypes (final TypeRegistryEvent<Species> event) {
        event.registerType(new ResourceLocation(DynamicTreesBOP.MOD_ID, "twiglet"), TwigletSpecies.TYPE);
        event.registerType(new ResourceLocation(DynamicTreesBOP.MOD_ID, "poplar"), PoplarSpecies.TYPE);
    }

    @SubscribeEvent
    public static void registerSpecies (final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<Species> event) {
        // Registers fake species for generating bushes.
        event.getRegistry().registerAll(new Bush("flowering_oak_bush", new ResourceLocation("oak_log"), new ResourceLocation("oak_leaves"), new ResourceLocation("biomesoplenty", "flowering_oak_leaves")));
        event.getRegistry().registerAll(new Bush("oak_bush", new ResourceLocation("oak_log"), new ResourceLocation("oak_leaves")));
        event.getRegistry().registerAll(new Bush("infested_oak_bush", new ResourceLocation("oak_log"), new ResourceLocation("oak_leaves"), new ResourceLocation("cobweb")));
        event.getRegistry().registerAll(new Bush("silk_bush", new ResourceLocation("oak_log"), new ResourceLocation("cobweb")));
        event.getRegistry().registerAll(new Bush("acacia_bush", new ResourceLocation("acacia_log"), new ResourceLocation("acacia_leaves")).addAcceptableSoils(DirtHelper.SAND_LIKE));
        event.getRegistry().registerAll(new Bush("spruce_bush", new ResourceLocation("spruce_log"), new ResourceLocation("spruce_leaves")));

    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
        Bush.INSTANCES.forEach(Bush::setup);

        final Species floweringOak = Species.REGISTRY.get(new ResourceLocation(DynamicTreesBOP.MOD_ID, "flowering_oak"));
        final Species infested = Species.REGISTRY.get(new ResourceLocation(DynamicTreesBOP.MOD_ID, "infested"));
        final Species silk = Species.REGISTRY.get(new ResourceLocation(DynamicTreesBOP.MOD_ID, "silk"));
        final Species rainbow_birch = Species.REGISTRY.get(new ResourceLocation(DynamicTreesBOP.MOD_ID, "rainbow_birch"));

        if (floweringOak.isValid()){
            LeavesProperties floweringLeaves = LeavesProperties.REGISTRY.get(new ResourceLocation(DynamicTreesBOP.MOD_ID, "flowering_oak"));
            floweringLeaves.setFamily(floweringOak.getFamily());
            floweringOak.addValidLeafBlocks(floweringLeaves);
        }
        if (infested.isValid()){
            LeavesProperties silkLeaves = LeavesProperties.REGISTRY.get(new ResourceLocation(DynamicTreesBOP.MOD_ID, "silk"));
            infested.addValidLeafBlocks(silkLeaves);
            infested.addDropCreator(new StringDropCreator(0.3f));
        }
        if (silk.isValid()){
            silk.addDropCreator(new StringDropCreator(1f));
        }
        if (rainbow_birch.isValid()){
            rainbow_birch.addGenFeature(new BeeNestGenFeature(new ResourceLocation("dynamictrees","bee_nest"))
                    .with(BeeNestGenFeature.WORLD_GEN_CHANCE_FUNCTION, (world,pos)->{
                        RegistryKey<Biome> biomeKey = RegistryKey.create(Registry.BIOME_REGISTRY, Objects.requireNonNull(world.getUncachedNoiseBiome(pos.getX() >> 2, pos.getY() >> 2, pos.getZ() >> 2).getRegistryName()));
                        if (biomeKey == BOPBiomes.rainbow_hills)
                            return 0.02;
                         else return BiomeDictionary.hasType(biomeKey, BiomeDictionary.Type.FOREST) ? 0.0005 : 0.0;
                    }));
        }

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
