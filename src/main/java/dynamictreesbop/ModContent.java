package dynamictreesbop;

import java.util.ArrayList;
import java.util.Collections;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.common.block.BlockBOPLeaves;
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
import dynamictreesbop.trees.species.SpeciesOakTwiglet;
import dynamictreesbop.trees.species.SpeciesOrangeAutumn;
import dynamictreesbop.trees.species.SpeciesPoplar;
import dynamictreesbop.trees.species.SpeciesSpruceBush;
import dynamictreesbop.trees.species.SpeciesYellowAutumn;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
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
	
	public static BlockDynamicLeavesFlowering floweringOakLeaves;
	public static BlockDynamicLeavesPalm palmFrondLeaves;
	
	// leaves properties for leaves without auto-generated leaves
	public static ILeavesProperties floweringOakLeavesProperties, decayedLeavesProperties, palmLeavesProperties;
	// leaves properties for leaves with auto-generated leaves
	public static ILeavesProperties yellowAutumnLeavesProperties, orangeAutumnLeavesProperties,
			magicLeavesProperties, umbranLeavesProperties, umbranConiferLeavesProperties,
			dyingOakLeavesProperties, firLeavesProperties, pinkCherryLeavesProperties,
			whiteCherryLeavesProperties, mapleLeavesProperties, deadLeavesProperties,
			jacarandaLeavesProperties, willowLeavesProperties, hellbarkLeavesProperties,
			pineLeavesProperties, mahoganyLeavesProperties, ebonyLeavesProperties,
			bambooLeavesProperties, eucalyptusLeavesProperties,
			oakConiferLeavesProperties, darkOakConiferLeavesProperties, darkOakDyingConiferLeavesProperties,
			oakTwigletLeavesProperties, poplarLeavesProperties, darkPoplarLeavesProperties,
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
		
		// Initialize Leaves Properties
		floweringOakLeavesProperties = new LeavesProperties(
				Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK),
				new ItemStack(Blocks.LEAVES, 1, BlockPlanks.EnumType.OAK.getMetadata()),
				TreeRegistry.findCellKit("deciduous"));
		decayedLeavesProperties = new LeavesProperties(null, ItemStack.EMPTY, TreeRegistry.findCellKit("bare"));
		palmLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.PALM),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.PALM),
				TreeRegistry.findCellKit("palm") ) {
			@Override
			public boolean appearanceChangesWithHydro() {
				return true;
			}
		};
		
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
					@Override
					public int getFlammability() {
						return 0;
					}
					@Override
					public int getFireSpreadSpeed() {
						return 0;
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
		mahoganyLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.MAHOGANY),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.MAHOGANY),
				TreeRegistry.findCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "mahogany"))){
					@Override
					public int getSmotherLeavesMax() {
						return 2;
					}
				};
		ebonyLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.EBONY),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.EBONY),
				TreeRegistry.findCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "sparse"))){
					@Override
					public int getSmotherLeavesMax() {
						return 1;
					}
				};
		bambooLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.BAMBOO),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.BAMBOO),
				TreeRegistry.findCellKit("deciduous")){
					@Override
					public int getRadiusForConnection(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, BlockBranch from, EnumFacing side, int fromRadius) {
						return from.getFamily().isCompatibleDynamicLeaves(blockAccess.getBlockState(pos), blockAccess, pos) ? 1 : 0;
					}
				};
		eucalyptusLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.EUCALYPTUS),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.EUCALYPTUS),
				TreeRegistry.findCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "eucalyptus"))){
					@Override
					public int getSmotherLeavesMax() {
						return 13;
					}
					@Override
					public int getLightRequirement() {
						return 13;
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
				TreeRegistry.findCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "poplar"))) {
					@Override
					public int getSmotherLeavesMax() {
						return 9; // because poplars are so thin and made mostly of leaves, this has to be very high
					}
					@Override
					public int getLightRequirement() {
						return 13; // allow leaves to grow under the branches to make the tree more rounded
					}
					@Override
					@SideOnly(Side.CLIENT)
					public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
						return ColorizerFoliage.getFoliageColorBirch();
					}
				};
		darkPoplarLeavesProperties = new LeavesProperties(
				Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.DARK_OAK),
				new ItemStack(Blocks.LEAVES2, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata()),
				TreeRegistry.findCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "poplar"))) {
					@Override
					public int getSmotherLeavesMax() {
						return 9; // because poplars are so thin and made mostly of leaves, this has to be very high
					}
					@Override
					public int getLightRequirement() {
						return 13; // allow leaves to grow under the branches to make the tree more rounded
					}
				};
		jungleTwigletLeavesProperties = new LeavesProperties(
				Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE),
				new ItemStack(Blocks.LEAVES, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()),
				TreeRegistry.findCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "sparse")));
		acaciaTwigletLeavesProperties = new LeavesProperties(
				Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.ACACIA),
				new ItemStack(Blocks.LEAVES2, 1, BlockPlanks.EnumType.ACACIA.getMetadata()),
				TreeRegistry.findCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "sparse")));
		acaciaBrushLeavesProperties = new LeavesProperties(
				Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.ACACIA),
				new ItemStack(Blocks.LEAVES2, 1, BlockPlanks.EnumType.ACACIA.getMetadata()),
				TreeRegistry.findCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "brush")));
		
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
				mahoganyLeavesProperties,
				ebonyLeavesProperties,
				eucalyptusLeavesProperties,
				oakConiferLeavesProperties,
				darkOakConiferLeavesProperties,
				darkOakDyingConiferLeavesProperties,
				oakTwigletLeavesProperties,
				poplarLeavesProperties,
				darkPoplarLeavesProperties,
				jungleTwigletLeavesProperties,
				acaciaTwigletLeavesProperties,
				acaciaBrushLeavesProperties,
				bambooLeavesProperties
		};
		
		// Generate leaves for leaves properties
		int seq = 0;
		for (ILeavesProperties lp : basicLeavesProperties) {
			TreeHelper.getLeavesBlockForSequence(DynamicTreesBOP.MODID, seq++, lp);
		}
		floweringOakLeavesProperties.setDynamicLeavesState(floweringOakLeaves.getDefaultState());
		floweringOakLeaves.setProperties(0, floweringOakLeavesProperties);
		
		palmLeavesProperties.setDynamicLeavesState(palmFrondLeaves.getDefaultState());
		palmFrondLeaves.setProperties(0, palmLeavesProperties);
		
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
		Species.REGISTRY.register(new SpeciesJungleTwiglet(jungleTree));
		Species.REGISTRY.register(new SpeciesAcaciaTwiglet(acaciaTree));
		Species.REGISTRY.register(new SpeciesAcaciaBrush(acaciaTree));
		
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
		TreeFamily willowTree = new TreeWillow();
		hellbarkTree = new TreeHellbark();
		TreeFamily pineTree = new TreePine();
		TreeFamily palmTree = new TreePalm();
		TreeFamily mahoganyTree = new TreeMahogany();
		TreeFamily ebonyTree = new TreeEbony();
		TreeFamily bambooTree = new TreeBamboo();
		TreeFamily eucalyptusTree = new TreeEucalyptus();
		
		Collections.addAll(trees, magicTree, umbranTree, firTree, cherryTree, deadTree, jacarandaTree, willowTree, hellbarkTree, pineTree, palmTree, mahoganyTree, ebonyTree, bambooTree, eucalyptusTree);
		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		
		// Register extra saplings
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
		//TreeHelper.getLeavesMapForModId(DynamicTreesBOP.MODID).forEach((key, block) -> registerItemBlock(registry, block));
		registry.registerAll(treeItems.toArray(new Item[treeItems.size()]));
	}
	
	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		int id = 0;
		
		EntityRegistry.registerModEntity(new ResourceLocation(DynamicTreesBOP.MODID, "maple_seed"), ItemMapleSeed.EntityItemMapleSeed.class, "maple_seed", id++, DynamicTreesBOP.MODID, 32, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(DynamicTreesBOP.MODID, "magic_seed"), ItemMagicSeed.EntityItemMagicSeed.class, "magic_seed", id++, DynamicTreesBOP.MODID, 32, 1, true);
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		for(TreeFamily tree : ModContent.trees) {
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
		// register models for custom magic seed animation
		for (int i = 1; i <= 3; i++) ModelHelper.regModel(TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBOP.MODID, "magic")).getSeed(), i);

		ModelLoader.setCustomStateMapper(ModContent.palmLeavesProperties.getDynamicLeavesState().getBlock(), new StateMap.Builder().ignore(BlockDynamicLeaves.TREE).build());
	}
	
}
