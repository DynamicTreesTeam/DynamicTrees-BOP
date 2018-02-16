package dynamictreesbop.items;

import javax.annotation.Nullable;

import com.ferreusveritas.dynamictrees.items.Seed;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMagicSeed extends Seed {

	public ItemMagicSeed(String name) {
		super(name);
	}
	
	@Override
	public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }
	
	@Override
	@Nullable
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		EntityItemMagicSeed mapleSeedEntity = new EntityItemMagicSeed(world, location.posX, location.posY, location.posZ, itemstack);
		
		//We need to also copy the motion of the replaced entity or it acts funny when the item spawns.
		mapleSeedEntity.motionX = location.motionX;
		mapleSeedEntity.motionY = location.motionY;
		mapleSeedEntity.motionZ = location.motionZ;
		
        return mapleSeedEntity;
    }
	
	public static class EntityItemMagicSeed extends EntityItem {
		
		public EntityItemMagicSeed(World worldIn) {
			super(worldIn);
		}
		
		public EntityItemMagicSeed(World worldIn, double x, double y, double z, ItemStack stack) {
			super(worldIn, x, y, z, stack);
			this.setDefaultPickupDelay();
		}
		
		@Override
		public void onUpdate() {
			
			//Add an impulse now and again as long as we're moving
			if (!this.onGround && Math.abs(motionX) + Math.abs(motionZ) > 0.01f) {
				if(getAge() / 4 % 15 == 14) {
					motionY += 0.025;
					motionX += (this.rand.nextDouble() - 0.5) * 0.05;
					motionZ += (this.rand.nextDouble() - 0.5) * 0.05;
				}
			}
			
			//Add lift to counteract the gravity that will be applied in super.onUpdate()
			motionY += 0.0385;
			
			//Counteract the air friction that will be applied in super.onUpdate() this results in a 0.99 factor instead of 0.98
			motionX *= 1.00f;
			motionZ *= 1.00f;
			
			
			super.onUpdate();
		}
		
		@Override
		public void setDefaultPickupDelay() {
	        this.setPickupDelay(50);
	    }
		
	}

}
