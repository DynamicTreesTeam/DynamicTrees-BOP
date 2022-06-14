package therealeststu.dtbop.trees;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.resources.ResourceLocation;
import therealeststu.dtbop.items.MapleSeed;

public class MapleSpecies extends Species {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(MapleSpecies::new);

    public MapleSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);
    }

    @Override
    public Species generateSeed() {
        this.setSeed(RegistryHandler.addItem(this.getSeedName(),()-> new MapleSeed(this)));
        return this;
    }
}
