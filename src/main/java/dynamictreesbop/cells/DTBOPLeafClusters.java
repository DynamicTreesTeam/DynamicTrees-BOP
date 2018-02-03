package dynamictreesbop.cells;

import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;

import net.minecraft.util.math.BlockPos;

public class DTBOPLeafClusters {
	
	public static final SimpleVoxmap bare = new SimpleVoxmap(1, 1, 1, new byte[] {0x20});
	
	public static final SimpleVoxmap sparse = new SimpleVoxmap(3, 2, 3, new byte[] {
			0, 1, 0,
			1, 0, 1,
			0, 1, 0,
			
			0, 0, 0,
			0, 1, 0,
			0, 0, 0,
	}).setCenter(new BlockPos(1, 0, 1));

}
