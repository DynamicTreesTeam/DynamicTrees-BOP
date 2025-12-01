package com.dtteam.dtbop.block;

import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictreesplus.block.mushroom.CapProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class GlowShroomCapProperties extends CapProperties {

    public static final TypedRegistry.EntryType<CapProperties> TYPE = TypedRegistry.newType(GlowShroomCapProperties::new);

    public GlowShroomCapProperties(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public BlockBehaviour.Properties getDefaultBlockProperties() {
        return super.getDefaultBlockProperties()
                .emissiveRendering((a,b,c)->true)
                .hasPostProcess((a,b,c)->true);
    }
}
