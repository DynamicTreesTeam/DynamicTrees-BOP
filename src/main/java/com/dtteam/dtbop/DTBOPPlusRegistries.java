package com.dtteam.dtbop;

import biomesoplenty.worldgen.feature.misc.*;

import com.dtteam.dynamictrees.event.TypeRegistryEvent;
import com.dtteam.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.dtteam.dynamictrees.api.worldgen.FeatureCanceller;
import com.dtteam.dynamictrees.tree.species.Species;
import com.dtteam.dynamictrees.worldgen.featurecancellation.MushroomFeatureCanceller;
import com.dtteam.dynamictreesplus.block.mushroom.CapProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import com.dtteam.dtbop.block.GlowShroomCapProperties;
import com.dtteam.dtbop.tree.GlowshroomSpecies;

public class DTBOPPlusRegistries {

    public static final FeatureCanceller MUSHROOM_CANCELLER = new MushroomFeatureCanceller<>(ResourceLocation.fromNamespaceAndPath(DynamicTreesBOP.MOD_ID,"mushroom"), HugeMushroomFeatureConfiguration.class){
        @Override
        public boolean shouldCancel(final ConfiguredFeature<?, ?> configuredFeature, final BiomePropertySelectors.NormalFeatureCancellation featureCancellations) {
            final ResourceLocation featureRegistryName = BuiltInRegistries.FEATURE.getKey(configuredFeature.feature());
            if (featureRegistryName == null) {return false;}

            if (configuredFeature.config() instanceof HugeMushroomFeatureConfiguration) {
                return true;
            }

            Feature<?> f = configuredFeature.feature();

            return f instanceof SmallRedMushroomFeature ||
                    f instanceof HugeToadstoolFeature ||
                    f instanceof SmallToadstoolFeature ||
                    f instanceof SmallGlowshroomFeature ||
                    f instanceof MediumGlowshroomFeature ||
                    f instanceof HugeGlowshroomFeature ||
                    f instanceof GiantGlowshroomFeature ||
                    f instanceof SmallBrownMushroomFeature;
        }
    };

    @SubscribeEvent
    public static void onFeatureCancellerRegistry(final com.dtteam.dynamictrees.event.RegistryEvent<FeatureCanceller> event) {
        if (event.isEntryOfType(FeatureCanceller.class)){
            event.getRegistry().registerAll(MUSHROOM_CANCELLER);
        }
    }

    @SubscribeEvent
    public static void registerCapPropertiesType(final TypeRegistryEvent<CapProperties> event) {
        if (event.isEntryOfType(CapProperties.class)){
            event.registerType(DynamicTreesBOP.location("glowshroom"), GlowShroomCapProperties.TYPE);
        }
    }

    @SubscribeEvent
    public static void registerSpeciesType(final TypeRegistryEvent<Species> event) {
        if (event.isEntryOfType(Species.class)){
            event.registerType(DynamicTreesBOP.location("glowshroom"), GlowshroomSpecies.TYPE);
        }
    }

}
