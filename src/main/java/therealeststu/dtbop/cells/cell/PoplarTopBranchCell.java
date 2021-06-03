package therealeststu.dtbop.cells.cell;

import com.ferreusveritas.dynamictrees.api.cells.ICell;
import net.minecraft.util.Direction;

public class PoplarTopBranchCell implements ICell {

	@Override
	public int getValue() {
		return 5;
	}

	static final int[] map = {3, 4, 3, 3, 3, 3};
	
	@Override
	public int getValueFromSide(Direction side) {
		return map[side.ordinal()];
	}

}
