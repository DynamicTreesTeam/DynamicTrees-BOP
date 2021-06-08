package therealeststu.dtbop.dropcreators;

import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreator;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class StringDropCreator extends DropCreator {

    private final float rarity;

    public StringDropCreator(float rarity) {
        super(new ResourceLocation("dynamictrees", "string_drop"));
        this.rarity = rarity;
    }

    public List<ItemStack> getHarvestDrop(World world, Species species, BlockPos leafPos, Random random, List<ItemStack> dropList, int fertility, int fortune) {
        int chance = (int) (10 * rarity);
        if (fortune > 0) {
            chance -= 2 << fortune;
            if (chance < 10) {
                chance = (int)(5 * rarity);
            }
        }

        if (random.nextInt(chance) == 0) {
            ItemStack drop = new ItemStack(Items.STRING);
            dropList.add(drop);
        }

        return dropList;
    }

    @Override
    public List<ItemStack> getLeavesDrop(World access, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, int fortune) {

        if (access.getRandom().nextFloat() < rarity)
            dropList.add(new ItemStack(Items.STRING));

        return dropList;
    }
}
