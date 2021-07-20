package therealeststu.dtbop.trees;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.RootyBlock;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.SoilHelper;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class GenOnStoneSpecies extends Species {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(GenOnStoneSpecies::new);

    public GenOnStoneSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);
    }

    @Override
    public boolean isAcceptableSoilForWorldgen(IWorld world, BlockPos pos, BlockState soilBlockState) {
        if (soilBlockState.is(Blocks.STONE))
            return true;
        return super.isAcceptableSoilForWorldgen(world, pos, soilBlockState);
    }

    @Override
    public boolean placeRootyDirtBlock(IWorld world, BlockPos rootPos, int fertility) {
        if (world.getBlockState(rootPos).is(Blocks.STONE)) {
            RootyBlock rootyBlock = SoilHelper.getProperties(Blocks.GRAVEL).getDynamicSoilBlock();
            if (rootyBlock != null)
                world.setBlock(rootPos, rootyBlock.defaultBlockState(), 3);//the super does the rest of setting up the soil
        }
        return super.placeRootyDirtBlock(world, rootPos, fertility);
    }

}
