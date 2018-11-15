package dynamictreesbop.worldgen;

import java.util.ArrayList;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.EnumChance;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.IChanceSelector;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.IDensitySelector;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.ISpeciesSelector;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.RandomSpeciesSelector;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.StaticSpeciesSelector;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase.Operation;
import com.google.common.base.Optional;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.biome.IExtendedBiome;
import biomesoplenty.common.biome.BOPBiome;
import dynamictreesbop.DynamicTreesBOP;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {
	
	private static final IChanceSelector ok = (rnd, spc, rad) -> EnumChance.OK;
	private static final IChanceSelector cancel = (rnd, spc, rad) -> EnumChance.CANCEL;
	
	private static Species swamp, apple, jungle, spruce, birch, oak, acacia, acaciaBrush, oakFloweringVine, oakConifer, megaOakConifer, darkOakConifer, darkOakDyingConifer, oakTwiglet, oakSparse, poplar, darkPoplar, jungleTwiglet, acaciaTwiglet, yellowAutumn, orangeAutumn, magic, floweringOak, umbran, umbranConifer, umbranConiferMega, oakDying, decayed, fir, firSmall, pinkCherry, whiteCherry, maple, dead, jacaranda, redwood, willow, pine, palm, ebony, ebonyTwiglet, mahogany, eucalyptus, bamboo, hellbark;
	private static Species acaciaBush, oakBush;
	private static Species cactus;
	private static Species mushroomRed, mushroomBrown;
	
	private static void createStaticAliases() {
		apple =					findVanSpecies("apple");
		jungle =				findVanSpecies("jungle");
		spruce =				findVanSpecies("spruce");
		birch =					findVanSpecies("birch");
		oak =					findVanSpecies("oak");
		acacia =				findVanSpecies("acacia");
		swamp =					findVanSpecies("oakswamp");
		oakFloweringVine =		findBopSpecies("oakfloweringvine");
		oakConifer =			findBopSpecies("oakconifer");
		megaOakConifer =		findBopSpecies("megaoakconifer");
		darkOakConifer =		findBopSpecies("darkoakconifer");
		darkOakDyingConifer =	findBopSpecies("darkoakdyingconifer");
		oakTwiglet =			findBopSpecies("oaktwiglet");
		oakSparse =				findBopSpecies("oaksparse");
		poplar =				findBopSpecies("poplar");
		darkPoplar =			findBopSpecies("darkpoplar");
		jungleTwiglet =			findBopSpecies("jungletwiglet");
		acaciaTwiglet =			findBopSpecies("acaciatwiglet");
		acaciaBrush =			findBopSpecies("acaciabrush");
		yellowAutumn =			findBopSpecies("yellowautumn");
		orangeAutumn =			findBopSpecies("orangeautumn");
		magic =					findBopSpecies("magic");
		umbran =				findBopSpecies("umbran");
		umbranConifer =			findBopSpecies("umbranconifer");
		umbranConiferMega =		findBopSpecies("umbranconifermega");
		oakDying =				findBopSpecies("oakdying");
		fir =					findBopSpecies("fir");
		firSmall =				findBopSpecies("firsmall");
		pinkCherry =			findBopSpecies("pinkcherry");
		whiteCherry =			findBopSpecies("whitecherry");
		maple =					findBopSpecies("maple");
		dead =					findBopSpecies("dead");
		jacaranda =				findBopSpecies("jacaranda");
		redwood =				findBopSpecies("redwood");
		willow =				findBopSpecies("willow");
		pine =					findBopSpecies("pine");
		ebony =					findBopSpecies("ebony");
		ebonyTwiglet =			findBopSpecies("ebonytwiglet");
		mahogany =				findBopSpecies("mahogany");
		eucalyptus =			findBopSpecies("eucalyptus");
		bamboo =				findBopSpecies("bamboo");
		hellbark = 				findBopSpecies("hellbark");
		floweringOak =			findBopSpecies("floweringoak");
		decayed =				findBopSpecies("decayed");
		palm =					findBopSpecies("palm");
		cactus =				findVanSpecies("cactus");
		acaciaBush =			findBopSpecies("acaciabush");
		oakBush =				findBopSpecies("oakbush");
		mushroomRed = 			findVanSpecies("mushroomred");
		mushroomBrown = 		findVanSpecies("mushroombrn");
	}
	
	public void populate(BiomeDataBase dbase) {

		if(oak == null) {
			createStaticAliases();
		}
		
		//Species Selectors
		addSpeciesSelector(dbase, BOPBiomes.alps_foothills,				new StaticSpeciesSelector(firSmall));
		addSpeciesSelector(dbase, BOPBiomes.bamboo_forest,				new RandomSpeciesSelector().add(bamboo, 4));
		addSpeciesSelector(dbase, BOPBiomes.bayou,						new RandomSpeciesSelector().add(willow, 4));
		addSpeciesSelector(dbase, BOPBiomes.bog,						new RandomSpeciesSelector().add(poplar, 3).add(darkPoplar, 1));
		addSpeciesSelector(dbase, BOPBiomes.boreal_forest,				new RandomSpeciesSelector().add(yellowAutumn, 4).add(spruce, 4).add(oak, 5));
		addSpeciesSelector(dbase, BOPBiomes.brushland,					new RandomSpeciesSelector().add(ebony, 2).add(ebonyTwiglet, 2).add(jungleTwiglet, 1).add(acaciaBrush, 1));
		addSpeciesSelector(dbase, BOPBiomes.chaparral,					new RandomSpeciesSelector().add(oakTwiglet, 3));
		addSpeciesSelector(dbase, BOPBiomes.cherry_blossom_grove,		new RandomSpeciesSelector().add(pinkCherry, 6).add(whiteCherry, 4));
		addSpeciesSelector(dbase, BOPBiomes.coniferous_forest,			new RandomSpeciesSelector().add(fir, 4).add(firSmall, 5));
		addSpeciesSelector(dbase, BOPBiomes.dead_forest,				new RandomSpeciesSelector().add(spruce, 3).add(decayed, 1).add(oakDying, 8));
		addSpeciesSelector(dbase, BOPBiomes.dead_swamp, 				new RandomSpeciesSelector().add(decayed, 1).add(dead, 2));
		addSpeciesSelector(dbase, BOPBiomes.eucalyptus_forest, 			new RandomSpeciesSelector().add(eucalyptus, 1).add(oakBush, 6));
		addSpeciesSelector(dbase, BOPBiomes.fen,						new RandomSpeciesSelector().add(decayed, 1).add(darkOakConifer, 5).add(darkOakDyingConifer, 10));
		addSpeciesSelector(dbase, BOPBiomes.grove,						new RandomSpeciesSelector().add(poplar, 1).add(darkPoplar, 1));
		addSpeciesSelector(dbase, BOPBiomes.land_of_lakes,				new RandomSpeciesSelector().add(spruce, 3).add(birch, 1).add(oak, 5));
		addSpeciesSelector(dbase, BOPBiomes.lavender_fields,			new RandomSpeciesSelector().add(floweringOak, 1).add(jacaranda, 3));
		addSpeciesSelector(dbase, BOPBiomes.lush_desert,				new RandomSpeciesSelector().add(decayed, 1).add(oakTwiglet, 5).add(acacia, 3));
		addSpeciesSelector(dbase, BOPBiomes.lush_swamp,					new StaticSpeciesSelector(swamp));
		addSpeciesSelector(dbase, BOPBiomes.maple_woods,				new RandomSpeciesSelector().add(spruce, 1).add(maple, 5));
		addSpeciesSelector(dbase, BOPBiomes.meadow,						new RandomSpeciesSelector().add(spruce, 4).add(oakBush, 3));
		addSpeciesSelector(dbase, BOPBiomes.mountain,					new RandomSpeciesSelector().add(oak, 1).add(pine, 2));
		addSpeciesSelector(dbase, BOPBiomes.mountain_foothills,			new RandomSpeciesSelector().add(oak, 1).add(pine, 2));
		addSpeciesSelector(dbase, BOPBiomes.mystic_grove,				new RandomSpeciesSelector().add(magic, 17).add(oakFloweringVine, 10).add(floweringOak, 8).add(jacaranda, 9).add(mushroomRed, 3).add(mushroomBrown, 1));
		addSpeciesSelector(dbase, BOPBiomes.oasis,						new RandomSpeciesSelector().add(palm, 4).add(jungleTwiglet, 2));
		addSpeciesSelector(dbase, BOPBiomes.ominous_woods,				new RandomSpeciesSelector().add(umbran, 4).add(umbranConifer, 5).add(umbranConiferMega, 4).add(decayed, 3).add(dead, 1));
		addSpeciesSelector(dbase, BOPBiomes.orchard,					new RandomSpeciesSelector().add(floweringOak, 6).add(apple, 1));
		addSpeciesSelector(dbase, BOPBiomes.outback,					new RandomSpeciesSelector().add(acaciaTwiglet, 3).add(acaciaBush, 3).add(cactus, 4));
		addSpeciesSelector(dbase, BOPBiomes.overgrown_cliffs,			new RandomSpeciesSelector().add(mahogany, 1).add(jungleTwiglet, 2).add(oakBush, 8));
		addSpeciesSelector(dbase, BOPBiomes.prairie,					new StaticSpeciesSelector(oakSparse));
		addSpeciesSelector(dbase, BOPBiomes.rainforest,					new RandomSpeciesSelector().add(jungle, 1).add(birch, 4).add(oak, 4).add(floweringOak, 7));
		addSpeciesSelector(dbase, BOPBiomes.redwood_forest,				new StaticSpeciesSelector(redwood));
		addSpeciesSelector(dbase, BOPBiomes.seasonal_forest,			new RandomSpeciesSelector().add(yellowAutumn, 4).add(orangeAutumn, 5).add(oak, 1).add(oakDying, 2).add(maple, 4));
		addSpeciesSelector(dbase, BOPBiomes.shield,						new RandomSpeciesSelector().add(spruce, 4).add(pine, 2));
		addSpeciesSelector(dbase, BOPBiomes.snowy_coniferous_forest,	new RandomSpeciesSelector().add(fir, 2).add(firSmall, 4));
		addSpeciesSelector(dbase, BOPBiomes.snowy_forest,				new RandomSpeciesSelector().add(oak, 3).add(oakDying, 1));
		addSpeciesSelector(dbase, BOPBiomes.temperate_rainforest,		new RandomSpeciesSelector().add(oakConifer, 3).add(megaOakConifer, 5).add(willow, 1));
		addSpeciesSelector(dbase, BOPBiomes.tropical_island,			new RandomSpeciesSelector().add(palm, 4).add(jungleTwiglet, 2));
		addSpeciesSelector(dbase, BOPBiomes.tropical_rainforest,		new RandomSpeciesSelector().add(jungle, 2).add(mahogany, 6));
		addSpeciesSelector(dbase, BOPBiomes.undergarden, 				new StaticSpeciesSelector(hellbark));
		addSpeciesSelector(dbase, BOPBiomes.wasteland,					new RandomSpeciesSelector().add(decayed, 3).add(dead, 1));
		addSpeciesSelector(dbase, BOPBiomes.wetland,					new RandomSpeciesSelector().add(spruce, 5).add(willow, 3));
		addSpeciesSelector(dbase, BOPBiomes.woodland,					new StaticSpeciesSelector(oak));
		addSpeciesSelector(dbase, BOPBiomes.xeric_shrubland,			new RandomSpeciesSelector().add(acaciaTwiglet, 1).add(cactus, 1));
		addSpeciesSelector(dbase, Biomes.BEACH,							new StaticSpeciesSelector(palm));
		addSpeciesSelector(dbase, Biomes.FOREST,						new RandomSpeciesSelector().add(oak, 8).add(birch, 2).add(floweringOak, 1));
		addSpeciesSelector(dbase, Biomes.FOREST_HILLS,					new RandomSpeciesSelector().add(oak, 8).add(birch, 2).add(floweringOak, 1));
		addSpeciesSelector(dbase, Biomes.EXTREME_HILLS,					new StaticSpeciesSelector(spruce));
		addSpeciesSelector(dbase, Biomes.EXTREME_HILLS_WITH_TREES,		new StaticSpeciesSelector(spruce));
		addSpeciesSelector(dbase, Biomes.SWAMPLAND,						new RandomSpeciesSelector().add(swamp, 5).add(willow, 1));
		
		
		//Density Selectors
		addDensitySelector(dbase, BOPBiomes.alps_foothills,				scale(0.05) );
		addDensitySelector(dbase, BOPBiomes.bamboo_forest,				scale(0.25, 0.75) );
		addDensitySelector(dbase, BOPBiomes.bayou,						scale(0.8) );
		addDensitySelector(dbase, BOPBiomes.bog,						scale(0.75, 0.25, 0.7) );
		addDensitySelector(dbase, BOPBiomes.boreal_forest,				scale() );
		addDensitySelector(dbase, BOPBiomes.brushland,					scale(0.75, 0.25, 0.4) );
		addDensitySelector(dbase, BOPBiomes.chaparral,					scale(0.7) );
		addDensitySelector(dbase, BOPBiomes.cherry_blossom_grove,		scale(0.3) );
		addDensitySelector(dbase, BOPBiomes.coniferous_forest,			scale(0.5, 0.5, 0.95) );
		addDensitySelector(dbase, BOPBiomes.dead_forest,				scale(0.3) );
		addDensitySelector(dbase, BOPBiomes.dead_swamp,					scale(0.06) );
		addDensitySelector(dbase, BOPBiomes.eucalyptus_forest,			scale(0.5, 0.5) );
		addDensitySelector(dbase, BOPBiomes.fen,						scale(0.9) );
		addDensitySelector(dbase, BOPBiomes.grove,						scale(0.25) );
		addDensitySelector(dbase, BOPBiomes.lavender_fields,			scale(0.1) );
		addDensitySelector(dbase, BOPBiomes.lush_desert,				scale(0.4) );
		addDensitySelector(dbase, BOPBiomes.lush_swamp,					scale(0.8) );
		addDensitySelector(dbase, BOPBiomes.maple_woods,				scale() );
		addDensitySelector(dbase, BOPBiomes.meadow,						scale(0.25) );
		addDensitySelector(dbase, BOPBiomes.mountain,					scale(0.3) );
		addDensitySelector(dbase, BOPBiomes.mountain_foothills,			scale(0.3) );
		addDensitySelector(dbase, BOPBiomes.mystic_grove,				scale() );
		addDensitySelector(dbase, BOPBiomes.oasis,						scale(0.25, 0.75, 0.7) );
		addDensitySelector(dbase, BOPBiomes.ominous_woods,				scale() );
		addDensitySelector(dbase, BOPBiomes.orchard,					scale(0.5) );
		addDensitySelector(dbase, BOPBiomes.outback,					scale(0.45) );
		addDensitySelector(dbase, BOPBiomes.overgrown_cliffs,			scale(0.75, 0.25) );
		addDensitySelector(dbase, BOPBiomes.prairie,					scale(0.03) );
		addDensitySelector(dbase, BOPBiomes.rainforest,					scale() );
		addDensitySelector(dbase, BOPBiomes.redwood_forest,				scale(0.25, 0.2) );
		addDensitySelector(dbase, BOPBiomes.seasonal_forest,			scale(0.9) );
		addDensitySelector(dbase, BOPBiomes.shield,						scale(0.9) );
		addDensitySelector(dbase, BOPBiomes.snowy_coniferous_forest,	scale(0.8) );
		addDensitySelector(dbase, BOPBiomes.snowy_forest,				scale(0.3) );
		addDensitySelector(dbase, BOPBiomes.temperate_rainforest,		scale() );
		addDensitySelector(dbase, BOPBiomes.tropical_island,			scale(0.5, 0.5) );
		addDensitySelector(dbase, BOPBiomes.tropical_rainforest,		scale(0.5, 0.5) );
		addDensitySelector(dbase, BOPBiomes.undergarden,				scale(0.0, 1.0) );
		addDensitySelector(dbase, BOPBiomes.wasteland,					scale(0.03) );
		addDensitySelector(dbase, BOPBiomes.wetland,					scale() );
		addDensitySelector(dbase, BOPBiomes.woodland,					scale() );
		addDensitySelector(dbase, BOPBiomes.xeric_shrubland,			scale(0.4) );
		
		
		//Chance Selectors
		addChanceSelector(dbase, BOPBiomes.alps_foothills,		rand(0.5f));
		addChanceSelector(dbase, BOPBiomes.bamboo_forest,		ok);
		addChanceSelector(dbase, BOPBiomes.brushland,			ok);
		addChanceSelector(dbase, BOPBiomes.bog,					ok);
		addChanceSelector(dbase, BOPBiomes.chaparral,			rand(0.7f));
		addChanceSelector(dbase, BOPBiomes.dead_swamp,			rand(0.6f));
		addChanceSelector(dbase, BOPBiomes.eucalyptus_forest,	ok);
		addChanceSelector(dbase, BOPBiomes.lavender_fields,		rand(0.3f));
		addChanceSelector(dbase, BOPBiomes.lush_desert,			rand(0.4f));
		addChanceSelector(dbase, BOPBiomes.meadow,				rand(0.6f));
		addChanceSelector(dbase, BOPBiomes.oasis,				(rnd, spc, rad) -> (!spc.equals(palm) || rnd.nextFloat() < 0.3f) ? EnumChance.OK : EnumChance.CANCEL);
		addChanceSelector(dbase, BOPBiomes.outback,				ok);
		addChanceSelector(dbase, BOPBiomes.overgrown_cliffs,	ok);
		addChanceSelector(dbase, BOPBiomes.prairie,				rand(0.075f));
		addChanceSelector(dbase, BOPBiomes.quagmire,			rand(0.02f));
		addChanceSelector(dbase, BOPBiomes.rainforest,			ok);
		addChanceSelector(dbase, BOPBiomes.tropical_island,		ok);
		addChanceSelector(dbase, BOPBiomes.tropical_rainforest,	ok);
		addChanceSelector(dbase, BOPBiomes.undergarden,			ok);
		addChanceSelector(dbase, BOPBiomes.wasteland,			rand(0.3f));
		addChanceSelector(dbase, BOPBiomes.xeric_shrubland,		rand(0.7f));
		addChanceSelector(dbase, BOPBiomes.cold_desert,			cancel);
		addChanceSelector(dbase, BOPBiomes.crag,				cancel);
		addChanceSelector(dbase, BOPBiomes.flower_field,		cancel);
		addChanceSelector(dbase, BOPBiomes.flower_island,		cancel);
		addChanceSelector(dbase, BOPBiomes.glacier,				cancel);
		addChanceSelector(dbase, BOPBiomes.grassland,			cancel);
		addChanceSelector(dbase, BOPBiomes.gravel_beach,		cancel);
		addChanceSelector(dbase, BOPBiomes.highland,			cancel);
		addChanceSelector(dbase, BOPBiomes.mangrove,			cancel);
		addChanceSelector(dbase, BOPBiomes.marsh,				cancel);
		addChanceSelector(dbase, BOPBiomes.moor,				cancel);
		addChanceSelector(dbase, BOPBiomes.origin_beach,		cancel);
		addChanceSelector(dbase, BOPBiomes.origin_island,		cancel);
		addChanceSelector(dbase, BOPBiomes.pasture,				cancel);
		addChanceSelector(dbase, BOPBiomes.redwood_forest,		cancel);
		addChanceSelector(dbase, BOPBiomes.sacred_springs,		cancel);
		addChanceSelector(dbase, BOPBiomes.shrubland,			cancel);
		addChanceSelector(dbase, BOPBiomes.steppe,				cancel);
		addChanceSelector(dbase, BOPBiomes.tundra,				cancel);
		addChanceSelector(dbase, BOPBiomes.volcanic_island,		cancel);

		ArrayList<Biome> blackList = new ArrayList<>();
		blackList.addAll(BOPBiomes.flower_island.asSet());
		blackList.addAll(BOPBiomes.sacred_springs.asSet());
		blackList.addAll(BOPBiomes.origin_island.asSet());
		blackList.addAll(BOPBiomes.shrubland.asSet());
		blackList.addAll(BOPBiomes.tundra.asSet());
		blackList.addAll(BOPBiomes.mangrove.asSet());
		blackList.addAll(BOPBiomes.redwood_forest.asSet());
		
		Biome.REGISTRY.forEach(biome -> {
			if (biome.getRegistryName().getResourceDomain().equals("biomesoplenty") && !blackList.contains(biome) ) {
				if (biome != null && biome instanceof BOPBiome) {
					((BOPBiome) biome).removeGenerator("trees");
					((BOPBiome) biome).removeGenerator("big_red_mushroom");
					((BOPBiome) biome).removeGenerator("big_brown_mushroom");
					if (!ModConfigs.vanillaCactusWorldGen) {
						((BOPBiome) biome).removeGenerator("cacti");
					}
				}
				dbase.setCancelVanillaTreeGen(biome, true);
				
				//Identify the "forestness" of the biome.  Affects forest spread rate for biome.
				dbase.setForestness(biome, identifyForestness(biome));
			}
		});
		
		if (BOPBiomes.tundra.isPresent()) dbase.setCancelVanillaTreeGen(BOPBiomes.tundra.get(), true);
		if (BOPBiomes.shrubland.isPresent()) dbase.setCancelVanillaTreeGen(BOPBiomes.shrubland.get(), true);
		if (BOPBiomes.mangrove.isPresent()) dbase.setCancelVanillaTreeGen(BOPBiomes.mangrove.get(), true);
		
		//Remove generators from extended biomes
		removeTreeGen(BOPBiomes.forest_extension);
		removeTreeGen(BOPBiomes.forest_hills_extension);
		removeTreeGen(BOPBiomes.extreme_hills_extension);
		removeTreeGen(BOPBiomes.extreme_hills_plus_extension);
		removeTreeGen(BOPBiomes.swampland_extension);
		
		dbase.setIsSubterranean(BOPBiomes.undergarden.orNull(), true);
	}
	
	private void removeTreeGen(IExtendedBiome extendedBiome) {
		extendedBiome.getGenerationManager().removeGenerator("trees");	
	}
	
	////////////////////////////////////////////////////////////////
	// Helper Members
	////////////////////////////////////////////////////////////////

	private static Species findBopSpecies(String name) {
		return TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, name));
	}

	private static Species findVanSpecies(String name) {
		return TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, name));
	}
	
	private void addSpeciesSelector(BiomeDataBase dbase, Optional<Biome> biome, ISpeciesSelector selector) {
		if(biome.isPresent()) {
			addSpeciesSelector(dbase, biome.get(), selector);
		}
	}
	
	private void addSpeciesSelector(BiomeDataBase dbase, Biome biome, ISpeciesSelector selector) {
		dbase.setSpeciesSelector(biome, selector, Operation.REPLACE);
	}
	
	private void addChanceSelector(BiomeDataBase dbase, Optional<Biome> biome, IChanceSelector selector) {
		if(biome.isPresent()) {
			dbase.setChanceSelector(biome.get(), selector, Operation.REPLACE);
		}
	}
	
	private IChanceSelector rand(float threshhold) {
		return (rnd, spc, rad) -> rnd.nextFloat() < threshhold ? EnumChance.OK : EnumChance.CANCEL;
	}
	
	private void addDensitySelector(BiomeDataBase dbase, Optional<Biome> biome, IDensitySelector selector) {
		if(biome.isPresent()) {
			dbase.setDensitySelector(biome.get(), selector, Operation.REPLACE);
		}
	}

	private IDensitySelector scale() {
		return (rnd, nd) -> nd;
	}
	
	private IDensitySelector scale(double factor1) {
		return (rnd, nd) -> nd * factor1;
	}
	
	private IDensitySelector scale(double factor1, double addend) {
		return (rnd, nd) -> (nd * factor1) + addend;
	}
	
	private IDensitySelector scale(double factor1, double addend, double factor2) {
		return (rnd, nd) -> ((nd * factor1) + addend) * factor2;
	}
	
}
