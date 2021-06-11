package therealeststu.dtbop.growthlogic;

import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PoplarLogic extends GrowthLogicKit {

    private final boolean isLarge;
    public PoplarLogic(ResourceLocation registryName, boolean isLarge) {
        super(registryName);
        this.isLarge = isLarge;
    }

    @Override
    public int[] directionManipulation(World world, BlockPos pos, Species species, int radius, GrowSignal signal, int[] probMap) {
        Direction originDir = signal.dir.getOpposite();

        // Alter probability map for direction change
        probMap[0] = signal.isInTrunk() ? 0 : 1;
        probMap[1] = signal.isInTrunk() ? species.getUpProbability() : 1;
        probMap[2] = probMap[3] = probMap[4] = probMap[5] = (((signal.isInTrunk() && signal.numSteps % 2 == 0) || !signal.isInTrunk())) ? 2 : 0; // && signal.energy < species.getSignalEnergy() * 0.8
        probMap[originDir.ordinal()] = 0; // Disable the direction we came from
        probMap[signal.dir.ordinal()] += (signal.isInTrunk() ? 0 : signal.numTurns == 1 ? 2 : 1); // Favor current travel direction

        return probMap;
    }

    @Override
    public Direction newDirectionSelected(Species species, Direction newDir, GrowSignal signal) {
        if (signal.isInTrunk() && newDir != Direction.UP) { // Turned out of trunk
            if (isLarge && signal.energy >= 12f)
                signal.energy = 1.8f;
            else if (isLarge && signal.energy >= 7f)
                signal.energy = 2.8f;
            else if (signal.energy >= 4f)
                signal.energy = 1.8f; // don't grow branches more than 1 block out from the trunk
            else
                signal.energy = 0.8f; // don't grow branches, only leaves
        }
        return newDir;
    }

    private float getHashedVariation (World world, BlockPos pos){
        long day = world.getGameTime() / 24000L;
        int month = (int)day / 30;//Change the hashs every in-game month
        return (CoordUtils.coordHashCode(pos.above(month), 2) % 3);//Vary the height energy by a psuedorandom hash function
    }

    @Override
    public float getEnergy(World world, BlockPos pos, Species species, float signalEnergy) {
        return signalEnergy + getHashedVariation(world, pos); // Vary the height energy by a psuedorandom hash function
    }

    @Override
    public int getLowestBranchHeight(World world, BlockPos pos, Species species, int lowestBranchHeight) {
        return (int) (lowestBranchHeight + getHashedVariation(world, pos));
    }
}
