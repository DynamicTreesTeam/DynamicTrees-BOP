package dynamictreesbop.models;

import java.util.ArrayList;
import java.util.List;

import com.google.common.primitives.Ints;

import dynamictreesbop.ModConstants;
import dynamictreesbop.blocks.BlockRootyWater;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;


@Mod.EventBusSubscriber(modid = ModConstants.MODID, value = { Side.CLIENT })
public class RootyWaterModel implements IBakedModel {
	
	protected IBakedModel rootsModel;
	protected TextureAtlasSprite waterTexture;
	protected int waterTextureAlpha;
	
	public RootyWaterModel(IBakedModel rootsModel) {
		this.rootsModel = rootsModel;
		this.waterTexture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still");
		int i = 0;
		int avg = 0;
		for (int[] pixels : this.waterTexture.getFrameTextureData(0)) {
			for (int p : pixels) {
				avg += (p >> 24) & 0xFF;
				i++;
			}
		}
		this.waterTextureAlpha = avg / i;
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		List<BakedQuad> quads = new ArrayList<BakedQuad>();
		if (side != null) return quads;
		//IBlockState waterState = Blocks.WATER.getDefaultState();
		if (state instanceof IExtendedBlockState) {
			
			BlockRenderLayer renderLayer = MinecraftForgeClient.getRenderLayer();
			
			if(renderLayer == BlockRenderLayer.CUTOUT_MIPPED) {
				quads.addAll(rootsModel.getQuads(state, side, rand));
			}
			
			if(renderLayer == BlockRenderLayer.TRANSLUCENT) {
				IExtendedBlockState extState = (IExtendedBlockState) state;
				
				float f7 = extState.getValue(BlockRootyWater.CORNER_HEIGHTS[0]);
				float f8 = extState.getValue(BlockRootyWater.CORNER_HEIGHTS[1]);
				float f9 = extState.getValue(BlockRootyWater.CORNER_HEIGHTS[2]);
				float f10 = extState.getValue(BlockRootyWater.CORNER_HEIGHTS[3]);
				float f11 = 0.001F;
				
				TextureAtlasSprite textureatlassprite = waterTexture;
				f7 -= f11;
				f8 -= f11;
				f9 -= f11;
				f10 -= f11;
				float f13 = 0;
				float f17 = 0;
				float f14 = f13;
				float f18 = 16;
				float f15 = 16;
				float f19 = f18;
				float f16 = f15;
				float f20 = f17;
				
				int color = 0xFFFFFFFF;
				if (extState.getValue(BlockRootyWater.RENDER_SIDES[1])) {
					quads.add(new BakedQuad(Ints.concat(
							vertexToInts(0, f7, 0, color, textureatlassprite, f13, f17),
							vertexToInts(0, f8, 1, color, textureatlassprite, f14, f18),
							vertexToInts(1, f9, 1, color, textureatlassprite, f15, f19),
							vertexToInts(1, f10, 0, color, textureatlassprite, f16, f20)
							), 0, EnumFacing.UP, textureatlassprite, false, DefaultVertexFormats.BLOCK));
					quads.add(new BakedQuad(Ints.concat(
							vertexToInts(0, f7, 0, color, textureatlassprite, f13, f17),
							vertexToInts(1, f10, 0, color, textureatlassprite, f16, f20),
							vertexToInts(1, f9, 1, color, textureatlassprite, f15, f19),
							vertexToInts(0, f8, 1, color, textureatlassprite, f14, f18)
							), 0, EnumFacing.DOWN, textureatlassprite, false, DefaultVertexFormats.BLOCK));
				}
				
				if (extState.getValue(BlockRootyWater.RENDER_SIDES[0])) {
					quads.add(new BakedQuad(Ints.concat(
							vertexToInts(0, 0, 1, color, textureatlassprite, f13, f18),
							vertexToInts(0, 0, 0, color, textureatlassprite, f14, f17),
							vertexToInts(1, 0, 0, color, textureatlassprite, f15, f20),
							vertexToInts(1, 0, 1, color, textureatlassprite, f16, f19)
							), 0, EnumFacing.DOWN, textureatlassprite, false, DefaultVertexFormats.BLOCK));
				}
				
			}
		}
		
		
		return quads;
	}
	
	protected int[] vertexToInts(float x, float y, float z, int color, TextureAtlasSprite texture, float u, float v) {
		return new int[] {
				Float.floatToRawIntBits(x), Float.floatToRawIntBits(y), Float.floatToRawIntBits(z),
				color,
				Float.floatToRawIntBits(texture.getInterpolatedU(u)), Float.floatToRawIntBits(texture.getInterpolatedV(v)),
				0,
		};
	}
	
	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}
	
	@Override
	public boolean isGui3d() {
		return false;
	}
	
	@Override
	public boolean isBuiltInRenderer() {
		return true;
	}
	
	@Override
	public TextureAtlasSprite getParticleTexture() {
		return waterTexture;
	}
	
	@Override
	public ItemOverrideList getOverrides() {
		return ItemOverrideList.NONE;
	}
	
	@SubscribeEvent
	public static void onModelBakeEvent(ModelBakeEvent event) {
		ModelResourceLocation rl = new ModelResourceLocation(new ResourceLocation(ModConstants.MODID, "rootywater"), "normal");
		IBakedModel originalModel = event.getModelRegistry().getObject(rl);
		event.getModelRegistry().putObject(rl, new RootyWaterModel(originalModel));
	}
	
}