package therealeststu.dtbop.tree;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.block.rooty.SoilHelper;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class GenOnStoneSpecies extends Species {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(GenOnStoneSpecies::new);

    public GenOnStoneSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);
    }

    @Override
    public boolean isAcceptableSoilForWorldgen(LevelAccessor level, BlockPos pos, BlockState soilBlockState) {
        if (soilBlockState.is(Blocks.STONE))
            return true;
        return super.isAcceptableSoilForWorldgen(level, pos, soilBlockState);
    }

    @Override
    public boolean placeRootyDirtBlock(LevelAccessor level, BlockPos rootPos, int fertility) {
        if (level.getBlockState(rootPos).is(Blocks.STONE)) {
            SoilHelper.getProperties(Blocks.GRAVEL).getBlock().ifPresent(rootyBlock -> {
                level.setBlock(rootPos, rootyBlock.defaultBlockState(), 3);//the super does the rest of setting up the soil
            });
        }
        return super.placeRootyDirtBlock(level, rootPos, fertility);
    }

}
