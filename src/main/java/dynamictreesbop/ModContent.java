package dynamictreesbop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.function.Function;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.ModItems;
import com.ferreusveritas.dynamictrees.ModRecipes;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.LeavesPropertiesJson;
import com.ferreusveritas.dynamictrees.blocks.LeavesPropertiesJson.PrimitiveLeavesComponents;
import com.ferreusveritas.dynamictrees.items.DendroPotion.DendroPotionType;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.google.gson.JsonElement;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
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
import dynamictreesbop.trees.species.SpeciesOakFloweringVine;
import dynamictreesbop.trees.species.SpeciesOakSparse;
import dynamictreesbop.trees.species.SpeciesOakTwiglet;
import dynamictreesbop.trees.species.SpeciesOrangeAutumn;
import dynamictreesbop.trees.species.SpeciesPoplar;
import dynamictreesbop.trees.species.SpeciesSpruceBush;
import dynamictreesbop.trees.species.SpeciesYellowAutumn;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
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
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = DynamicTreesBOP.MODID)
@ObjectHolder(DynamicTreesBOP.MODID)
public class ModContent {
	
	public static BlockDynamicLeavesFlowering floweringOakLeaves;
	public static BlockDynamicLeavesPalm palmFrondLeaves;
	
	// leaves properties for leaves without auto-generated leaves
	public static ILeavesProperties floweringOakLeavesProperties, decayedLeavesProperties, palmLeavesProperties;
	// leaves properties for leaves with auto-generated leaves
	public static ILeavesProperties yellowAutumnLeavesProperties, orangeAutumnLeavesProperties,
			magicLeavesProperties, umbranLeavesProperties, umbranConiferLeavesProperties,
			dyingOakLeavesProperties, firLeavesProperties, pinkCherryLeavesProperties,
			whiteCherryLeavesProperties, mapleLeavesProperties, deadLeavesProperties,
			jacarandaLeavesProperties, redwoodLeavesProperties, willowLeavesProperties, hellbarkLeavesProperties,
			pineLeavesProperties, mahoganyLeavesProperties, ebonyLeavesProperties,
			bambooLeavesProperties, eucalyptusLeavesProperties,
			oakConiferLeavesProperties, darkOakConiferLeavesProperties, darkOakDyingConiferLeavesProperties,
			oakSparseLeavesProperties, poplarLeavesProperties, darkPoplarLeavesProperties,
			jungleTwigletLeavesProperties, acaciaTwigletLeavesProperties, acaciaBrushLeavesProperties;
	
	// array of leaves properties with auto-generated leaves
	public static ILeavesProperties[] basicLeavesProperties;
	
	// trees added by this mod
	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();
	
	// store hellbark out here so it can be quickly accessed for world gen
	public static TreeFamily hellbarkTree;
	
	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		
		// Register Special Leaf Blocks
		floweringOakLeaves = new BlockDynamicLeavesFlowering();
		registry.register(floweringOakLeaves);
		palmFrondLeaves = new BlockDynamicLeavesPalm();
		registry.register(palmFrondLeaves);
		
