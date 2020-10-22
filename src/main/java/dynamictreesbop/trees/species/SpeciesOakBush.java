package dynamictreesbop.trees.species;

import com.ferreusveritas.dynamictrees.api.IGenFeature;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenBush;
import com.ferreusveritas.dynamictrees.trees.Species;

import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import net.minecraft.util.ResourceLocation;

public class SpeciesOakBush extends Species {
	
FeatureGenBush bushGen;
	
	public SpeciesOakBush() {
		super();
		setRegistryName(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.OAKBUSH));
		
		setStandardSoils();
		
		addGenFeature(new FeatureGenBush(), IGenFeature.FULLGEN);
	}
	
}
