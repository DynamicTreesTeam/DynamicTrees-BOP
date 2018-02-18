package dynamictreesbop.cells;

import com.ferreusveritas.dynamictrees.api.cells.ICell;

import net.minecraft.util.EnumFacing;

public class CellMahoganyBranch implements ICell {

	@Override
	public int getValue() {
		return 5;
	}
	
	final int map[] = {0, 2, 5, 5, 5, 5};

	@Override
	public int getValueFromSide(EnumFacing side) {
		return map[side.ordinal()];
	}

}
