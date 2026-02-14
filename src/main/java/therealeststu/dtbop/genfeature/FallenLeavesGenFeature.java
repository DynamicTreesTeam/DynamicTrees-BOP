package therealeststu.dtbop.genfeature;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.block.branch.BranchBlock;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGrowContext;
import com.ferreusveritas.dynamictrees.systems.nodemapper.FindEndsNode;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class FallenLeavesGenFeature extends GenFeature {

    public static final ConfigurationProperty<Block> BLOCK = ConfigurationProperty.block("block");

    public FallenLeavesGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        this.register(BLOCK, PLACE_CHANCE, QUANTITY);
    }

    @Override
    protected GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(BLOCK, Blocks.AIR)
                .with(PLACE_CHANCE, 0.5f)
                .with(QUANTITY, 5);
    }

    // Méthode simplifiée car on a directement le Block
    public BlockState getBasicLeafBlock(GenFeatureConfiguration configuration) {
        return configuration.get(BLOCK).defaultBlockState();
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        final LevelAccessor world = context.level();
        final RandomSource random = context.random();
        final float placeChance = configuration.get(PLACE_CHANCE);

        if (random.nextFloat() >= placeChance) {
            return false;
        }

        final FindEndsNode endFinder = new FindEndsNode();
        TreeHelper.startAnalysisFromRoot(world, context.pos(), new MapSignal(endFinder));
        final List<BlockPos> endPoints = endFinder.getEnds();

        if (endPoints.isEmpty()) {
            return false;
        }

        final int quantity = configuration.get(QUANTITY);
        int processedCount = 0;

        for (BlockPos pos : endPoints) {
            if (processedCount >= quantity) {
                break;
            }

            int x = pos.getX() + random.nextInt(5) - 2;
            int z = pos.getZ() + random.nextInt(5) - 2;

            for (int i = 0; i < 32; i++) {
                BlockPos offPos = new BlockPos(x, pos.getY() - 1 - i, z);

                if (!world.isEmptyBlock(offPos)) {
                    Block block = world.getBlockState(offPos).getBlock();

                    if (block instanceof BranchBlock || block instanceof MushroomBlock || block instanceof LeavesBlock) {
                        continue;
                    } else if (block != Blocks.AIR) {
                        testAir(world, offPos, configuration, random, placeChance);
                        testAir(world, offPos.east(1), configuration, random, placeChance);
                        testAir(world, offPos.east(1).north(1), configuration, random, placeChance);
                        testAir(world, offPos.east(1).south(1), configuration, random, placeChance);
                        testAir(world, offPos.west(1), configuration, random, placeChance);
                        testAir(world, offPos.west(1).north(1), configuration, random, placeChance);
                        testAir(world, offPos.west(1).south(1), configuration, random, placeChance);
                        testAir(world, offPos.south(1), configuration, random, placeChance);
                        testAir(world, offPos.north(1), configuration, random, placeChance);
                        processedCount++;
                    }
                    break;
                }
            }
        }
        return processedCount > 0;
    }

    @Override
    protected boolean postGrow(GenFeatureConfiguration configuration, PostGrowContext context) {
        final LevelAccessor world = context.level();
        final RandomSource random = context.random();
        final float placeChance = configuration.get(PLACE_CHANCE);

        if (random.nextFloat() >= placeChance) {
            return false;
        }

        final FindEndsNode endFinder = new FindEndsNode();
        TreeHelper.startAnalysisFromRoot(world, context.pos(), new MapSignal(endFinder));
        final List<BlockPos> endPoints = endFinder.getEnds();

        if (endPoints.isEmpty()) {
            return false;
        }

        final BlockPos pos = endPoints.get(random.nextInt(endPoints.size()));
        int x = pos.getX() + random.nextInt(5) - 2;
        int z = pos.getZ() + random.nextInt(5) - 2;

        for (int i = 0; i < 32; i++) {
            BlockPos offPos = new BlockPos(x, pos.getY() - 1 - i, z);

            if (!world.isEmptyBlock(offPos)) {
                Block block = world.getBlockState(offPos).getBlock();

                if (block instanceof BranchBlock || block instanceof MushroomBlock || block instanceof LeavesBlock) {
                    continue;
                } else if (block != Blocks.AIR) {
                    testAir(world, offPos, configuration, random, placeChance);
                    testAir(world, offPos.east(1), configuration, random, placeChance);
                    testAir(world, offPos.east(1).north(1), configuration, random, placeChance);
                    testAir(world, offPos.east(1).south(1), configuration, random, placeChance);
                    testAir(world, offPos.west(1), configuration, random, placeChance);
                    testAir(world, offPos.west(1).north(1), configuration, random, placeChance);
                    testAir(world, offPos.west(1).south(1), configuration, random, placeChance);
                    testAir(world, offPos.south(1), configuration, random, placeChance);
                    testAir(world, offPos.north(1), configuration, random, placeChance);
                }
                break;
            }
        }
        return true;
    }

    private void testAir(LevelAccessor world, BlockPos pos, GenFeatureConfiguration configuration,
                         RandomSource random, float placeChance) {
        if (world.getBlockState(pos).getBlock() != Blocks.AIR) {
            pos = pos.above(1);
            if (world.getBlockState(pos).getBlock() instanceof BranchBlock
                    || world.getBlockState(pos.below(1)).getBlock() instanceof BranchBlock) {
                return;
            }

            if (world.getBlockState(pos).getBlock() == Blocks.AIR) {
                if (world.getBlockState(pos.below(1)).isCollisionShapeFullBlock(world, pos.below(1))) {
                    if (random.nextFloat() < placeChance) {
                        world.setBlock(pos, getBasicLeafBlock(configuration), Block.UPDATE_ALL);
                    }
                }
            }
        }
    }
}