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
        
    	if(entity.onGround) {
    		vanillaEntityItemRenderer.doRender(entity, x, y, z, entityYaw, partialTicks);
    		return;
    	}
    	
    	ItemStack itemstack = entity.getItem();
        boolean textureMipmap = false;

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

       	GlStateManager.rotate((float) -45f, 0, 0, 1);//twist the wing slightly to give it the right cut angle
        
        //This piece will do the actual Minecraft style extruded pixel rendering
        IBakedModel transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);
        this.itemRenderer.renderItem(itemstack, transformedModel);
        
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
