package therealeststu.dtbop.dropcreators;

import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreator;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorConfiguration;
import com.ferreusveritas.dynamictrees.systems.dropcreators.context.DropContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.resources.ResourceLocation;

public class StringDropCreator extends DropCreator {

    public StringDropCreator(ResourceLocation name) {
        super(name);
    }

    @Override
    protected DropCreatorConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration().with(RARITY, 1f);
    }

    @Override
    protected void registerProperties() {
        this.register(RARITY);
    }

    @Override
    public void appendHarvestDrops(DropCreatorConfiguration configuration, DropContext context) {
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
    public void appendLeavesDrops(DropCreatorConfiguration configuration, DropContext context) {
        if (context.world().getRandom().nextFloat() < configuration.get(RARITY))
            context.drops().add(new ItemStack(Items.STRING));
    }

}
