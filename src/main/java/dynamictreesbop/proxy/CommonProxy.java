package dynamictreesbop.proxy;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import dynamictreesbop.cells.CellKits;
import dynamictreesbop.worldgen.BiomeDataBasePopulator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Loader;

public class CommonProxy {
	
	public void preInit() {
		CellKits.init();		
	}

	public void init() {
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
		
		WorldGenRegistry.registerBiomeDataBasePopulator(new BiomeDataBasePopulator());
	}
	
	public void postInit() {
		String rusticModid = "rustic";
		String dttcModid = "dynamictreestc";
		if (Loader.isModLoaded(rusticModid)) {
			Species olive = TreeRegistry.findSpecies(new ResourceLocation(rusticModid, "olive"));
			Species ironwood = TreeRegistry.findSpecies(new ResourceLocation(rusticModid, "ironwood"));
			if (olive != null) olive.addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
			if (ironwood != null) ironwood.addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		}
		if (Loader.isModLoaded(dttcModid)) {
			Species greatwood = TreeRegistry.findSpecies(new ResourceLocation(dttcModid, "greatwood"));
			Species silverwood = TreeRegistry.findSpecies(new ResourceLocation(dttcModid, "silverwood"));
			if (greatwood != null) greatwood.addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
			if (silverwood != null) silverwood.addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		}
	}
	
}
