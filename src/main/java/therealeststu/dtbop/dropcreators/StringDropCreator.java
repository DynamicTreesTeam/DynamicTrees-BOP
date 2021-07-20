package therealeststu.dtbop.dropcreators;

import com.ferreusveritas.dynamictrees.systems.dropcreators.ConfiguredDropCreator;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreator;
import com.ferreusveritas.dynamictrees.systems.dropcreators.context.DropContext;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class StringDropCreator extends DropCreator {

    public StringDropCreator(ResourceLocation name) {
        super(name);
    }

    @Override
    protected ConfiguredDropCreator<DropCreator> createDefaultConfiguration() {
        return super.createDefaultConfiguration().with(RARITY, 1f);
    }

    @Override
    protected void registerProperties() {
        this.register(RARITY);
    }

    @Override
    public void appendHarvestDrops(ConfiguredDropCreator<DropCreator> configuration, DropContext context) {
        int chance = (int) (10 * configuration.get(RARITY));
        if (context.fortune() > 0) {
            chance -= 2 << context.fortune();
            if (chance < 10) {
                chance = (int)(5 * configuration.get(RARITY));
            }
        }

        if (context.random().nextInt(chance) == 0) {
            ItemStack drop = new ItemStack(Items.STRING);
            context.drops().add(drop);
        }
    }

    @Override
    public void appendLeavesDrops(ConfiguredDropCreator<DropCreator> configuration, DropContext context) {
        if (context.world().getRandom().nextFloat() < configuration.get(RARITY))
            context.drops().add(new ItemStack(Items.STRING));
    }

}
