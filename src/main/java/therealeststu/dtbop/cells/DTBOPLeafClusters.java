package therealeststu.dtbop.cells;

import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import net.minecraft.util.math.BlockPos;

public class DTBOPLeafClusters {

    public static final SimpleVoxmap SPARSE = new SimpleVoxmap(3, 2, 3, new byte[] {
            0, 1, 0,
            1, 0, 1,
            0, 1, 0,

            0, 0, 0,
            0, 1, 0,
            0, 0, 0,
    }).setCenter(new BlockPos(1, 0, 1));

    public static final SimpleVoxmap POPLAR = new SimpleVoxmap(3, 4, 3, new byte[] {
            0, 0, 0,
            0, 1, 0,
            0, 0, 0,

            0, 2, 0,
            2, 0, 2,
            0, 2, 0,

            0, 1, 0,
            1, 2, 1,
            0, 1, 0,

            0, 0, 0,
            0, 1, 0,
            0, 0, 0,
    }).setCenter(new BlockPos(1, 1, 1));

    public static final SimpleVoxmap POPLAR_TOP = new SimpleVoxmap(3, 4, 3, new byte[] {
            0, 1, 0,
            1, 0, 1,
            0, 1, 0,

            0, 0, 0,
            0, 2, 0,
            0, 0, 0,

            0, 0, 0,
            0, 1, 0,
            0, 0, 0,
    }).setCenter(new BlockPos(1, 0, 1));

    public static final SimpleVoxmap MAHOGANY = new SimpleVoxmap(5, 2, 5, new byte[] {
            0, 1, 1, 1, 0,
            1, 2, 3, 2, 1,
            1, 3, 0, 3, 1,
            1, 2, 3, 2, 1,
            0, 1, 1, 1, 0,

            0, 0, 0, 0, 0,
            0, 1, 1, 1, 0,
            0, 1, 1, 1, 0,
            0, 1, 1, 1, 0,
            0, 0, 0, 0, 0,
    }).setCenter(new BlockPos(2, 0, 2));

    public static final SimpleVoxmap BRUSH = new SimpleVoxmap(5, 3, 5, new byte[] {
            0, 0, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 1, 1, 1, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 0, 0,

            0, 1, 1, 1, 0,
            1, 2, 3, 2, 1,
            1, 3, 0, 3, 1,
            1, 2, 3, 2, 1,
            0, 1, 1, 1, 0,

            0, 0, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 1, 1, 1, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 0, 0,
    }).setCenter(new BlockPos(2, 1, 2));

    public static final SimpleVoxmap EUCALYPTUS_TOP = new SimpleVoxmap(5, 4, 5, new byte[] {
            0, 0, 0, 0, 0,
            0, 1, 1, 1, 0,
            0, 1, 0, 1, 0,
            0, 1, 1, 1, 0,
            0, 0, 0, 0, 0,

            0, 1, 1, 1, 0,
            1, 3, 4, 3, 1,
            1, 4, 0, 4, 1,
            1, 3, 4, 3, 1,
            0, 1, 1, 1, 0,

            0, 0, 0, 0, 0,
            0, 1, 1, 1, 0,
            0, 1, 2, 1, 0,
            0, 1, 1, 1, 0,
            0, 0, 0, 0, 0,

            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
    }).setCenter(new BlockPos(2, 1, 2));

    public static final SimpleVoxmap EUCALYPTUS = new SimpleVoxmap(3, 3, 3, new byte[] {
            0, 0, 0,
            0, 1, 0,
            0, 0, 0,

            0, 1, 0,
            1, 0, 1,
            0, 1, 0,

            0, 0, 0,
            0, 1, 0,
            0, 0, 0,
    }).setCenter(new BlockPos(1, 1, 1));

    public static final SimpleVoxmap EUCALYPTUS_TRUNK = new SimpleVoxmap(3, 2, 3, new byte[] {
            0, 1, 0,
            1, 0, 1,
            0, 1, 0,

            0, 2, 0,
            2, 0, 2,
            0, 2, 0,
    }).setCenter(new BlockPos(1, 1, 1));

    public static final SimpleVoxmap HELLBARK = new SimpleVoxmap(5, 2, 5, new byte[] {

            //Layer 0(Bottom)
            0, 2, 1, 2, 0,
            2, 3, 4, 3, 2,
            1, 4, 0, 4, 1,
            2, 3, 4, 3, 2,
            0, 2, 1, 2, 0,

            //Layer 1 (Top)
            0, 0, 0, 0, 0,
            0, 0, 5, 0, 0,
            0, 5, 6, 5, 0,
            0, 0, 5, 0, 0,
            0, 0, 0, 0, 0,

    }).setCenter(new BlockPos(2, 0, 2));

}
