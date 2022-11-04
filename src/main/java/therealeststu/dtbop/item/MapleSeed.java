package therealeststu.dtbop.item;

import com.ferreusveritas.dynamictrees.item.Seed;
import com.ferreusveritas.dynamictrees.tree.species.Species;

public class MapleSeed extends Seed {

    public MapleSeed(Species species) {
        super(species);
    }

//    @Override
//    public boolean hasCustomEntity(ItemStack stack) {
//        return true;
//    }
//
//    @Override
//    @Nullable
//    public Entity createEntity(World world, Entity oldEntity, ItemStack itemstack) {
//        EntityItemMapleSeed mapleSeedEntity = new EntityItemMapleSeed(world, oldEntity.getX(), oldEntity.getY(), oldEntity.getZ(), itemstack);
//
//        //We need to also copy the motion of the replaced entity or it acts funny when the item spawns.
//        mapleSeedEntity.setDeltaMovement(oldEntity.getDeltaMovement());
//
//        return mapleSeedEntity;
//    }
//
//    public static class EntityItemMapleSeed extends ItemEntity {
//
//        public EntityItemMapleSeed(World worldIn, double x, double y, double z, ItemStack stack) {
//            super(worldIn, x, y, z, stack);
//            this.setDefaultPickUpDelay();
//        }
//
//        @Override
//        public void tick() {
//            if (getDeltaMovement().y<=0){
//                //Counteract the air friction that will be applied in super.tick() this results in a 0.99 factor instead of 0.98
//                setDeltaMovement(getDeltaMovement().multiply(1.01, 1, 1.01));
//
//                //Add lift to counteract the gravity that will be applied in super.onUpdate()
//                setDeltaMovement(getDeltaMovement().add(0,0.03,0));
//            }
//            super.tick();
//        }
//
//        @Override
//        public void setDefaultPickUpDelay() {
//            super.setPickUpDelay(50);
//        }
//
//    }

}
