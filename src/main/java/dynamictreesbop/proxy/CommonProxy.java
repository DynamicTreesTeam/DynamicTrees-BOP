package dynamictreesbop.proxy;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;

import biomesoplenty.api.biome.BOPBiomes;
import dynamictreesbop.cells.CellKits;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public class CommonProxy {
	
	public void preInit() {
		CellKits.init();		
	}

	public void init() {
		addSpeciesLocationOverrides();
	}
	
	private void addSpeciesLocationOverrides() {
		Species oakConifer = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oakconifer"));
		Species megaOakConifer = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "megaoakconifer"));
		Species darkOakConifer = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "darkoakconifer"));
		Species poplar = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "poplar"));
		Species darkPoplar = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "darkpoplar"));
		Species oakSparse = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oaksparse"));
		
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oak")).getFamily().addSpeciesLocationOverride((access, trunkPos) -> {
			Biome biome = access.getBiome(trunkPos);
			
			if (biome == BOPBiomes.prairie.orNull()) return oakSparse;
			if (biome == BOPBiomes.temperate_rainforest.orNull()) return access.rand.nextInt(3) == 0 ? megaOakConifer : oakConifer; 
			
			return Species.NULLSPECIES;
		});
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "birch")).getFamily().addSpeciesLocationOverride((access, trunkPos) -> {
			Biome biome = access.getBiome(trunkPos);
			
			if (biome == BOPBiomes.grove.orNull()) return poplar;
			
			return Species.NULLSPECIES;
		});
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "darkoak")).getFamily().addSpeciesLocationOverride((access, trunkPos) -> {
			Biome biome = access.getBiome(trunkPos);
			
			if (biome == BOPBiomes.grove.orNull()) return darkPoplar;
			if (biome == BOPBiomes.fen.orNull()) return darkOakConifer; 
			
			return Species.NULLSPECIES;
		});
	}
	
}
