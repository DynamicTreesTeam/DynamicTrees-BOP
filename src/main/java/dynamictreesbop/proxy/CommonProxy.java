package dynamictreesbop.proxy;

import com.ferreusveritas.dynamictrees.api.WorldGenRegistry;
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
		
	}
	
	public void postInit() {
		if(WorldGenRegistry.isWorldGenEnabled()) {
			removeTreeGen(BOPBiomes.alps_foothills);
			removeTreeGen(BOPBiomes.bayou, "willow"); // still has willow_large
			removeTreeGen(BOPBiomes.boreal_forest);
			removeTreeGen(BOPBiomes.cherry_blossom_grove);
			removeTreeGen(BOPBiomes.coniferous_forest);
			removeTreeGen(BOPBiomes.dead_forest);
			removeTreeGen(BOPBiomes.dead_swamp);
			removeTreeGen(BOPBiomes.fen);
			removeTreeGen(BOPBiomes.grove);
			removeTreeGen(BOPBiomes.land_of_lakes);
			removeTreeGen(BOPBiomes.lavender_fields);
			removeTreeGen(BOPBiomes.lush_desert, "dead_tree"); // still has twiglet, decaying_tree (deciduous acacia)
			removeTreeGen(BOPBiomes.lush_swamp);
			removeTreeGen(BOPBiomes.maple_woods);
			removeTreeGen(BOPBiomes.meadow);
			removeTreeGen(BOPBiomes.mountain);
			removeTreeGen(BOPBiomes.mountain_foothills);
			removeTreeGen(BOPBiomes.mystic_grove);
			removeTreeGen(BOPBiomes.ominous_woods);
			removeTreeGen(BOPBiomes.orchard);
			removeTreeGen(BOPBiomes.prairie);
			removeTreeGen(BOPBiomes.rainforest);
			removeTreeGen(BOPBiomes.seasonal_forest);
			removeTreeGen(BOPBiomes.shield);
			removeTreeGen(BOPBiomes.snowy_coniferous_forest);
			removeTreeGen(BOPBiomes.snowy_forest);
			removeTreeGen(BOPBiomes.temperate_rainforest);
			removeTreeGen(BOPBiomes.tropical_rainforest, "jungle"); // still has mahogany
			removeTreeGen(BOPBiomes.wasteland);
			removeTreeGen(BOPBiomes.wetland);
			removeTreeGen(BOPBiomes.woodland);
			
			removeTreeGen(BOPBiomes.undergarden);
			
			removeTreeGen(BOPBiomes.forest_extension);
			removeTreeGen(BOPBiomes.forest_hills_extension);
			removeTreeGen(BOPBiomes.extreme_hills_extension);
			removeTreeGen(BOPBiomes.extreme_hills_plus_extension);
			removeTreeGen(BOPBiomes.swampland_extension);
			
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
