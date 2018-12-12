package dynamictreesbop.models;

import java.util.function.Function;

import com.ferreusveritas.dynamictrees.models.blockmodels.ModelBlockBranchBasic;
import com.ferreusveritas.dynamictrees.models.loaders.ModelLoaderGeneric;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;

public class ModelLoaderBlockBranchBamboo extends ModelLoaderGeneric {
	
	public ModelLoaderBlockBranchBamboo() {
		super("dynamicbamboo", new ResourceLocation("dynamictrees", "block/smartmodel/branch"));
	}
	
	@Override
	protected IModel loadModel(ResourceLocation resourceLocation, ModelBlock baseModelBlock) {
		return new ModelBlockBranchBasic(baseModelBlock) {
			@Override
			public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
				try {
					return new BakedModelBlockBranchBamboo(barkTexture, ringsTexture, bakedTextureGetter);
				} catch (Exception exception) {
					System.err.println("BambooModel.bake() failed due to exception:" + exception);
					return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
				}
			}
		};
	}
	
}
