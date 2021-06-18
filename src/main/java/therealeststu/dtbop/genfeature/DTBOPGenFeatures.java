package therealeststu.dtbop.genfeature;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.init.ModBiomes;
import com.ferreusveritas.dynamictrees.api.registry.IRegistry;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.VinesGenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.config.ConfiguredGenFeature;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import therealeststu.dtbop.DynamicTreesBOP;

import java.util.List;
import java.util.Objects;

public class DTBOPGenFeatures {

    public static final GenFeature ALT_LEAVES = new AlternativeLeavesGenFeature(new ResourceLocation(DynamicTreesBOP.MOD_ID, "alt_leaves"));
    public static final GenFeature EXTRA_FLARE_BOTTOM = new ExtraBottomFlareGenFeature(new ResourceLocation(DynamicTreesBOP.MOD_ID, "extra_bottom_flare"));
    public static final GenFeature VINES_DEEP_BAYOU = new VinesGenFeature(new ResourceLocation(DynamicTreesBOP.MOD_ID, "vines_deep_bayou")){
        @Override public boolean postGeneration(ConfiguredGenFeature<?> configuredGenFeature, IWorld world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, BlockState initialDirtState, Float seasonValue, Float seasonFruitProductionFactor) {
            if (!Objects.requireNonNull(world.getBiome(rootPos).getRegistryName()).toString().equals("biomesoplenty:deep_bayou")) return false;
            return super.postGeneration(configuredGenFeature, world, rootPos, species, biome, radius, endPoints, safeBounds, initialDirtState, seasonValue, seasonFruitProductionFactor);
        }
    }; //vines that only appear in

    public static void register(final IRegistry<GenFeature> registry) {
        registry.registerAll(ALT_LEAVES, EXTRA_FLARE_BOTTOM, VINES_DEEP_BAYOU);
    }

}
