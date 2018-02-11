package dynamictreesbop.cells;

import com.ferreusveritas.dynamictrees.api.cells.ICell;

import net.minecraft.util.EnumFacing;

public class CellPoplarBranch implements ICell {
	
	@Override
	public int getValue() {
		return 5;
	}

	static final int map[] = {3, 4, 4, 4, 4, 4};
	
	@Override
	public int getValueFromSide(EnumFacing side) {
		return map[side.ordinal()];
	}

}
