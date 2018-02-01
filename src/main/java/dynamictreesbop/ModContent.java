package dynamictreesbop;

import java.util.ArrayList;
import java.util.Collections;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSaplingRare;
import com.ferreusveritas.dynamictrees.trees.DynamicTree;
import com.ferreusveritas.dynamictrees.trees.Species;

import dynamictreesbop.trees.TreeFloweringOak;
import dynamictreesbop.trees.TreeMagic;
import dynamictreesbop.trees.TreeOrangeAutumn;
import dynamictreesbop.trees.TreeUmbran;
import dynamictreesbop.trees.TreeUmbranConifer;
import dynamictreesbop.trees.TreeYellowAutumn;
import dynamictreesbop.trees.species.SpeciesBirch;
import dynamictreesbop.trees.species.SpeciesOak;
import dynamictreesbop.trees.species.SpeciesOakFloweringVine;
import dynamictreesbop.trees.species.SpeciesOakLarge;
import dynamictreesbop.trees.species.SpeciesSpruce;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
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
	
	public static ArrayList<DynamicTree> trees = new ArrayList<DynamicTree>();
	
	public enum Tree {
		YELLOW_AUTUMN,
		ORANGE_AUTUMN,
		MAGIC,
		UMBRAN,
		UMBRAN_CONIFER,
		DYING,
		FIR,
		PINK_CHERRY,
		WHITE_CHERRY,
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
		
		// Get tree types from base mod so they can be given new species
		DynamicTree spruceTree = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "spruce")).getTree();
		DynamicTree birchTree = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "birch")).getTree();
		DynamicTree oakTree = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oak")).getTree();
		
		// Register new species of trees from the base mod
		Species.REGISTRY.register(new SpeciesSpruce(spruceTree));
		Species.REGISTRY.register(new SpeciesBirch(birchTree));
		Species.REGISTRY.register(new SpeciesOak(oakTree));
		Species.REGISTRY.register(new SpeciesOakLarge(oakTree));
		Species.REGISTRY.register(new SpeciesOakFloweringVine(oakTree));
		
		// Register new tree types
		DynamicTree yellowAutumnTree = new TreeYellowAutumn(Tree.YELLOW_AUTUMN.getSeq());
		DynamicTree orangeAutumnTree = new TreeOrangeAutumn(Tree.ORANGE_AUTUMN.getSeq());
		DynamicTree magicTree = new TreeMagic(Tree.MAGIC.getSeq());
		DynamicTree umbranTree = new TreeUmbran(Tree.UMBRAN.getSeq());
		DynamicTree umbranConiferTree = new TreeUmbranConifer(Tree.UMBRAN_CONIFER.getSeq());
		
		// Register new tree types that don't get auto-generated leaves
		DynamicTree floweringOakTree = new TreeFloweringOak();
		
		Collections.addAll(trees, yellowAutumnTree, orangeAutumnTree, magicTree, umbranTree, umbranConiferTree);
		Collections.addAll(trees, floweringOakTree);
		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
						
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(TreeHelper.getLeavesMapForModId(DynamicTreesBOP.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		
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
