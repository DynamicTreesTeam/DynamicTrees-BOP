package dynamictreesbop.models;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.google.common.primitives.Ints;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.block.model.SimpleBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.property.IExtendedBlockState;

public class CompositePalmFrondsModel implements IBakedModel {

	protected ModelBlock modelBlock;
	
	TextureAtlasSprite barkParticles;
	
	private IBakedModel fronds[] = new IBakedModel[8]; // 8 = Number of surrounding blocks
	
	public CompositePalmFrondsModel(ResourceLocation frondRes, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {		
		modelBlock = new ModelBlock(null, null, null, false, false, ItemCameraTransforms.DEFAULT, null);
		
		TextureAtlasSprite barkIcon = bakedTextureGetter.apply(frondRes);
		barkParticles = barkIcon;
		
		for (CoordUtils.Surround surr : CoordUtils.Surround.values()) {

			SimpleBakedModel.Builder builder = new SimpleBakedModel.Builder(modelBlock, ItemOverrideList.NONE).setTexture(barkIcon);

			BlockVertexData quadData[] = {
					new BlockVertexData(0.0f, 0.0f, 0.0f),
					new BlockVertexData(1.0f, 0.0f, 0.0f),
					new BlockVertexData(1.0f, 1.0f, 0.0f),
					new BlockVertexData(0.0f, 1.0f, 0.0f)
			};
			
			for (BlockVertexData bvd : quadData) {

				for (int pass = 0; pass < 3; pass++) {
					for (int half = 0; half < 2; half++) {
						
						BlockVertexData outData[] = new BlockVertexData[4];
						
						for (int v = 0; v < 4; v++) {
							
							// Nab the vertex;
							float x = bvd.x;
							float z = bvd.z;
							float y = bvd.y;
							
							x *= (40f / 32f);
							z *= (40f / 32f);
	
							double len;
							double angle;
							
							// Rotate the vertex around x0,y=0.75
							// Rotate on z axis
							len = 0.75 - y;
							angle = Math.atan2(x, y);
							angle += Math.PI * (half == 1 ? 0.8 : -0.8);
							x = (float) (Math.sin(angle) * len);
							y = (float) (Math.cos(angle) * len);
							
							// Rotate the vertex around x0,z0
							// Rotate on x axis
							len = Math.sqrt(y * y + z * z);
							angle = Math.atan2(y, z);
							angle += Math.PI * (pass == 2 ? 0.28 : pass == 1 ? 0.06 : -0.17);
							y = (float) (Math.sin(angle) * len);
							z = (float) (Math.cos(angle) * len);
	
							// Rotate the vertex around x0,z0
							// Rotate on y axis
							len = Math.sqrt(x * x + z * z);
							angle = Math.atan2(x, z);
							angle += Math.PI * 0.25 * surr.ordinal() + (Math.PI * (pass == 1 ? 0.185 : pass == 2 ? 0.08 : 0.005));
							x = (float) (Math.sin(angle) * len);
							z = (float) (Math.cos(angle) * len);
							
							// Move to center of block
							x += 0.5f;
							z += 0.5f;
							y += pass == 2 ? -0.125 : pass == 0 ? 0.125 : 0;
							//y -= 0.25f;
	
							// Move to center of palm crown
							x += surr.getOffset().getX();
							z += surr.getOffset().getZ();
	
							outData[v] = new BlockVertexData(x, y, z);
						}
						
						builder.addGeneralQuad(
							new BakedQuad(
								Ints.concat(outData[0].toInts(), outData[1].toInts(), outData[2].toInts(), outData[3].toInts()),
								0, EnumFacing.WEST, barkIcon, true, DefaultVertexFormats.BLOCK)
						);
	
					}
				}
			}
		}
		
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
	public List<BakedQuad> getQuads(IBlockState blockState, EnumFacing side, long rand) {
		List<BakedQuad> quadsList = new LinkedList<BakedQuad>();
		if (MinecraftForgeClient.getRenderLayer() != BlockRenderLayer.CUTOUT_MIPPED) return quadsList;
		
		IExtendedBlockState extendedBlockState = (IExtendedBlockState)blockState;
		if (blockState instanceof IExtendedBlockState) {
			return quadsList;
		}
		
		return quadsList;
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

	// used for block breaking shards
	@Override
	public TextureAtlasSprite getParticleTexture() {
		return barkParticles;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return fronds[0].getItemCameraTransforms();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return null;
	}

	
	
	public class BlockVertexData {

		public float x;
		public float y;
		public float z;
		public int color;
		public float u;
		public float v;

		 // Default format of the data in IBakedModel
		 /* DEFAULT_BAKED_FORMAT.addElement(new VertexFormatElement(0, EnumType.FLOAT, EnumUsage.POSITION, 3));
		 * DEFAULT_BAKED_FORMAT.addElement(new VertexFormatElement(0, EnumType.UBYTE, EnumUsage.COLOR, 4));
		 * DEFAULT_BAKED_FORMAT.addElement(new VertexFormatElement(0, EnumType.FLOAT, EnumUsage.UV, 2));
		 * DEFAULT_BAKED_FORMAT.addElement(new VertexFormatElement(0, EnumType.BYTE, EnumUsage.PADDING, 4));
		 */

		public BlockVertexData(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public BlockVertexData(BakedQuad quad, int vIndex) {
			this(quad.getVertexData(), vIndex);
		}

		public BlockVertexData(int data[], int vIndex) {
			vIndex *= 7;
			x = Float.intBitsToFloat(data[vIndex++]);
			y = Float.intBitsToFloat(data[vIndex++]);
			z = Float.intBitsToFloat(data[vIndex++]);
			color = data[vIndex++];
			u = Float.intBitsToFloat(data[vIndex++]);
			v = Float.intBitsToFloat(data[vIndex++]);
		}

		public int[] toInts() {
			return new int[] { Float.floatToRawIntBits(x), Float.floatToRawIntBits(y), Float.floatToRawIntBits(z),
					color, Float.floatToRawIntBits(u), Float.floatToRawIntBits(v), 0, };
		}

	}
}
