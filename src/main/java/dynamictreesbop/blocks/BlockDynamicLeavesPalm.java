package dynamictreesbop.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;

import dynamictreesbop.DynamicTreesBOP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockDynamicLeavesPalm extends BlockDynamicLeaves {

	public BlockDynamicLeavesPalm() {
		setDefaultState(this.blockState.getBaseState().withProperty(HYDRO, 4));
		setRegistryName(DynamicTreesBOP.MODID, "leaves_palm");
		setUnlocalizedName("leaves_palm");
	}
	
	@Override
	public int getRadiusForConnection(IBlockAccess blockAccess, BlockPos pos, BlockBranch from, EnumFacing side, int fromRadius) {
		return side == EnumFacing.UP && from.getTree().isCompatibleDynamicLeaves(blockAccess, pos) ? fromRadius : 0;
	}
	
	@Override
	public int branchSupport(IBlockAccess blockAccess, BlockBranch branch, BlockPos pos, EnumFacing dir, int radius) {
		return branch.getTree() == getTree(blockAccess, pos) ? BlockBranch.setSupport(0, 1) : 0;
	}
}
