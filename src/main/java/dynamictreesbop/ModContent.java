package dynamictreesbop;

import java.util.ArrayList;
import java.util.Collections;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSaplingRare;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictrees.trees.DynamicTree;
import com.ferreusveritas.dynamictrees.trees.Species;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.common.block.BlockBOPLeaves;
import dynamictreesbop.trees.TreeDead;
import dynamictreesbop.trees.TreeFir;
import dynamictreesbop.trees.TreeHellbark;
import dynamictreesbop.trees.TreeJacaranda;
import dynamictreesbop.trees.TreeMagic;
import dynamictreesbop.trees.TreePine;
import dynamictreesbop.trees.TreeCherry;
import dynamictreesbop.trees.TreeUmbran;
import dynamictreesbop.trees.TreeWillow;
import dynamictreesbop.blocks.BlockDynamicLeavesFlowering;
import dynamictreesbop.items.ItemMapleSeed;
import dynamictreesbop.trees.species.SpeciesDarkOakConifer;
import dynamictreesbop.trees.species.SpeciesDarkOakDyingConifer;
import dynamictreesbop.trees.species.SpeciesDyingOak;
import dynamictreesbop.trees.species.SpeciesFloweringOak;
import dynamictreesbop.trees.species.SpeciesMaple;
import dynamictreesbop.trees.species.SpeciesMegaOakConifer;
import dynamictreesbop.trees.species.SpeciesOakConifer;
import dynamictreesbop.trees.species.SpeciesOakFloweringVine;
import dynamictreesbop.trees.species.SpeciesOakTwiglet;
import dynamictreesbop.trees.species.SpeciesOrangeAutumn;
import dynamictreesbop.trees.species.SpeciesPoplar;
import dynamictreesbop.trees.species.SpeciesYellowAutumn;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = DynamicTreesBOP.MODID)
@ObjectHolder(DynamicTreesBOP.MODID)
public class ModContent {
	
	public static final Block leaves_flowering = null;
	
	// leaves properties for leaves without auto-generated leaves
	public static ILeavesProperties floweringOakLeavesProperties, decayedLeavesProperties;
	// leaves properties for leaves with auto-generated leaves
	public static ILeavesProperties yellowAutumnLeavesProperties, orangeAutumnLeavesProperties,
			magicLeavesProperties, umbranLeavesProperties, umbranConiferLeavesProperties,
			dyingOakLeavesProperties, firLeavesProperties, pinkCherryLeavesProperties,
			whiteCherryLeavesProperties, mapleLeavesProperties, deadLeavesProperties,
			jacarandaLeavesProperties, willowLeavesProperties, hellbarkLeavesProperties,
			pineLeavesProperties,
			oakConiferLeavesProperties, darkOakConiferLeavesProperties, darkOakDyingConiferLeavesProperties,
			oakTwigletLeavesProperties, poplarLeavesProperties, darkPoplarLeavesProperties;
	
	// array of leaves properties with auto-generated leaves
	public static ILeavesProperties[] basicLeavesProperties;
	
	// trees added by this mod
	public static ArrayList<DynamicTree> trees = new ArrayList<DynamicTree>();
	
	// store hellbark out here so it can be quickly accessed for world gen
	public static DynamicTree hellbarkTree;
	
	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		
		// Register Special Leaf Blocks
		BlockDynamicLeavesFlowering floweringOakLeaves = new BlockDynamicLeavesFlowering();
		registry.register(floweringOakLeaves);
		
