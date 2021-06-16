package therealeststu.dtbop.growthlogic;

import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NoteBlock;
import net.minecraft.state.properties.NoteBlockInstrument;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SmallRedwoodLogic extends ConiferLogic {

    public SmallRedwoodLogic(ResourceLocation registryName) {
        super(registryName, 5.0f);
    }

    private float getHashedVariation (World world, BlockPos pos, int mod){
        long day = world.getGameTime() / 24000L;
        int month = (int)day / 30;//Change the hashs every in-game month
        return (CoordUtils.coordHashCode(pos.above(month), 2) % mod);//Vary the height energy by a psuedorandom hash function
    }

    @Override
    public float getEnergy(World world, BlockPos rootPos, Species species, float signalEnergy) {
        return getLowestBranchHeight(world, rootPos, species, species.getLowestBranchHeight()) + signalEnergy + getHashedVariation(world, rootPos, 8);

    }

    @Override
    public int getLowestBranchHeight(World world, BlockPos pos, Species species, int lowestBranchHeight) {
        return (int) (lowestBranchHeight + getHashedVariation(world, pos, 11)); // Vary the minimum branch height by a psuedorandom hash function
    }
}
