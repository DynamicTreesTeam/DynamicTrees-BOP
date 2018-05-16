package dynamictreesbop.blocks;

import java.util.Random;

import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.util.CoordUtils.Surround;

import dynamictreesbop.DynamicTreesBOP;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

public class BlockDynamicLeavesPalm extends BlockDynamicLeaves {
	
	public static final Surround hydroSurroundMap[][]  = new Surround[][] {
		{}, //Hydro 0
		{Surround.NE, Surround.SE, Surround.SW, Surround.NW}, //Hydro 1
		{Surround.N, Surround.E, Surround.S, Surround.W}, //Hydro 2
		{}, //Hydro 3
		{} //Hydro 4
	};
	
	public static final IUnlistedProperty<Boolean> CONNECTIONS[];
	
	static {		
		CONNECTIONS = new Properties.PropertyAdapter[Surround.values().length];
		
		for (Surround surr : Surround.values()) {
			CONNECTIONS[surr.ordinal()] = new Properties.PropertyAdapter<Boolean>(PropertyBool.create("conn_" + surr.getName()));
			//System.out.println(CONNECTIONS[surr.ordinal()]);
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
	public int age(World world, BlockPos pos, IBlockState state, Random rand, boolean rapid) {
		ILeavesProperties leavesProperties = getProperties(state);
		int oldHydro = state.getValue(BlockDynamicLeaves.HYDRO);
		
		//Check hydration level.  Dry leaves are dead leaves.
		int newHydro = getHydrationLevelFromNeighbors(world, pos, leavesProperties);
		if(newHydro == 0 || (!rapid && !hasAdequateLight(state, world, leavesProperties, pos))) { //Light doesn't work right during worldgen so we'll just disable it during worldgen for now.
			world.setBlockToAir(pos);//No water, no light .. no leaves
			return -1;//Leaves were destroyed
		} else { 
			if(oldHydro != newHydro) {//Only update if the hydro has changed. A little performance gain
				//We do not use the 0x02 flag(update client) for performance reasons.  The clients do not need to know the hydration level of the leaves blocks as it
				//does not affect appearance or behavior.  For the same reason we use the 0x04 flag to prevent the block from being re-rendered.
				world.setBlockState(pos, leavesProperties.getDynamicLeavesState(newHydro), leavesProperties.appearanceChangesWithHydro() ? 2 : 4);
			}
		}
		
		//We should do this even if the hydro is only 1.  Since there could be adjacent branch blocks that could use a leaves block
		for(EnumFacing dir: EnumFacing.VALUES) {//Go on all 6 sides of this block
			if(newHydro > 1 || rand.nextInt(4) == 0 ) {//we'll give it a 1 in 4 chance to grow leaves if hydro is low to help performance
				BlockPos offpos = pos.offset(dir);
				if(isLocationSuitableForNewLeaves(world, leavesProperties, offpos)) {//Attempt to grow new leaves
					int hydro = getHydrationLevelFromNeighbors(world, offpos, leavesProperties);
					if(hydro > 0) {
						world.setBlockState(offpos, leavesProperties.getDynamicLeavesState(hydro), 2);//Removed Notify Neighbors Flag for performance
					}
				}
			}
		}
		
		return newHydro;//Leaves were not destroyed
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess access, BlockPos pos) {
		
		if (state instanceof IExtendedBlockState) {
			IExtendedBlockState extState = (IExtendedBlockState) state;
			for(Surround surr : hydroSurroundMap[state.getValue(BlockDynamicLeaves.HYDRO)]) {
				IBlockState scanState = access.getBlockState(pos.add(surr.getOffset()));
				if(scanState.getBlock() == this) {
					if( scanState.getValue(BlockDynamicLeaves.HYDRO) == 3 ) {
						extState = extState.withProperty(CONNECTIONS[surr.ordinal()], true);
					}
				}
			}
			
			return extState;
		}
		
		return state;
	}
	
	@Override
	public int getRadiusForConnection(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, BlockBranch from, EnumFacing side, int fromRadius) {
		return side == EnumFacing.UP && from.getFamily().isCompatibleDynamicLeaves(blockState, blockAccess, pos) ? fromRadius : 0;
	}
	
	@Override
	public int branchSupport(IBlockState blockState, IBlockAccess blockAccess, BlockBranch branch, BlockPos pos, EnumFacing dir, int radius) {
		return branch.getFamily() == getFamily(blockState, blockAccess, pos) ? BlockBranch.setSupport(0, 1) : 0;
	}
	
}
