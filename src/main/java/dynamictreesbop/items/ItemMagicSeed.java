package dynamictreesbop.items;

import javax.annotation.Nullable;

import com.ferreusveritas.dynamictrees.items.Seed;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMagicSeed extends Seed {
	
	public ItemMagicSeed(String name) {
		super(name);
		this.setHasSubtypes(true);
		// Add a property getter instead of just registering different models for different damage values so that cheating in items with different metadata values won't look different
		this.addPropertyOverride(new ResourceLocation("puff"), new IItemPropertyGetter() {
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				return entityIn == null ? stack.getMetadata() : 0;
			}
		});
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}
	
	@Override
	@Nullable
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		EntityItemMagicSeed magicSeedEntity = new EntityItemMagicSeed(world, location.posX, location.posY, location.posZ, itemstack);
		
		//We need to also copy the motion of the replaced entity or it acts funny when the item spawns.
		magicSeedEntity.motionX = location.motionX;
		magicSeedEntity.motionY = location.motionY;
		magicSeedEntity.motionZ = location.motionZ;
		
		return magicSeedEntity;
	}
	
	
	public static class EntityItemMagicSeed extends EntityItem {
		
		public int animFrame = 0;
		public boolean puffing = false;
		
		public EntityItemMagicSeed(World worldIn) {
			super(worldIn);
			this.setDefaultPickupDelay();
		}
		
		public EntityItemMagicSeed(World worldIn, double x, double y, double z, ItemStack stack) {
			super(worldIn, x, y, z, stack);
			this.setDefaultPickupDelay();
		}
		
		@Override
		public void onUpdate() {
			
			//Add an impulse now and again as long as we're moving
			int interval = 15;
			int t = ticksExisted / 5 % interval;
			double horizontalSpeed = Math.abs(motionX) + Math.abs(motionZ);
			if ((!this.onGround && horizontalSpeed > 0.012f) || puffing || animFrame > 0) {
				puffing = false;
				if (t == interval - 1) {
					motionY += 0.035;
					if (horizontalSpeed > 0.012f) motionX += (this.rand.nextGaussian()) * 0.035;
					if (horizontalSpeed > 0.012f) motionZ += (this.rand.nextGaussian()) * 0.035;
					
					// if (rand.nextInt(3) == 0) world.spawnParticle(EnumParticleTypes.CLOUD, posX, posY, posZ, this.rand.nextGaussian() * 0.05, -0.05, this.rand.nextGaussian() * 0.05);
					
					puffing = true;
					animFrame = animFrame == 1 ? 0 : 1;
				} else if (t >= interval - 2) {
					animFrame = 3;
				} else if (t >= interval - 3) {
					animFrame = 2;
				} else if (t >= interval - 4) {
					animFrame = 1;
				} else {
					animFrame = 0;
				}
			} else {
				animFrame = 0;
			}
			
			//Add lift to counteract the gravity that will be applied in super.onUpdate()
			motionY += motionY < 0 ? 0.0385 : 0.036;
			
			motionX *= 0.99;
			motionZ *= 0.99;
			
			super.onUpdate();
		}
		
		@Override
		public void setDefaultPickupDelay() {
			this.setPickupDelay(50);
		}
		
	}
	
}
