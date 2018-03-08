package dynamictreesbop.models;

import com.ferreusveritas.dynamictrees.models.ModelLoaderGeneric;

import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;

public class ModelLoaderPalmFronds extends ModelLoaderGeneric {
	
	public ModelLoaderPalmFronds() {
		super("dynamicpalmfronds", new ResourceLocation("dynamictrees", "block/smartmodel/branch"));
	}
	
	@Override
	protected IModel loadModel(ResourceLocation resourceLocation, ModelBlock baseModelBlock) {
		return new PalmFrondsModel(baseModelBlock);
	}
	
}
