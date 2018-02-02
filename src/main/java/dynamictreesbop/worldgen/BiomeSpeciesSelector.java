package dynamictreesbop.worldgen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeSpeciesSelector;
import com.ferreusveritas.dynamictrees.trees.Species;

import biomesoplenty.api.biome.BOPBiomes;
import dynamictreesbop.DynamicTreesBOP;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class BiomeSpeciesSelector implements IBiomeSpeciesSelector {

	Species swamp, apple, jungle, spruce, birch, oak, oakLarge, oakFloweringVine, yellowAutumn, orangeAutumn, magic, floweringOak, floweringOakLarge, umbran, umbranConifer, decayed;
	
	HashMap<Integer, ITreeSelector> fastTreeLookup = new HashMap<Integer, ITreeSelector>();
	
	@Override
	public String getName() {
		return DynamicTreesBOP.MODID + ":" + "default";
	}

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public Decision getSpecies(World world, Biome biome, BlockPos pos, IBlockState state, Random rand) {
		if (biome == null) return new Decision();
		
		int biomeId = Biome.getIdForBiome(biome);
		ITreeSelector select;
				
		if(fastTreeLookup.containsKey(biomeId)) {
			select = fastTreeLookup.get(biomeId);//Speedily look up the selector for the biome id
		} else {
			if (biome == BOPBiomes.boreal_forest.get()) select = new RandomDecision(rand).addSpecies(yellowAutumn, 4).addSpecies(spruce, 4).addSpecies(oak, 3).addSpecies(oakLarge, 2);
			
			else if (biome == BOPBiomes.dead_forest.get()) select = new RandomDecision(rand).addSpecies(spruce, 3).addSpecies(decayed, 1);
			
			else if (biome == BOPBiomes.dead_swamp.get()) select = new RandomDecision(rand).addSpecies(decayed, 1);
			
			else if (biome == BOPBiomes.fen.get()) select = new RandomDecision(rand).addSpecies(decayed, 1);
			
			else if (biome == BOPBiomes.land_of_lakes.get()) select = new RandomDecision(rand).addSpecies(spruce, 3).addSpecies(birch, 1).addSpecies(oak, 5);
			
			else if (biome == BOPBiomes.lavender_fields.get()) select = new RandomDecision(rand).addSpecies(floweringOak, 1);
			
			else if (biome == BOPBiomes.lush_desert.get()) select = new RandomDecision(rand).addSpecies(decayed, 1);
			
			else if (biome == BOPBiomes.lush_swamp.get()) select = new StaticDecision(new Decision(swamp));
			
			else if (biome == BOPBiomes.maple_woods.get()) select = new RandomDecision(rand).addSpecies(spruce, 1);
			
			else if (biome == BOPBiomes.meadow.get()) select = new StaticDecision(new Decision(spruce));
			
			else if (biome == BOPBiomes.mountain.get()) select = new RandomDecision(rand).addSpecies(oak, 1);
			
			else if (biome == BOPBiomes.mountain_foothills.get()) select = new RandomDecision(rand).addSpecies(oak, 1);
			
			else if (biome == BOPBiomes.mystic_grove.get()) select = new RandomDecision(rand).addSpecies(magic, 17).addSpecies(oakFloweringVine, 10).addSpecies(floweringOakLarge, 8);
			
			else if (biome == BOPBiomes.ominous_woods.get()) select = new RandomDecision(rand).addSpecies(umbran, 4).addSpecies(umbranConifer, 9).addSpecies(decayed, 3);
			
			else if (biome == BOPBiomes.orchard.get()) select = new RandomDecision(rand).addSpecies(floweringOak, 4).addSpecies(floweringOakLarge, 2).addSpecies(apple, 1);
			
			else if (biome == BOPBiomes.rainforest.get()) select = new RandomDecision(rand).addSpecies(jungle, 1).addSpecies(birch, 4).addSpecies(oakLarge, 4).addSpecies(floweringOak, 7);
			
			else if (biome == BOPBiomes.seasonal_forest.get()) select = new RandomDecision(rand).addSpecies(yellowAutumn, 4).addSpecies(orangeAutumn, 5).addSpecies(oakLarge, 1);
			
			else if (biome == BOPBiomes.shield.get()) select = new RandomDecision(rand).addSpecies(spruce, 4);
			
			else if (biome == BOPBiomes.snowy_forest.get()) select = new RandomDecision(rand).addSpecies(oak, 3);
			
			else if (biome == BOPBiomes.tropical_rainforest.get()) select = new RandomDecision(rand).addSpecies(jungle, 2);
			
			else if (biome == BOPBiomes.wasteland.get()) select = new RandomDecision(rand).addSpecies(decayed, 3);
			
			else if (biome == BOPBiomes.wetland.get()) select = new RandomDecision(rand).addSpecies(spruce, 5);
			
			else if (biome == BOPBiomes.woodland.get()) select = new StaticDecision(new Decision(oak));
			
			
			else if (biome == Biomes.FOREST || biome == Biomes.FOREST_HILLS) select = new RandomDecision(world.rand).addSpecies(oak, 8).addSpecies(birch, 2).addSpecies(floweringOak, 1);
			
			
			else select = new StaticDecision(new Decision());
			
			fastTreeLookup.put(biomeId, select);//Cache decision for future use
		}
		
		return select.getDecision();
	}

	@Override
	public void init() {
		swamp = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oakswamp"));
		apple = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oakapple"));
		jungle = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "jungle"));
		spruce = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "spruce"));
		birch = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "birch"));
		oak = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "oak"));
		oakLarge = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "oaklarge"));
		oakFloweringVine = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "oakfloweringvine"));
		
		yellowAutumn = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "yellowautumn"));
		orangeAutumn = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "orangeautumn"));
		magic = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "magic"));
		umbran = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "umbran"));
		umbranConifer = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "umbranconifer"));
		
		floweringOak = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "floweringoak"));
		floweringOakLarge = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "floweringoaklarge"));
		decayed = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "decayed"));
	}
	
	private interface ITreeSelector {
		Decision getDecision();
	}
	
	private class StaticDecision implements ITreeSelector {
		final Decision decision;
		
		public StaticDecision(Decision decision) {
			this.decision = decision;
		}

		@Override
		public Decision getDecision() {
			return decision;
		}
	}
	
	private class RandomDecision implements ITreeSelector {

		private class Entry {
			public Entry(Decision d, int w) {
				decision = d;
				weight = w;
			}
			
			public Decision decision;
			public int weight;
		}
		
		ArrayList<Entry> decisionTable = new ArrayList<Entry>();
		int totalWeight;
		Random rand;
		
		public RandomDecision(Random rand) {
			this.rand = rand;
		}
		
		public RandomDecision addSpecies(Species species, int weight) {
			decisionTable.add(new Entry(new Decision(species), weight));
			totalWeight += weight;
			return this;
		}
		
		@Override
		public Decision getDecision() {
			int chance = rand.nextInt(totalWeight);
			
			for(Entry entry: decisionTable) {
				if(chance < entry.weight) {
					return entry.decision;
				}
				chance -= entry.weight;
			};

			return decisionTable.get(decisionTable.size() - 1).decision;
		}
		
	}

}

