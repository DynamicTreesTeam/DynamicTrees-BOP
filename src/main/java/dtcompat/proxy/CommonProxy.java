package dtcompat.proxy;

import com.google.common.base.Optional;

import biomesoplenty.api.biome.BOPBiomes;
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
		removeTreeGen(BOPBiomes.rainforest, "jungle"); // still has large_oak, oak
		removeTreeGen(BOPBiomes.rainforest, "birch"); // still has large_oak, oak
		removeTreeGen(BOPBiomes.lush_swamp);
		removeTreeGen(BOPBiomes.meadow);
		removeTreeGen(BOPBiomes.wetland, "spruce"); // still has willow
		removeTreeGen(BOPBiomes.tropical_rainforest, "jungle"); // still has mahogany
		removeTreeGen(BOPBiomes.boreal_forest, "yellow_autumn"); // still has oak_bush, small_oak, oak
		removeTreeGen(BOPBiomes.boreal_forest, "spruce"); // still has oak_bush, small_oak, oak
		removeTreeGen(BOPBiomes.maple_woods, "spruce"); // still has maple
		removeTreeGen(BOPBiomes.dead_forest, "spruce"); // still has dying_tree, oak, dead_tree
		removeTreeGen(BOPBiomes.shield, "spruce"); // still has oak_bush, pine
		removeTreeGen(BOPBiomes.seasonal_forest, "yellow_autumn"); // still has large_oak, dying_tree, maple, orange_autumn
		removeTreeGen(BOPBiomes.land_of_lakes, "spruce"); // still has oak
		removeTreeGen(BOPBiomes.land_of_lakes, "birch"); // still has oak
		
		
		if (BOPBiomes.meadow.get() != null) ((BOPBiome) BOPBiomes.meadow.get()).addGenerator("bush", GeneratorStage.TREE, (new GeneratorBush.Builder()).amountPerChunk(1).maxHeight(2).log(BlockPlanks.EnumType.SPRUCE).leaves(BlockPlanks.EnumType.SPRUCE).create());
	}
	
	private void removeTreeGen(Optional<Biome> optionalBiome, String tree) {
		Biome biome = optionalBiome.get();
		if (biome != null && biome instanceof BOPBiome) {
			IGenerator gen = ((BOPBiome) biome).getGenerator("trees");
			
			if (gen instanceof GeneratorWeighted) {
				GeneratorWeighted treeGen = (GeneratorWeighted) gen;
				treeGen.removeGenerator(tree);
			}
		}
	}
	
	private void removeTreeGen(Optional<Biome> optionalBiome) {
		Biome biome = optionalBiome.get();
		if (biome != null && biome instanceof BOPBiome) ((BOPBiome) biome).removeGenerator("trees");
	}
	
}