		// Get tree types from base mod so they can be given new species
		DynamicTree oakTree = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oak")).getTree();
		DynamicTree spruceTree = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "spruce")).getTree();
		DynamicTree birchTree = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "birch")).getTree();
		DynamicTree jungleTree = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "jungle")).getTree();
		DynamicTree acaciaTree = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "acacia")).getTree();
		DynamicTree darkOakTree = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "darkoak")).getTree();
		
		// Add BOP dirt and grass as acceptable soils to species from base Dynamic Trees
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oak")).addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oakswamp")).addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "apple")).addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "spruce")).addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "birch")).addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "jungle")).addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "acacia")).addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "darkoak")).addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		
		// Initialize Leaves Properties
		floweringOakLeavesProperties = new LeavesProperties(
				Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK),
				new ItemStack(Blocks.LEAVES, 1, BlockPlanks.EnumType.OAK.getMetadata()),
				TreeRegistry.findCellKit("deciduous"));
		decayedLeavesProperties = new LeavesProperties(null, ItemStack.EMPTY, TreeRegistry.findCellKit("bare"));
		
		yellowAutumnLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.YELLOW_AUTUMN),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.YELLOW_AUTUMN),
				TreeRegistry.findCellKit("deciduous")) {
					@Override
					public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
						return 0xffffff;
					}
				};
		orangeAutumnLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.ORANGE_AUTUMN),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.ORANGE_AUTUMN),
				TreeRegistry.findCellKit("deciduous")) {
					@Override
					public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
						return 0xffffff;
					}
				};
		magicLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.MAGIC),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.MAGIC),
				TreeRegistry.findCellKit("deciduous")) {
					@Override
					public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
						return 0xffffff;
					}
				};
		umbranLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.UMBRAN),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.UMBRAN),
				TreeRegistry.findCellKit("deciduous")) {
					@Override
					public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
						return 0xffffff;
					}
				};
		umbranConiferLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.UMBRAN),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.UMBRAN),
				TreeRegistry.findCellKit("conifer")) {
					@Override
					public int getSmotherLeavesMax() {
						return 3;
					}
					@Override
					public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
						return 0xffffff;
					}
				};
		dyingOakLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.DEAD),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.DEAD),
				TreeRegistry.findCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "sparse"))) {
					@Override
					public int getSmotherLeavesMax() {
						return 1;
					}
					@Override
					public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
						return 0xffffff;
					}
				};
		firLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.FIR),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.FIR),
				TreeRegistry.findCellKit("conifer")){
					@Override
					public int getSmotherLeavesMax() {
						return 3;
					}
					@Override
					public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
						return 0xffffff;
					}
				};
		pinkCherryLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.PINK_CHERRY),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.PINK_CHERRY),
				TreeRegistry.findCellKit("deciduous")) {
					@Override
					public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
						return 0xffffff;
					}
				};
		whiteCherryLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.WHITE_CHERRY),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.WHITE_CHERRY),
				TreeRegistry.findCellKit("deciduous")) {
					@Override
					public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
						return 0xffffff;
					}
				};
		mapleLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.MAPLE),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.MAPLE),
				TreeRegistry.findCellKit("deciduous")) {
					@Override
					public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
						return 0xffffff;
					}
				};
		deadLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.DEAD),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.DEAD),
				TreeRegistry.findCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "sparse"))) {
					@Override
					public int getSmotherLeavesMax() {
						return 1;
					}
					@Override
					public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
						return 0xffffff;
					}
				};
		jacarandaLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.JACARANDA),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.JACARANDA),
				TreeRegistry.findCellKit("deciduous")) {
					@Override
					public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
						return 0xffffff;
					}
				};
		willowLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.WILLOW),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.WILLOW),
				TreeRegistry.findCellKit("deciduous"));
		hellbarkLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.HELLBARK),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.HELLBARK),
				TreeRegistry.findCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "sparse"))) {
					@Override
					public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
						return 0xffffff;
					}
				};
		pineLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.PINE),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.PINE),
				TreeRegistry.findCellKit("conifer")){
					@Override
					public int getSmotherLeavesMax() {
						return 13; // because pines are so thin and made mostly of leaves, this has to be very high
					}
				};
		
		oakConiferLeavesProperties = new LeavesProperties(
				Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK),
				new ItemStack(Blocks.LEAVES, 1, BlockPlanks.EnumType.OAK.getMetadata()),
				TreeRegistry.findCellKit("conifer")){
					@Override
					public int getSmotherLeavesMax() {
						return 3;
					}
				};
		darkOakConiferLeavesProperties = new LeavesProperties(
				Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.DARK_OAK),
				new ItemStack(Blocks.LEAVES2, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata()),
				TreeRegistry.findCellKit("conifer")){
					@Override
					public int getSmotherLeavesMax() {
						return 3;
					}
				};
		darkOakDyingConiferLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.DEAD),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.DEAD),
				TreeRegistry.findCellKit("conifer")){
					@Override
					public int getSmotherLeavesMax() {
						return 3;
					}
				};
		oakTwigletLeavesProperties = new LeavesProperties(
				Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK),
				new ItemStack(Blocks.LEAVES, 1, BlockPlanks.EnumType.OAK.getMetadata()),
				TreeRegistry.findCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "sparse")));
		poplarLeavesProperties = new LeavesProperties(
				Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH),
				new ItemStack(Blocks.LEAVES, 1, BlockPlanks.EnumType.BIRCH.getMetadata()),
				TreeRegistry.findCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "poplar"))){
					@Override
					public int getSmotherLeavesMax() {
						return 9; // because poplars are so thin and made mostly of leaves, this has to be very high
					}
					@Override
					public int getLightRequirement() {
						return 13; // allow leaves to grow under the branches to make the tree more rounded
					}
				};
		darkPoplarLeavesProperties = new LeavesProperties(
				Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.DARK_OAK),
				new ItemStack(Blocks.LEAVES2, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata()),
				TreeRegistry.findCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "poplar"))){
					@Override
					public int getSmotherLeavesMax() {
						return 9; // because poplars are so thin and made mostly of leaves, this has to be very high
					}
					@Override
					public int getLightRequirement() {
						return 13; // allow leaves to grow under the branches to make the tree more rounded
					}
				};
		
		// Add leaves properties that need leaves to be generated to an array
		basicLeavesProperties = new ILeavesProperties[] {
				yellowAutumnLeavesProperties,
				orangeAutumnLeavesProperties,
				magicLeavesProperties,
				umbranLeavesProperties,
				umbranConiferLeavesProperties,
				dyingOakLeavesProperties,
				firLeavesProperties,
				pinkCherryLeavesProperties,
				whiteCherryLeavesProperties,
				mapleLeavesProperties,
				hellbarkLeavesProperties,
				deadLeavesProperties,
				jacarandaLeavesProperties,
				LeavesProperties.NULLPROPERTIES, // placeholder for mangrove
				LeavesProperties.NULLPROPERTIES, // placeholder for redwood
				willowLeavesProperties,
				pineLeavesProperties,
				LeavesProperties.NULLPROPERTIES, // placeholder for mahogany
				LeavesProperties.NULLPROPERTIES, // placeholder for ebony
				LeavesProperties.NULLPROPERTIES, // placeholder for eucalyptus
				oakConiferLeavesProperties,
				darkOakConiferLeavesProperties,
				darkOakDyingConiferLeavesProperties,
				oakTwigletLeavesProperties,
				poplarLeavesProperties,
				darkPoplarLeavesProperties,
		};
		
		// Generate leaves for leaves properties
		int seq = 0;
		for (ILeavesProperties lp : basicLeavesProperties) {
			TreeHelper.getLeavesBlockForSequence(DynamicTreesBOP.MODID, seq++, lp);
		}
		floweringOakLeavesProperties.setDynamicLeavesState(floweringOakLeaves.getDefaultState());
		floweringOakLeaves.setProperties(0, floweringOakLeavesProperties);
			
		// Register new species of trees from the base mod
		Species.REGISTRY.register(new SpeciesOakFloweringVine(oakTree));
		Species.REGISTRY.register(new SpeciesFloweringOak(oakTree));
		Species.REGISTRY.register(new SpeciesYellowAutumn(birchTree));
		Species.REGISTRY.register(new SpeciesOrangeAutumn(oakTree));
		Species.REGISTRY.register(new SpeciesDyingOak(oakTree));
		Species.REGISTRY.register(new SpeciesMaple(oakTree));
		Species.REGISTRY.register(new SpeciesOakConifer(oakTree));
		Species.REGISTRY.register(new SpeciesMegaOakConifer(oakTree));
		Species.REGISTRY.register(new SpeciesDarkOakConifer(darkOakTree));
		Species.REGISTRY.register(new SpeciesDarkOakDyingConifer(darkOakTree));
		Species.REGISTRY.register(new SpeciesOakTwiglet(oakTree));
		Species.REGISTRY.register(new SpeciesPoplar(birchTree, "poplar", poplarLeavesProperties));
		Species.REGISTRY.register(new SpeciesPoplar(darkOakTree, "darkpoplar", darkPoplarLeavesProperties));
		
		// Register new tree types
		DynamicTree magicTree = new TreeMagic();
		DynamicTree umbranTree = new TreeUmbran();
		DynamicTree firTree = new TreeFir();
		DynamicTree cherryTree = new TreeCherry();
		DynamicTree deadTree = new TreeDead();
		DynamicTree jacarandaTree = new TreeJacaranda();
		DynamicTree willowTree = new TreeWillow();
		hellbarkTree = new TreeHellbark();
		DynamicTree pineTree = new TreePine();
		
		Collections.addAll(trees, magicTree, umbranTree, firTree, cherryTree, deadTree, jacarandaTree, willowTree, hellbarkTree, pineTree);
		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		
		// Register saplings
		registry.registerAll(
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "floweringoak")).getDynamicSapling().getBlock(),
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "yellowautumn")).getDynamicSapling().getBlock(),
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "orangeautumn")).getDynamicSapling().getBlock(),
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "oakdying")).getDynamicSapling().getBlock(),
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "maple")).getDynamicSapling().getBlock()
		);
		
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(TreeHelper.getLeavesMapForModId(DynamicTreesBOP.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		
		// register ItemBlocks
		registerItemBlock(registry, leaves_flowering);
		
		// register seeds
		registry.registerAll(
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "floweringoak")).getSeed(),
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "yellowautumn")).getSeed(),
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "orangeautumn")).getSeed(),
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "oakdying")).getSeed(),
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "maple")).getSeed()
		);
		
		ArrayList<Item> treeItems = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		TreeHelper.getLeavesMapForModId(DynamicTreesBOP.MODID).forEach((key, block) -> registerItemBlock(registry, block));
		registry.registerAll(treeItems.toArray(new Item[treeItems.size()]));
	}
	
	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		int id = 0;
		
		EntityRegistry.registerModEntity(new ResourceLocation(DynamicTreesBOP.MODID, "maple_seed"), ItemMapleSeed.EntityItemMapleSeed.class, "maple_seed", id++, DynamicTreesBOP.MODID, 32, 1, true);
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		for(DynamicTree tree : ModContent.trees) {
			ModelHelper.regModel(tree.getDynamicBranch());
			ModelHelper.regModel(tree.getCommonSpecies().getSeed());
			ModelHelper.regModel(tree);
		}
		ModelHelper.regModel(TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "floweringoak")).getSeed());
		ModelHelper.regModel(TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "yellowautumn")).getSeed());
		ModelHelper.regModel(TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "orangeautumn")).getSeed());
		ModelHelper.regModel(TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "oakdying")).getSeed());
		ModelHelper.regModel(TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "maple")).getSeed());
		ModelHelper.regModel(TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "floweringoak")).getSeed());
		ModelHelper.regModel(TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "umbranconifer")).getSeed());
		ModelHelper.regModel(TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "whitecherry")).getSeed());
		
		for (BlockDynamicLeaves leaves : TreeHelper.getLeavesMapForModId(DynamicTreesBOP.MODID).values()) {
			Item item = Item.getItemFromBlock(leaves);
			ModelHelper.regModel(item);
		}
		ModelHelper.regModel(Item.getItemFromBlock(leaves_flowering));
	}
	
	private static void registerItemBlock(IForgeRegistry<Item> registry, Block block) {
		registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}
	
}
