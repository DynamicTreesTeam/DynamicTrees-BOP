package therealeststu.dtbop.block;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.block.leaves.ScruffyLeavesProperties;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ForgeSoundType;

import javax.annotation.Nullable;

public class CobwebLeavesProperties extends ScruffyLeavesProperties {
    public static final TypedRegistry.EntryType<LeavesProperties> TYPE = TypedRegistry.newType(CobwebLeavesProperties::new);

    public CobwebLeavesProperties(ResourceLocation registryName) {
        super(registryName);
    }

    public String getBlockRegistryNameSuffix() {
        return "_web";
    }

    private float leafChance = 0.66f;
    private int maxHydro = 1;

    @Override public void setLeafChance (float leafChance){
        this.leafChance = leafChance;
    }
    @Override public void setMaxHydro (int maxHydro) {
        this.maxHydro = maxHydro;
    }

    @Override
    protected DynamicLeavesBlock createDynamicLeaves(BlockBehaviour.Properties properties) {
        return new DynamicLeavesBlock(this, properties) {
            @Override
            public void fallOn(Level level, BlockState blockState, BlockPos pos, Entity entity, float fallDistance) {
            }

            @Override
            public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
                entity.makeStuckInBlock(state, new Vec3(0.25D, 0.05F, 0.25D));
            }

            @Override
            public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
                return new ForgeSoundType(1.0F, 1.0F, () -> SoundEvents.VINE_STEP, () -> SoundEvents.VINE_STEP, () -> SoundEvents.VINE_STEP, () -> SoundEvents.GRASS_HIT, () -> SoundEvents.VINE_STEP);
            }

            public int getHydrationLevelFromNeighbors(LevelAccessor level, BlockPos pos, LeavesProperties leavesProperties) {
                int hydro = super.getHydrationLevelFromNeighbors(level, pos, leavesProperties);
                if (hydro <= maxHydro){
                    int hash = CoordUtils.coordHashCode(pos, 2) % 1000;
                    float rand = hash / 1000f;
                    if (rand >= leafChance) return 0;
                }
                return hydro;
            }
        };
    }
}
