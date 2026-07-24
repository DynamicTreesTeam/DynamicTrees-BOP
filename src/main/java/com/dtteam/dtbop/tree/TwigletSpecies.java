package com.dtteam.dtbop.tree;

import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.systems.nodemapper.NetVolumeNode;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.resources.Identifier;

public class TwigletSpecies extends Species {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(TwigletSpecies::new);

    public TwigletSpecies(Identifier name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);
        setBasicGrowingParameters(0.3f, 2.5f, 1, 2, 1.0f);
    }

    @Override
    public void processVolume(NetVolumeNode.Volume volume) {
        volume.addVolume(NetVolumeNode.Volume.VOXELSPERLOG);
        super.processVolume(volume);
    }

    @Override
    public LogsAndSticks getLogsAndSticks(NetVolumeNode.Volume volume, boolean silkTouch, int fortuneLevel) {
        NetVolumeNode.Volume modifiedVolume = new NetVolumeNode.Volume(volume.getRawVolumesArray());
        modifiedVolume.addVolume(NetVolumeNode.Volume.VOXELSPERLOG);
        return super.getLogsAndSticks(modifiedVolume, silkTouch, fortuneLevel);
    }
}
