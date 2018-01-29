package dtcompat.proxy;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.trees.DynamicTree;

import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockColoring;
import dtcompat.DynamicTreesCompat;
import dtcompat.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		super.preInit();
	}
	
	@Override
	public void init() {
		super.init();
		registerColorHandlers();
	}
	
	@Override public void postInit() {
		super.postInit();
	}

	public void registerColorHandlers() {	
		for(BlockDynamicLeaves leaves: TreeHelper.getLeavesMapForModId(DynamicTreesCompat.MODID).values()) {
			
			ModelHelper.regColorHandler(leaves, new IBlockColor() {
				@Override
				public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
					boolean inWorld = worldIn != null && pos != null;
					
					IBlockState primLeaves = leaves.getTree(state).getPrimitiveLeaves();
					
					if (primLeaves.getBlock() instanceof BlockBOPLeaves) {
		            	switch (BlockBOPLeaves.getColoringType((BOPTrees) primLeaves.getValue(((BlockBOPLeaves) primLeaves.getBlock()).variantProperty))) {
		            		case TINTED:
		            			return inWorld ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos) : ColorizerFoliage.getFoliageColorBasic();
		            		case OVERLAY:
		            			if (tintIndex == 0) return inWorld ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos) : ColorizerFoliage.getFoliageColorBasic();
		            		default:
		            			return 0xffffff;
		            	}
					} else {
						return inWorld ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos) : ColorizerFoliage.getFoliageColorBasic();
					}
				}
			});
				
			ModelHelper.regColorHandler(Item.getItemFromBlock(leaves), new IItemColor() {
				@Override
				public int colorMultiplier(ItemStack stack, int tintIndex) {
					Block block = leaves.getTree(leaves.getStateFromMeta(stack.getMetadata())).getPrimitiveLeaves().getBlock();
					
					if (block instanceof BlockBOPLeaves) {
						return BlockColoring.BLOCK_ITEM_COLORING.colorMultiplier(new ItemStack(block, (stack.getMetadata() & 3) | 8), tintIndex);
					}
					
					return ColorizerFoliage.getFoliageColorBasic();
				}
			});
		}

		for(DynamicTree tree: ModContent.trees) {
			ModelHelper.regDynamicSaplingColorHandler((BlockDynamicSapling) tree.getCommonSpecies().getDynamicSapling().getBlock());
		}

	}
	
}
