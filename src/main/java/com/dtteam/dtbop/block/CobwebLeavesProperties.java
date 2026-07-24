package com.dtteam.dtbop.block;

import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.block.leaves.DynamicLeavesBlock;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.block.leaves.ScruffyLeavesProperties;
import com.dtteam.dynamictrees.utility.CoordUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class CobwebLeavesProperties extends ScruffyLeavesProperties {

    public static final TypedRegistry.EntryType<LeavesProperties> TYPE = TypedRegistry.newType(CobwebLeavesProperties::new);

    private static final SoundType COBWEB_SOUND_TYPE = new SoundType(1.0F, 1.0F,
            SoundEvents.VINE_STEP, SoundEvents.VINE_STEP, SoundEvents.VINE_STEP,
            SoundEvents.GRASS_HIT, SoundEvents.VINE_STEP);

    public CobwebLeavesProperties(Identifier registryName) {
        super(registryName);
    }

    @Override
    public String getBlockRegistryNameSuffix() {
        return "_web";
    }

    private float leafChance = 0.66f;
    private int maxHydro = 1;

    @Override
    public void setLeafChance(float leafChance) {
        this.leafChance = leafChance;
    }

    @Override
    public void setMaxHydro(int maxHydro) {
        this.maxHydro = maxHydro;
    }

    @Override
    protected @NotNull DynamicLeavesBlock createDynamicLeaves(BlockBehaviour.Properties properties) {
        return new DynamicLeavesBlock(getBlockRegistryName(), this, properties) {
            @Override
            public void fallOn(Level level, BlockState blockState, BlockPos pos, Entity entity, double fallDistance) {
            }

            @Override
            protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
                entity.makeStuckInBlock(state, new Vec3(0.25D, 0.05F, 0.25D));
            }

            @Override
            protected @NotNull SoundType getSoundType(@NotNull BlockState state) {
                return COBWEB_SOUND_TYPE;
            }

            @Override
            public int getHydrationLevelFromNeighbors(LevelAccessor level, BlockPos pos, LeavesProperties leavesProperties) {
                int hydro = super.getHydrationLevelFromNeighbors(level, pos, leavesProperties);
                if (hydro <= maxHydro) {
                    int hash = CoordUtils.coordHashCode(pos, 2) % 1000;
                    float rand = hash / 1000f;
                    if (rand >= leafChance) return 0;
                }
                return hydro;
            }
        };
    }
}
