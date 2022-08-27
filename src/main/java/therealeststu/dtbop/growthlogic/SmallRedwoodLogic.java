package therealeststu.dtbop.growthlogic;

import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.PositionalSpeciesContext;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SmallRedwoodLogic extends ConiferLogic {

    public SmallRedwoodLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected GrowthLogicKitConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(ENERGY_DIVISOR, 5.0F);
    }

    @Override
    public float getEnergy(GrowthLogicKitConfiguration configuration, PositionalSpeciesContext context) {
        return configuration.getLowestBranchHeight(context) + context.species().getSignalEnergy() +
                getHashedVariation(context.world(), context.pos(), 8);
    }

    @Override
    public int getLowestBranchHeight(GrowthLogicKitConfiguration configuration, PositionalSpeciesContext context) {
        // Vary the minimum branch height by a psuedorandom hash function
        return (int) (super.getLowestBranchHeight(configuration, context) +
                getHashedVariation(context.world(), context.pos(), 11));
    }

    private float getHashedVariation(World world, BlockPos pos, int mod) {
        long day = world.getGameTime() / 24000L;
        int month = (int) day / 30;//Change the hashs every in-game month
        return (CoordUtils.coordHashCode(pos.above(month), 2) %
                mod);//Vary the height energy by a psuedorandom hash function
    }

}
