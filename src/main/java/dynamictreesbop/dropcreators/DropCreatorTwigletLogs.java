package dynamictreesbop.dropcreators;

import java.util.List;
import java.util.Random;

import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreator;
import com.ferreusveritas.dynamictrees.trees.Species;

import dynamictreesbop.DynamicTreesBOP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DropCreatorTwigletLogs extends DropCreator {
	
	public DropCreatorTwigletLogs() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "twigletlogs"));
	}

	@Override
	public List<ItemStack> getLogsDrop(World world, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, int volume) {
		dropList.add(species.getTree().getPrimitiveLogItemStack(volume / 256));
		dropList.add(species.getTree().getStick((volume % 256) / 64));
		return dropList;
	}
	
}
