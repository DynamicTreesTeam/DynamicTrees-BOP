package therealeststu.dtbop.blocks.leaves;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.ForgeSoundType;

import javax.annotation.Nullable;

public class CobwebLeavesProperties extends LeavesProperties {
    public static final TypedRegistry.EntryType<LeavesProperties> TYPE = TypedRegistry.newType(CobwebLeavesProperties::new);

    public CobwebLeavesProperties(ResourceLocation registryName) {
        super(registryName);
    }

    protected String getBlockRegistryNameSuffix() {
        return "_web";
    }

    @Override
    protected DynamicLeavesBlock createDynamicLeaves(BlockBehaviour.Properties properties) {
        return new DynamicLeavesBlock(this, properties){


            @Override
            public void fallOn(Level world,BlockState blockState,  BlockPos pos, Entity entity, float fallDistance) {}

            @Override
            public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
                entity.makeStuckInBlock(state, new Vec3(0.25D, 0.05F, 0.25D));
            }

            @Override
            public SoundType getSoundType(BlockState state, LevelReader world, BlockPos pos, @Nullable Entity entity) {
                return new ForgeSoundType(1.0F, 1.0F, ()->SoundEvents.VINE_STEP, ()->SoundEvents.VINE_STEP, ()->SoundEvents.VINE_STEP, ()->SoundEvents.GRASS_HIT, ()->SoundEvents.VINE_STEP);
            }

        };
    }
}
