package therealeststu.dtbop.growthlogic;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ITreePart;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.MathHelper;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RedwoodLogic extends GrowthLogicKit {

    public RedwoodLogic(ResourceLocation registryName) {
        super(registryName);
    }

    private int getHashedVariation (World world, BlockPos pos, int readyMade){
        long day = world.getGameTime() / 24000L;
        int month = (int)day / 30;//Change the hashs every in-game month
        return CoordUtils.coordHashCode(pos.above(month), readyMade);
    }
    private float getHashedVariation (World world, BlockPos pos, int readyMade, Integer mod){
        return (getHashedVariation(world, pos, readyMade) % mod);//Vary the height energy by a psuedorandom hash function
    }

    @Override
    public Direction selectNewDirection(World world, BlockPos pos, Species species, BranchBlock branch, GrowSignal signal) {
        Direction originDir = signal.dir.getOpposite();
        int signalY = signal.delta.getY();

        // prevent branches on the ground
        if(signal.numSteps + 1 <= species.getLowestBranchHeight(world, signal.rootPos)) {
            return Direction.UP;
        }

        int[] probMap = new int[6]; // 6 directions possible DUNSWE

        // Probability taking direction into account
        probMap[Direction.UP.ordinal()] = signal.dir != Direction.DOWN ? species.getUpProbability(): 0; // Favor up
        probMap[signal.dir.ordinal()] += species.getReinfTravel(); // Favor current direction

        int radius = branch.getRadius(world.getBlockState(pos));

        if (signal.delta.getY() < species.getLowestBranchHeight(world, pos) - 3) {

            int treeHash = getHashedVariation(world, signal.rootPos, 2);
            int posHash = getHashedVariation(world, pos, 2);

            int hashMod = signalY < 7 ? 3 : 11;
            boolean sideTurn = !signal.isInTrunk() || (signal.isInTrunk() && ((signal.numSteps + treeHash) % hashMod == 0) && (radius > 1)); // Only allow turns when we aren't in the trunk(or the branch is not a twig)

            if (!sideTurn) return Direction.UP;

            probMap[2 + (posHash % 4)] = 1;
        }

        // Create probability map for direction change
        for (Direction dir: Direction.values()) {
            if (!dir.equals(originDir)) {
                BlockPos deltaPos = pos.offset(dir.getNormal());
                // Check probability for surrounding blocks
                // Typically Air:1, Leaves:2, Branches: 2+r
                if (signalY >= species.getLowestBranchHeight(world, pos)) {
                    BlockState deltaBlockState = world.getBlockState(deltaPos);
                    ITreePart treePart = TreeHelper.getTreePart(deltaBlockState);

                    probMap[dir.ordinal()] += treePart.probabilityForBlock(deltaBlockState, world, deltaPos, branch);
                }
            }
        }

        //Do custom stuff or override probability map for various species
        probMap = directionManipulation(world, pos, species, radius, signal, probMap);

        //Select a direction from the probability map
        int choice = MathHelper.selectRandomFromDistribution(signal.rand, probMap); // Select a direction from the probability map.
        return newDirectionSelected(species, Direction.values()[choice != -1 ? choice : 1], signal); // Default to up if things are screwy
    }

    @Override
    public int[] directionManipulation(World world, BlockPos pos, Species species, int radius, GrowSignal signal, int[] probMap) {
        Direction originDir = signal.dir.getOpposite();

        // Alter probability map for direction change
        probMap[0] = 0; // Down is always disallowed
        probMap[1] = signal.isInTrunk() || signal.delta.getY() >= species.getLowestBranchHeight(world, pos) ? species.getUpProbability() : 1;
        probMap[originDir.ordinal()] = 0; // Disable the direction we came from

        if(signal.numTurns == 1 && signal.delta.distSqr(0, signal.delta.getY(), 0, true) == 1.0 ) {
            probMap[1] = 0; //disable up if we JUST turned out of the trunk, this is to prevent branches from interfering at the tip
        }

        return probMap;
    }

    @Override
    public Direction newDirectionSelected(Species species, Direction newDir, GrowSignal signal) {
        if (signal.isInTrunk() && newDir != Direction.UP) { // Turned out of trunk
            int signalY = signal.delta.getY();
            if (signalY < species.getLowestBranchHeight()) {
                signal.energy = 0.9f + ((1f - ((float) signalY / species.getLowestBranchHeight())) * 3.7f);
            } else {
                signal.energy += 5;
                signal.energy /= 4.8f;
                if (signal.energy > 5.8f) signal.energy = 5.8f;
            }
        }
        return newDir;
    }

    @Override
    public float getEnergy(World world, BlockPos pos, Species species, float signalEnergy) {
        return signalEnergy * species.biomeSuitability(world, pos) + getHashedVariation(world, pos,2, 16); // Vary the height energy by a psuedorandom hash function
    }

    @Override
    public int getLowestBranchHeight(World world, BlockPos pos, Species species, int lowestBranchHeight) {
        return (int) ((lowestBranchHeight + getHashedVariation(world, pos,2, 16) * 0.5f) * species.biomeSuitability(world, pos)) ;
    }
}
