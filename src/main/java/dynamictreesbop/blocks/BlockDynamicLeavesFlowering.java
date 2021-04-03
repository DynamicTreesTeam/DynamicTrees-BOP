package dynamictreesbop.blocks;

import java.util.ArrayList;

import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;

import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.common.block.BlockBOPLeaves;
import dynamictreesbop.DynamicTreesBOP;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDynamicLeavesFlowering extends BlockDynamicLeaves {

	public static final PropertyBool FAST_LEAVES = PropertyBool.create("fast_leaves");
	
	public BlockDynamicLeavesFlowering() {
		setDefaultState(this.blockState.getBaseState().withProperty(HYDRO, 4).withProperty(FAST_LEAVES, false));
		setRegistryName(DynamicTreesBOP.MODID, "leaves_flowering");
		setUnlocalizedName("leaves_flowering");
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, HYDRO, TREE, FAST_LEAVES); // TREE is unused, but it has to be there to prevent a crash when the constructor of BlockDynamicLeaves sets the default state
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(FAST_LEAVES, Blocks.LEAVES.isOpaqueCube(state));//Hacks within Hacks
	}

	@Override
	protected NewLeavesPropertiesHandler getNewLeavesPropertiesHandler(World world, BlockPos pos, IBlockState state, int newHydro, boolean worldGen) {
		
		// If this block can flower, check if flowers should grow or decay
		if (canFlower(state)) {
			boolean flowering = worldGen || world.getLight(pos) >= 14;
			if (isFlowering(state) != flowering) {
				setFlowering(world, pos, flowering, state.withProperty(HYDRO, MathHelper.clamp(newHydro, 1, 4)));
			}
		}
		
		return (w, p, l) -> {
			boolean canFlower = world.rand.nextInt(4) == 0;
			boolean flowering = canFlower && world.getLight(pos) >= 14;
			return stateFromFlower(l, canFlower, flowering);
		};
		
	}

	private IBlockState stateFromFlower(IBlockState state, boolean canFlower, boolean flowering){
		int tree = (canFlower?1:0) + (flowering?2:0);
		return state.withProperty(TREE, tree);
	}

	@Override
	public boolean growLeavesIfLocationIsSuitable(World world, ILeavesProperties leavesProp, BlockPos pos, int hydro) {
		hydro = hydro == 0 ? leavesProp.getCellKit().getDefaultHydration() : hydro;
		if (isLocationSuitableForNewLeaves(world, leavesProp, pos)) {
			boolean canFlower = world.rand.nextInt(4) == 0;
			boolean flowering = canFlower && world.getLight(pos) >= 14;
			IBlockState state = stateFromFlower(this.getDefaultState(), canFlower, flowering).withProperty(HYDRO, hydro);
			world.setBlockState(pos, state, 2 | (leavesProp.appearanceChangesWithHydro() ? 1 : 0)); // Removed Notify Neighbors Flag for performance
			return true;
		}
		return false;
	}

	/**		       |  notFlowering	| flowering
	 * 	cantFlower |		0		|    2
	 * 	canFlower  |		1		|    3
	 */

	public boolean isFlowering(IBlockState blockState) {
		int tree = blockState.getValue(TREE);
		return tree == 2 || tree == 3;
	}
	
	public boolean canFlower(IBlockState blockState) {
		int tree = blockState.getValue(TREE);
		return tree == 1 || tree == 3;
	}
	
	public static void setFlowering(World world, BlockPos pos, boolean flowering, IBlockState currentBlockState) {
		int tree = currentBlockState.getValue(TREE);
		if (flowering){
			if (tree == 0) tree = 2;
			else if (tree == 1) tree = 3;
			else return;
		} else {
			if (tree == 2) tree = 0;
			else if (tree == 3) tree = 1;
			else return;
		}
		world.setBlockState(pos, currentBlockState.withProperty(TREE, tree), 2);
	}
	
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
}
