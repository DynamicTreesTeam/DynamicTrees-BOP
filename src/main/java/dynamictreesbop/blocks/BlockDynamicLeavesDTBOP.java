package dynamictreesbop.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;

import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockDynamicLeavesDTBOP extends BlockDynamicLeaves {

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 60;
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 30;
	}

}
