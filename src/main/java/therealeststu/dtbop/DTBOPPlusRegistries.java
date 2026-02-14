package therealeststu.dtbop;

import biomesoplenty.worldgen.feature.misc.*;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.FeatureCanceller;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictrees.worldgen.featurecancellation.MushroomFeatureCanceller;
import com.ferreusveritas.dynamictreesplus.block.mushroom.CapProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import therealeststu.dtbop.block.GlowShroomCapProperties;
import therealeststu.dtbop.tree.GlowshroomSpecies;

public class DTBOPPlusRegistries {

    public static final FeatureCanceller MUSHROOM_CANCELLER = new MushroomFeatureCanceller<>(DynamicTreesBOP.location("mushroom"), HugeMushroomFeatureConfiguration.class){
        @Override
        public boolean shouldCancel(final ConfiguredFeature<?, ?> configuredFeature, final BiomePropertySelectors.NormalFeatureCancellation featureCancellations) {
            final ResourceLocation featureRegistryName = ForgeRegistries.FEATURES.getKey(configuredFeature.feature());
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
    public static void onFeatureCancellerRegistry(final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<FeatureCanceller> event) {
        event.getRegistry().registerAll(MUSHROOM_CANCELLER);
    }

    @SubscribeEvent
    public static void registerCapPropertiesType(final TypeRegistryEvent<CapProperties> event) {
        event.registerType(DynamicTreesBOP.location("glowshroom"), GlowShroomCapProperties.TYPE);
    }

    @SubscribeEvent
    public static void registerSpeciesType(final TypeRegistryEvent<Species> event) {
        event.registerType(DynamicTreesBOP.location("glowshroom"), GlowshroomSpecies.TYPE);
    }

}
