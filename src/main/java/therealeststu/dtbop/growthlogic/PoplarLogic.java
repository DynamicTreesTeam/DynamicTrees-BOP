package therealeststu.dtbop.growthlogic;

import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionSelectionContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.PositionalSpeciesContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PoplarLogic extends GrowthLogicKit {

    public static final ConfigurationProperty<Boolean> LARGE = ConfigurationProperty.bool("large");

    public PoplarLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected GrowthLogicKitConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(LARGE, false);
    }

    @Override
    protected void registerProperties() {
        this.register(LARGE);
    }

    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration,
                                                 DirectionManipulationContext context) {
        final GrowSignal signal = context.signal();
        final int[] probMap = context.probMap();
        final Direction originDir = signal.dir.getOpposite();

        // Alter probability map for direction change
        probMap[0] = signal.isInTrunk() ? 0 : 1;
        probMap[1] = signal.isInTrunk() ? context.species().getUpProbability() : 1;
        probMap[2] = probMap[3] = probMap[4] = probMap[5] =
                (((signal.isInTrunk() && signal.numSteps % 2 == 0) || !signal.isInTrunk())) ? 2 :
                        0; // && signal.energy < species.getSignalEnergy() * 0.8
        probMap[originDir.ordinal()] = 0; // Disable the direction we came from
        probMap[signal.dir.ordinal()] +=
                (signal.isInTrunk() ? 0 : signal.numTurns == 1 ? 2 : 1); // Favor current travel direction

        return probMap;
    }

    @Override
    public Direction selectNewDirection(GrowthLogicKitConfiguration configuration, DirectionSelectionContext context) {
        final Direction direction = super.selectNewDirection(configuration, context);
        final GrowSignal signal = context.signal();
        final boolean large = configuration.get(LARGE);
        if (signal.isInTrunk() && direction != Direction.UP) { // Turned out of trunk
            if (large && signal.energy >= 12f) {
                signal.energy = 1.8f;
            } else if (large && signal.energy >= 7f) {
                signal.energy = 2.8f;
            } else if (signal.energy >= 4f) {
                signal.energy = 1.8f; // don't grow branches more than 1 block out from the trunk
            } else {
                signal.energy = 0.8f; // don't grow branches, only leaves
            }
        }
        return direction;
    }

    @Override
    public float getEnergy(GrowthLogicKitConfiguration configuration, PositionalSpeciesContext context) {
        // Vary the height energy by a psuedorandom hash function
        return context.species().getSignalEnergy() +
                getHashedVariation(context.world(), context.pos(), 3);
    }

    @Override
    public int getLowestBranchHeight(GrowthLogicKitConfiguration configuration, PositionalSpeciesContext context) {
        return (int) (super.getLowestBranchHeight(configuration, context) +
                getHashedVariation(context.world(), context.pos(), 3));
    }

    private float getHashedVariation(World world, BlockPos pos, int mod) {
        long day = world.getGameTime() / 24000L;
        int month = (int) day / 30;//Change the hashs every in-game month
        return (CoordUtils.coordHashCode(pos.above(month), 2) %
                mod);//Vary the height energy by a psuedorandom hash function
    }
}
