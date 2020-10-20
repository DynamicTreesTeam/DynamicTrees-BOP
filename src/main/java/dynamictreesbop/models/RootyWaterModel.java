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
	protected TextureAtlasSprite stillWaterTexture;
	protected TextureAtlasSprite flowWaterTexture;
	
	public RootyWaterModel(IBakedModel rootsModel) {
		this.rootsModel = rootsModel;
		this.stillWaterTexture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still");
		this.flowWaterTexture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_flow");
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		List<BakedQuad> quads = new ArrayList<BakedQuad>();
		if (side != null) return quads;
		
		if (state instanceof IExtendedBlockState) {
			
			BlockRenderLayer renderLayer = MinecraftForgeClient.getRenderLayer();
			
			if(renderLayer == BlockRenderLayer.CUTOUT_MIPPED) {
				quads.addAll(rootsModel.getQuads(state, side, rand));
			}
			
			if(renderLayer == BlockRenderLayer.TRANSLUCENT) {
				IExtendedBlockState extState = (IExtendedBlockState) state;

				float yOffset = 0.001F;
				float y0 = extState.getValue(BlockRootyWater.CORNER_HEIGHTS[0]) - yOffset;
				float y1 = extState.getValue(BlockRootyWater.CORNER_HEIGHTS[1]) - yOffset;
				float y2 = extState.getValue(BlockRootyWater.CORNER_HEIGHTS[2]) - yOffset;
				float y3 = extState.getValue(BlockRootyWater.CORNER_HEIGHTS[3]) - yOffset;
				
				TextureAtlasSprite textureatlassprite = stillWaterTexture;
				
				int colors[] = new int[6];
				for(EnumFacing facing : EnumFacing.values()) {
					int v = (int) (255 * net.minecraftforge.client.model.pipeline.LightUtil.diffuseLight(facing));
					colors[facing.ordinal()] = 0xFF000000 | v << 16 | v << 8 | v; //0xAARRGGBB'
				}
				
				//DOWN
				if (extState.getValue(BlockRootyWater.RENDER_SIDES[0])) {
					quads.add(new BakedQuad(Ints.concat(
							vertexToInts(0, 0, 1, colors[0], textureatlassprite, 0.0f, 16.0f),
							vertexToInts(0, 0, 0, colors[0], textureatlassprite, 0.0f, 0.0f),
							vertexToInts(1, 0, 0, colors[0], textureatlassprite, 16.0f, 0.0f),
							vertexToInts(1, 0, 1, colors[0], textureatlassprite, 16.0f, 16.0f)
							), 0, EnumFacing.DOWN, textureatlassprite, false, DefaultVertexFormats.BLOCK));
				}
				
				//UP
				if (extState.getValue(BlockRootyWater.RENDER_SIDES[1])) {
					quads.add(new BakedQuad(Ints.concat(
							vertexToInts(0, y0, 0, colors[1], textureatlassprite, 0.0f, 0.0f),
							vertexToInts(0, y1, 1, colors[1], textureatlassprite, 0.0f, 16.0f),
							vertexToInts(1, y2, 1, colors[1], textureatlassprite, 16.0f, 16.0f),
							vertexToInts(1, y3, 0, colors[1], textureatlassprite, 16.0f, 0.0f)
							), 0, EnumFacing.UP, textureatlassprite, false, DefaultVertexFormats.BLOCK));
					quads.add(new BakedQuad(Ints.concat(
							vertexToInts(0, y0, 0, colors[1], textureatlassprite, 0.0f, 0.0f),
							vertexToInts(1, y3, 0, colors[1], textureatlassprite, 16.0f, 0.0f),
							vertexToInts(1, y2, 1, colors[1], textureatlassprite, 16.0f, 16.0f),
							vertexToInts(0, y1, 1, colors[1], textureatlassprite, 0.0f, 16.0f)
							), 0, EnumFacing.DOWN, textureatlassprite, false, DefaultVertexFormats.BLOCK));
				}
				
				//NORTH
				if (extState.getValue(BlockRootyWater.RENDER_SIDES[2])) {
					quads.add(new BakedQuad(Ints.concat(
							vertexToInts(1, y3, 0, colors[2], flowWaterTexture, 8.0f, (1.0f - y3) * 8.0f),
							vertexToInts(1,  0, 0, colors[2], flowWaterTexture, 8.0f, 8.0f),
							vertexToInts(0,  0, 0, colors[2], flowWaterTexture, 0.0f, 8.0f),
							vertexToInts(0, y0, 0, colors[2], flowWaterTexture, 0.0f, (1.0f - y0) * 8.0f)
							), 0, EnumFacing.NORTH, flowWaterTexture, false, DefaultVertexFormats.BLOCK));
					quads.add(new BakedQuad(Ints.concat(
							vertexToInts(0, y3, 0, colors[2], flowWaterTexture, 8.0f, (1.0f - y3) * 8.0f),
							vertexToInts(0,  0, 0, colors[2], flowWaterTexture, 8.0f, 8.0f),
							vertexToInts(1,  0, 0, colors[2], flowWaterTexture, 0.0f, 8.0f),
							vertexToInts(1, y0, 0, colors[2], flowWaterTexture, 0.0f, (1.0f - y0) * 8.0f)
							), 0, EnumFacing.SOUTH, flowWaterTexture, false, DefaultVertexFormats.BLOCK));
				}
				
				//SOUTH
				if (extState.getValue(BlockRootyWater.RENDER_SIDES[3])) {
					quads.add(new BakedQuad(Ints.concat(
							vertexToInts(0, y1, 1, colors[3], flowWaterTexture, 8.0f, (1.0f - y1) * 8.0f),
							vertexToInts(0,  0, 1, colors[3], flowWaterTexture, 8.0f, 8.0f),
							vertexToInts(1,  0, 1, colors[3], flowWaterTexture, 0.0f, 8.0f),
							vertexToInts(1, y2, 1, colors[3], flowWaterTexture, 0.0f, (1.0f - y2) * 8.0f)
							), 0, EnumFacing.SOUTH, flowWaterTexture, false, DefaultVertexFormats.BLOCK));
					quads.add(new BakedQuad(Ints.concat(
							vertexToInts(1, y1, 1, colors[3], flowWaterTexture, 8.0f, (1.0f - y1) * 8.0f),
							vertexToInts(1,  0, 1, colors[3], flowWaterTexture, 8.0f, 8.0f),
							vertexToInts(0,  0, 1, colors[3], flowWaterTexture, 0.0f, 8.0f),
							vertexToInts(0, y2, 1, colors[3], flowWaterTexture, 0.0f, (1.0f - y2) * 8.0f)
							), 0, EnumFacing.NORTH, flowWaterTexture, false, DefaultVertexFormats.BLOCK));
				}
				
				//WEST
				if (extState.getValue(BlockRootyWater.RENDER_SIDES[4])) {
					quads.add(new BakedQuad(Ints.concat(
							vertexToInts(0, y0, 0, colors[4], flowWaterTexture, 8.0f, (1.0f - y0) * 8.0f),
							vertexToInts(0,  0, 0, colors[4], flowWaterTexture, 8.0f, 8.0f),
							vertexToInts(0,  0, 1, colors[4], flowWaterTexture, 0.0f, 8.0f),
							vertexToInts(0, y1, 1, colors[4], flowWaterTexture, 0.0f, (1.0f - y1) * 8.0f)
							), 0, EnumFacing.WEST, flowWaterTexture, false, DefaultVertexFormats.BLOCK));
					quads.add(new BakedQuad(Ints.concat(
							vertexToInts(0, y0, 1, colors[4], flowWaterTexture, 8.0f, (1.0f - y0) * 8.0f),
							vertexToInts(0,  0, 1, colors[4], flowWaterTexture, 8.0f, 8.0f),
							vertexToInts(0,  0, 0, colors[4], flowWaterTexture, 0.0f, 8.0f),
							vertexToInts(0, y1, 0, colors[4], flowWaterTexture, 0.0f, (1.0f - y1) * 8.0f)
							), 0, EnumFacing.EAST, flowWaterTexture, false, DefaultVertexFormats.BLOCK));
				}
				
				//EAST
				if (extState.getValue(BlockRootyWater.RENDER_SIDES[5])) {
					quads.add(new BakedQuad(Ints.concat(
							vertexToInts(1, y2, 1, colors[5], flowWaterTexture, 8.0f, (1.0f - y2) * 8.0f),
							vertexToInts(1,  0, 1, colors[5], flowWaterTexture, 8.0f, 8.0f),
							vertexToInts(1,  0, 0, colors[5], flowWaterTexture, 0.0f, 8.0f),
							vertexToInts(1, y3, 0, colors[5], flowWaterTexture, 0.0f, (1.0f - y3) * 8.0f)
							), 0, EnumFacing.EAST, flowWaterTexture, false, DefaultVertexFormats.BLOCK));
					quads.add(new BakedQuad(Ints.concat(
							vertexToInts(1, y2, 0, colors[5], flowWaterTexture, 8.0f, (1.0f - y2) * 8.0f),
							vertexToInts(1,  0, 0, colors[5], flowWaterTexture, 8.0f, 8.0f),
							vertexToInts(1,  0, 1, colors[5], flowWaterTexture, 0.0f, 8.0f),
							vertexToInts(1, y3, 1, colors[5], flowWaterTexture, 0.0f, (1.0f - y3) * 8.0f)
							), 0, EnumFacing.WEST, flowWaterTexture, false, DefaultVertexFormats.BLOCK));
					
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
		return stillWaterTexture;
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