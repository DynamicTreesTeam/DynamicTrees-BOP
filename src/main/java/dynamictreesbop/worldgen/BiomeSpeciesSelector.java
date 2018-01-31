package dynamictreesbop.worldgen;

import java.util.ArrayList;
import java.util.Random;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeSpeciesSelector;
import com.ferreusveritas.dynamictrees.trees.Species;

import biomesoplenty.api.biome.BOPBiomes;
import dynamictreesbop.DynamicTreesBOP;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class BiomeSpeciesSelector implements IBiomeSpeciesSelector {

	Species swamp, jungle, spruce, birch, oak, oakLarge, yellowAutumn, orangeAutumn;
	
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
		
		if (biome == BOPBiomes.rainforest.get()) return new RandomDecision(rand).addSpecies(jungle, 1).addSpecies(birch, 4).addSpecies(oakLarge, 4).getDecision();
		
		if (biome == BOPBiomes.snowy_forest.get()) return new RandomDecision(rand).addSpecies(oak, 3).getDecision();
		
		if (biome == BOPBiomes.lush_swamp.get()) return new Decision(swamp);
		
		if (biome == BOPBiomes.meadow.get()) return new Decision(spruce);
		
		if (biome == BOPBiomes.wetland.get()) return new RandomDecision(rand).addSpecies(spruce, 5).getDecision();
		
		if (biome == BOPBiomes.tropical_rainforest.get()) return new RandomDecision(rand).addSpecies(jungle, 2).getDecision();
		
		if (biome == BOPBiomes.boreal_forest.get()) return new RandomDecision(rand).addSpecies(yellowAutumn, 4).addSpecies(spruce, 4).getDecision();
		
		if (biome == BOPBiomes.maple_woods.get()) return new RandomDecision(rand).addSpecies(spruce, 1).getDecision();
		
		if (biome == BOPBiomes.dead_forest.get()) return new RandomDecision(rand).addSpecies(spruce, 3).getDecision();
		
		if (biome == BOPBiomes.shield.get()) return new RandomDecision(rand).addSpecies(spruce, 4).getDecision();
		
		if (biome == BOPBiomes.seasonal_forest.get()) return new RandomDecision(rand).addSpecies(yellowAutumn, 4).addSpecies(orangeAutumn, 5).addSpecies(oakLarge, 1).getDecision();
		
		if (biome == BOPBiomes.land_of_lakes.get()) return new RandomDecision(rand).addSpecies(spruce, 3).addSpecies(birch, 1).getDecision();
		
		return new Decision();
	}

	@Override
	public void init() {
		swamp = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oakswamp"));
		jungle = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "jungle"));
		spruce = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "spruce"));
		birch = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "birch"));
		oak = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "oak"));
		oakLarge = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "oaklarge"));
		yellowAutumn = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "yellowautumn"));
		orangeAutumn = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "orangeautumn"));
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

