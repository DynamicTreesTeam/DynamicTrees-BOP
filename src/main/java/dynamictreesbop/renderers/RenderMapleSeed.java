package dynamictreesbop.renderers;

import java.util.Random;

import dynamictreesbop.items.ItemMapleSeed;
import dynamictreesbop.items.ItemMapleSeed.EntityItemMapleSeed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMapleSeed extends Render<ItemMapleSeed.EntityItemMapleSeed> {
	
	private final RenderItem itemRenderer;
    private final Random random = new Random();
    
    public RenderMapleSeed(RenderManager renderManagerIn, RenderItem renderItem) {
        super(renderManagerIn);
        this.itemRenderer = renderItem;
        this.shadowSize = 0F;
        this.shadowOpaque = 0F;
    }
    
    private int transformModelCount(EntityItem itemIn, double x, double y, double z, float partialTicks, IBakedModel itemModel) {
        ItemStack itemstack = itemIn.getItem();
        Item item = itemstack.getItem();

        if (item == null) {
            return 0;
        } else {
            boolean flag = itemModel.isGui3d();
            int i = 1;
            float f = 0.25F;
            float f2 = itemModel.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
            GlStateManager.translate((float) x, (float) y + 0.03125f, (float) z);

            if (flag || this.renderManager.options != null) {
            	float spinSpeed = itemIn.onGround ? 0 : ((float) itemIn.getAge() + partialTicks) / 2.5F;
                float yaw = (spinSpeed + itemIn.hoverStart) * (180F / (float) Math.PI);
                GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            return i;
        }
    }
    
    @Override
    public void doRender(EntityItemMapleSeed entity, double x, double y, double z, float entityYaw, float partialTicks) {
        ItemStack itemstack = entity.getItem();
        int i = itemstack.isEmpty() ? 187 : Item.getIdFromItem(itemstack.getItem()) + itemstack.getMetadata();
        this.random.setSeed((long) i);
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
        int j = this.transformModelCount(entity, x, y, z, partialTicks, ibakedmodel);
        boolean flag1 = ibakedmodel.isGui3d();

        if (!flag1) {
            float f3 = -0.0F * (float)(j - 1) * 0.5F;
            float f4 = -0.0F * (float)(j - 1) * 0.5F;
            float f5 = -0.09375F * (float)(j - 1) * 0.5F;
            GlStateManager.translate(f3, f4, f5);
        }
        
        if (entity.onGround) GlStateManager.rotate((float) 90f, 1f, 0f, 0f);
        else if (entity.motionY < 0) GlStateManager.rotate((float) 30f, 1f, 0f, -0.35f);

        for (int k = 0; k < j; ++k) {
            if (flag1) {
                GlStateManager.pushMatrix();

                if (k > 0) {
                    float f7 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f9 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f6 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    GlStateManager.translate(f7, f9, f6);
                }

                IBakedModel transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);
                this.itemRenderer.renderItem(itemstack, transformedModel);
                GlStateManager.popMatrix();
            } else {
                GlStateManager.pushMatrix();

                if (k > 0) {
                    float f8 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    float f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    GlStateManager.translate(f8, f10, 0.0F);
                }

                IBakedModel transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);
                this.itemRenderer.renderItem(itemstack, transformedModel);
                GlStateManager.popMatrix();
                GlStateManager.translate(0.0F, 0.0F, 0.09375F);
            }
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
	protected ResourceLocation getEntityTexture(EntityItemMapleSeed entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
	
	
	public static class Factory implements IRenderFactory<ItemMapleSeed.EntityItemMapleSeed> {

		@Override
		public Render<ItemMapleSeed.EntityItemMapleSeed> createRenderFor(RenderManager manager) {
			return new RenderMapleSeed(manager, Minecraft.getMinecraft().getRenderItem());
		}
		
	}
	
}
