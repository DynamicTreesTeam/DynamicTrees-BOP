package dynamictreesbop.blocks;

import java.util.ArrayList;

import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;

import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.common.block.BlockBOPLeaves;
import dynamictreesbop.DynamicTreesBOP;
import net.minecraft.block.properties.IProperty;
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
	
	public static final PropertyBool FLOWERING = PropertyBool.create("flowering");
	public static final PropertyBool CAN_FLOWER = PropertyBool.create("can_flower");
	public static final PropertyBool FAST_LEAVES = PropertyBool.create("fast_leaves");
	
	private ILeavesProperties properties = LeavesProperties.NULLPROPERTIES;
	
	public BlockDynamicLeavesFlowering() {
		setDefaultState(this.blockState.getBaseState().withProperty(HYDRO, 4).withProperty(FLOWERING, false).withProperty(CAN_FLOWER, false).withProperty(FAST_LEAVES, false));
		setRegistryName(DynamicTreesBOP.MODID, "leaves_flowering");
		setUnlocalizedName("leaves_flowering");
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {HYDRO, FLOWERING, CAN_FLOWER, TREE, FAST_LEAVES }); // TREE is unused, but it has to be there to prevent a crash when the constructor of BlockDynamicLeaves sets the default state
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FLOWERING, ((meta >> 2) & 1) > 0).withProperty(CAN_FLOWER, ((meta >> 3) & 1) > 0).withProperty(HYDRO, (meta & 3) + 1);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(HYDRO) - 1) | (state.getValue(FLOWERING) ? 4 : 0) | (state.getValue(CAN_FLOWER) ? 8 : 0);
	}
	
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(FAST_LEAVES, Blocks.LEAVES.isOpaqueCube(state));//Hacks within Hacks
	}
	
	public void setProperties(int tree, ILeavesProperties properties) {
		this.properties = properties;
		for (int i = 0; i < 4; i++) super.setProperties(i, properties);
	}
	
	@Override
	public ILeavesProperties getProperties(IBlockState blockState) {
		return properties;
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
			return l.withProperty(CAN_FLOWER, canFlower).withProperty(FLOWERING, canFlower && world.getLight(pos) >= 14);
		};
		
	}
	
	@Override
	public boolean growLeavesIfLocationIsSuitable(World world, ILeavesProperties leavesProp, BlockPos pos, int hydro) {
		hydro = hydro == 0 ? leavesProp.getCellKit().getDefaultHydration() : hydro;
		if (isLocationSuitableForNewLeaves(world, leavesProp, pos)) {
			boolean canFlower = world.rand.nextInt(4) == 0;
			IBlockState state = this.getDefaultState().withProperty(CAN_FLOWER, canFlower).withProperty(FLOWERING, canFlower && world.getLight(pos) >= 14).withProperty(HYDRO, hydro);
			world.setBlockState(pos, state, 2 | (leavesProp.appearanceChangesWithHydro() ? 1 : 0)); // Removed Notify Neighbors Flag for performance
			return true;
		}
		return false;
	}
	
	public boolean isFlowering(IBlockState blockState) {
		return blockState.getValue(FLOWERING);
	}
	
	public boolean canFlower(IBlockState blockState) {
		return blockState.getValue(CAN_FLOWER);
	}
	
	public static void setFlowering(World world, BlockPos pos, boolean flowering, IBlockState currentBlockState) {
		world.setBlockState(pos, currentBlockState.withProperty(FLOWERING, flowering), 2);
	}
	
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess blockAccess, BlockPos pos, int fortune) {
		IBlockState state = blockAccess.getBlockState(pos);
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		
		if (state.getBlock() == this) {
			ret.add(isFlowering(state)
					? BlockBOPLeaves.paging.getVariantItem(BOPTrees.FLOWERING)
					: properties.getPrimitiveLeavesItemStack()
			);
		}
		
		return ret;
	}
	
}
