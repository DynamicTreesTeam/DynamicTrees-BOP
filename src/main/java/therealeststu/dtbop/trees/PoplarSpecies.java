package therealeststu.dtbop.trees;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.network.INodeInspector;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.api.treedata.ITreePart;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import therealeststu.dtbop.cells.DTBOPLeafClusters;

public class PoplarSpecies extends Species {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(PoplarSpecies::new);

    public PoplarSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);
    }

    @Override
    public INodeInspector getNodeInflator(SimpleVoxmap leafMap) {
        return new NodeInflatorPoplar(this, leafMap);
    }

    public class NodeInflatorPoplar implements INodeInspector {

        private float radius;
        private BlockPos last;

        Species species;
        SimpleVoxmap leafMap;

        public NodeInflatorPoplar(Species species, SimpleVoxmap leafMap) {
            this.species = species;
            this.leafMap = leafMap;
            last = BlockPos.ZERO;
        }

        @Override
        public boolean run(BlockState blockState, IWorld world, BlockPos pos, Direction fromDir) {
            BranchBlock branch = TreeHelper.getBranch(blockState);

            if (branch != null) {
                radius = species.getFamily().getPrimaryThickness();
            }
            return false;
        }

        @Override
        public boolean returnRun(BlockState blockState, IWorld world, BlockPos pos, Direction fromDir) {
            // Calculate Branch Thickness based on neighboring branches
            BranchBlock branch = TreeHelper.getBranch(blockState);

            if (branch != null) {
                float areaAccum = radius * radius; // Start by accumulating the branch we just came from
                boolean isTwig = true;
                boolean isTop = (world.getBlockState(pos.below()).getBlock() == branch);

                for (Direction dir: Direction.values()) {
                    if(!dir.equals(fromDir)) { // Don't count where the signal originated from

                        BlockPos dPos = pos.offset(dir.getNormal());

                        if (dPos.equals(last)) { // or the branch we just came back from
                            isTwig = false; // on the return journey if the block we just came from is a branch we are obviously not the endpoint(twig)
                            isTop = false;
                            continue;
                        }

                        BlockState deltaBlockState = world.getBlockState(dPos);
                        ITreePart treepart = TreeHelper.getTreePart(deltaBlockState);
                        if (branch.isSameTree(treepart)) {
                            int branchRadius = treepart.getRadius(deltaBlockState);
                            areaAccum += branchRadius * branchRadius;
                        }
                    }
                }

                if (isTop) {
                    // Handle top leaves here
                    leafMap.setVoxel(pos, (byte) 16); // 16(bit 5) is code for a twig
                    SimpleVoxmap leafCluster = DTBOPLeafClusters.POPLAR_TOP;
                    leafMap.blitMax(pos, leafCluster);
                } else if (isTwig) {
                    // Handle branch leaves here
                    leafMap.setVoxel(pos, (byte) 16); // 16(bit 5) is code for a twig
                    SimpleVoxmap leafCluster = species.getLeavesProperties().getCellKit().getLeafCluster();
                    leafMap.blitMax(pos, leafCluster);
                } else {
                    // The new branch should be the square root of all of the sums of the areas of the branches coming into it.
                    radius = (float) Math.sqrt(areaAccum) + (species.getTapering() * species.getWorldGenTaperingFactor());

                    //Ensure the branch is never inflated past it's species maximum
                    int maxRadius = species.getMaxBranchRadius();
                    if(radius > maxRadius) {
                        radius = maxRadius;
                    }

                    // Make sure that non-twig branches are at least radius 2
                    float secondaryThickness = species.getFamily().getSecondaryThickness();
                    if (radius < secondaryThickness) {
                        radius = secondaryThickness;
                    }

                    branch.setRadius(world, pos, (int) Math.floor(radius), null);
                    leafMap.setVoxel(pos, (byte) 32); // 32(bit 6) is code for a branch
                    if (Math.floor(radius) < 3) {
                        SimpleVoxmap leafCluster = species.getLeavesProperties().getCellKit().getLeafCluster();
                        leafMap.blitMax(pos, leafCluster);
                    }
                }
                last = pos;
            }
            return false;
        }

    }
}
