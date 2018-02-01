package dynamictreesbop.proxy;

import com.google.common.base.Optional;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.biome.IExtendedBiome;
import biomesoplenty.api.generation.GeneratorStage;
import biomesoplenty.api.generation.IGenerator;
import biomesoplenty.common.biome.BOPBiome;
import biomesoplenty.common.world.generator.GeneratorWeighted;
import biomesoplenty.common.world.generator.tree.GeneratorBush;
import net.minecraft.block.BlockPlanks;
import net.minecraft.world.biome.Biome;

public class CommonProxy {
	
	public void preInit() {
		
	}

	public void init() {
		
	}
	
	public void postInit() {
		removeTreeGen(BOPBiomes.boreal_forest);
		removeTreeGen(BOPBiomes.dead_forest, "spruce"); // still has dying_tree, dead_tree, oak (dead)
		removeTreeGen(BOPBiomes.land_of_lakes);
		removeTreeGen(BOPBiomes.lavender_fields, "oak"); // still has jacaranda
		removeTreeGen(BOPBiomes.lush_swamp);
		removeTreeGen(BOPBiomes.maple_woods, "spruce"); // still has maple
		removeTreeGen(BOPBiomes.meadow);
		removeTreeGen(BOPBiomes.mountain, "oak"); // still has pine
		removeTreeGen(BOPBiomes.mountain_foothills, "oak"); // still has pine
		removeTreeGen(BOPBiomes.mystic_grove, "magic", "flowering_vine", "oak_large"); // still has jacaranda
		removeTreeGen(BOPBiomes.orchard);
		removeTreeGen(BOPBiomes.rainforest);
		removeTreeGen(BOPBiomes.seasonal_forest, "yellow_autumn", "orange_autumn", "large_oak"); // still has dying_tree, maple
		removeTreeGen(BOPBiomes.shield, "spruce"); // still has oak_bush, pine
		removeTreeGen(BOPBiomes.snowy_forest, "oak"); // still has dying_tree
		removeTreeGen(BOPBiomes.tropical_rainforest, "jungle"); // still has mahogany
		removeTreeGen(BOPBiomes.wetland, "spruce"); // still has willow
		removeTreeGen(BOPBiomes.woodland);
		
		removeTreeGen(BOPBiomes.forest_extension);
		removeTreeGen(BOPBiomes.forest_hills_extension);
		
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
