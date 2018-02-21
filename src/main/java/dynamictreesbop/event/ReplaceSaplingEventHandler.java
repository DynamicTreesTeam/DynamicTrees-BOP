package dynamictreesbop.event;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CompatHelper;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.common.block.BlockBOPSapling;
import dynamictreesbop.DynamicTreesBOP;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DynamicTreesBOP.MODID)
public class ReplaceSaplingEventHandler {
	
	@SubscribeEvent
	public static void onPlaceSapling(PlaceEvent event) {
		IBlockState state = event.getPlacedBlock();
		
		Species species = null;
		
		if (state.getBlock() == BOPBlocks.sapling_0) {
			int tree = ((BOPTrees) state.getValue(((BlockBOPSapling) state.getBlock()).variantProperty)).ordinal();
			switch (tree) {
			case 0:
				species = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "yellowautumn"));
				break;
			case 1:
				species = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "orangeautumn"));
				break;
			case 3:
				species = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "magic"));
				break;
			case 4:
				species = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, event.getPlayer().getRNG().nextInt(3) == 0 ? "umbran" : "umbranconifer"));
				break;
			case 5:
				species = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "dying"));
				break;
			case 6:
				species = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "fir"));
				break;
			default:
				break;
			}
		} else if (state.getBlock() == BOPBlocks.sapling_1) {
			int tree = ((BOPTrees) state.getValue(((BlockBOPSapling) state.getBlock()).variantProperty)).ordinal();
			switch (tree) {
			case 9:
				species = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "pinkcherry"));
				break;
			case 10:
				species = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "whitecherry"));
				break;
			case 11:
				species = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "maple"));
				break;
			case 12:
				species = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "hellbark"));
				break;
			case 13:	
				species = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "floweringoak"));
				break;
			case 14:	
				species = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "jacaranda"));
				break;
			default:
				break;
			}
		} else if (state.getBlock() == BOPBlocks.sapling_2) {
			int tree = ((BOPTrees) state.getValue(((BlockBOPSapling) state.getBlock()).variantProperty)).ordinal();
			switch (tree) {
			case 17:	
				species = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "palm"));
				break;
			case 19:	
				species = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "willow"));
				break;
			case 20:	
				species = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "pine"));
				break;
			case 21:	
				species = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "mahogany"));
				break;
			default:
				break;
			}
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
