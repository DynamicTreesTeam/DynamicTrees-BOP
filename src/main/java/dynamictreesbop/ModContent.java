package dynamictreesbop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.ModItems;
import com.ferreusveritas.dynamictrees.ModRecipes;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry.BiomeDataBaseJsonCapabilityRegistryEvent;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry.BiomeDataBasePopulatorRegistryEvent;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockRootyWater;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.LeavesPropertiesJson;
import com.ferreusveritas.dynamictrees.blocks.LeavesPropertiesJson.PrimitiveLeavesComponents;
import com.ferreusveritas.dynamictrees.items.DendroPotion.DendroPotionType;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.google.gson.JsonElement;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.common.biome.BOPBiome;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockBOPSapling;
import dynamictreesbop.blocks.BlockDynamicLeavesFlowering;
import dynamictreesbop.blocks.BlockDynamicLeavesPalm;
import dynamictreesbop.items.ItemMagicSeed;
import dynamictreesbop.items.ItemMapleSeed;
import dynamictreesbop.trees.TreeBamboo;
import dynamictreesbop.trees.TreeCherry;
import dynamictreesbop.trees.TreeDead;
import dynamictreesbop.trees.TreeEbony;
import dynamictreesbop.trees.TreeEucalyptus;
import dynamictreesbop.trees.TreeFir;
import dynamictreesbop.trees.TreeHellbark;
import dynamictreesbop.trees.TreeJacaranda;
import dynamictreesbop.trees.TreeMagic;
import dynamictreesbop.trees.TreeMahogany;
import dynamictreesbop.trees.TreeMangrove;
import dynamictreesbop.trees.TreePalm;
import dynamictreesbop.trees.TreePine;
import dynamictreesbop.trees.TreeRedwood;
import dynamictreesbop.trees.TreeUmbran;
import dynamictreesbop.trees.TreeWillow;
import dynamictreesbop.trees.species.SpeciesAcaciaBrush;
import dynamictreesbop.trees.species.SpeciesAcaciaBush;
import dynamictreesbop.trees.species.SpeciesAcaciaTwiglet;
import dynamictreesbop.trees.species.SpeciesDarkOakConifer;
import dynamictreesbop.trees.species.SpeciesDarkOakDyingConifer;
import dynamictreesbop.trees.species.SpeciesDyingOak;
import dynamictreesbop.trees.species.SpeciesFloweringOak;
import dynamictreesbop.trees.species.SpeciesJungleTwiglet;
import dynamictreesbop.trees.species.SpeciesMaple;
import dynamictreesbop.trees.species.SpeciesMegaOakConifer;
import dynamictreesbop.trees.species.SpeciesOakBush;
import dynamictreesbop.trees.species.SpeciesOakConifer;
import dynamictreesbop.trees.species.SpeciesOakIvyVines;
import dynamictreesbop.trees.species.SpeciesOakSparse;
import dynamictreesbop.trees.species.SpeciesOakTwiglet;
import dynamictreesbop.trees.species.SpeciesOrangeAutumn;
import dynamictreesbop.trees.species.SpeciesPoplar;
import dynamictreesbop.trees.species.SpeciesSpruceBush;
import dynamictreesbop.trees.species.SpeciesYellowAutumn;
import dynamictreesbop.worldgen.BiomeDataBasePopulator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = DynamicTreesBOP.MODID)
public class ModContent {
	
	public static BlockRootyWater rootyWater;
	
	public static BlockDynamicLeavesFlowering floweringOakLeaves;
	public static BlockDynamicLeavesPalm palmFrondLeaves;
	
	// leaves properties for leaves without auto-generated leaves
	public static ILeavesProperties decayedLeavesProperties, palmLeavesProperties;
	public static ILeavesProperties[] floweringOakLeavesProperties;
	
	// A map for leaves properties for leaves with auto-generated leaves
	public static Map<String, ILeavesProperties> leaves;
	
