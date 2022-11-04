package therealeststu.dtbop.cell;

import com.ferreusveritas.dynamictrees.api.cell.Cell;
import net.minecraft.core.Direction;

public class PoplarBranchCell implements Cell {

    @Override
    public int getValue() {
        return 5;
    }

    static final int[] map = {3, 4, 4, 4, 4, 4};

    @Override
    public int getValueFromSide(Direction side) {
        return map[side.ordinal()];
    }

}
