package dynamictreesbop.cells;

import com.ferreusveritas.dynamictrees.api.cells.ICell;

import net.minecraft.util.EnumFacing;

public class CellPoplarTopBranch implements ICell {

	@Override
	public int getValue() {
		return 5;
	}

	static final int map[] = {3, 4, 3, 3, 3, 3};
	
	@Override
	public int getValueFromSide(EnumFacing side) {
		return map[side.ordinal()];
	}

}
