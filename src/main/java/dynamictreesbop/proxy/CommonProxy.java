package dynamictreesbop.proxy;

import com.google.common.base.Optional;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.biome.IExtendedBiome;
import biomesoplenty.api.generation.GeneratorStage;
import biomesoplenty.api.generation.IGenerator;
import biomesoplenty.common.biome.BOPBiome;
import biomesoplenty.common.world.generator.GeneratorWeighted;
import biomesoplenty.common.world.generator.tree.GeneratorBush;
import dynamictreesbop.cells.CellKits;
import net.minecraft.block.BlockPlanks;
import net.minecraft.world.biome.Biome;

public class CommonProxy {
	
	public void preInit() {
		CellKits.init();
	}

	public void init() {
		
	}
	
	public void postInit() {
		removeTreeGen(BOPBiomes.alps_foothills);
		removeTreeGen(BOPBiomes.boreal_forest);
		removeTreeGen(BOPBiomes.cherry_blossom_grove);
		removeTreeGen(BOPBiomes.coniferous_forest);
		removeTreeGen(BOPBiomes.dead_forest);
		removeTreeGen(BOPBiomes.dead_swamp);
		removeTreeGen(BOPBiomes.fen, "dead"); // still has dying, dark_oak_taiga
		removeTreeGen(BOPBiomes.land_of_lakes);
		removeTreeGen(BOPBiomes.lavender_fields);
		removeTreeGen(BOPBiomes.lush_desert, "dead_tree"); // still has twiglet, decaying_tree
		removeTreeGen(BOPBiomes.lush_swamp);
		removeTreeGen(BOPBiomes.maple_woods);
		removeTreeGen(BOPBiomes.meadow);
		removeTreeGen(BOPBiomes.mountain, "oak"); // still has pine
		removeTreeGen(BOPBiomes.mountain_foothills, "oak"); // still has pine
		removeTreeGen(BOPBiomes.mystic_grove);
		removeTreeGen(BOPBiomes.ominous_woods);
		removeTreeGen(BOPBiomes.orchard);
		removeTreeGen(BOPBiomes.rainforest);
		removeTreeGen(BOPBiomes.seasonal_forest);
		removeTreeGen(BOPBiomes.shield, "spruce"); // still has oak_bush, pine
		removeTreeGen(BOPBiomes.snowy_coniferous_forest);
		removeTreeGen(BOPBiomes.snowy_forest);
		removeTreeGen(BOPBiomes.tropical_rainforest, "jungle"); // still has mahogany
		removeTreeGen(BOPBiomes.wasteland);
		removeTreeGen(BOPBiomes.wetland, "spruce"); // still has willow
		removeTreeGen(BOPBiomes.woodland);
		
		removeTreeGen(BOPBiomes.forest_extension);
		removeTreeGen(BOPBiomes.forest_hills_extension);
		removeTreeGen(BOPBiomes.extreme_hills_extension);
		removeTreeGen(BOPBiomes.extreme_hills_plus_extension);
		
		if (BOPBiomes.boreal_forest.get() != null) ((BOPBiome) BOPBiomes.boreal_forest.get()).addGenerator("bush", GeneratorStage.TREE, (new GeneratorBush.Builder()).amountPerChunk(3).maxHeight(2).create());
		if (BOPBiomes.meadow.get() != null) ((BOPBiome) BOPBiomes.meadow.get()).addGenerator("bush", GeneratorStage.TREE, (new GeneratorBush.Builder()).amountPerChunk(1).maxHeight(2).log(BlockPlanks.EnumType.SPRUCE).leaves(BlockPlanks.EnumType.SPRUCE).create());
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
