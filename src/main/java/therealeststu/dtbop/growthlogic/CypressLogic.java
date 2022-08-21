package therealeststu.dtbop.growthlogic;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.PositionalSpeciesContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CypressLogic extends GrowthLogicKit {

    public CypressLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration,
                                                 DirectionManipulationContext context) {
        final GrowSignal signal = context.signal();
        final int[] probMap = context.probMap();

        if (signal.isInTrunk()) {
            int sideProb = 2;
            Direction branchSide = null;
            if (signal.energy >= 6) {
                if (signal.numSteps % 3 == 0) {
                    for (Direction dir : CoordUtils.HORIZONTALS) {
                        if (TreeHelper.isBranch(context.world().getBlockState(context.pos().offset(dir.getNormal())))) {
                            sideProb = 0;
                            branchSide = dir;
                        }
                    }
                } else {
                    sideProb = 0;
                }
            }
            probMap[2] = probMap[3] = probMap[4] = probMap[5] = sideProb;
            if (branchSide != null) {
                probMap[branchSide.ordinal()] = 2;
            }
        }

        probMap[0] = 0; //disable down
        probMap[signal.dir.getOpposite().ordinal()] = 0; // Disable the direction we came from
        return probMap;
    }

    @Override
    public float getEnergy(GrowthLogicKitConfiguration configuration, PositionalSpeciesContext context) {
        final World world = context.world();
        final BlockPos rootPos = context.pos();
        final Species species = context.species();
        return species.getSignalEnergy() +
                getLowestBranchHeight(configuration, context) * (1.5f +
                        (getHashedVariation(world, rootPos, 10) /
                                20)); // Vary the energy between 1.5 and 2.0 times the minimum branch height
    }

    @Override
    public int getLowestBranchHeight(GrowthLogicKitConfiguration configuration, PositionalSpeciesContext context) {
        return (int) (super.getLowestBranchHeight(configuration, context) +
                getHashedVariation(context.world(), context.pos(),
                        9)); // Vary the minimum branch height by a psuedorandom hash function
    }

    private float getHashedVariation(World world, BlockPos pos, int mod) {
        long day = world.getGameTime() / 24000L;
        int month = (int) day / 30;//Change the hashs every in-game month
        return (CoordUtils.coordHashCode(pos.above(month), 2) %
                mod);//Vary the height energy by a psuedorandom hash function
    }
}
