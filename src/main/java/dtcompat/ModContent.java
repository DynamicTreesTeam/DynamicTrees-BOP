package dtcompat;

import java.util.ArrayList;
import java.util.Collections;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.trees.DynamicTree;
import com.ferreusveritas.dynamictrees.trees.Species;

import dtcompat.trees.TreeBirch;
import dtcompat.trees.TreeOak;
import dtcompat.trees.TreeSpruce;
import dtcompat.trees.TreeYellowAutumn;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = DynamicTreesCompat.MODID)
@ObjectHolder(DynamicTreesCompat.MODID)
public class ModContent {
	
	public static final Block yellowautumnsapling = null;
	
	public static ArrayList<DynamicTree> trees = new ArrayList<DynamicTree>();
	
	public enum Tree {
		SPRUCE,
		BIRCH,
		OAK,
		YELLOW_AUTUMN;
		
		public int getSeq() {
			return ordinal();
		}
	}
	
	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		DynamicTree spruceTree = new TreeSpruce(Tree.SPRUCE.getSeq());
		DynamicTree birchTree = new TreeBirch(Tree.BIRCH.getSeq());
		DynamicTree oakTree = new TreeOak(Tree.OAK.getSeq());
		DynamicTree yellowAutumnTree = new TreeYellowAutumn(Tree.YELLOW_AUTUMN.getSeq());
		
		Collections.addAll(trees, spruceTree, birchTree, oakTree, yellowAutumnTree);
		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		
		IForgeRegistry<Block> registry = event.getRegistry();
						
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(TreeHelper.getLeavesMapForModId(DynamicTreesCompat.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		
		ArrayList<Item> treeItems = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		TreeHelper.getLeavesMapForModId(DynamicTreesCompat.MODID).forEach((key, block) -> registerItemBlock(registry, block));
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
		
		for(BlockDynamicLeaves leaves : TreeHelper.getLeavesMapForModId(DynamicTreesCompat.MODID).values()) {
			Item item = Item.getItemFromBlock(leaves);
			ModelHelper.regModel(item);
		}
	}
	
	private static void registerItemBlock(IForgeRegistry<Item> registry, Block block) {
		registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}
	
}