	// array of leaves properties with auto-generated leaves
	public static ILeavesProperties[] basicLeavesProperties;
	
	// trees added by this mod
	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();
	
	//DT tree name constants
	public static final String ACACIA = "acacia";
	public static final String APPLE = "apple";
	public static final String BIRCH = "birch";
	public static final String CACTUS = "cactus";
	public static final String DARKOAK = "darkoak";
	public static final String JUNGLE = "jungle";
	public static final String OAK = "oak";
	public static final String OAKSWAMP = "oakswamp";
	public static final String SPRUCE = "spruce";
	public static final String MUSHROOMRED = "mushroomred";
	public static final String MUSHROOMBRN = "mushroombrn";
	
	//BOP tree name constants
	public static final String ACACIABRUSH = "acaciabrush";
	public static final String ACACIABUSH = "acaciabush";
	public static final String ACACIATWIGLET = "acaciatwiglet";
	public static final String BAMBOO = "bamboo";
	public static final String CHERRY = "cherry";
	public static final String DARKOAKCONIFER = "darkoakconifer";
	public static final String DARKOAKDYINGCONIFER = "darkoakdyingconifer";
	public static final String DARKPOPLAR = "darkpoplar";
	public static final String DEAD = "dead";
	public static final String DECAYED = "decayed";
	public static final String EBONY = "ebony";
	public static final String EBONYTWIGLET = "ebonytwiglet";
	public static final String EUCALYPTUS = "eucalyptus";
	public static final String FIR = "fir";
	public static final String FLOWERINGOAK = "floweringoak";
	public static final String HELLBARK = "hellbark";
	public static final String JACARANDA = "jacaranda";
	public static final String JUNGLETWIGLET = "jungletwiglet";
	public static final String MAGIC = "magic";
	public static final String MAPLE = "maple";
	public static final String MAHOGANY = "mahogany";
	public static final String MANGROVE = "mangrove";
	public static final String MEGAOAKCONIFER = "megaoakconifer";
	public static final String OAKBUSH = "oakbush";
	public static final String OAKCONIFER = "oakconifer";
	public static final String OAKDYING = "oakdying";
	public static final String OAKIVYVINE = "oakivyvines";
	public static final String OAKSPARSE = "oaksparse";
	public static final String OAKTWIGLET = "oaktwiglet";
	public static final String ORANGEAUTUMN = "orangeautumn";
	public static final String PALM = "palm";
	public static final String PINE = "pine";
	public static final String PINKCHERRY = "pinkcherry";
	public static final String POPLAR = "poplar";
	public static final String REDWOOD = "redwood";
	public static final String SPRUCEBUSH = "sprucebush";
	public static final String UMBRAN = "umbran";
	public static final String UMBRANCONIFER = "umbranconifer";
	public static final String UMBRANCONIFERMEGA = "umbranconifermega";
	public static final String WHITECHERRY = "whitecherry";
	public static final String WILLOW = "willow";
	public static final String YELLOWAUTUMN = "yellowautumn";
	
	public static final String REMOVEBOPGEN = "removebopgen";
	
	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		
		// TODO: create reference
		rootyWater = new BlockRootyWater(false);
		registry.register(rootyWater);
		
		// Register Special Leaf Blocks
		floweringOakLeaves = new BlockDynamicLeavesFlowering();
		registry.register(floweringOakLeaves);
		palmFrondLeaves = new BlockDynamicLeavesPalm();
		registry.register(palmFrondLeaves);
		
