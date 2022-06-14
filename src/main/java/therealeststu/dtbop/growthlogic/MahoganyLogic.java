package therealeststu.dtbop.growthlogic;

import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionSelectionContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class MahoganyLogic extends GrowthLogicKit {

    public MahoganyLogic(ResourceLocation registryName) { super(registryName); }

    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration,
                                                 DirectionManipulationContext context) {
        final GrowSignal signal = context.signal();
        final int[] probMap = context.probMap();
        final Direction originDir = signal.dir.getOpposite();

        probMap[0] = 0; // Down is always disallowed for mahogany

        if (signal.energy < 3.5 && signal.energy > 1) {
            probMap[1] = 1;
            probMap[2] = probMap[3] = probMap[4] = probMap[5] = 0;
        } else {
            float r = Math.max(Math.abs(signal.delta.getX()), Math.abs(signal.delta.getZ()));

            probMap[2] = probMap[3] = probMap[4] = probMap[5] = 3;
            probMap[1] = 1 + (int) (r * 2.5);

            if (signal.delta.getZ() > 0) probMap[2] = 0;
            if (signal.delta.getZ() < 0) probMap[3] = 0;
            if (signal.delta.getX() > 0) probMap[4] = 0;
            if (signal.delta.getX() < 0) probMap[5] = 0;

            probMap[originDir.ordinal()] = 0; // Disable the direction we came from
            probMap[signal.dir.ordinal()] += signal.dir == Direction.UP ? (int) ((r - 1.75) * 1.5f) : 0;
        }

        return probMap;
    }

    @Override
    public Direction selectNewDirection(GrowthLogicKitConfiguration configuration, DirectionSelectionContext context) {
        final Direction direction = super.selectNewDirection(configuration, context);
        final GrowSignal signal = context.signal();
        if (direction != Direction.UP) {
            signal.energy += 0.75f;
        }
        if (direction == Direction.UP && signal.dir != Direction.UP) {
            signal.energy += (Math.max(Math.abs(signal.delta.getX()), Math.abs(signal.delta.getZ())) - 2f) * 1.5f;
        }
        return direction;
    }

}
