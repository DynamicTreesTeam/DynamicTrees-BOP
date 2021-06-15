package therealeststu.dtbop.trees;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.RootyBlock;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.RootyBlockHelper;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictrees.worldgen.JoCode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import therealeststu.dtbop.DTBOPRegistries;

public class CypressSpecies extends Species {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(CypressSpecies::new);

    public CypressSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);
    }

    @Override
    public Direction selectNewDirection(World world, BlockPos pos, BranchBlock branch, GrowSignal signal) {
        return super.selectNewDirection(world, pos, branch, signal);
    }

    @Override
    public boolean placeRootyDirtBlock(IWorld world, BlockPos rootPos, int fertility) {
        if (this.isWater(world.getBlockState(rootPos)))
            return world.setBlock(rootPos, (DTBOPRegistries.largeRootyWater.defaultBlockState().setValue(RootyBlock.FERTILITY, fertility)).setValue(RootyBlock.IS_VARIANT, this.doesRequireTileEntity(world, rootPos)), 3);
        else
            return super.placeRootyDirtBlock(world, rootPos, fertility);
    }

    @Override
    public BlockPos preGeneration(IWorld world, BlockPos rootPosition, int radius, Direction facing, SafeChunkBounds safeBounds, JoCode joCode) {
        BlockPos root = rootPosition;
        if (this.isWater(world.getBlockState(rootPosition))){
            if (this.isWater(world.getBlockState(rootPosition.below()))){
                root = root.below();
            }
        }
        return super.preGeneration(world, root, radius, facing, safeBounds, joCode);
    }

    @Override
    public boolean postGrow(World world, BlockPos rootPos, BlockPos treePos, int fertility, boolean natural) {
        if (world.getBlockState(rootPos).getBlock() == RootyBlockHelper.getRootyBlock(Blocks.WATER)){
            if (TreeHelper.isBranch(world.getBlockState(rootPos.above()))){
                int radius = TreeHelper.getRadius(world, rootPos.above());
                if (radius >= 8){
                    TileEntity rootTE = world.getBlockEntity(rootPos);
                    world.setBlockAndUpdate(rootPos, DTBOPRegistries.largeRootyWater.defaultBlockState()
                            .setValue(RootyBlock.FERTILITY, fertility)
                            .setValue(RootyBlock.IS_VARIANT, world.getBlockState(rootPos).getValue(RootyBlock.IS_VARIANT)));
                    if (rootTE != null)
                        world.setBlockEntity(rootPos, rootTE);
                }
            }
        }
        return super.postGrow(world, rootPos, treePos, fertility, natural);
    }

}
