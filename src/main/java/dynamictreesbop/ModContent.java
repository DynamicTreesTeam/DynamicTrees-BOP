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
import dynamictreesbop.trees.TreeJacaranda;
import dynamictreesbop.trees.TreeMagic;
import dynamictreesbop.trees.TreeCherry;
import dynamictreesbop.trees.TreeUmbran;
import dynamictreesbop.blocks.BlockDynamicLeavesFlowering;
import dynamictreesbop.trees.species.SpeciesFloweringOak;
import dynamictreesbop.trees.species.SpeciesMaple;
import dynamictreesbop.trees.species.SpeciesOakFloweringVine;
import dynamictreesbop.trees.species.SpeciesOrangeAutumn;
import dynamictreesbop.trees.species.SpeciesYellowAutumn;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = DynamicTreesBOP.MODID)
@ObjectHolder(DynamicTreesBOP.MODID)
public class ModContent {
	
	public static final Block leaves_flowering = null;
	
	public static ILeavesProperties floweringOakLeavesProperties, decayedLeavesProperties;
	public static ILeavesProperties yellowAutumnLeavesProperties, orangeAutumnLeavesProperties,
			magicLeavesProperties, umbranLeavesProperties, umbranConiferLeavesProperties,
			dyingOakLeavesProperties, firLeavesProperties, pinkCherryLeavesProperties,
			whiteCherryLeavesProperties, mapleLeavesProperties, deadLeavesProperties,
			jacarandaLeavesProperties;
	
	public static ILeavesProperties[] basicLeavesProperties;
	
	public static ArrayList<DynamicTree> trees = new ArrayList<DynamicTree>();
	
	public enum Tree {
		MAGIC,
		UMBRAN,
		FIR,
		CHERRY,
		MAPLE,
		HELLBARK,
		DEAD,
		JACARANDA,
		MANGROVE,
		REDWOOD,
		WILLOW,
		PINE,
		MAHOGANY,
		EBONY,
		EUCALYPTUS;
		
		public int getSeq() {
			return ordinal();
		}
	}
	
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
				TreeRegistry.findCellKit("deciduous"));
		orangeAutumnLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.ORANGE_AUTUMN),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.ORANGE_AUTUMN),
				TreeRegistry.findCellKit("deciduous"));
		magicLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.MAGIC),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.MAGIC),
				TreeRegistry.findCellKit("deciduous"));
		umbranLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.UMBRAN),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.UMBRAN),
				TreeRegistry.findCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "deciduous")));
		umbranConiferLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.UMBRAN),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.UMBRAN),
				TreeRegistry.findCellKit("conifer")) {
					@Override
					public int getSmotherLeavesMax() {
						return 3;
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
				};
		firLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.FIR),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.FIR),
				TreeRegistry.findCellKit("conifer")){
					@Override
					public int getSmotherLeavesMax() {
						return 3;
					}
				};
		pinkCherryLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.PINK_CHERRY),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.PINK_CHERRY),
				TreeRegistry.findCellKit("deciduous"));
		whiteCherryLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.WHITE_CHERRY),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.WHITE_CHERRY),
				TreeRegistry.findCellKit("deciduous"));
		mapleLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.MAPLE),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.MAPLE),
				TreeRegistry.findCellKit("deciduous"));
		deadLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.DEAD),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.DEAD),
				TreeRegistry.findCellKit(new ResourceLocation(DynamicTreesBOP.MODID, "sparse"))) {
					@Override
					public int getSmotherLeavesMax() {
						return 1;
					}
				};
		jacarandaLeavesProperties = new LeavesProperties(
				BlockBOPLeaves.paging.getVariantState(BOPTrees.JACARANDA),
				BlockBOPLeaves.paging.getVariantItem(BOPTrees.JACARANDA),
				TreeRegistry.findCellKit("deciduous"));
		
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
				LeavesProperties.NULLPROPERTIES, // placeholder for hellbark
				deadLeavesProperties,
				jacarandaLeavesProperties,
				LeavesProperties.NULLPROPERTIES, // placeholder for mangrove
				LeavesProperties.NULLPROPERTIES, // placeholder for redwood
				LeavesProperties.NULLPROPERTIES, // placeholder for willow
				LeavesProperties.NULLPROPERTIES, // placeholder for pine
				LeavesProperties.NULLPROPERTIES, // placeholder for mahogany
				LeavesProperties.NULLPROPERTIES, // placeholder for ebony
				LeavesProperties.NULLPROPERTIES, // placeholder for eucalyptus
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
		Species.REGISTRY.register(new SpeciesMaple(oakTree));
		
		// Register new tree types
		DynamicTree magicTree = new TreeMagic();
		DynamicTree umbranTree = new TreeUmbran();
		DynamicTree firTree = new TreeFir();
		DynamicTree cherryTree = new TreeCherry();
		DynamicTree deadTree = new TreeDead();
		DynamicTree jacarandaTree = new TreeJacaranda();
		
		Collections.addAll(trees, magicTree, umbranTree, firTree, cherryTree, deadTree, jacarandaTree);
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
