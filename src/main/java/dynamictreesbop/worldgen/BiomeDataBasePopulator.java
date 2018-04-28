package dynamictreesbop.worldgen;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.EnumChance;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.IChanceSelector;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.IDensitySelector;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.ISpeciesSelector;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.RandomSpeciesSelector;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.StaticSpeciesSelector;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase.Operation;
import com.google.common.base.Optional;

import biomesoplenty.api.biome.BOPBiomes;
import dynamictreesbop.DynamicTreesBOP;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public class BiomeDataBasePopulator {
	
	private final BiomeDataBase dbase;
	private final IChanceSelector ok = (rnd, spc, rad) -> { return EnumChance.OK; };
	private final IChanceSelector cancel = (rnd, spc, rad) -> { return EnumChance.CANCEL; };
	
	private Species swamp, apple, jungle, spruce, birch, oak, acacia, acaciaBrush, oakFloweringVine, oakConifer, megaOakConifer, darkOakConifer, darkOakDyingConifer, oakTwiglet, poplar, darkPoplar, jungleTwiglet, acaciaTwiglet, yellowAutumn, orangeAutumn, magic, floweringOak, umbran, umbranConifer, umbranConiferMega, oakDying, decayed, fir, firSmall, pinkCherry, whiteCherry, maple, dead, jacaranda, willow, pine, palm, ebony, ebonyTwiglet, mahogany, eucalyptus, bamboo;
	private Species acaciaBush, oakBush;
	private Species cactus;
	
	public BiomeDataBasePopulator(BiomeDataBase dbase) {
		this.dbase = dbase;
	}
	
	public void init () {
		apple =					findVanSpecies("oakapple");
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
		willow =				findBopSpecies("willow");
		pine =					findBopSpecies("pine");
		ebony =					findBopSpecies("ebony");
		ebonyTwiglet =			findBopSpecies("ebonytwiglet");
		mahogany =				findBopSpecies("mahogany");
		eucalyptus =			findBopSpecies("eucalyptus");
		bamboo =				findBopSpecies("bamboo");
		floweringOak =			findBopSpecies("floweringoak");
		decayed =				findBopSpecies("decayed");
		palm =					findBopSpecies("palm");
		cactus =				findVanSpecies("cactus");
		acaciaBush =			findBopSpecies("acaciabush");
		oakBush =				findBopSpecies("oakbush");
		
		
		
		//Species Selectors
		addSpeciesSelector(BOPBiomes.alps_foothills,			new StaticSpeciesSelector(firSmall));
		addSpeciesSelector(BOPBiomes.bamboo_forest,				new RandomSpeciesSelector().add(bamboo, 4));
		addSpeciesSelector(BOPBiomes.bayou,						new RandomSpeciesSelector().add(willow, 4));
		addSpeciesSelector(BOPBiomes.bog,						new RandomSpeciesSelector().add(poplar, 3).add(darkPoplar, 1));
		addSpeciesSelector(BOPBiomes.boreal_forest,				new RandomSpeciesSelector().add(yellowAutumn, 4).add(spruce, 4).add(oak, 5));
		addSpeciesSelector(BOPBiomes.brushland,					new RandomSpeciesSelector().add(ebony, 2).add(ebonyTwiglet, 2).add(jungleTwiglet, 1).add(acaciaBrush, 1));
		addSpeciesSelector(BOPBiomes.chaparral,					new RandomSpeciesSelector().add(oakTwiglet, 3));
		addSpeciesSelector(BOPBiomes.cherry_blossom_grove,		new RandomSpeciesSelector().add(pinkCherry, 6).add(whiteCherry, 4));
		addSpeciesSelector(BOPBiomes.coniferous_forest,			new RandomSpeciesSelector().add(fir, 4).add(firSmall, 5));
		addSpeciesSelector(BOPBiomes.dead_forest,				new RandomSpeciesSelector().add(spruce, 3).add(decayed, 1).add(oakDying, 8));
		addSpeciesSelector(BOPBiomes.dead_swamp, 				new RandomSpeciesSelector().add(decayed, 1).add(dead, 2));
		addSpeciesSelector(BOPBiomes.eucalyptus_forest, 		new RandomSpeciesSelector().add(eucalyptus, 1).add(oakBush, 6));
		addSpeciesSelector(BOPBiomes.fen,						new RandomSpeciesSelector().add(decayed, 1).add(darkOakConifer, 5).add(darkOakDyingConifer, 10));
		addSpeciesSelector(BOPBiomes.grove,						new RandomSpeciesSelector().add(poplar, 1).add(darkPoplar, 1));
		addSpeciesSelector(BOPBiomes.land_of_lakes,				new RandomSpeciesSelector().add(spruce, 3).add(birch, 1).add(oak, 5));
		addSpeciesSelector(BOPBiomes.lavender_fields,			new RandomSpeciesSelector().add(floweringOak, 1).add(jacaranda, 3));
		addSpeciesSelector(BOPBiomes.lush_desert,				new RandomSpeciesSelector().add(decayed, 1).add(oakTwiglet, 5).add(acacia, 3));
		addSpeciesSelector(BOPBiomes.lush_swamp,				new StaticSpeciesSelector(swamp));
		addSpeciesSelector(BOPBiomes.maple_woods,				new RandomSpeciesSelector().add(spruce, 1).add(maple, 5));
		addSpeciesSelector(BOPBiomes.meadow,					new RandomSpeciesSelector().add(spruce, 4).add(oakBush, 3));
		addSpeciesSelector(BOPBiomes.mountain,					new RandomSpeciesSelector().add(oak, 1).add(pine, 2));
		addSpeciesSelector(BOPBiomes.mountain_foothills,		new RandomSpeciesSelector().add(oak, 1).add(pine, 2));
		addSpeciesSelector(BOPBiomes.mystic_grove,				new RandomSpeciesSelector().add(magic, 17).add(oakFloweringVine, 10).add(floweringOak, 8).add(jacaranda, 9));
		addSpeciesSelector(BOPBiomes.oasis,						new RandomSpeciesSelector().add(palm, 4).add(jungleTwiglet, 2));
		addSpeciesSelector(BOPBiomes.ominous_woods,				new RandomSpeciesSelector().add(umbran, 4).add(umbranConifer, 5).add(umbranConiferMega, 4).add(decayed, 3).add(dead, 1));
		addSpeciesSelector(BOPBiomes.orchard,					new RandomSpeciesSelector().add(floweringOak, 6).add(apple, 1));
		addSpeciesSelector(BOPBiomes.outback,					new RandomSpeciesSelector().add(acaciaTwiglet, 3).add(acaciaBush, 3).add(cactus, 4));
		addSpeciesSelector(BOPBiomes.overgrown_cliffs,			new RandomSpeciesSelector().add(mahogany, 1).add(jungleTwiglet, 2).add(oakBush, 8));
		addSpeciesSelector(BOPBiomes.prairie,					new StaticSpeciesSelector(oakConifer));
		addSpeciesSelector(BOPBiomes.rainforest,				new RandomSpeciesSelector().add(jungle, 1).add(birch, 4).add(oak, 4).add(floweringOak, 7));
		addSpeciesSelector(BOPBiomes.seasonal_forest,			new RandomSpeciesSelector().add(yellowAutumn, 4).add(orangeAutumn, 5).add(oak, 1).add(oakDying, 2).add(maple, 4));
		addSpeciesSelector(BOPBiomes.shield,					new RandomSpeciesSelector().add(spruce, 4).add(pine, 2));
		addSpeciesSelector(BOPBiomes.snowy_coniferous_forest,	new RandomSpeciesSelector().add(fir, 2).add(firSmall, 4));
		addSpeciesSelector(BOPBiomes.snowy_forest,				new RandomSpeciesSelector().add(oak, 3).add(oakDying, 1));
		addSpeciesSelector(BOPBiomes.temperate_rainforest,		new RandomSpeciesSelector().add(oakConifer, 3).add(megaOakConifer, 5).add(willow, 1));
		addSpeciesSelector(BOPBiomes.tropical_island,			new RandomSpeciesSelector().add(palm, 4).add(jungleTwiglet, 2));
		addSpeciesSelector(BOPBiomes.tropical_rainforest,		new RandomSpeciesSelector().add(jungle, 2).add(mahogany, 6));
		addSpeciesSelector(BOPBiomes.wasteland,					new RandomSpeciesSelector().add(decayed, 3).add(dead, 1));
		addSpeciesSelector(BOPBiomes.wetland,					new RandomSpeciesSelector().add(spruce, 5).add(willow, 3));
		addSpeciesSelector(BOPBiomes.woodland,					new StaticSpeciesSelector(oak));
		addSpeciesSelector(BOPBiomes.xeric_shrubland,			new RandomSpeciesSelector().add(acaciaTwiglet, 1).add(cactus, 1));
		addSpeciesSelector(Biomes.BEACH,						new StaticSpeciesSelector(palm));
		addSpeciesSelector(Biomes.FOREST,						new RandomSpeciesSelector().add(oak, 8).add(birch, 2).add(floweringOak, 1));
		addSpeciesSelector(Biomes.FOREST_HILLS,					new RandomSpeciesSelector().add(oak, 8).add(birch, 2).add(floweringOak, 1));
		addSpeciesSelector(Biomes.EXTREME_HILLS,				new RandomSpeciesSelector().add(spruce, 3).add(jacaranda, 1));
		addSpeciesSelector(Biomes.EXTREME_HILLS_WITH_TREES,		new RandomSpeciesSelector().add(spruce, 3).add(jacaranda, 1));
		addSpeciesSelector(Biomes.SWAMPLAND,					new RandomSpeciesSelector().add(swamp, 5).add(willow, 1));
		
		
		
		//Density Selectors
		addDensitySelector(BOPBiomes.alps_foothills,			scale(0.05) );
		addDensitySelector(BOPBiomes.bamboo_forest,				scale(0.25, 0.75) );
		addDensitySelector(BOPBiomes.bayou,						scale(0.8) );
		addDensitySelector(BOPBiomes.bog,						scale(0.75, 0.25, 0.7) );
		addDensitySelector(BOPBiomes.boreal_forest,				scale() );
		addDensitySelector(BOPBiomes.brushland,					scale(0.75, 0.25, 0.4) );
		addDensitySelector(BOPBiomes.chaparral,					scale(0.7) );
		addDensitySelector(BOPBiomes.cherry_blossom_grove,		scale(0.3) );
		addDensitySelector(BOPBiomes.coniferous_forest,			scale(0.5, 0.5, 0.95) );
		addDensitySelector(BOPBiomes.dead_forest,				scale(0.3) );
		addDensitySelector(BOPBiomes.dead_swamp,				scale(0.06) );
		addDensitySelector(BOPBiomes.eucalyptus_forest,			scale(0.5, 0.5) );
		addDensitySelector(BOPBiomes.fen,						scale(0.9) );
		addDensitySelector(BOPBiomes.grove,						scale(0.25) );
		addDensitySelector(BOPBiomes.lavender_fields,			scale(0.1) );
		addDensitySelector(BOPBiomes.lush_desert,				scale(0.4) );
		addDensitySelector(BOPBiomes.lush_swamp,				scale(0.8) );
		addDensitySelector(BOPBiomes.maple_woods,				scale() );
		addDensitySelector(BOPBiomes.meadow,					scale(0.25) );
		addDensitySelector(BOPBiomes.mountain,					scale(0.3) );
		addDensitySelector(BOPBiomes.mountain_foothills,		scale(0.3) );
		addDensitySelector(BOPBiomes.mystic_grove,				scale() );
		addDensitySelector(BOPBiomes.oasis,						scale(0.25, 0.75, 0.7) );
		addDensitySelector(BOPBiomes.ominous_woods,				scale() );
		addDensitySelector(BOPBiomes.orchard,					scale(0.5) );
		addDensitySelector(BOPBiomes.outback,					scale(0.45) );
		addDensitySelector(BOPBiomes.overgrown_cliffs,			scale(0.75, 0.25) );
		addDensitySelector(BOPBiomes.prairie,					scale(0.1) );
		addDensitySelector(BOPBiomes.rainforest,				scale() );
		addDensitySelector(BOPBiomes.seasonal_forest,			scale(0.9) );
		addDensitySelector(BOPBiomes.shield,					scale(0.9) );
		addDensitySelector(BOPBiomes.snowy_coniferous_forest,	scale(0.8) );
		addDensitySelector(BOPBiomes.snowy_forest,				scale(0.3) );
		addDensitySelector(BOPBiomes.temperate_rainforest,		scale() );
		addDensitySelector(BOPBiomes.tropical_island,			scale(0.5, 0.5) );
		addDensitySelector(BOPBiomes.tropical_rainforest,		scale(0.5, 0.5) );
		addDensitySelector(BOPBiomes.wasteland,					scale(0.03) );
		addDensitySelector(BOPBiomes.wetland,					scale() );
		addDensitySelector(BOPBiomes.woodland,					scale() );
		addDensitySelector(BOPBiomes.xeric_shrubland,			scale(0.4) );
		
		
		
		//Chance Selectors
		addChanceSelector(BOPBiomes.alps_foothills, 	rand(0.5f));
		addChanceSelector(BOPBiomes.bamboo_forest,		ok);
		addChanceSelector(BOPBiomes.brushland,			ok);
		addChanceSelector(BOPBiomes.bog,				ok);
		addChanceSelector(BOPBiomes.chaparral,			rand(0.7f));
		addChanceSelector(BOPBiomes.dead_swamp,			rand(0.6f));
		addChanceSelector(BOPBiomes.eucalyptus_forest,	ok);
		addChanceSelector(BOPBiomes.lavender_fields,	rand(0.3f));
		addChanceSelector(BOPBiomes.lush_desert,		rand(0.4f));
		addChanceSelector(BOPBiomes.meadow,				rand(0.6f));
		addChanceSelector(BOPBiomes.oasis,				(rnd, spc, rad) -> { return spc.equals(palm) && rnd.nextFloat() < 0.5f ? EnumChance.OK : EnumChance.CANCEL; });
		addChanceSelector(BOPBiomes.outback,			ok);
		addChanceSelector(BOPBiomes.overgrown_cliffs,	ok);
		addChanceSelector(BOPBiomes.prairie,			rand(0.15f));
		addChanceSelector(BOPBiomes.quagmire,			rand(0.02f));
		addChanceSelector(BOPBiomes.rainforest,			ok);
		addChanceSelector(BOPBiomes.tropical_island,	ok);
		addChanceSelector(BOPBiomes.tropical_rainforest,ok);
		addChanceSelector(BOPBiomes.wasteland,			rand(0.3f));
		addChanceSelector(BOPBiomes.xeric_shrubland,	rand(0.7f));
		addChanceSelector(BOPBiomes.cold_desert,		cancel);
		addChanceSelector(BOPBiomes.crag,				cancel);
		addChanceSelector(BOPBiomes.flower_field,		cancel);
		addChanceSelector(BOPBiomes.flower_island,		cancel);
		addChanceSelector(BOPBiomes.glacier,			cancel);
		addChanceSelector(BOPBiomes.grassland,			cancel);
		addChanceSelector(BOPBiomes.gravel_beach,		cancel);
		addChanceSelector(BOPBiomes.highland,			cancel);
		addChanceSelector(BOPBiomes.mangrove,			cancel);
		addChanceSelector(BOPBiomes.marsh,				cancel);
		addChanceSelector(BOPBiomes.moor,				cancel);
		addChanceSelector(BOPBiomes.origin_beach,		cancel);
		addChanceSelector(BOPBiomes.origin_island,		cancel);
		addChanceSelector(BOPBiomes.pasture,			cancel);
		addChanceSelector(BOPBiomes.redwood_forest,		cancel);
		addChanceSelector(BOPBiomes.sacred_springs,		cancel);
		addChanceSelector(BOPBiomes.shrubland,			cancel);
		addChanceSelector(BOPBiomes.steppe,				cancel);
		addChanceSelector(BOPBiomes.tundra,				cancel);
		addChanceSelector(BOPBiomes.volcanic_island,	cancel);
	}
	
	
	
	////////////////////////////////////////////////////////////////
	// Helper Members
	////////////////////////////////////////////////////////////////

	private Species findBopSpecies(String name) {
		return TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, name));
	}

	private Species findVanSpecies(String name) {
		return TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, name));
	}
	
	private void addSpeciesSelector(Optional<Biome> biome, ISpeciesSelector selector) {
		if(biome.isPresent()) {
			addSpeciesSelector(biome.get(), selector);
		}
	}
	
	private void addSpeciesSelector(Biome biome, ISpeciesSelector selector) {
		dbase.setSpeciesSelector(biome, selector, Operation.REPLACE);
	}
	
	private void addChanceSelector(Optional<Biome> biome, IChanceSelector selector) {
		if(biome.isPresent()) {
			dbase.setChanceSelector(biome.get(), selector, Operation.REPLACE);
		}
	}
	
	private IChanceSelector rand(float threshhold) {
		return (rnd, spc, rad) -> { return rnd.nextFloat() < threshhold ? EnumChance.OK : EnumChance.CANCEL; };
	}
	
	private void addDensitySelector(Optional<Biome> biome, IDensitySelector selector) {
		if(biome.isPresent()) {
			dbase.setDensitySelector(biome.get(), selector, Operation.REPLACE);
		}
	}

	private IDensitySelector scale() {
		return (rnd, nd) -> { return nd; };
	}
	
	private IDensitySelector scale(double factor1) {
		return (rnd, nd) -> { return nd * factor1; };
	}
	
	private IDensitySelector scale(double factor1, double addend) {
		return (rnd, nd) -> { return (nd * factor1) + addend; };
	}
	
	private IDensitySelector scale(double factor1, double addend, double factor2) {
		return (rnd, nd) -> { return ((nd * factor1) + addend) * factor2; };
	}
	
}
