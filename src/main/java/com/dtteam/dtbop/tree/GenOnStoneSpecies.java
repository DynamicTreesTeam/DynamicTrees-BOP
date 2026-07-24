package com.dtteam.dtbop.tree;

import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.block.soil.SoilHelper;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class GenOnStoneSpecies extends Species {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(GenOnStoneSpecies::new);

    public GenOnStoneSpecies(Identifier name, Family family, LeavesProperties leavesProperties) {
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
