package dynamictreesbop.dropcreators;

import java.util.List;
import java.util.Random;

import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorSeed;
import com.ferreusveritas.dynamictrees.trees.Species;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DropCreatorInvoluntarySeed extends DropCreatorSeed {
	
	@Override
	public List<ItemStack> getVoluntaryDrop(World world, Species species, BlockPos rootPos, Random random, List<ItemStack> dropList, int soilLife) {
		return dropList;
	}
	
}
