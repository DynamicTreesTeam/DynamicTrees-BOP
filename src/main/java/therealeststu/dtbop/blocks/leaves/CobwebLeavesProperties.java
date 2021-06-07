package therealeststu.dtbop.blocks.leaves;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.leaves.SolidLeavesProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
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
    protected DynamicLeavesBlock createDynamicLeaves(AbstractBlock.Properties properties) {
        return new DynamicLeavesBlock(this, properties){

            @Override
            public void fallOn(World world, BlockPos pos, Entity entity, float fallDistance) {}

            @Override
            public void entityInside(BlockState state, World world, BlockPos pos, Entity entity) {
                entity.makeStuckInBlock(state, new Vector3d(0.25D, 0.05F, 0.25D));
            }

            @Override
            public SoundType getSoundType(BlockState state, IWorldReader world, BlockPos pos, @Nullable Entity entity) {
                return new ForgeSoundType(1.0F, 1.0F, ()->SoundEvents.VINE_STEP, ()->SoundEvents.VINE_STEP, ()->SoundEvents.VINE_STEP, ()->SoundEvents.GRASS_HIT, ()->SoundEvents.VINE_STEP);
            }

        };
    }
}
