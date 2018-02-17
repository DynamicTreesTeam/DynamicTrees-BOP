package dynamictreesbop.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;

import dynamictreesbop.DynamicTreesBOP;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

public class BlockDynamicLeavesPalm extends BlockDynamicLeaves {

	public enum Surround {
		N ("n" , new Vec3i( 0, 0,-1)),
		NE("ne", new Vec3i( 1, 0,-1)),
		E ("e" , new Vec3i( 1, 0, 0)),
		SE("se", new Vec3i( 1, 0, 1)),
		S ("s" , new Vec3i( 0, 0, 1)),
		SW("sw", new Vec3i(-1, 0, 1)),
		W ("w" , new Vec3i(-1, 0, 0)),
		NW("nw", new Vec3i(-1, 0,-1));
		
		final private String name;
		final private Vec3i offset;
		final int checkMask;
		
		private Surround(String name, Vec3i offset) {
			this.name = name;
			this.offset = offset;
			this.checkMask = 0b1000 | ((offset.getX() != 0) && (offset.getZ() != 0) ? 0b0010 : 0b0100);
		}
		
		public String getName() {
			return name;
		}
		
		public Vec3i getOffset() {
			return offset;
		}
		
		public boolean shouldCheck(int hydro) {
			return (checkMask & (1 << hydro)) != 0;
		}
	}
	
	public static final IUnlistedProperty CONNECTIONS[];
	
	static {
		CONNECTIONS = new Properties.PropertyAdapter[Surround.values().length];
		
		for(Surround surr : Surround.values()) {
			CONNECTIONS[surr.ordinal()] = new Properties.PropertyAdapter<Boolean>(PropertyBool.create("conn_" + surr.getName()));
		}
	}
	
	public BlockDynamicLeavesPalm() {
		setDefaultState(this.blockState.getBaseState().withProperty(HYDRO, 4));
		setRegistryName(DynamicTreesBOP.MODID, "leaves_palm");
		setUnlocalizedName("leaves_palm");
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[] {HYDRO, TREE}, CONNECTIONS);
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess access, BlockPos pos) {
		if (state instanceof IExtendedBlockState) {
			IExtendedBlockState retval = (IExtendedBlockState) state;
			int hydro = state.getValue(BlockDynamicLeaves.HYDRO);
			
			for( Surround surr : Surround.values()) {
				if(surr.shouldCheck(hydro)) {
					IBlockState readState = access.getBlockState(pos.add(surr.getOffset()));
					if(readState.getBlock() == this) {
						int readHydro = readState.getValue(BlockDynamicLeaves.HYDRO);
						if( (0b00000110_00001000_00001000_00000000 & (1 << ((hydro << 3) | readHydro))) != 0) {//Binary connection map lookup table
							retval = retval.withProperty(CONNECTIONS[surr.ordinal()], true);
						}
					}
				}
			}
			
			return retval;
		}
		
		return state;
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
