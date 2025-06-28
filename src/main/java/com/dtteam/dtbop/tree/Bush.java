package com.dtteam.dtbop.tree;

import com.dtteam.dynamictrees.systems.genfeature.BushGenFeature;
import com.dtteam.dynamictrees.systems.genfeature.GenFeatures;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.registries.BuiltInRegistries;
import com.dtteam.dtbop.DynamicTreesBOP;

import java.util.LinkedList;
import java.util.List;

public class Bush extends Species {

    public static List<Bush> INSTANCES = new LinkedList<>();

    ResourceLocation log, leaves, altLeaves;

    public Bush(String name, ResourceLocation log, ResourceLocation leaves) {
        this(name, log, leaves, null);
    }

    public Bush(String name, ResourceLocation log, ResourceLocation leaves, ResourceLocation altLeaves) {
        this.setRegistryName(ResourceLocation.fromNamespaceAndPath(DynamicTreesBOP.MOD_ID, name));
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.setStandardSoils();
        this.log = log;
        this.leaves = leaves;
        this.altLeaves = altLeaves;

        INSTANCES.add(this);
    }

    public void setup() {
        Block logBlock = BuiltInRegistries.BLOCK.get(log);
        Block leavesBlock = BuiltInRegistries.BLOCK.get(leaves);
        Block altLeavesBlock = null;
        if (altLeaves != null) {
            Block altLeafBlock = BuiltInRegistries.BLOCK.get(altLeaves);
            if (altLeafBlock != Blocks.AIR) altLeavesBlock = altLeafBlock;
        }
        this.addGenFeature(GenFeatures.BUSH.with(BushGenFeature.LOG, logBlock)
                .with(BushGenFeature.LEAVES, leavesBlock).with(BushGenFeature.SECONDARY_LEAVES, altLeavesBlock));
    }

}
