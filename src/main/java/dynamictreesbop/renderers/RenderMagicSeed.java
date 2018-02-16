package dynamictreesbop.renderers;

import dynamictreesbop.items.ItemMagicSeed;
import dynamictreesbop.items.ItemMagicSeed.EntityItemMagicSeed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMagicSeed extends Render<ItemMagicSeed.EntityItemMagicSeed> {
	
	private final RenderItem itemRenderer;
	private Render<Entity> vanillaEntityItemRenderer;
    
    public RenderMagicSeed(RenderManager renderManagerIn, RenderItem renderItem) {
        super(renderManagerIn);
        this.itemRenderer = renderItem;
        this.vanillaEntityItemRenderer = renderManagerIn.getEntityClassRenderObject(EntityItem.class);
        this.shadowSize = 0F;
        this.shadowOpaque = 0F;
    }
    
    @Override
    public void doRender(EntityItemMagicSeed entity, double x, double y, double z, float entityYaw, float partialTicks) {
        
    	ItemStack itemstack = entity.getItem();
        boolean textureMipmap = false;
        
        int itemMeta = itemstack.getMetadata(); // save the stack's metadata value
       	if (entity.animFrame != itemstack.getMetadata()) itemstack.setItemDamage(entity.animFrame); // change the stack's metadata for animation
        
        if (entity.onGround) {
    		vanillaEntityItemRenderer.doRender(entity, x, y, z, entityYaw, partialTicks);
    		itemstack.setItemDamage(itemMeta); // restore the stack's metadata value
    		return;
    	}
        
        if (this.bindEntityTexture(entity)) {
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).setBlurMipmap(false, false);
            textureMipmap = true;
        }
        
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.pushMatrix();
        IBakedModel ibakedmodel = this.itemRenderer.getItemModelWithOverrides(itemstack, entity.world, (EntityLivingBase)null);
        
        GlStateManager.translate((float) x, (float) y + 0.03125f, (float) z);//Move the item up off the ground a bit
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);//Make sure the operating multiplier is white for all colors
        
        float spinOrient = entity.onGround ? 0 : ((float) entity.getAge() + partialTicks) / 45F;//The angular orientation in radians at this given animation frame
        float yaw = (spinOrient + entity.hoverStart) * (180F / (float) Math.PI);//Convert from radians to degrees and add the starting hover position for individuality
        GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);//the actual spinning rotation component of the animation
       	GlStateManager.rotate((float) -45f, 0, 0, 1);//twist the wing slightly to give it the right cut angle
        
       	IBakedModel transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);
        this.itemRenderer.renderItem(itemstack, transformedModel); // This piece will do the actual Minecraft style extruded pixel rendering
        
        if (!ibakedmodel.isGui3d()) {//Don't know why we need this.. but whatever
            GlStateManager.translate(0.0F, 0.0F, 0.09375F);
        }
            
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        this.bindEntityTexture(entity);

        if (textureMipmap) {
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).restoreLastBlurMipmap();
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        
        itemstack.setItemDamage(itemMeta); // restore the stack's metadata value
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityItemMagicSeed entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
	
	
	public static class Factory implements IRenderFactory<ItemMagicSeed.EntityItemMagicSeed> {

		@Override
		public Render<ItemMagicSeed.EntityItemMagicSeed> createRenderFor(RenderManager manager) {
			return new RenderMagicSeed(manager, Minecraft.getMinecraft().getRenderItem());
		}
		
	}
	
}
