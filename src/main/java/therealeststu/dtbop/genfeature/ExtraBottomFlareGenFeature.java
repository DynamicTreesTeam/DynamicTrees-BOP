package therealeststu.dtbop.genfeature;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.IPostGrowFeature;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.config.ConfiguredGenFeature;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;

public class ExtraBottomFlareGenFeature extends GenFeature implements IPostGenFeature, IPostGrowFeature{

	// Min radius for the flare.
	public static final ConfigurationProperty<Integer> MIN_RADIUS = ConfigurationProperty.integer("min_radius");

	// Min radius to flare further
	public static final ConfigurationProperty<Integer> SECONDARY_MIN_RADIUS = ConfigurationProperty.integer("secondary_min_radius");


	public ExtraBottomFlareGenFeature(ResourceLocation registryName) {
		super(registryName);
	}

	@Override
	protected void registerProperties() {
		this.register(MIN_RADIUS, SECONDARY_MIN_RADIUS);
	}

	@Override
	public ConfiguredGenFeature<GenFeature> createDefaultConfiguration() {
		return super.createDefaultConfiguration().with(MIN_RADIUS, 6).with(SECONDARY_MIN_RADIUS, 8);
	}

	@Override
	public boolean postGrow(ConfiguredGenFeature<?> configuredGenFeature, World world, BlockPos rootPos, BlockPos treePos, Species species, int fertility, boolean natural) {
		if(fertility > 0) {
			this.flareBottom(configuredGenFeature, world, rootPos, species);
			return true;
		}
		return false;
	}

	@Override
	public boolean postGeneration(ConfiguredGenFeature<?> configuredGenFeature, IWorld world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, BlockState initialDirtState, Float seasonValue, Float seasonFruitProductionFactor) {
		this.flareBottom(configuredGenFeature, world, rootPos, species);
		return true;
	}
	
	/**
	 * Put a cute little flare on the bottom of the dark oaks
	 * 
	 * @param world The world
	 * @param rootPos The position of the rooty dirt block of the tree
	 */
	public void flareBottom(ConfiguredGenFeature<?> configuredGenFeature, IWorld world, BlockPos rootPos, Species species) {
		Family family = species.getFamily();

		int radius4 = TreeHelper.getRadius(world, rootPos.above(4));
		if (radius4 > configuredGenFeature.get(SECONDARY_MIN_RADIUS)) {
			family.getBranch().setRadius(world, rootPos.above(3), radius4 + 1, Direction.UP);
			family.getBranch().setRadius(world, rootPos.above(2), radius4 + 3, Direction.UP);
			family.getBranch().setRadius(world, rootPos.above(1), radius4 + 6, Direction.UP);
		} else {
			int radius3 = TreeHelper.getRadius(world, rootPos.above(3));
			if (radius3 > configuredGenFeature.get(MIN_RADIUS)) {
				family.getBranch().setRadius(world, rootPos.above(2), radius3 + 1, Direction.UP);
				family.getBranch().setRadius(world, rootPos.above(1), radius3 + 2, Direction.UP);
			}
		}
	}
	
}
