package therealeststu.dtbop.cells.cell;

import com.ferreusveritas.dynamictrees.api.cells.ICell;
import net.minecraft.util.Direction;

public class SparseBranchCell implements ICell {

	@Override
	public int getValue() {
		return 2;
	}

	static final int[] map = {0, 2, 2, 2, 2, 2};

	@Override
	public int getValueFromSide(Direction side) {
		return map[side.ordinal()];
	}

}
