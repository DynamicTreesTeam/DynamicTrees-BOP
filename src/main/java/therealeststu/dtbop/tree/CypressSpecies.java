package therealeststu.dtbop.tree;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.block.rooty.SoilHelper;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictrees.worldgen.JoCode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class CypressSpecies extends Species {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(CypressSpecies::new);

    public CypressSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);
    }

    private static final int maxDepth = 3;

    public boolean isAcceptableSoilForWorldgen(LevelAccessor level, BlockPos pos, BlockState soilBlockState) {
        final boolean isAcceptableSoil = isAcceptableSoil(level, pos, soilBlockState);

        // If the block is water, check the block below it is valid soil (and not water).
        if (isAcceptableSoil && isWater(soilBlockState)) {
            for (int i = 1; i <= maxDepth; i++) {
                final BlockPos down = pos.below(i);
                final BlockState downState = level.getBlockState(down);

                if (!isWater(downState) && isAcceptableSoilUnderWater(downState))
                    return true;
            }
            return false;
        }

        return isAcceptableSoil;
    }

    @Override
    public BlockPos preGeneration(LevelAccessor level, BlockPos.MutableBlockPos rootPosition, int radius, Direction facing, SafeChunkBounds safeBounds, JoCode joCode) {
        if (this.isWater(level.getBlockState(rootPosition))) {
            int i = 1;
            for (; i <= maxDepth; i++) {
                final BlockPos down = rootPosition.below(i);
                final BlockState downState = level.getBlockState(down);

                if (!isWater(downState) && isAcceptableSoilUnderWater(downState))
                    break;
            }
            rootPosition.move(Direction.DOWN, i);
        }
        return super.preGeneration(level, rootPosition, radius, facing, safeBounds, joCode);
    }

    public boolean isAcceptableSoilUnderWater(BlockState soilBlockState) {
        return SoilHelper.isSoilAcceptable(soilBlockState, this.soilTypeFlags | SoilHelper.getSoilFlags("sand_like", "mud_like"));
    }

}
