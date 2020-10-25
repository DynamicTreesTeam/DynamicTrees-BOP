package dynamictreesbop.items;

import javax.annotation.Nullable;

import com.ferreusveritas.dynamictrees.items.Seed;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMapleSeed extends Seed {
	
	public ItemMapleSeed(String name) {
		super(name);
	}
	
	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}
	
	@Override
	@Nullable
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		EntityItemMapleSeed mapleSeedEntity = new EntityItemMapleSeed(world, location.posX, location.posY, location.posZ, itemstack);
		
		//We need to also copy the motion of the replaced entity or it acts funny when the item spawns.
		mapleSeedEntity.motionX = location.motionX;
		mapleSeedEntity.motionY = location.motionY;
		mapleSeedEntity.motionZ = location.motionZ;
		
		return mapleSeedEntity;
	}

	public static class EntityItemMapleSeed extends EntityItem {
		
		public EntityItemMapleSeed(World worldIn) {
			super(worldIn);
		}
		
		public EntityItemMapleSeed(World worldIn, double x, double y, double z, ItemStack stack) {
			super(worldIn, x, y, z, stack);
			this.setDefaultPickupDelay();
		}

		@Override
		public void onUpdate() {

			if (motionY<=0){
				//Counteract the air friction that will be applied in super.onUpdate() this results in a 0.99 factor instead of 0.98
				motionX *= 1.01f;
				motionZ *= 1.01f;

				//Add lift to counteract the gravity that will be applied in super.onUpdate()
				motionY += 0.03;
			}


			super.onUpdate();
		}
		
		@Override
		public void setDefaultPickupDelay() {
			this.setPickupDelay(50);
		}

	}

}
