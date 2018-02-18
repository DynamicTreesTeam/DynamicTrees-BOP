package dynamictreesbop.cells;

import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;

import net.minecraft.util.math.BlockPos;

public class DTBOPLeafClusters {
	
	public static final SimpleVoxmap sparse = new SimpleVoxmap(3, 2, 3, new byte[] {
			0, 1, 0,
			1, 0, 1,
			0, 1, 0,
			
			0, 0, 0,
			0, 1, 0,
			0, 0, 0,
	}).setCenter(new BlockPos(1, 0, 1));
	
	public static final SimpleVoxmap poplar = new SimpleVoxmap(3, 4, 3, new byte[] {
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
	
	public static final SimpleVoxmap poplarTop = new SimpleVoxmap(3, 4, 3, new byte[] {
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
	
	public static final SimpleVoxmap mahogany = new SimpleVoxmap(5, 2, 5, new byte[] {
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
	
	
	public static final SimpleVoxmap bush = new SimpleVoxmap(5, 2, 5, new byte[] {
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
	
}
