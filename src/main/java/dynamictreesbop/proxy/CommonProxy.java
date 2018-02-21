package dynamictreesbop.proxy;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.google.common.base.Optional;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.biome.IExtendedBiome;
import biomesoplenty.api.generation.GeneratorStage;
import biomesoplenty.api.generation.IGenerator;
import biomesoplenty.common.biome.BOPBiome;
import biomesoplenty.common.world.generator.GeneratorWeighted;
import biomesoplenty.common.world.generator.tree.GeneratorBush;
import dynamictreesbop.cells.CellKits;
import dynamictreesbop.worldgen.NetherTreeGenerator;
import net.minecraft.block.BlockPlanks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
	
	public void preInit() {
		CellKits.init();
		
		if(WorldGenRegistry.isWorldGenEnabled()) {
			GameRegistry.registerWorldGenerator(new NetherTreeGenerator(), 1);
		}
	}

	public void init() {
		Species oakConifer = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oakconifer"));
		Species megaOakConifer = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "megaoakconifer"));
		Species darkOakConifer = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "darkoakconifer"));
		Species poplar = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "poplar"));
		Species darkPoplar = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "darkpoplar"));
		
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oak")).getTree().addSpeciesLocationOverride((access, trunkPos) -> {
			Biome biome = access.getBiome(trunkPos);
			
			if (biome == BOPBiomes.prairie.get()) return oakConifer;
			if (biome == BOPBiomes.temperate_rainforest.get()) return access.rand.nextInt(3) == 0 ? megaOakConifer : oakConifer; 
			
			return Species.NULLSPECIES;
		});
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "birch")).getTree().addSpeciesLocationOverride((access, trunkPos) -> {
			Biome biome = access.getBiome(trunkPos);
			
			if (biome == BOPBiomes.grove.get()) return poplar;
			
			return Species.NULLSPECIES;
		});
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "darkoak")).getTree().addSpeciesLocationOverride((access, trunkPos) -> {
			Biome biome = access.getBiome(trunkPos);
			
			if (biome == BOPBiomes.grove.get()) return darkPoplar;
			if (biome == BOPBiomes.fen.get()) return darkOakConifer; 
			
			return Species.NULLSPECIES;
		});
	}
	
	public void postInit() {
		if (WorldGenRegistry.isWorldGenEnabled()) {
			removeTreeGen(BOPBiomes.alps_foothills);
			removeTreeGen(BOPBiomes.bayou);
			removeTreeGen(BOPBiomes.bog);
			removeTreeGen(BOPBiomes.boreal_forest);
			removeTreeGen(BOPBiomes.brushland);
			removeTreeGen(BOPBiomes.chaparral);
			removeTreeGen(BOPBiomes.cherry_blossom_grove);
			removeTreeGen(BOPBiomes.coniferous_forest);
			removeTreeGen(BOPBiomes.dead_forest);
			removeTreeGen(BOPBiomes.dead_swamp);
			removeTreeGen(BOPBiomes.fen);
			removeTreeGen(BOPBiomes.grove);
			removeTreeGen(BOPBiomes.land_of_lakes);
			removeTreeGen(BOPBiomes.lavender_fields);
			removeTreeGen(BOPBiomes.lush_desert);
			removeTreeGen(BOPBiomes.lush_swamp);
			removeTreeGen(BOPBiomes.maple_woods);
			removeTreeGen(BOPBiomes.meadow);
			removeTreeGen(BOPBiomes.mountain);
			removeTreeGen(BOPBiomes.mountain_foothills);
			removeTreeGen(BOPBiomes.mystic_grove);
			removeTreeGen(BOPBiomes.oasis);
			removeTreeGen(BOPBiomes.ominous_woods);
			removeTreeGen(BOPBiomes.orchard);
			removeTreeGen(BOPBiomes.outback);
			removeTreeGen(BOPBiomes.overgrown_cliffs);
			removeTreeGen(BOPBiomes.prairie);
			removeTreeGen(BOPBiomes.rainforest);
			removeTreeGen(BOPBiomes.seasonal_forest);
			removeTreeGen(BOPBiomes.shield);
			removeTreeGen(BOPBiomes.snowy_coniferous_forest);
			removeTreeGen(BOPBiomes.snowy_forest);
			removeTreeGen(BOPBiomes.temperate_rainforest);
			removeTreeGen(BOPBiomes.tropical_island);
			removeTreeGen(BOPBiomes.tropical_rainforest);
			//removeTreeGen(BOPBiomes.undergarden);
			removeTreeGen(BOPBiomes.wasteland);
			removeTreeGen(BOPBiomes.wetland);
			removeTreeGen(BOPBiomes.woodland);
			removeTreeGen(BOPBiomes.xeric_shrubland);
			
			removeTreeGen(BOPBiomes.undergarden);
			
			removeTreeGen(BOPBiomes.forest_extension);
			removeTreeGen(BOPBiomes.forest_hills_extension);
			removeTreeGen(BOPBiomes.extreme_hills_extension);
			removeTreeGen(BOPBiomes.extreme_hills_plus_extension);
			removeTreeGen(BOPBiomes.swampland_extension);
			
			if (!ModConfigs.vanillaCactusWorldGen) {
				removeCactusGen(BOPBiomes.oasis);
				removeCactusGen(BOPBiomes.outback);
				removeCactusGen(BOPBiomes.xeric_shrubland);
			}
			
			if (BOPBiomes.meadow.get() != null) ((BOPBiome) BOPBiomes.meadow.get()).addGenerator("bush", GeneratorStage.TREE, (new GeneratorBush.Builder()).amountPerChunk(1).maxHeight(2).log(BlockPlanks.EnumType.SPRUCE).leaves(BlockPlanks.EnumType.SPRUCE).create());
		}
	}
	
	private void removeTreeGen(Optional<Biome> optionalBiome, String... trees) {
		Biome biome = optionalBiome.get();
		if (biome != null && biome instanceof BOPBiome) {
			IGenerator gen = ((BOPBiome) biome).getGenerator("trees");
			if (gen instanceof GeneratorWeighted) {
				for (String tree : trees) {
					GeneratorWeighted treeGen = (GeneratorWeighted) gen;
					treeGen.removeGenerator(tree);
				}
			}
		}
	}
	
	private void removeTreeGen(Optional<Biome> optionalBiome) {
		Biome biome = optionalBiome.get();
		if (biome != null && biome instanceof BOPBiome) ((BOPBiome) biome).removeGenerator("trees");
	}
	
	private void removeCactusGen(Optional<Biome> optionalBiome) {
		Biome biome = optionalBiome.get();
		if (biome != null && biome instanceof BOPBiome) ((BOPBiome) biome).removeGenerator("cacti");
	}
	
	@SuppressWarnings("unused")
	private void removeTreeGen(IExtendedBiome extendedBiome, String... trees) {
		IGenerator gen = extendedBiome.getGenerationManager().getGenerator("trees");
		if (gen instanceof GeneratorWeighted) {
			for (String tree : trees) {
				GeneratorWeighted treeGen = (GeneratorWeighted) gen;
				treeGen.removeGenerator(tree);
			}
		}
	}
	
	private void removeTreeGen(IExtendedBiome extendedBiome) {
		extendedBiome.getGenerationManager().removeGenerator("trees");
	}
	
}
