package dynamictreesbop.proxy;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.trees.DynamicTree;

import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockColoring;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.items.ItemMapleSeed;
import dynamictreesbop.renderers.RenderMapleSeed;
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
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		super.preInit();
		registerEntityRenderers();
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
		
		final int magenta = 0x00FF00FF;//for errors.. because magenta sucks.
		
		ModelHelper.regColorHandler(ModContent.leaves_flowering, new IBlockColor() {
			@Override
			public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
				boolean inWorld = worldIn != null && pos != null;
				Block block = state.getBlock();
				
    			if (tintIndex == 0 && TreeHelper.isLeaves(block)) {
					return ((BlockDynamicLeaves) block).getProperties(state).foliageColorMultiplier(state, worldIn, pos);
    			}
    			return 0xffffff;
			}
		});
		ModelHelper.regColorHandler(Item.getItemFromBlock(ModContent.leaves_flowering), new IItemColor() {
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) {
    			if (tintIndex == 0) return ColorizerFoliage.getFoliageColorBasic();
    			return 0xffffff;
			}
		});
		
		for(BlockDynamicLeaves leaves: TreeHelper.getLeavesMapForModId(DynamicTreesBOP.MODID).values()) {
			
			ModelHelper.regColorHandler(leaves, new IBlockColor() {
				@Override
				public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
					boolean inWorld = worldIn != null && pos != null;
					
					IBlockState primLeaves = leaves.getProperties(state).getPrimitiveLeaves();
					Block block = state.getBlock();
					
					if (primLeaves.getBlock() instanceof BlockBOPLeaves) {
		            	switch (BlockBOPLeaves.getColoringType((BOPTrees) primLeaves.getValue(((BlockBOPLeaves) primLeaves.getBlock()).variantProperty))) {
		            		case TINTED:
								if(TreeHelper.isLeaves(block)) {
									return ((BlockDynamicLeaves) block).getProperties(state).foliageColorMultiplier(state, worldIn, pos);
								}
								return magenta;
		            		case OVERLAY:
		            			if (tintIndex == 0) {
		    						if(TreeHelper.isLeaves(block)) {
		    							return ((BlockDynamicLeaves) block).getProperties(state).foliageColorMultiplier(state, worldIn, pos);
		    						}
		    						return magenta;
		            			}
		            		default:
		            			return 0xffffff;
		            	}
					} else {
						if(TreeHelper.isLeaves(block)) {
							return ((BlockDynamicLeaves) block).getProperties(state).foliageColorMultiplier(state, worldIn, pos);
						}
						return magenta;
					}
				}
			});
				
			ModelHelper.regColorHandler(Item.getItemFromBlock(leaves), new IItemColor() {
				@Override
				public int colorMultiplier(ItemStack stack, int tintIndex) {
					return ColorizerFoliage.getFoliageColorBasic();
				}
			});
		}

		for(DynamicTree tree: ModContent.trees) {
			if (tree.getName().getResourcePath().equals("decayed")) continue;
			if (tree.getName().getResourcePath().equals("dead")) continue;
			BlockDynamicSapling sapling = (BlockDynamicSapling) tree.getCommonSpecies().getDynamicSapling().getBlock();
			if (tree.getName().getResourcePath().equals("floweringoak")) {
				ModelHelper.regColorHandler(sapling, new IBlockColor() {
					@Override
					public int colorMultiplier(IBlockState state, IBlockAccess access, BlockPos pos, int tintIndex) {
						return access == null || pos == null ? -1 : tintIndex != 0 ? 0xffffff : sapling.getSpecies(access, pos, state).getLeavesProperties().foliageColorMultiplier(state, access, pos);
					}
				});
			} else {
				ModelHelper.regDynamicSaplingColorHandler(sapling);
			}
		}
	}
	
	public void registerEntityRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(ItemMapleSeed.EntityItemMapleSeed.class, new RenderMapleSeed.Factory());
	}
	
}