		// Add BOP dirt, grass and sand as acceptable soils
		DirtHelper.registerSoil(BOPBlocks.grass, DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(BOPBlocks.dirt, DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(BOPBlocks.white_sand, DirtHelper.SANDLIKE);
		DirtHelper.registerSoil(BOPBlocks.mud, DirtHelper.MUDLIKE);
		
		//Registers a BoP Primitive Leaves finding function for use with Json leaves properties
		LeavesPropertiesJson.addLeavesFinderFunction("bop", new Function<JsonElement, LeavesPropertiesJson.PrimitiveLeavesComponents>() {
			@Override
			public PrimitiveLeavesComponents apply(JsonElement element) {
				if(element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
					String searchData = element.getAsString();
					for(BOPTrees tree: BOPTrees.values()) {
						if(tree.getName().equals(searchData.toLowerCase())) {
							return new PrimitiveLeavesComponents(BlockBOPLeaves.paging.getVariantState(tree), BlockBOPLeaves.paging.getVariantItem(tree));
						}
					}
				}
				return null;
			}
		});
		
		//Leaves properties are read from a Json file
		leaves = LeavesPaging.build(DynamicTreesBOP.MODID, new ResourceLocation(DynamicTreesBOP.MODID, "leaves/common.json"));

		// Leaves properties that could not be automatically paging generated
		decayedLeavesProperties = new LeavesPropertiesJson("{`cellkit`:`bare`}");
		floweringOakLeavesProperties = new ILeavesProperties[]{
				new LeavesProperties(
						Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK),
						new ItemStack(Blocks.LEAVES, 1, BlockPlanks.EnumType.OAK.getMetadata())) {
					Random rand = new Random();
					@Override
					public IBlockState getDynamicLeavesState(int hydro) {
						if (rand.nextInt(4) == 0) {
							return super.getDynamicLeavesState(hydro).withProperty(BlockDynamicLeaves.TREE, 3);
						}
						return super.getDynamicLeavesState(hydro);
					}
				},
				new LeavesProperties(
						Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK),
						BlockBOPLeaves.paging.getVariantItem(BOPTrees.FLOWERING))
		};
		palmLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.PALM),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.PALM),
				TreeRegistry.findCellKit("palm") ) {
					@Override
					public boolean appearanceChangesWithHydro() {
						return true;
					}
				};


		floweringOakLeavesProperties[0].setDynamicLeavesState(floweringOakLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		floweringOakLeavesProperties[1].setDynamicLeavesState(floweringOakLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 2));
		floweringOakLeaves.setProperties(0, floweringOakLeavesProperties[0]);
		floweringOakLeaves.setProperties(1, floweringOakLeavesProperties[0]);
		floweringOakLeaves.setProperties(2, floweringOakLeavesProperties[1]);
		floweringOakLeaves.setProperties(3, floweringOakLeavesProperties[1]);

		palmLeavesProperties.setDynamicLeavesState(palmFrondLeaves.getDefaultState());
		palmFrondLeaves.setProperties(0, palmLeavesProperties);
		
		
		// Get tree types from base mod so they can be given new species
		TreeFamily oakTree = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, OAK)).getFamily();
		TreeFamily birchTree = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, BIRCH)).getFamily();
		TreeFamily jungleTree = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, JUNGLE)).getFamily();
		TreeFamily acaciaTree = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, ACACIA)).getFamily();
		TreeFamily darkOakTree = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, DARKOAK)).getFamily();
			
		Species.REGISTRY.registerAll(
			// Register new species of trees from the base mod
			new SpeciesOakIvyVines(oakTree),
			new SpeciesFloweringOak(oakTree),
			new SpeciesYellowAutumn(birchTree),
			new SpeciesOrangeAutumn(darkOakTree),
			new SpeciesDyingOak(oakTree),
			new SpeciesMaple(oakTree),
			new SpeciesOakConifer(oakTree),
			new SpeciesMegaOakConifer(oakTree),
			new SpeciesDarkOakConifer(darkOakTree),
			new SpeciesDarkOakDyingConifer(darkOakTree),
			new SpeciesOakTwiglet(oakTree),
			new SpeciesPoplar(birchTree, POPLAR),
			new SpeciesPoplar(darkOakTree, DARKPOPLAR),
			new SpeciesJungleTwiglet(jungleTree),
			new SpeciesAcaciaTwiglet(acaciaTree),
			new SpeciesAcaciaBrush(acaciaTree),
			new SpeciesOakSparse(oakTree),

			// Register bush dummies
			new SpeciesAcaciaBush(),
			new SpeciesOakBush(),
			new SpeciesSpruceBush()
		);
		
		// Register new tree types
		Collections.addAll(trees,
			new TreeMagic(), new TreeUmbran(), new TreeFir(), new TreeCherry(),
			new TreeDead(), new TreeJacaranda(), new TreeRedwood(), new TreeWillow(),
			new TreeHellbark(), new TreePine(), new TreePalm(), new TreeMahogany(),
			new TreeMangrove(), new TreeEbony(), new TreeBamboo(), new TreeEucalyptus()
		);
		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesBOP.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));
		
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {		
		// register seeds
		event.getRegistry().registerAll(
			getSpeciesSeed(FLOWERINGOAK),
			getSpeciesSeed(YELLOWAUTUMN),
			getSpeciesSeed(ORANGEAUTUMN),
			getSpeciesSeed(OAKDYING),
			getSpeciesSeed(MAPLE)
		);
		
		ArrayList<Item> treeItems = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		event.getRegistry().registerAll(treeItems.toArray(new Item[treeItems.size()]));
	}
	
	private static Item getSpeciesSeed(String name) {
		return TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, name)).getSeed();		
	}
	
	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		int id = 0;
		
		EntityRegistry.registerModEntity(new ResourceLocation(DynamicTreesBOP.MODID, "maple_seed"), ItemMapleSeed.EntityItemMapleSeed.class, "maple_seed", id++, DynamicTreesBOP.MODID, 32, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(DynamicTreesBOP.MODID, "magic_seed"), ItemMagicSeed.EntityItemMagicSeed.class, "magic_seed", id++, DynamicTreesBOP.MODID, 32, 1, true);
	}
	
	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		// Add transformation potion recipes
		for(String name: new String[] { MAGIC, UMBRANCONIFER, UMBRAN, FIR, WHITECHERRY, PINKCHERRY, JACARANDA, REDWOOD, WILLOW, HELLBARK, PINE, MAHOGANY, EBONY, EUCALYPTUS } ) {
			addTransformationPotion(name);
		};
		
		// Do dead trees separately because the seed grows a species of oak tree
		ItemStack outputStack = ModItems.dendroPotion.setTargetTree(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, DEAD)).getFamily());
		BrewingRecipeRegistry.addRecipe(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, DEAD)).getSeedStack(1), outputStack);
		
		// Add seed <-> sapling recipes
		addSeedExchange(BOPTrees.FLOWERING, FLOWERINGOAK);
		addSeedExchange(BOPTrees.YELLOW_AUTUMN, YELLOWAUTUMN);
		addSeedExchange(BOPTrees.ORANGE_AUTUMN, ORANGEAUTUMN);
		addSeedExchange(BOPTrees.DEAD, OAKDYING);
		addSeedExchange(BOPTrees.MAPLE, MAPLE);
		addSeedExchange(BOPTrees.MAGIC, MAGIC);
		addSeedExchange(BOPTrees.UMBRAN, UMBRAN);
		addSeedExchange(BOPTrees.FIR, FIR);
		addSeedExchange(BOPTrees.WHITE_CHERRY, WHITECHERRY);
		addSeedExchange(BOPTrees.PINK_CHERRY, PINKCHERRY);
		addSeedExchange(BOPTrees.JACARANDA, JACARANDA);
		addSeedExchange(BOPTrees.REDWOOD, REDWOOD);
		addSeedExchange(BOPTrees.WILLOW, WILLOW);
		addSeedExchange(BOPTrees.HELLBARK, HELLBARK);
		addSeedExchange(BOPTrees.PINE, PINE);
		addSeedExchange(BOPTrees.PALM, PALM);
		addSeedExchange(BOPTrees.MAHOGANY, MAHOGANY);
		addSeedExchange(BOPTrees.MANGROVE, MANGROVE);
		addSeedExchange(BOPTrees.EBONY, EBONY);
		addSeedExchange(BOPTrees.BAMBOO, BAMBOO);
		addSeedExchange(BOPTrees.EUCALYPTUS, EUCALYPTUS);
		// Do umbran conifer seeds manually since they don't have a sapling in BOP
		ItemStack saplingStack = BlockBOPSapling.paging.getVariantItem(BOPTrees.UMBRAN);
		ItemStack seedStack = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, UMBRANCONIFER)).getSeedStack(1);
		GameRegistry.addShapelessRecipe(new ResourceLocation(DynamicTreesBOP.MODID, UMBRANCONIFER + "sapling"),
				null, saplingStack,
				new Ingredient[] {Ingredient.fromStacks(seedStack), Ingredient.fromItem(ModItems.dirtBucket)});
		OreDictionary.registerOre("treeSapling", seedStack);
	}
	
	private static void addTransformationPotion(String tree) {
		Species species = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, tree));
		ItemStack outputStack = ModItems.dendroPotion.setTargetTree(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), species.getFamily());
		BrewingRecipeRegistry.addRecipe(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), species.getSeedStack(1), outputStack);
	}
	
	private static void addSeedExchange(BOPTrees saplingType, String species) {
		ModRecipes.createDirtBucketExchangeRecipes(BlockBOPSapling.paging.getVariantItem(saplingType),
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, species)).getSeedStack(1), true);
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		
		ModelLoader.setCustomStateMapper(rootyWater, new StateMap.Builder().ignore(BlockRootyWater.LIFE, BlockLiquid.LEVEL).build());
		
		//Register all of the models used for the tree families
		for(TreeFamily tree : ModContent.trees) {
			ModelHelper.regModel(tree.getDynamicBranch());
			ModelHelper.regModel(tree.getCommonSpecies().getSeed());
			ModelHelper.regModel(tree);
		}
		
		//Register all of the seeds that where not constructed as part of a tree family
		for(String name: new String[]{ FLOWERINGOAK, YELLOWAUTUMN, ORANGEAUTUMN, OAKDYING, MAPLE, UMBRANCONIFER, WHITECHERRY }) {
			ModelHelper.regModel(getSpeciesSeed(name));
		}
		
		//Register models for custom magic seed animation
		for (int i = 1; i <= 3; i++) ModelHelper.regModel(TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, MAGIC)).getSeed(), i);
		
		ModelLoader.setCustomStateMapper(ModContent.palmLeavesProperties.getDynamicLeavesState().getBlock(), new StateMap.Builder().ignore(BlockDynamicLeaves.TREE).build());
	}
		
	@SubscribeEvent
	public static void registerDataBaseCapability(final BiomeDataBaseJsonCapabilityRegistryEvent event) {
		
		//Add a removebopgen applier to the JSON capabilities
		event.register(REMOVEBOPGEN, (database, element, biome) -> {
			if(biome instanceof BOPBiome) {
				List<String> featuresToRemove = new ArrayList<>();
				
				if(element.isJsonPrimitive()) {
					featuresToRemove.add(element.getAsString());
				}
				else if(element.isJsonArray()) {
					for(JsonElement e : element.getAsJsonArray()) {
						if(e.isJsonPrimitive()) {
							featuresToRemove.add(e.getAsString());
						}
					}
				}
				
				for(String featureName : featuresToRemove) {
					if(CACTUS.equals(featureName) && ModConfigs.vanillaCactusWorldGen) {
						return; //Don't allow the cancellation of the cactus generator if the DynamicTrees has dual dynamic/vanilla cactus generation enabled 
					}
					((BOPBiome) biome).removeGenerator(featureName);
				}
				
			}
		});
		
	}
	
	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
		event.register(new BiomeDataBasePopulator());
	}
	
}
