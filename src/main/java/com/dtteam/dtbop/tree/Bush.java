package com.dtteam.dtbop.tree;

import com.dtteam.dynamictrees.systems.genfeature.BushGenFeature;
import com.dtteam.dynamictrees.systems.genfeature.GenFeatures;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.registries.BuiltInRegistries;
import com.dtteam.dtbop.DynamicTreesBOP;

import java.util.LinkedList;
import java.util.List;

public class Bush extends Species {

    public static List<Bush> INSTANCES = new LinkedList<>();

    Identifier log, leaves, altLeaves;

    public Bush(String name, Identifier log, Identifier leaves) {
        this(name, log, leaves, null);
    }

    public Bush(String name, Identifier log, Identifier leaves, Identifier altLeaves) {
        this.setRegistryName(Identifier.fromNamespaceAndPath(DynamicTreesBOP.MOD_ID, name));
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.setStandardSoils();
        this.log = log;
        this.leaves = leaves;
        this.altLeaves = altLeaves;

        INSTANCES.add(this);
    }

    public void setup() {
        Block logBlock = BuiltInRegistries.BLOCK.get(log).map(holder -> holder.value()).orElse(Blocks.AIR);
        Block leavesBlock = BuiltInRegistries.BLOCK.get(leaves).map(holder -> holder.value()).orElse(Blocks.AIR);
        Block altLeavesBlock = null;
        if (altLeaves != null) {
            Block altLeafBlock = BuiltInRegistries.BLOCK.get(altLeaves).map(holder -> holder.value()).orElse(Blocks.AIR);
            if (altLeafBlock != Blocks.AIR) altLeavesBlock = altLeafBlock;
        }
        this.addGenFeature(GenFeatures.BUSH.with(BushGenFeature.LOG, logBlock)
                .with(BushGenFeature.LEAVES, leavesBlock).with(BushGenFeature.SECONDARY_LEAVES, altLeavesBlock));
    }

}
