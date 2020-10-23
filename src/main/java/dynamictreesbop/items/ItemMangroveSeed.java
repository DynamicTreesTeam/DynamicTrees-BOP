package dynamictreesbop.items;

import com.ferreusveritas.dynamictrees.items.Seed;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemMangroveSeed extends Seed {
	
	public ItemMangroveSeed(String name) {
		super(name);
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);
		
		if (raytraceresult == null) {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
		}
		else {
			if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
				BlockPos blockpos = raytraceresult.getBlockPos();
				
				if (!worldIn.isBlockModifiable(playerIn, blockpos) || !playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemstack)) {
					return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
				}
				
				BlockPos abovePos = blockpos.up();
				IBlockState iblockstate = worldIn.getBlockState(blockpos);
				
				if (iblockstate.getMaterial() == Material.WATER && ((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0 && worldIn.isAirBlock(abovePos)) {
					if(doPlanting(worldIn, abovePos, playerIn, itemstack)) {
						if (!playerIn.capabilities.isCreativeMode) {
							itemstack.shrink(1);
						}
						return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
					}
				}
			}
			
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
		}
	}
	
}
