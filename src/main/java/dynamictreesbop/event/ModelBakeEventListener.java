package dynamictreesbop.event;

import dynamictreesbop.ModContent;
import dynamictreesbop.models.PalmFrondsCompositeModel;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModelBakeEventListener {
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		
		for(int hydro = 1; hydro <= 2; hydro++) {
			ModelResourceLocation resourceLocation = new ModelResourceLocation(ModContent.palmLeavesProperties.getDynamicLeavesState().getBlock().getRegistryName(), "hydro=" + hydro);

			Object palmLeavesObject =  event.getModelRegistry().getObject(resourceLocation);
			if (palmLeavesObject instanceof IBakedModel) {
				IBakedModel baseModel = (IBakedModel) palmLeavesObject;
				PalmFrondsCompositeModel compositeModel = new PalmFrondsCompositeModel(baseModel);
				event.getModelRegistry().putObject(resourceLocation, compositeModel);
			}
		}
	}
	
}