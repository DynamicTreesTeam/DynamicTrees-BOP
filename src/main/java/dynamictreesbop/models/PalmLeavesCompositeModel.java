package dynamictreesbop.models;

import java.util.ArrayList;
import java.util.List;

import dynamictreesbop.blocks.BlockDynamicLeavesPalm;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

public class PalmLeavesCompositeModel implements IBakedModel {
	
	protected IBakedModel rootsModel;
	
	public PalmLeavesCompositeModel(IBakedModel rootsModel) {
		this.rootsModel = rootsModel;
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {		
		
		boolean connections[] = new boolean[8];
		
		if (state != null && state.getBlock() instanceof BlockDynamicLeavesPalm && state instanceof IExtendedBlockState) {			
			IExtendedBlockState extendedBlockState = (IExtendedBlockState)state;

			for(int i = 0; i < 8; i++) {
				Boolean b = extendedBlockState.getValue(BlockDynamicLeavesPalm.CONNECTIONS[i]);
				connections[i] = b != null ? b.booleanValue() : false;
			}
			
		} else {
			return new ArrayList<BakedQuad>();
		}
			
		Minecraft mc = Minecraft.getMinecraft();
		BlockRendererDispatcher blockRendererDispatcher = mc.getBlockRendererDispatcher();
		BlockModelShapes blockModelShapes = blockRendererDispatcher.getBlockModelShapes();
		
		List<BakedQuad> quads = new ArrayList<BakedQuad>();
		
		ModelBlock modelBlock = new ModelBlock(null, null, null, false, false, ItemCameraTransforms.DEFAULT, null);

		
		int i = 0;
		for(boolean conn : connections) {
			//IBlockState mimicState = Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.values()[i]);
			//IBakedModel mimicModel = blockModelShapes.getModelForState(mimicState);
			//quads.addAll(mimicModel.getQuads(mimicState, side, rand));
			
			//SimpleBakedModel.Builder builder = new SimpleBakedModel.Builder(modelBlock, ItemOverrideList.NONE).setTexture(bark);

			
			i++;
		}
		
		quads.addAll(rootsModel.getQuads(state, side, rand));
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
		return rootsModel.getParticleTexture();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return null;
	}

}
