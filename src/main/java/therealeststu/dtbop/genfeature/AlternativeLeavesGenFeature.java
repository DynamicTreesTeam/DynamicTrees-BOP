package therealeststu.dtbop.genfeature;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.block.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGrowContext;
import com.ferreusveritas.dynamictrees.systems.nodemapper.FindEndsNode;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictrees.util.BlockBounds;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AlternativeLeavesGenFeature extends GenFeature {

    public static final ConfigurationProperty<Block> ALT_LEAVES = ConfigurationProperty.block("alternative_leaves");

    public AlternativeLeavesGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        this.register(ALT_LEAVES, PLACE_CHANCE, QUANTITY);
    }

    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(ALT_LEAVES, Blocks.AIR)
                .with(PLACE_CHANCE, 0.5f)
                .with(QUANTITY, 5);
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        BlockBounds bounds =
                context.species().getFamily().expandLeavesBlockBounds(new BlockBounds(context.endPoints()));

        return setAltLeaves(configuration, context.level(), bounds, context.bounds(), context.species());
    }

    @Override
    protected boolean postGrow(GenFeatureConfiguration configuration, PostGrowContext context) {
        if (context.fertility() == 0) {
            return false;
        }

        final LevelAccessor level = context.level();
        final BlockPos rootPos = context.pos();
        final Species species = context.species();

        FindEndsNode endFinder = new FindEndsNode();
        TreeHelper.startAnalysisFromRoot(level, rootPos, new MapSignal(endFinder));
        List<BlockPos> endPoints = endFinder.getEnds();
        if (endPoints.isEmpty()) {
            return false;
        }
        BlockPos chosenEndPoint = endPoints.get(level.getRandom().nextInt(endPoints.size()));
        BlockBounds bounds = species.getFamily().expandLeavesBlockBounds(new BlockBounds(chosenEndPoint));

        return setAltLeaves(configuration, level, bounds, SafeChunkBounds.ANY, species);
    }

    private boolean setAltLeaves(GenFeatureConfiguration configuration, LevelAccessor level, BlockBounds leafPositions,
                                 SafeChunkBounds safeBounds, Species species) {
        boolean worldGen = safeBounds != SafeChunkBounds.ANY;

        if (worldGen) {
            AtomicBoolean isSet = new AtomicBoolean(false);
            leafPositions.iterator().forEachRemaining((pos) -> {
                if (safeBounds.inBounds(pos, true) && level.getRandom().nextFloat() < configuration.get(PLACE_CHANCE)) {
                    if (level.setBlock(pos,
                            getSwapBlockState(configuration, level, species, level.getBlockState(pos), true), 2)) {
                        isSet.set(true);
                    }
                }
            });
            return isSet.get();
        } else {
            boolean isSet = false;
            List<BlockPos> posList = new LinkedList<>();
            for (BlockPos leafPosition : leafPositions) {
                posList.add(new BlockPos(leafPosition));
            }
            if (posList.isEmpty()) {
                return false;
            }
            for (int i = 0; i < configuration.get(QUANTITY); i++) {
                BlockPos pos = posList.get(level.getRandom().nextInt(posList.size()));
                if (level.setBlock(pos,
                        getSwapBlockState(configuration, level, species, level.getBlockState(pos), false), 2)) {
                    isSet = true;
                }
            }
            return isSet;
        }
    }

    private BlockState getSwapBlockState(GenFeatureConfiguration configuration, LevelAccessor level, Species species,
                                         BlockState state, boolean worldgen) {
        DynamicLeavesBlock originalLeaves = species.getLeavesBlock().orElse(null);
        Block alt = configuration.get(ALT_LEAVES);
        DynamicLeavesBlock altLeaves = alt instanceof DynamicLeavesBlock ? (DynamicLeavesBlock) alt : null;
        if (originalLeaves != null && altLeaves != null) {
            if (worldgen || level.getRandom().nextFloat() < configuration.get(PLACE_CHANCE)) {
                if (state.getBlock() == originalLeaves) {
                    return altLeaves.properties.getDynamicLeavesState(state.getValue(LeavesBlock.DISTANCE));
                }
            } else {
                if (state.getBlock() == altLeaves) {
                    return originalLeaves.properties.getDynamicLeavesState(state.getValue(LeavesBlock.DISTANCE));
                }
            }
        }
        return state;
    }
}
