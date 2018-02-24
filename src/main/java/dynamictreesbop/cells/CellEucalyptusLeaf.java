package dynamictreesbop.cells;

import com.ferreusveritas.dynamictrees.cells.CellMatrix;

public class CellEucalyptusLeaf extends CellMatrix {

	public CellEucalyptusLeaf(int value) {
		super(value, valMap);
	}
	
	static final byte valMap[] = {
			0, 1, 2, 2, 2, 0, 0, 0, //D Maps 3 -> 2, 4 -> 2, * -> *
			0, 1, 2, 2, 2, 0, 0, 0, //U Maps 3 -> 2, 4 -> 2, * -> *
			0, 1, 1, 2, 4, 0, 0, 0, //N Maps 2 -> 1, 3 -> 2, * -> *
			0, 1, 1, 2, 4, 0, 0, 0, //S Maps 2 -> 1, 3 -> 2, * -> *
			0, 1, 1, 2, 4, 0, 0, 0, //W Maps 2 -> 1, 3 -> 2, * -> *
			0, 1, 1, 2, 4, 0, 0, 0  //E Maps 2 -> 1, 3 -> 2, * -> *
	};

}
