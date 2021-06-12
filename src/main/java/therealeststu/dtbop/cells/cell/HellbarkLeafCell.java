package therealeststu.dtbop.cells.cell;

import com.ferreusveritas.dynamictrees.cells.MatrixCell;

public class HellbarkLeafCell extends MatrixCell {

    public HellbarkLeafCell(int value) {
        super(value, valMap);
    }

    static final byte[] valMap = {
            0, 0, 0, 0, 0, 0, 0, 0, //D Maps * -> 0
            0, 0, 0, 0, 0, 0, 0, 7, //U Maps 7 -> 7, * -> 0
            0, 1, 2, 3, 4, 0, 6, 0, //N Maps 5 -> 0, 6 -> 6, 0 -> 0, * -> *
            0, 1, 2, 3, 4, 0, 6, 0, //S Maps 5 -> 0, 6 -> 6, 0 -> 0, * -> *
            0, 1, 2, 3, 4, 0, 6, 0, //W Maps 5 -> 0, 6 -> 6, 0 -> 0, * -> *
            0, 1, 2, 3, 4, 0, 6, 0  //E Maps 5 -> 0, 6 -> 6, 0 -> 0, * -> *
    };

}
