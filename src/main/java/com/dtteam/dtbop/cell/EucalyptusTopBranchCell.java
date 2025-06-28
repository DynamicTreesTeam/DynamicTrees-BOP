package com.dtteam.dtbop.cell;

import com.dtteam.dynamictrees.api.cell.Cell;
import net.minecraft.core.Direction;

public class EucalyptusTopBranchCell implements Cell {

    @Override
    public int getValue() {
        return 5;
    }

    static final int[] map = {0, 3, 5, 5, 5, 5};

    @Override
    public int getValueFromSide(Direction side) {
        return map[side.ordinal()];
    }

}
