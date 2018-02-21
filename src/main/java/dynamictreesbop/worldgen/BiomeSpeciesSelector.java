package dynamictreesbop.worldgen;

import java.util.HashMap;
import java.util.Random;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeSpeciesSelector;
import com.ferreusveritas.dynamictrees.trees.Species;

import biomesoplenty.api.biome.BOPBiomes;
import dynamictreesbop.DynamicTreesBOP;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class BiomeSpeciesSelector implements IBiomeSpeciesSelector {

	Species swamp, apple, jungle, spruce, birch, oak, acacia, acaciaBrush, oakFloweringVine, oakConifer, megaOakConifer, darkOakConifer, darkOakDyingConifer, oakTwiglet, poplar, darkPoplar, jungleTwiglet, acaciaTwiglet, yellowAutumn, orangeAutumn, magic, floweringOak, umbran, umbranConifer, umbranConiferMega, oakDying, decayed, fir, firSmall, pinkCherry, whiteCherry, maple, dead, jacaranda, willow, pine, palm, ebony, ebonyTwiglet, mahogany;
	Species acaciaBush, oakBush;
	Species cactus;
	
	HashMap<Integer, DecisionProvider> fastTreeLookup = new HashMap<Integer, DecisionProvider>();
	
	@Override
	public ResourceLocation getName() {
		return new ResourceLocation(DynamicTreesBOP.MODID, "default");
	}

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public Decision getSpecies(World world, Biome biome, BlockPos pos, IBlockState state, Random rand) {
		if (biome == null) return new Decision();
		
		int biomeId = Biome.getIdForBiome(biome);
		DecisionProvider select;
				
		if(fastTreeLookup.containsKey(biomeId)) {
			select = fastTreeLookup.get(biomeId); // Speedily look up the selector for the biome id
		} else {
			if (biome == BOPBiomes.alps_foothills.get()) select = new StaticDecision(new Decision(firSmall));
			else if (biome == BOPBiomes.bayou.get()) select = new RandomDecision(rand).addSpecies(willow, 4);
			else if (biome == BOPBiomes.bog.get()) select = new RandomDecision(rand).addSpecies(poplar, 3).addSpecies(darkPoplar, 1);
			else if (biome == BOPBiomes.boreal_forest.get()) select = new RandomDecision(rand).addSpecies(yellowAutumn, 4).addSpecies(spruce, 4).addSpecies(oak, 5);
			else if (biome == BOPBiomes.brushland.get()) select = new RandomDecision(rand).addSpecies(ebony, 2).addSpecies(ebonyTwiglet, 2).addSpecies(jungleTwiglet, 1).addSpecies(acaciaBrush, 1);
			else if (biome == BOPBiomes.chaparral.get()) select = new RandomDecision(rand).addSpecies(oakTwiglet, 3);
			else if (biome == BOPBiomes.cherry_blossom_grove.get()) select = new RandomDecision(rand).addSpecies(pinkCherry, 6).addSpecies(whiteCherry, 4);
			else if (biome == BOPBiomes.coniferous_forest.get()) select = new RandomDecision(rand).addSpecies(fir, 3).addSpecies(firSmall, 5);
			else if (biome == BOPBiomes.dead_forest.get()) select = new RandomDecision(rand).addSpecies(spruce, 3).addSpecies(decayed, 1).addSpecies(oakDying, 8);
			else if (biome == BOPBiomes.dead_swamp.get()) select = new RandomDecision(rand).addSpecies(decayed, 1).addSpecies(dead, 2);
			else if (biome == BOPBiomes.fen.get()) select = new RandomDecision(rand).addSpecies(decayed, 1).addSpecies(darkOakConifer, 5).addSpecies(darkOakDyingConifer, 10);
			else if (biome == BOPBiomes.grove.get()) select = new RandomDecision(rand).addSpecies(poplar, 1).addSpecies(darkPoplar, 1);
			else if (biome == BOPBiomes.land_of_lakes.get()) select = new RandomDecision(rand).addSpecies(spruce, 3).addSpecies(birch, 1).addSpecies(oak, 5);
			else if (biome == BOPBiomes.lavender_fields.get()) select = new RandomDecision(rand).addSpecies(floweringOak, 1).addSpecies(jacaranda, 3);
			else if (biome == BOPBiomes.lush_desert.get()) select = new RandomDecision(rand).addSpecies(decayed, 1).addSpecies(oakTwiglet, 5).addSpecies(acacia, 3);
			else if (biome == BOPBiomes.lush_swamp.get()) select = new StaticDecision(new Decision(swamp));
			else if (biome == BOPBiomes.maple_woods.get()) select = new RandomDecision(rand).addSpecies(spruce, 1).addSpecies(maple, 5);
			else if (biome == BOPBiomes.meadow.get()) select = new StaticDecision(new Decision(spruce));
			else if (biome == BOPBiomes.mountain.get()) select = new RandomDecision(rand).addSpecies(oak, 1).addSpecies(pine, 2);
			else if (biome == BOPBiomes.mountain_foothills.get()) select = new RandomDecision(rand).addSpecies(oak, 1).addSpecies(pine, 2);
			else if (biome == BOPBiomes.mystic_grove.get()) select = new RandomDecision(rand).addSpecies(magic, 17).addSpecies(oakFloweringVine, 10).addSpecies(floweringOak, 8).addSpecies(jacaranda, 9);
			else if (biome == BOPBiomes.oasis.get()) select = new RandomDecision(rand).addSpecies(palm, 4).addSpecies(jungleTwiglet, 2);
			else if (biome == BOPBiomes.ominous_woods.get()) select = new RandomDecision(rand).addSpecies(umbran, 4).addSpecies(umbranConifer, 5).addSpecies(umbranConiferMega, 4).addSpecies(decayed, 3).addSpecies(dead, 1);
			else if (biome == BOPBiomes.orchard.get()) select = new RandomDecision(rand).addSpecies(floweringOak, 6).addSpecies(apple, 1);
			else if (biome == BOPBiomes.outback.get()) select = new RandomDecision(rand).addSpecies(acaciaTwiglet, 3).addSpecies(acaciaBush, 3).addSpecies(cactus, 4);
			else if (biome == BOPBiomes.overgrown_cliffs.get()) select = new RandomDecision(rand).addSpecies(mahogany, 1).addSpecies(jungleTwiglet, 2).addSpecies(oakBush, 8);
			else if (biome == BOPBiomes.prairie.get()) select = new StaticDecision(new Decision(oakConifer));
			else if (biome == BOPBiomes.rainforest.get()) select = new RandomDecision(rand).addSpecies(jungle, 1).addSpecies(birch, 4).addSpecies(oak, 4).addSpecies(floweringOak, 7);
			else if (biome == BOPBiomes.seasonal_forest.get()) select = new RandomDecision(rand).addSpecies(yellowAutumn, 4).addSpecies(orangeAutumn, 5).addSpecies(oak, 1).addSpecies(oakDying, 2).addSpecies(maple, 4);
			else if (biome == BOPBiomes.shield.get()) select = new RandomDecision(rand).addSpecies(spruce, 4).addSpecies(pine, 2);
			else if (biome == BOPBiomes.snowy_coniferous_forest.get()) select = new RandomDecision(rand).addSpecies(fir, 2).addSpecies(firSmall, 4);
			else if (biome == BOPBiomes.snowy_forest.get()) select = new RandomDecision(rand).addSpecies(oak, 3).addSpecies(oakDying, 1);
			else if (biome == BOPBiomes.temperate_rainforest.get()) select = new RandomDecision(rand).addSpecies(oakConifer, 3).addSpecies(megaOakConifer, 5).addSpecies(willow, 1);
			else if (biome == BOPBiomes.tropical_island.get()) select = new RandomDecision(rand).addSpecies(palm, 4).addSpecies(jungleTwiglet, 2);
			else if (biome == BOPBiomes.tropical_rainforest.get()) select = new RandomDecision(rand).addSpecies(jungle, 2).addSpecies(mahogany, 6);
			else if (biome == BOPBiomes.wasteland.get()) select = new RandomDecision(rand).addSpecies(decayed, 3).addSpecies(dead, 1);
			else if (biome == BOPBiomes.wetland.get()) select = new RandomDecision(rand).addSpecies(spruce, 5).addSpecies(willow, 3);
			else if (biome == BOPBiomes.woodland.get()) select = new StaticDecision(new Decision(oak));
			else if (biome == BOPBiomes.xeric_shrubland.get()) select = new RandomDecision(rand).addSpecies(acaciaTwiglet, 1).addSpecies(cactus, 1);
			
			else if (biome == Biomes.BEACH) select = new StaticDecision(new Decision(palm));
			else if (biome == Biomes.FOREST || biome == Biomes.FOREST_HILLS) select = new RandomDecision(world.rand).addSpecies(oak, 8).addSpecies(birch, 2).addSpecies(floweringOak, 1);
			else if (biome == Biomes.EXTREME_HILLS || biome == Biomes.EXTREME_HILLS_WITH_TREES) select = new RandomDecision(world.rand).addSpecies(spruce, 3).addSpecies(jacaranda, 1);
			else if (biome == Biomes.SWAMPLAND) select = new RandomDecision(world.rand).addSpecies(swamp, 5).addSpecies(willow, 1);
			
			else select = new StaticDecision(new Decision());
			
			fastTreeLookup.put(biomeId, select); //Cache decision for future use
		}
		
		return select.getDecision();
	}

	@Override
	public void init() {
		cactus = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "cactus"));
		
		acaciaBush = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "acaciabush"));
		oakBush = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "oakbush"));
		
		apple = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oakapple"));
		jungle = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "jungle"));
		spruce = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "spruce"));
		birch = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "birch"));
		oak = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oak"));
		acacia = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "acacia"));
		swamp = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oakswamp"));
		oakFloweringVine = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "oakfloweringvine"));
		oakConifer = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "oakconifer"));
		megaOakConifer = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "megaoakconifer"));
		darkOakConifer = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "darkoakconifer"));
		darkOakDyingConifer = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "darkoakdyingconifer"));
		oakTwiglet = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "oaktwiglet"));
		poplar = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "poplar"));
		darkPoplar = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "darkpoplar"));
		jungleTwiglet = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "jungletwiglet"));
		acaciaTwiglet = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "acaciatwiglet"));
		acaciaBrush = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "acaciabrush"));
		
		yellowAutumn = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "yellowautumn"));
		orangeAutumn = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "orangeautumn"));
		magic = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "magic"));
		umbran = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "umbran"));
		umbranConifer = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "umbranconifer"));
		umbranConiferMega = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "umbranconifermega"));
		oakDying = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "oakdying"));
		fir = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "fir"));
		firSmall = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "firsmall"));
		pinkCherry = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "pinkcherry"));
		whiteCherry = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "whitecherry"));
		maple = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "maple"));
		dead = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "dead"));
		jacaranda = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "jacaranda"));
		willow = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "willow"));
		pine = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "pine"));
		ebony = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "ebony"));
		ebonyTwiglet = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "ebonytwiglet"));
		mahogany = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "mahogany"));
		
		floweringOak = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "floweringoak"));
		decayed = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "decayed"));
		palm = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "palm"));
	}
	
}

