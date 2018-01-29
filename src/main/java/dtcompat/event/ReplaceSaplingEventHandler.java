package dtcompat.event;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CompatHelper;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.common.block.BlockBOPSapling;
import dtcompat.DynamicTreesCompat;
import dtcompat.ModContent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DynamicTreesCompat.MODID)
public class ReplaceSaplingEventHandler {
	
	@SubscribeEvent
	public static void onPlaceSapling(PlaceEvent event) {
		IBlockState state = event.getPlacedBlock();
		
		if (state.getBlock() instanceof BlockBOPSapling) {
			int tree = ((BOPTrees) state.getValue(((BlockBOPSapling) state.getBlock()).variantProperty)).ordinal();
			Species species = null;

			switch (tree) {
			case 0:
				species = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesCompat.MODID, "yellowautumn"));
				break;
			default:
				break;
			}
			
			if (species != null) {
				event.getWorld().setBlockToAir(event.getPos());
				if(!species.plantSapling(event.getWorld(), event.getPos())) {
					double x = event.getPos().getX() + 0.5;
					double y = event.getPos().getY() + 0.5;
					double z = event.getPos().getZ() + 0.5;
					EntityItem itemEntity = new EntityItem(event.getWorld(), x, y, z, species.getSeedStack(1));
					CompatHelper.spawnEntity(event.getWorld(), itemEntity);
				}
			}
		}
	}
	
}