		// Get tree types from base mod so they can be given new species
		TreeFamily oakTree = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oak")).getFamily();
		//DynamicTree spruceTree = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "spruce")).getTree();
		TreeFamily birchTree = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "birch")).getFamily();
		TreeFamily jungleTree = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "jungle")).getFamily();
		TreeFamily acaciaTree = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "acacia")).getFamily();
		TreeFamily darkOakTree = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "darkoak")).getFamily();
		
		// Add BOP dirt and grass as acceptable soils to species from base Dynamic Trees
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oak")).addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oakswamp")).addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "apple")).addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "spruce")).addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "birch")).addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "jungle")).addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "acacia")).addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt, Blocks.HARDENED_CLAY);
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "darkoak")).addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "cactus")).addAcceptableSoil(BOPBlocks.white_sand);
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "mushroomred")).addAcceptableSoil(BOPBlocks.dirt, BOPBlocks.grass);
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "mushroombrn")).addAcceptableSoil(BOPBlocks.dirt, BOPBlocks.grass);
		
		//This registers a finding function for use with Json leaves properties
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
		
		// Initialize Leaves Properties
		floweringOakLeavesProperties = new LeavesProperties(
				Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK),
				new ItemStack(Blocks.LEAVES, 1, BlockPlanks.EnumType.OAK.getMetadata())) {
					Random rand = new Random();
					@Override
					public IBlockState getDynamicLeavesState(int hydro) {
						if (rand.nextInt(4) == 0) {
							return super.getDynamicLeavesState(hydro).withProperty(BlockDynamicLeavesFlowering.CAN_FLOWER, true).withProperty(BlockDynamicLeavesFlowering.FLOWERING, true);
						}
						return super.getDynamicLeavesState(hydro);
					}
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
		
		decayedLeavesProperties 		= new LeavesPropertiesJson("{`cellkit`:`bare`}");
		
		//Leaves properties that are to be LeavesPaging generated from an array
		basicLeavesProperties = new ILeavesProperties[] {
				yellowAutumnLeavesProperties		= new LeavesPropertiesJson("{`leaves`:{`bop`:`yellow_autumn`},`color`:`#ffffff`}"),
				orangeAutumnLeavesProperties		= new LeavesPropertiesJson("{`leaves`:{`bop`:`orange_autumn`},`color`:`#ffffff`}"),
				magicLeavesProperties				= new LeavesPropertiesJson("{`leaves`:{`bop`:`magic`},`color`:`#ffffff`}"),
				umbranLeavesProperties				= new LeavesPropertiesJson("{`leaves`:{`bop`:`umbran`},`color`:`#ffffff`}"),
				umbranConiferLeavesProperties		= new LeavesPropertiesJson("{`leaves`:{`bop`:`umbran`},`color`:`#ffffff`,`cellkit`:`conifer`,`smother`:4}"),
				dyingOakLeavesProperties			= new LeavesPropertiesJson("{`leaves`:{`bop`:`dead`},`color`:`#ffffff`,`cellkit`:`dynamictreesbop:sparse`,`smother`:1}"),
				firLeavesProperties					= new LeavesPropertiesJson("{`leaves`:{`bop`:`fir`},`color`:`#ffffff`,`cellkit`:`conifer`,`smother`:5}"),
				pinkCherryLeavesProperties			= new LeavesPropertiesJson("{`leaves`:{`bop`:`pink_cherry`},`color`:`#ffffff`}"),
				whiteCherryLeavesProperties			= new LeavesPropertiesJson("{`leaves`:{`bop`:`white_cherry`},`color`:`#ffffff`}"),
				mapleLeavesProperties				= new LeavesPropertiesJson("{`leaves`:{`bop`:`maple`},`color`:`#ffffff`}"),
				hellbarkLeavesProperties			= new LeavesPropertiesJson("{`leaves`:{`bop`:`hellbark`},`color`:`#ffffff`,`cellkit`:`dynamictreesbop:sparse`,`flammability`:0,`fireSpreadSpeed`:0,`light`:0}"),
				deadLeavesProperties				= new LeavesPropertiesJson("{`leaves`:{`bop`:`dead`},`color`:`#ffffff`,`cellkit`:`dynamictreesbop:sparse`,`smother`:1}"),
				jacarandaLeavesProperties			= new LeavesPropertiesJson("{`leaves`:{`bop`:`jacaranda`},`color`:`#ffffff`}"),
				LeavesProperties.NULLPROPERTIES, // placeholder for mangrove
				redwoodLeavesProperties				= new LeavesPropertiesJson("{`leaves`:{`bop`:`redwood`},`smother`:26,`light`:9}"),
				willowLeavesProperties				= new LeavesPropertiesJson("{`leaves`:{`bop`:`willow`},`smother`:3}"),
				pineLeavesProperties				= new LeavesPropertiesJson("{`leaves`:{`bop`:`pine`},`cellkit`:`conifer`,`smother`:13}"),
				mahoganyLeavesProperties 			= new LeavesPropertiesJson("{`leaves`:{`bop`:`mahogany`},`cellkit`:`dynamictreesbop:mahogany`,`smother`:2}"),
				ebonyLeavesProperties				= new LeavesPropertiesJson("{`leaves`:{`bop`:`ebony`},`cellkit`:`dynamictreesbop:sparse`,`smother`:1}"),
				eucalyptusLeavesProperties			= new LeavesPropertiesJson("{`leaves`:{`bop`:`eucalyptus`},`cellkit`:`dynamictreesbop:eucalyptus`,`smother`:13,`light`;13}"),
				oakConiferLeavesProperties			= new LeavesPropertiesJson("{`leaves`:`minecraft:leaves variant=oak`,`cellkit`:`conifer`,`smother`:4}"),
				darkOakConiferLeavesProperties		= new LeavesPropertiesJson("{`leaves`:`minecraft:leaves2 variant=dark_oak`,`cellkit`:`conifer`,`smother`:3}"),
				darkOakDyingConiferLeavesProperties	= new LeavesPropertiesJson("{`leaves`:{`bop`:`dead`},`cellkit`:`conifer`,`smother`:3}"),
				oakSparseLeavesProperties			= new LeavesPropertiesJson("{`leaves`:`minecraft:leaves variant=oak`,`cellkit`:`dynamictreesbop:sparse`}"),
				poplarLeavesProperties				= new LeavesPropertiesJson("{`leaves`:`minecraft:leaves variant=birch`,`color`:`@biome`,`cellkit`:`dynamictreesbop:poplar`,`smother`:9,`light`:13}"),
				darkPoplarLeavesProperties			= new LeavesPropertiesJson("{`leaves`:`minecraft:leaves2 variant=dark_oak`,`cellkit`:`dynamictreesbop:poplar`,`smother`:9,`light`:13}"),
				jungleTwigletLeavesProperties		= new LeavesPropertiesJson("{`leaves`:`minecraft:leaves variant=jungle`,`cellkit`:`dynamictreesbop:sparse`}"),
				acaciaTwigletLeavesProperties		= new LeavesPropertiesJson("{`leaves`:`minecraft:leaves2 variant=acacia`,`cellkit`:`dynamictreesbop:sparse`}"),
				acaciaBrushLeavesProperties			= new LeavesPropertiesJson("{`leaves`:`minecraft:leaves2 variant=acacia`,`cellkit`:`dynamictreesbop:brush`}"),
				bambooLeavesProperties				= new LeavesPropertiesJson("{`leaves`:{`bop`:`bamboo`},`cellkit`:`dynamictreesbop:bamboo`,`smother`:6,`light`;13,`connectAny`:true}")
		};
		
		// Generate leaves for leaves properties
		for (ILeavesProperties lp : basicLeavesProperties) {
			LeavesPaging.getNextLeavesBlock(DynamicTreesBOP.MODID, lp);
		}
		
		// Leaves properties that could not be automatically paging generated
		floweringOakLeavesProperties.setDynamicLeavesState(floweringOakLeaves.getDefaultState());
		floweringOakLeaves.setProperties(0, floweringOakLeavesProperties);
		palmLeavesProperties.setDynamicLeavesState(palmFrondLeaves.getDefaultState());
		palmFrondLeaves.setProperties(0, palmLeavesProperties);
		
		// Register new species of trees from the base mod
		Species.REGISTRY.register(new SpeciesOakFloweringVine(oakTree));
		Species.REGISTRY.register(new SpeciesFloweringOak(oakTree));
		Species.REGISTRY.register(new SpeciesYellowAutumn(birchTree));
		Species.REGISTRY.register(new SpeciesOrangeAutumn(darkOakTree));
		Species.REGISTRY.register(new SpeciesDyingOak(oakTree));
		Species.REGISTRY.register(new SpeciesMaple(oakTree));
		Species.REGISTRY.register(new SpeciesOakConifer(oakTree));
		Species.REGISTRY.register(new SpeciesMegaOakConifer(oakTree));
		Species.REGISTRY.register(new SpeciesDarkOakConifer(darkOakTree));
		Species.REGISTRY.register(new SpeciesDarkOakDyingConifer(darkOakTree));
		Species.REGISTRY.register(new SpeciesOakTwiglet(oakTree));
		Species.REGISTRY.register(new SpeciesPoplar(birchTree, "poplar", poplarLeavesProperties));
		Species.REGISTRY.register(new SpeciesPoplar(darkOakTree, "darkpoplar", darkPoplarLeavesProperties));
		Species.REGISTRY.register(new SpeciesJungleTwiglet(jungleTree));
		Species.REGISTRY.register(new SpeciesAcaciaTwiglet(acaciaTree));
		Species.REGISTRY.register(new SpeciesAcaciaBrush(acaciaTree));
		Species.REGISTRY.register(new SpeciesOakSparse(oakTree));
		
		// Register bush dummies
		Species.REGISTRY.register(new SpeciesAcaciaBush());
		Species.REGISTRY.register(new SpeciesOakBush());
		Species.REGISTRY.register(new SpeciesSpruceBush());
		
		// Register new tree types
		TreeFamily magicTree = new TreeMagic();
		TreeFamily umbranTree = new TreeUmbran();
		TreeFamily firTree = new TreeFir();
		TreeFamily cherryTree = new TreeCherry();
		TreeFamily deadTree = new TreeDead();
		TreeFamily jacarandaTree = new TreeJacaranda();
		TreeFamily redwoodTree = new TreeRedwood();
		TreeFamily willowTree = new TreeWillow();
		hellbarkTree = new TreeHellbark();
		TreeFamily pineTree = new TreePine();
		TreeFamily palmTree = new TreePalm();
		TreeFamily mahoganyTree = new TreeMahogany();
		TreeFamily ebonyTree = new TreeEbony();
		TreeFamily bambooTree = new TreeBamboo();
		TreeFamily eucalyptusTree = new TreeEucalyptus();
		
		Collections.addAll(trees, magicTree, umbranTree, firTree, cherryTree, deadTree, jacarandaTree, redwoodTree, willowTree, hellbarkTree, pineTree, palmTree, mahoganyTree, ebonyTree, bambooTree, eucalyptusTree);
		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesBOP.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		
		// register seeds
		registry.registerAll(
			getSpeciesSeed("floweringoak"),
			getSpeciesSeed("yellowautumn"),
			getSpeciesSeed("orangeautumn"),
			getSpeciesSeed("oakdying"),
			getSpeciesSeed("maple")
		);
		
		ArrayList<Item> treeItems = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		registry.registerAll(treeItems.toArray(new Item[treeItems.size()]));
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
		String[] trees = new String[] {
				"magic", "umbranconifer", "umbran", "fir", "whitecherry", "pinkcherry",
				"jacaranda", "redwood", "willow", "hellbark", "pine", "mahogany",
				"ebony", "eucalyptus",
		};
		for (String tree : trees) {
			addTransformationPotion(tree);
		}
		// Do dead trees separately because the seed grows a species of oak tree
		ItemStack outputStack = ModItems.dendroPotion.setTargetTree(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "dead")).getFamily());
		BrewingRecipeRegistry.addRecipe(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "oakdying")).getSeedStack(1), outputStack);
		
		// Add seed <-> sapling recipes
		addSeedExchange(BOPTrees.FLOWERING, "floweringoak");
		addSeedExchange(BOPTrees.YELLOW_AUTUMN, "yellowautumn");
		addSeedExchange(BOPTrees.ORANGE_AUTUMN, "orangeautumn");
		addSeedExchange(BOPTrees.DEAD, "oakdying");
		addSeedExchange(BOPTrees.MAPLE, "maple");
		addSeedExchange(BOPTrees.MAGIC, "magic");
		addSeedExchange(BOPTrees.UMBRAN, "umbran");
		addSeedExchange(BOPTrees.FIR, "fir");
		addSeedExchange(BOPTrees.WHITE_CHERRY, "whitecherry");
		addSeedExchange(BOPTrees.PINK_CHERRY, "pinkcherry");
		addSeedExchange(BOPTrees.JACARANDA, "jacaranda");
		addSeedExchange(BOPTrees.REDWOOD, "redwood");
		addSeedExchange(BOPTrees.WILLOW, "willow");
		addSeedExchange(BOPTrees.HELLBARK, "hellbark");
		addSeedExchange(BOPTrees.PINE, "pine");
		addSeedExchange(BOPTrees.PALM, "palm");
		addSeedExchange(BOPTrees.MAHOGANY, "mahogany");
		addSeedExchange(BOPTrees.EBONY, "ebony");
		addSeedExchange(BOPTrees.BAMBOO, "bamboo");
		addSeedExchange(BOPTrees.EUCALYPTUS, "eucalyptus");
		// Do umbran conifer seeds manually since they don't have a sapling in BOP
		ItemStack saplingStack = BlockBOPSapling.paging.getVariantItem(BOPTrees.UMBRAN);
		ItemStack seedStack = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "umbranconifer")).getSeedStack(1);
		GameRegistry.addShapelessRecipe(new ResourceLocation(DynamicTreesBOP.MODID, "umbranconifer" + "sapling"),
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
		
		//Register all of the models used for the tree families
		for(TreeFamily tree : ModContent.trees) {
			ModelHelper.regModel(tree.getDynamicBranch());
			ModelHelper.regModel(tree.getCommonSpecies().getSeed());
			ModelHelper.regModel(tree);
		}
		
		//Register all of the seeds that where not constructed as part of a tree family
		for(String name: new String[]{ "floweringoak", "yellowautumn", "orangeautumn", "oakdying", "maple", "umbranconifer", "whitecherry" }) {
			ModelHelper.regModel(getSpeciesSeed(name));
		}
		
		//Register models for custom magic seed animation
		for (int i = 1; i <= 3; i++) ModelHelper.regModel(TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "magic")).getSeed(), i);

		LeavesPaging.getLeavesMapForModId(DynamicTreesBOP.MODID).forEach((key,leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));
		
		ModelLoader.setCustomStateMapper(ModContent.palmLeavesProperties.getDynamicLeavesState().getBlock(), new StateMap.Builder().ignore(BlockDynamicLeaves.TREE).build());
	}
	
}
