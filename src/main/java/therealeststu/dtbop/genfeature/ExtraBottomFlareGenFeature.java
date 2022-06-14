package therealeststu.dtbop.genfeature;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeatures.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeatures.context.PostGrowContext;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;

public class ExtraBottomFlareGenFeature extends GenFeature {

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
	public GenFeatureConfiguration createDefaultConfiguration() {
		return super.createDefaultConfiguration().with(MIN_RADIUS, 6).with(SECONDARY_MIN_RADIUS, 8);
	}

	@Override
	protected boolean postGrow(GenFeatureConfiguration configuration, PostGrowContext context) {
		if (context.fertility() > 0) {
			this.flareBottom(configuration, context.world(), context.pos(), context.species());
			return true;
		}
		return false;
	}

	@Override
	protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
		this.flareBottom(configuration, context.world(), context.pos(), context.species());
		return true;
	}

	/**
	 * Put a cute little flare on the bottom of the dark oaks
	 * 
	 * @param world The world
	 * @param rootPos The position of the rooty dirt block of the tree
	 */
	public void flareBottom(GenFeatureConfiguration configuredGenFeature, LevelAccessor world, BlockPos rootPos, Species species) {
		species.getFamily().getBranch().ifPresent(branch -> {
			int radius4 = TreeHelper.getRadius(world, rootPos.above(4));
			if (radius4 > configuredGenFeature.get(SECONDARY_MIN_RADIUS)) {
				branch.setRadius(world, rootPos.above(3), radius4 + 1, Direction.UP);
				branch.setRadius(world, rootPos.above(2), radius4 + 3, Direction.UP);
				branch.setRadius(world, rootPos.above(1), radius4 + 6, Direction.UP);
			} else {
				int radius3 = TreeHelper.getRadius(world, rootPos.above(3));
				if (radius3 > configuredGenFeature.get(MIN_RADIUS)) {
					branch.setRadius(world, rootPos.above(2), radius3 + 1, Direction.UP);
					branch.setRadius(world, rootPos.above(1), radius3 + 2, Direction.UP);
				}
			}	
		});
	}
	
}
