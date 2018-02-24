package dynamictreesbop.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.blocks.MimicProperty;
import com.ferreusveritas.dynamictrees.tileentity.TileEntitySpecies;

import dynamictreesbop.blocks.properties.UnlistedPropertyBool;
import dynamictreesbop.blocks.properties.UnlistedPropertyFloat;
import dynamictreesbop.blocks.properties.UnlistedPropertyInt;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRootyWater extends BlockRooty {

	public static final UnlistedPropertyBool[] RENDER_SIDES = new UnlistedPropertyBool[] {
			new UnlistedPropertyBool("render_d"),
			new UnlistedPropertyBool("render_u"),
			new UnlistedPropertyBool("render_n"),
			new UnlistedPropertyBool("render_s"),
			new UnlistedPropertyBool("render_w"),
			new UnlistedPropertyBool("render_e"),
	};
	public static final UnlistedPropertyFloat[] CORNER_HEIGHTS = new UnlistedPropertyFloat[] {
			new UnlistedPropertyFloat("level_nw"),
			new UnlistedPropertyFloat("level_sw"),
			new UnlistedPropertyFloat("level_se"),
			new UnlistedPropertyFloat("level_ne"),
	};
	public static final UnlistedPropertyInt COLOR = new UnlistedPropertyInt("color");
	
	public BlockRootyWater(boolean isTileEntity) {
		super("rootywater", Material.WATER, isTileEntity);
		setSoundType(SoundType.PLANT);
		setDefaultState(super.getDefaultState().withProperty(BlockLiquid.LEVEL, 15));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[] {LIFE, BlockLiquid.LEVEL}, new IUnlistedProperty[] {
				MimicProperty.MIMIC,
				RENDER_SIDES[0],
				RENDER_SIDES[1],
				RENDER_SIDES[2],
				RENDER_SIDES[3],
				RENDER_SIDES[4],
				RENDER_SIDES[5],
				CORNER_HEIGHTS[0],
				CORNER_HEIGHTS[1],
				CORNER_HEIGHTS[2],
				CORNER_HEIGHTS[3],
				COLOR,
		});
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess access, BlockPos pos) {
		int i = 0;
		float avg = 0;
		avg += BlockLiquid.getLiquidHeightPercent(7);
		i++;
		float h = getFluidHeight(access, pos.west(), Material.WATER);
		if (h != 0) i++;
		avg += h;
		
		if (state instanceof IExtendedBlockState) {
			IExtendedBlockState extState = (IExtendedBlockState) state;
			for (EnumFacing dir : EnumFacing.VALUES) {
				extState = extState.withProperty(RENDER_SIDES[dir.ordinal()], Blocks.WATER.shouldSideBeRendered(state, access, pos, dir));
			}
			//System.out.println(avg + " " + i);
			extState = extState.withProperty(CORNER_HEIGHTS[0], avg / i);
			//extState = extState.withProperty(CORNER_HEIGHTS[0], getFluidHeight(access, pos.west().north(), Material.WATER));
			extState = extState.withProperty(CORNER_HEIGHTS[1], getFluidHeight(access, pos.south(), Material.WATER));
			extState = extState.withProperty(CORNER_HEIGHTS[2], getFluidHeight(access, pos.east().south(), Material.WATER));
			extState = extState.withProperty(CORNER_HEIGHTS[3], getFluidHeight(access, pos.east(), Material.WATER));
			
			extState = extState.withProperty(COLOR, Minecraft.getMinecraft().getBlockColors().colorMultiplier(Blocks.WATER.getDefaultState(), access, pos, 0));
			
			return extState;
		}
		//return state.withProperty(BlockLiquid.LEVEL, i > 0 ? avg / i : 7);
		return state;
	}
	
	private float getFluidHeight(IBlockAccess blockAccess, BlockPos blockPosIn, Material blockMaterial) {
        int i = 0;
        float f = 0.0F;

        for (int j = 0; j < 4; ++j) {
            BlockPos blockpos = blockPosIn.add(-(j & 1), 0, -(j >> 1 & 1));

            if (blockAccess.getBlockState(blockpos.up()).getMaterial() == blockMaterial) {
                return 1.0F;
            }

            IBlockState iblockstate = blockAccess.getBlockState(blockpos);
            Material material = iblockstate.getMaterial();

            if (material != blockMaterial) {
                if (!material.isSolid()) {
                    ++f;
                    ++i;
                }
            } else {
                int k = iblockstate.getValue(BlockLiquid.LEVEL);

                if (k >= 8 || k == 0) {
                    f += BlockLiquid.getLiquidHeightPercent(k) * 10.0F;
                    i += 10;
                }

                f += BlockLiquid.getLiquidHeightPercent(k);
                ++i;
            }
        }

        return 1.0F - f / (float) i;
    }
	
	@Override
	public IBlockState getMimic(IBlockAccess access, BlockPos pos) {
		return  Blocks.WATER.getDefaultState();
	}
	
	@Override
	public IBlockState getDecayBlockState(IBlockAccess access, BlockPos pos) {
		return Blocks.WATER.getDefaultState();
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.AIR;
	}
	
	@Override
	public int quantityDropped(Random random) {
        return 0;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (blockAccess.getBlockState(pos.offset(side)).getMaterial() == this.blockMaterial) {
            return false;
        } else {
            return side == EnumFacing.UP ? true : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return hasTileEntity ? new TileEntitySpecies() : null;
	}
	
	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }
	
	@Override
	public boolean isFullCube(IBlockState state) {
        return false;
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
	
	private static final Vec3d acceleration_modifier = new Vec3d(0, 0, 0);
	@Override
	public Vec3d modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3d motion) {
        return motion.add(acceleration_modifier);
    }
	
	@Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FULL_BLOCK_AABB;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos) {
        int i = source.getCombinedLight(pos, 0);
        int j = source.getCombinedLight(pos.up(), 0);
        int k = i & 255;
        int l = j & 255;
        int i1 = i >> 16 & 255;
        int j1 = j >> 16 & 255;
        return (k > l ? k : l) | (i1 > j1 ? i1 : j1) << 16;
    }
	
	@Override
    @SideOnly (Side.CLIENT)
    public Vec3d getFogColor(World world, BlockPos pos, IBlockState state, Entity entity, Vec3d originalColor, float partialTicks) {
        return super.getFogColor(world, pos, Blocks.WATER.getDefaultState(), entity, originalColor, partialTicks);
    }
	
}
