package therealeststu.dtbop.trees;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NetVolumeNode;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.species.PalmSpecies;
import net.minecraft.util.ResourceLocation;

public class TwigletSpecies extends Species {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(TwigletSpecies::new);

    public TwigletSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);
        setBasicGrowingParameters(0.3f, 2.5f, 1, 2, 1.0f);
    }

    @Override
    protected void processVolume(NetVolumeNode.Volume volume) {
        volume.addVolume(NetVolumeNode.Volume.VOXELSPERLOG);
        super.processVolume(volume);
    }

    @Override
    public LogsAndSticks getLogsAndSticks(NetVolumeNode.Volume volume) {
        NetVolumeNode.Volume modifiedVolume = new NetVolumeNode.Volume(volume.getRawVolumesArray());
        modifiedVolume.addVolume(NetVolumeNode.Volume.VOXELSPERLOG);
        return super.getLogsAndSticks(modifiedVolume);
    }
}