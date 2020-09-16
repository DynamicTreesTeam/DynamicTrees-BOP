package dynamictreesbop.models;

import com.ferreusveritas.dynamictrees.models.loaders.ModelLoaderGeneric;

import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;

public class ModelLoaderBlockPalmFronds extends ModelLoaderGeneric {
	
	public ModelLoaderBlockPalmFronds() {
		super("dynamicpalmfronds", new ResourceLocation("dynamictreesbop", "block/dynamicpalmfronds"));
	}
	
	@Override
	protected IModel loadModel(ResourceLocation resourceLocation, ModelBlock baseModelBlock) {
		return new ModelBlockPalmFronds(baseModelBlock);
	}
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	
}
