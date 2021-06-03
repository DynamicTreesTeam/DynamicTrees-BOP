package therealeststu.dtbop.cells.cell;

import com.ferreusveritas.dynamictrees.cells.MatrixCell;

public class PoplarLeafCell extends MatrixCell {
	
	public PoplarLeafCell(int value) {
		super(value, valMap);
	}
	
	static final byte[] valMap = {
			0, 0, 0, 0, 0, 0, 0, 0, //D Maps * -> 0
			0, 1, 2, 3, 4, 0, 0, 0, //U Maps 0 -> 0, * -> *
			0, 1, 1, 1, 1, 0, 0, 0, //N Maps 0 -> 0, * -> 1
			0, 1, 1, 1, 1, 0, 0, 0, //S Maps 0 -> 0, * -> 1
			0, 1, 1, 1, 1, 0, 0, 0, //W Maps 0 -> 0, * -> 1
			0, 1, 1, 1, 1, 0, 0, 0  //E Maps 0 -> 0, * -> 1
	};
	
}
