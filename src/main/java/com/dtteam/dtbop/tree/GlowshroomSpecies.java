package com.dtteam.dtbop.tree;

import com.dtteam.dynamictrees.api.registry.RegistryHandler;
import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.block.sapling.DynamicSaplingBlock;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import com.dtteam.dynamictreesplus.block.mushroom.CapProperties;
import com.dtteam.dynamictreesplus.tree.HugeMushroomSpecies;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class GlowshroomSpecies extends HugeMushroomSpecies {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultMushroomType(GlowshroomSpecies::new);

    public GlowshroomSpecies(ResourceLocation name, Family family, CapProperties capProperties) {
        super(name, family, capProperties);
    }

    public Species generateSapling() {
        return !this.shouldGenerateSapling() || this.saplingBlock != null ? this :
                this.setSapling(RegistryHandler.addBlock(this.getSaplingRegName(), () -> new DynamicSaplingBlock(this){
                    @Override
                    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
                        return 6;
                    }

                }));
    }

}
