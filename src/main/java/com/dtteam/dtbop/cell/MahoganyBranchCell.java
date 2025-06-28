package com.dtteam.dtbop.cell;

import com.dtteam.dynamictrees.api.cell.Cell;
import net.minecraft.core.Direction;

public class MahoganyBranchCell implements Cell {

    @Override
    public int getValue() {
        return 5;
    }

    final int[] map = {0, 2, 5, 5, 5, 5};

    @Override
    public int getValueFromSide(Direction side) {
        return map[side.ordinal()];
    }

}
