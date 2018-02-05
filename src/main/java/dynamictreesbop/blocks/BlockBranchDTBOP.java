package dynamictreesbop.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockBranchDTBOP extends BlockBranch {
	
	public BlockBranchDTBOP(String name) {
		super(name);
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 5;
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
		int radius = getRadius(world, pos);
		return (5 * radius) / 8;
	}

}
