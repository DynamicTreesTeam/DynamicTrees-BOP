package dynamictreesbop.featuregen;

import com.ferreusveritas.dynamictrees.api.IFullGenFeature;
import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class FeatureGenBrushBush implements IFullGenFeature, IPostGenFeature {

    private Predicate<Biome> biomePredicate = (i) -> true;
    private IBlockState state;
    protected int generationAttempts = 16;

    public FeatureGenBrushBush setBlockState (IBlockState state){
        this.state = state;
        return this;
    }

    public FeatureGenBrushBush setBiomePredicate(Predicate<Biome> biomePredicate) {
        this.biomePredicate = biomePredicate;
        return this;
    }

    public boolean generate(World world, BlockPos rootPos, Species species, Biome biome, Random random, int radius, SafeChunkBounds safeBounds) {
        this.commonGen(world, rootPos, species, random, radius, safeBounds);
        return true;
    }

    public boolean postGeneration(World world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
        if (safeBounds != SafeChunkBounds.ANY && this.biomePredicate.test(biome)) {
            this.commonGen(world, rootPos, species, world.rand, radius, safeBounds);
            return true;
        } else
            return false;
    }

    protected void commonGen(World world, BlockPos rootPos, Species species, Random random, int radius, SafeChunkBounds safeBounds) {
        Block block = state.getBlock();

        int rad = 12;

        int yOff = 4;

        for (int i = 0; i < this.generationAttempts; ++i) {
            int x = rootPos.getX() + rad - random.nextInt(rad*2 +1);
            int z = rootPos.getZ() + rad - random.nextInt(rad*2 +1);
            BlockPos.MutableBlockPos genPos = new BlockPos.MutableBlockPos(x, rootPos.getY() + 1 - yOff, z);

            if (!safeBounds.inBounds(genPos, true)) continue;

            for (int y = 0; y<=yOff*2; y++){
                if (world.isAirBlock(genPos)) break;
                genPos.move(EnumFacing.UP);
            }

            if (species.isAcceptableSoil(world, genPos.down(), world.getBlockState(genPos.down())) && world.isAirBlock(genPos) && !world.isOutsideBuildHeight(genPos)) {
                if (block.canPlaceBlockAt(world, genPos))
                    world.setBlockState(genPos, state, 2);
            }
        }
    }

}
