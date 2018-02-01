package dynamictreesbop.dropcreators;

import java.util.List;
import java.util.Random;

import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreator;
import com.ferreusveritas.dynamictrees.trees.Species;

import dynamictreesbop.DynamicTreesBOP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class DropCreatorFruit extends DropCreator {
	
	private final Item fruit;
	private final int fruitMeta;
	
	public DropCreatorFruit(ItemStack fruitStack) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, fruitStack.getItem().getRegistryName().getResourcePath()));
		this.fruit = fruitStack.getItem();
		this.fruitMeta = fruitStack.getMetadata();
	}
	
	public DropCreatorFruit(Item fruit) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, fruit.getRegistryName().getResourcePath()));
		this.fruit = fruit;
		this.fruitMeta = 0;
	}
	
	@Override
	public List<ItemStack> getLeavesDrop(IBlockAccess access, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, int fortune) {
		return getLeafDrops(access, species, breakPos, random, dropList, fortune, 200);
	}
	
	@Override
	public List<ItemStack> getHarvestDrop(World world, Species species, BlockPos leafPos, Random random, List<ItemStack> dropList, int soilLife, int fortune) {
		return getLeafDrops(world, species, leafPos, random, dropList, fortune, 400);
	}
	
	protected List<ItemStack> getLeafDrops(IBlockAccess access, Species species, BlockPos leafPos, Random random, List<ItemStack> dropList, int fortune, int baseChance) {
		int chance = baseChance;
		if (fortune > 0) {
			chance -= 10 << fortune;
			if (chance < 40) {
				chance = 40;
			}
		}
		
		if(random.nextInt(chance) == 0) {
			dropList.add(new ItemStack(fruit, 1, fruitMeta));
		}
		return dropList;
	}

}
