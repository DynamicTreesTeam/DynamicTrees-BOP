package dynamictreesbop.models;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.lwjgl.util.vector.Vector3f;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.model.SimpleBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BakedModelBlockBranchBamboo implements IBakedModel {

	protected ModelBlock modelBlock;
	
	TextureAtlasSprite barkParticles;
	
	// Not as many baked models as normal branches, although each model has more quads. Still fewer quads in total, though.
	private IBakedModel sleeves[][] = new IBakedModel[6][3];
	private IBakedModel cores[][] = new IBakedModel[3][3]; // 3 Cores for 3 axis with the bark texture all all 6 sides rotated appropriately.
	private IBakedModel rings[] = new IBakedModel[3]; //3 Cores with the ring textures on all 6 sides
	private IBakedModel coreLeaves[] = new IBakedModel[3]; // 3 cores with only the leafy edges
	
	public BakedModelBlockBranchBamboo(ResourceLocation barkRes, ResourceLocation ringsRes, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		this.modelBlock = new ModelBlock(null, null, null, false, false, ItemCameraTransforms.DEFAULT, null);
		
		TextureAtlasSprite barkIcon = bakedTextureGetter.apply(barkRes);
		TextureAtlasSprite ringIcon = bakedTextureGetter.apply(ringsRes);
		barkParticles = barkIcon;
		
		for (int i = 0; i < 3; i++) {
			int radius = i + 1;
			
			for (EnumFacing dir: EnumFacing.VALUES) {
				sleeves[dir.getIndex()][i] = bakeSleeve(radius, dir, barkIcon, ringIcon);
			}
			
			cores[0][i] = bakeCore(radius, Axis.Y, barkIcon); //DOWN<->UP
			cores[1][i] = bakeCore(radius, Axis.Z, barkIcon); //NORTH<->SOUTH
			cores[2][i] = bakeCore(radius, Axis.X, barkIcon); //WEST<->EAST
			
			rings[i] = bakeCore(radius, Axis.Y, ringIcon);
			
			coreLeaves[i] = bakeCoreLeaves(radius, barkIcon);
		}
	}

	public IBakedModel bakeSleeve(int radius, EnumFacing dir, TextureAtlasSprite bark, TextureAtlasSprite top) {
		// Work in double units(*2)
		int dradius = radius * 2;
		int halfSize = (16 - dradius) / 2;
		int halfSizeX = dir.getFrontOffsetX() != 0 ? halfSize : dradius;
		int halfSizeY = dir.getFrontOffsetY() != 0 ? halfSize : dradius;
		int halfSizeZ = dir.getFrontOffsetZ() != 0 ? halfSize : dradius;
		int move = 16 - halfSize;
		int centerX = 16 + (dir.getFrontOffsetX() * move);
		int centerY = 16 + (dir.getFrontOffsetY() * move);
		int centerZ = 16 + (dir.getFrontOffsetZ() * move);
		
		Vector3f posFrom = new Vector3f((centerX - halfSizeX) / 2, (centerY - halfSizeY) / 2, (centerZ - halfSizeZ) / 2);
		Vector3f posTo = new Vector3f((centerX + halfSizeX) / 2, (centerY + halfSizeY) / 2, (centerZ + halfSizeZ) / 2);
		
		boolean negative = dir.getAxisDirection() == AxisDirection.NEGATIVE;
		if (dir.getAxis() == Axis.Z) { // North/South
			negative = !negative;
		}
		
		Map<EnumFacing, BlockPartFace> mapFacesIn = Maps.newEnumMap(EnumFacing.class);
		
		for (EnumFacing face: EnumFacing.VALUES) {
			if (dir.getOpposite() != face) { // Discard side of sleeve that faces core
				BlockFaceUV uvface = null;
				if (dir == face) { // Side of sleeve that faces away from core
					if (radius == 1) { // We're only interested in end faces for radius == 1
						uvface = new BlockFaceUV(new float[] {8 - radius, 8 - radius, 8 + radius, 8 + radius}, 0);
					}
				} else { // UV for Bark texture
					uvface = new BlockFaceUV(new float[]{ 8 - radius, negative ? 16 - halfSize : 0, 8 + radius, negative ? 16 : halfSize }, getFaceAngle(dir.getAxis(), face));
				}
				if (uvface != null) {
					mapFacesIn.put(face, new BlockPartFace(null, -1, null, uvface));
				}
			}
		}
		
		BlockPart part = new BlockPart(posFrom, posTo, mapFacesIn, null, true);
		SimpleBakedModel.Builder builder = new SimpleBakedModel.Builder(modelBlock, ItemOverrideList.NONE).setTexture(bark);
		
		for (Map.Entry<EnumFacing, BlockPartFace> e : part.mapFaces.entrySet()) {
			EnumFacing face = e.getKey();
			builder.addFaceQuad(face, makeBakedQuad(part, e.getValue(), (dir == face) ? top : bark, face, ModelRotation.X0_Y0, false));
		}
		float minV = negative ? 16 - halfSize : 0;
		float maxV = negative ? 16 : halfSize;
		if (dir.getAxis() == Axis.Y) {
			builder.addFaceQuad(EnumFacing.WEST, new BakedQuad(Ints.concat(
					vertexToInts(posFrom.x / 16f, posTo.y / 16f, posTo.z / 16f + 0.3125f, 0xFFFFFFFF, bark, 16, minV),
					vertexToInts(posFrom.x / 16f, posTo.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 11, minV),
					vertexToInts(posFrom.x / 16f, posFrom.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 11, maxV),
					vertexToInts(posFrom.x / 16f, posFrom.y / 16f, posTo.z / 16f + 0.3125f, 0xFFFFFFFF, bark, 16, maxV)
			), 0, EnumFacing.WEST, bark, true, DefaultVertexFormats.BLOCK));
			builder.addFaceQuad(EnumFacing.WEST, new BakedQuad(Ints.concat(
					vertexToInts(posFrom.x / 16f, posTo.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 5, minV),
					vertexToInts(posFrom.x / 16f, posTo.y / 16f, posFrom.z / 16f - 0.3125f, 0xFFFFFFFF, bark, 0, minV),
					vertexToInts(posFrom.x / 16f, posFrom.y / 16f, posFrom.z / 16f - 0.3125f, 0xFFFFFFFF, bark, 0, maxV),
					vertexToInts(posFrom.x / 16f, posFrom.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 5, maxV)
			), 0, EnumFacing.WEST, bark, true, DefaultVertexFormats.BLOCK));
			builder.addFaceQuad(EnumFacing.EAST, new BakedQuad(Ints.concat(
					vertexToInts(posTo.x / 16f, posFrom.y / 16f, posTo.z / 16f + 0.3125f, 0xFFFFFFFF, bark, 16, maxV),
					vertexToInts(posTo.x / 16f, posFrom.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 11, maxV),
					vertexToInts(posTo.x / 16f, posTo.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 11, minV),
					vertexToInts(posTo.x / 16f, posTo.y / 16f, posTo.z / 16f + 0.3125f, 0xFFFFFFFF, bark, 16, minV)
			), 0, EnumFacing.EAST, bark, true, DefaultVertexFormats.BLOCK));
			builder.addFaceQuad(EnumFacing.EAST, new BakedQuad(Ints.concat(
					vertexToInts(posTo.x / 16f, posFrom.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 5, maxV),
					vertexToInts(posTo.x / 16f, posFrom.y / 16f, posFrom.z / 16f - 0.3125f, 0xFFFFFFFF, bark, 0, maxV),
					vertexToInts(posTo.x / 16f, posTo.y / 16f, posFrom.z / 16f - 0.3125f, 0xFFFFFFFF, bark, 0, minV),
					vertexToInts(posTo.x / 16f, posTo.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 5, minV)
			), 0, EnumFacing.EAST, bark, true, DefaultVertexFormats.BLOCK));
			
			builder.addFaceQuad(EnumFacing.NORTH, new BakedQuad(Ints.concat(
					vertexToInts(posTo.x / 16f + 0.3125f, posFrom.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 16, maxV),
					vertexToInts(posTo.x / 16f, posFrom.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 11, maxV),
					vertexToInts(posTo.x / 16f, posTo.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 11, minV),
					vertexToInts(posTo.x / 16f + 0.3125f, posTo.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 16, minV)
			), 0, EnumFacing.NORTH, bark, true, DefaultVertexFormats.BLOCK));
			builder.addFaceQuad(EnumFacing.NORTH, new BakedQuad(Ints.concat(
					vertexToInts(posFrom.x / 16f, posFrom.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 5, maxV),
					vertexToInts(posFrom.x / 16f - 0.3125f, posFrom.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 0, maxV),
					vertexToInts(posFrom.x / 16f - 0.3125f, posTo.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 0, minV),
					vertexToInts(posFrom.x / 16f, posTo.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 5, minV)
			), 0, EnumFacing.NORTH, bark, true, DefaultVertexFormats.BLOCK));
			builder.addFaceQuad(EnumFacing.SOUTH, new BakedQuad(Ints.concat(
					vertexToInts(posTo.x / 16f + 0.3125f, posTo.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 16, minV),
					vertexToInts(posTo.x / 16f, posTo.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 11, minV),
					vertexToInts(posTo.x / 16f, posFrom.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 11, maxV),
					vertexToInts(posTo.x / 16f + 0.3125f, posFrom.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 16, maxV)
			), 0, EnumFacing.SOUTH, bark, true, DefaultVertexFormats.BLOCK));
			builder.addFaceQuad(EnumFacing.SOUTH, new BakedQuad(Ints.concat(
					vertexToInts(posFrom.x / 16f, posTo.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 5, minV),
					vertexToInts(posFrom.x / 16f - 0.3125f, posTo.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 0, minV),
					vertexToInts(posFrom.x / 16f - 0.3125f, posFrom.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 0, maxV),
					vertexToInts(posFrom.x / 16f, posFrom.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 5, maxV)
			), 0, EnumFacing.SOUTH, bark, true, DefaultVertexFormats.BLOCK));
		}
		
		return builder.makeBakedModel();
	}
	
	public IBakedModel bakeCore(int radius, Axis axis, TextureAtlasSprite icon) {
		
		Vector3f posFrom = new Vector3f(8 - radius, 8 - radius, 8 - radius);
		Vector3f posTo = new Vector3f(8 + radius, 8 + radius, 8 + radius);
		
		Map<EnumFacing, BlockPartFace> mapFacesIn = Maps.newEnumMap(EnumFacing.class);
		
		for (EnumFacing face: EnumFacing.VALUES) {
			BlockFaceUV uvface = new BlockFaceUV(new float[]{ 8 - radius, 8 - radius, 8 + radius, 8 + radius }, getFaceAngle(axis, face));
			mapFacesIn.put(face, new BlockPartFace(null, -1, null, uvface));
		}
		
		BlockPart part = new BlockPart(posFrom, posTo, mapFacesIn, null, true);
		SimpleBakedModel.Builder builder = new SimpleBakedModel.Builder(modelBlock, ItemOverrideList.NONE).setTexture(icon);
		
		for(Map.Entry<EnumFacing, BlockPartFace> e : part.mapFaces.entrySet()) {
			EnumFacing face = e.getKey();
			builder.addFaceQuad(face, makeBakedQuad(part, e.getValue(), icon, face, ModelRotation.X0_Y0, false));
		}
		
		return builder.makeBakedModel();
	}
	
	public IBakedModel bakeCoreLeaves(int radius, TextureAtlasSprite bark) {
			float minV = 8 - radius;
			float maxV = 8 + radius;
			
			Vector3f posFrom = new Vector3f(8 - radius, 8 - radius, 8 - radius);
			Vector3f posTo = new Vector3f(8 + radius, 8 + radius, 8 + radius);
			
			SimpleBakedModel.Builder builder = new SimpleBakedModel.Builder(modelBlock, ItemOverrideList.NONE).setTexture(bark);
			
			// Y
			builder.addFaceQuad(EnumFacing.SOUTH, new BakedQuad(Ints.concat(
					vertexToInts(posFrom.x / 16f, posTo.y / 16f, posTo.z / 16f + 0.3125f, 0xFFFFFFFF, bark, 16, minV),
					vertexToInts(posFrom.x / 16f, posTo.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 11, minV),
					vertexToInts(posFrom.x / 16f, posFrom.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 11, maxV),
					vertexToInts(posFrom.x / 16f, posFrom.y / 16f, posTo.z / 16f + 0.3125f, 0xFFFFFFFF, bark, 16, maxV)
			), 0, EnumFacing.WEST, bark, true, DefaultVertexFormats.BLOCK));
			builder.addFaceQuad(EnumFacing.NORTH, new BakedQuad(Ints.concat(
					vertexToInts(posFrom.x / 16f, posTo.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 5, minV),
					vertexToInts(posFrom.x / 16f, posTo.y / 16f, posFrom.z / 16f - 0.3125f, 0xFFFFFFFF, bark, 0, minV),
					vertexToInts(posFrom.x / 16f, posFrom.y / 16f, posFrom.z / 16f - 0.3125f, 0xFFFFFFFF, bark, 0, maxV),
					vertexToInts(posFrom.x / 16f, posFrom.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 5, maxV)
			), 0, EnumFacing.WEST, bark, true, DefaultVertexFormats.BLOCK));
			builder.addFaceQuad(EnumFacing.SOUTH, new BakedQuad(Ints.concat(
					vertexToInts(posTo.x / 16f, posFrom.y / 16f, posTo.z / 16f + 0.3125f, 0xFFFFFFFF, bark, 16, maxV),
					vertexToInts(posTo.x / 16f, posFrom.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 11, maxV),
					vertexToInts(posTo.x / 16f, posTo.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 11, minV),
					vertexToInts(posTo.x / 16f, posTo.y / 16f, posTo.z / 16f + 0.3125f, 0xFFFFFFFF, bark, 16, minV)
			), 0, EnumFacing.EAST, bark, true, DefaultVertexFormats.BLOCK));
			builder.addFaceQuad(EnumFacing.NORTH, new BakedQuad(Ints.concat(
					vertexToInts(posTo.x / 16f, posFrom.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 5, maxV),
					vertexToInts(posTo.x / 16f, posFrom.y / 16f, posFrom.z / 16f - 0.3125f, 0xFFFFFFFF, bark, 0, maxV),
					vertexToInts(posTo.x / 16f, posTo.y / 16f, posFrom.z / 16f - 0.3125f, 0xFFFFFFFF, bark, 0, minV),
					vertexToInts(posTo.x / 16f, posTo.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 5, minV)
			), 0, EnumFacing.EAST, bark, true, DefaultVertexFormats.BLOCK));
			
			builder.addFaceQuad(EnumFacing.EAST, new BakedQuad(Ints.concat(
					vertexToInts(posTo.x / 16f + 0.3125f, posFrom.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 16, maxV),
					vertexToInts(posTo.x / 16f, posFrom.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 11, maxV),
					vertexToInts(posTo.x / 16f, posTo.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 11, minV),
					vertexToInts(posTo.x / 16f + 0.3125f, posTo.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 16, minV)
			), 0, EnumFacing.NORTH, bark, true, DefaultVertexFormats.BLOCK));
			builder.addFaceQuad(EnumFacing.WEST, new BakedQuad(Ints.concat(
					vertexToInts(posFrom.x / 16f, posFrom.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 5, maxV),
					vertexToInts(posFrom.x / 16f - 0.3125f, posFrom.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 0, maxV),
					vertexToInts(posFrom.x / 16f - 0.3125f, posTo.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 0, minV),
					vertexToInts(posFrom.x / 16f, posTo.y / 16f, posFrom.z / 16f, 0xFFFFFFFF, bark, 5, minV)
			), 0, EnumFacing.NORTH, bark, true, DefaultVertexFormats.BLOCK));
			builder.addFaceQuad(EnumFacing.EAST, new BakedQuad(Ints.concat(
					vertexToInts(posTo.x / 16f + 0.3125f, posTo.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 16, minV),
					vertexToInts(posTo.x / 16f, posTo.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 11, minV),
					vertexToInts(posTo.x / 16f, posFrom.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 11, maxV),
					vertexToInts(posTo.x / 16f + 0.3125f, posFrom.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 16, maxV)
			), 0, EnumFacing.SOUTH, bark, true, DefaultVertexFormats.BLOCK));
			builder.addFaceQuad(EnumFacing.WEST, new BakedQuad(Ints.concat(
					vertexToInts(posFrom.x / 16f, posTo.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 5, minV),
					vertexToInts(posFrom.x / 16f - 0.3125f, posTo.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 0, minV),
					vertexToInts(posFrom.x / 16f - 0.3125f, posFrom.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 0, maxV),
					vertexToInts(posFrom.x / 16f, posFrom.y / 16f, posTo.z / 16f, 0xFFFFFFFF, bark, 5, maxV)
			), 0, EnumFacing.SOUTH, bark, true, DefaultVertexFormats.BLOCK));
			
		return builder.makeBakedModel();
	}

	/**
	 * A Hack to determine the UV face angle for a block column on a certain axis
	 * 
	 * @param axis
	 * @param face
	 * @return
	 */
	public int getFaceAngle (Axis axis, EnumFacing face) {
		if(axis == Axis.Y) { //UP / DOWN
			return 0;
		}
		else if(axis == Axis.Z) {//NORTH / SOUTH
			switch(face) {
				case UP: return 0;
				case WEST: return 270;
				case DOWN: return 180;
				default: return 90;
			}
		} else { //EAST/WEST
			return (face == EnumFacing.NORTH) ? 270 : 90;
		}
	}
	
	protected BakedQuad makeBakedQuad(BlockPart blockPart, BlockPartFace partFace, TextureAtlasSprite atlasSprite, EnumFacing dir, net.minecraftforge.common.model.ITransformation transform, boolean uvlocked) {
		return new FaceBakery().makeBakedQuad(blockPart.positionFrom, blockPart.positionTo, partFace, atlasSprite, dir, transform, blockPart.partRotation, uvlocked, blockPart.shade);
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
		
		IExtendedBlockState extendedBlockState = (IExtendedBlockState)blockState;
		if (blockState instanceof IExtendedBlockState && blockState.getBlock() instanceof BlockBranch) {
			int coreRadius = MathHelper.clamp(((BlockBranch) blockState.getBlock()).getRadius(blockState), 1, 3);
			int[] connections = pollConnections(coreRadius, extendedBlockState);
			
			//Count number of connections
			int numConnections = 0;
			for (int i : connections) {
				numConnections += (i != 0) ? 1 : 0;
			}
			
			//The source direction is the biggest connection from one of the 6 directions
			EnumFacing sourceDir = getSourceDir(coreRadius, connections);
			if(sourceDir == null) {
				sourceDir = EnumFacing.DOWN;
			}
			int coreDir = resolveCoreDir(sourceDir);
			
			//This is for drawing the rings on a terminating branch
			EnumFacing coreRingDir = (numConnections == 1) ? sourceDir.getOpposite() : null;
			
			// Get quads for core model
			if (side == null || coreRadius != connections[side.getIndex()]) {
				if (coreRingDir == null || coreRingDir != side) {
					quadsList.addAll(cores[coreDir][coreRadius - 1].getQuads(blockState, side, rand));
				} else {
					quadsList.addAll(rings[coreRadius - 1].getQuads(blockState, side, rand));
				}
			}
			// Get quads for core leaves
			for (EnumFacing dir : EnumFacing.values()) {
				if (coreRadius > connections[dir.getIndex()]) {
					for (BakedQuad quad : coreLeaves[coreRadius - 1].getQuads(blockState, dir, rand)) {
						if (coreRadius > connections[quad.getFace().getIndex()]) {
							quadsList.add(quad);
						}
					}
				}
			}
			// Get quads for sleeves models
			for (EnumFacing connDir : EnumFacing.VALUES) {
				int idx = connDir.getIndex();
				int connRadius = connections[idx];
				// If the connection side matches the quadpull side then cull the sleeve face.  Don't cull radius 1 connections for leaves(which are partly transparent).
				if (connRadius > 0 && (connRadius == 1 || side != connDir)) {
					quadsList.addAll(sleeves[idx][connRadius - 1].getQuads(extendedBlockState, side, rand));
				}
			}
		}
		
		return quadsList;
	}

	/**
	 * Checks all neighboring tree parts to determine the connection radius for each side of this branch block.
	 * 
	 * @param coreRadius the radius of this block
	 * @param extendedBlockState
	 * @return an array of 6 integers, one for the radius of each connecting side. DUNSWE.
	 */
	protected int[] pollConnections(int coreRadius, IExtendedBlockState extendedBlockState) {
		int[] connections = new int[6];
		for(EnumFacing dir: EnumFacing.VALUES) {
			int connection = getConnectionRadius(extendedBlockState, BlockBranch.CONNECTIONS[dir.getIndex()]);
			connections[dir.getIndex()] = MathHelper.clamp(connection, 0, coreRadius);//Do not allow connections to exceed core radius
		}
		return connections;
	}

	/**
	 * Locates the side with the largest neighbor radius that's equal to or greater than this branch block
	 * 
	 * @param coreRadius
	 * @param connections an array of 6 integers, one for the radius of each connecting side. DUNSWE.
	 * @return
	 */
	protected EnumFacing getSourceDir(int coreRadius, int[] connections) {
		int largestConnection = 0;
		EnumFacing sourceDir = null;
		
		for(EnumFacing dir: EnumFacing.VALUES){
			int connRadius = connections[dir.getIndex()];
			if(connRadius > largestConnection){
				largestConnection = connRadius;
				sourceDir = dir;
			}
		}
		
		if(largestConnection < coreRadius){
			sourceDir = null;//Has no source node
		}
		return sourceDir;
	}
	
	/**
	 * Converts direction DUNSWE to 3 axis numbers for Y,Z,X
	 * 
	 * @param dir
	 * @return
	 */
	protected int resolveCoreDir(EnumFacing dir) {
		return dir.getIndex() >> 1;
	}
	
	/**
	 * Get the connection radius of a direction from the ExtendedBlockState
	 * 
	 * @param iExtendedBlockState
	 * @param whichConnection
	 * @return
	 */
	protected int getConnectionRadius(IExtendedBlockState iExtendedBlockState, IUnlistedProperty<Integer> whichConnection) {
		Integer connection = iExtendedBlockState.getValue(whichConnection);
		return connection != null ? connection.intValue() : 0;
	}
	
	@Override
	public boolean isAmbientOcclusion() {
		return true;
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
		return sleeves[0][0].getItemCameraTransforms();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return null;
	}

}
