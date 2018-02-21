package dynamictreesbop.models;

import java.util.ArrayList;
import java.util.List;

import com.google.common.primitives.Ints;

import dynamictreesbop.ModContent;
import dynamictreesbop.blocks.BlockDynamicLeavesPalm;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

public class PalmLeavesCompositeModel implements IBakedModel {

	protected IBakedModel baseModel;
	protected List<BakedQuad>[] cachedFrondQuads = new List[8]; // 8 = Number of surrounding blocks

	public PalmLeavesCompositeModel(IBakedModel rootsModel) {
		this.baseModel = rootsModel;
		bakeFronds();
	}

	private void bakeFronds() {
		IBlockState state = ModContent.palmLeavesProperties.getDynamicLeavesState(1);

		for (BlockDynamicLeavesPalm.Surround surr : BlockDynamicLeavesPalm.Surround.values()) {
			List<BakedQuad> quadsForSurr = cachedFrondQuads[surr.ordinal()] = new ArrayList<BakedQuad>(0);
			List<BakedQuad> baseQuads = baseModel.getQuads(state, null, 0);

			for (BakedQuad bq : baseQuads) {
				BlockVertexData vertexData[] = new BlockVertexData[4];

				for (int pass = 0; pass < 2; pass++) {
					for (int half = 0; half < 2; half++) {
						for (int v = 0; v < 4; v++) {
							vertexData[v] = new BlockVertexData(bq, v);
	
							// Nab the vertex;
							float x = vertexData[v].x;
							float z = vertexData[v].z;
							float y = vertexData[v].y;
	
							double len;
							double angle;
							
							// Rotate the vertex around x0,y=0.75
							// Rotate on z axis
							//len = Math.sqrt(x * x + (0.75 - y) * (0.75 - y));
							len = 0.75 - y;
							angle = Math.atan2(x, y);
							angle += Math.PI * (half == 1 ? 0.8 : -0.8);
							x = (float) (Math.sin(angle) * len);
							y = (float) (Math.cos(angle) * len);
							
							// Rotate the vertex around x0,z0
							// Rotate on x axis
							len = Math.sqrt(y * y + z * z);
							angle = Math.atan2(y, z);
							angle += Math.PI * (pass == 1 ? 0.10 : -0.125);
							y = (float) (Math.sin(angle) * len);
							z = (float) (Math.cos(angle) * len);
	
							// Rotate the vertex around x0,z0
							// Rotate on y axis
							len = Math.sqrt(x * x + z * z);
							angle = Math.atan2(x, z);
							angle += Math.PI * 0.25 * surr.ordinal() + (Math.PI * (pass == 1 ? 0.125 : 0.005));
							x = (float) (Math.sin(angle) * len);
							z = (float) (Math.cos(angle) * len);
	
							// Move to center of block
							x += 0.5f;
							z += 0.5f;
							//y -= 0.25f;
	
							// Move to center of palm crown
							x += surr.getOffset().getX();
							z += surr.getOffset().getZ();
	
							vertexData[v].x = x;
							vertexData[v].z = z;
							vertexData[v].y = y;
						}
	
						BakedQuad movedQuad = new BakedQuad(
								Ints.concat(vertexData[0].toInts(), vertexData[1].toInts(), vertexData[2].toInts(),
										vertexData[3].toInts()),
								bq.getTintIndex(), bq.getFace(), bq.getSprite(), bq.shouldApplyDiffuseLighting(),
								bq.getFormat());
	
						quadsForSurr.add(movedQuad);
					}
				}
			}
		}

	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		List<BakedQuad> quads = new ArrayList<BakedQuad>();

		if (side == null && state != null && state.getBlock() instanceof BlockDynamicLeavesPalm && state instanceof IExtendedBlockState) {
			for (int i = 0; i < 8; i++) {
				Boolean b = ((IExtendedBlockState) state).getValue(BlockDynamicLeavesPalm.CONNECTIONS[i]);
				if(b != null && b.booleanValue()) {
					quads.addAll(cachedFrondQuads[i]);
				}
			}
		}

		return quads;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return true;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return baseModel.getParticleTexture();
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
