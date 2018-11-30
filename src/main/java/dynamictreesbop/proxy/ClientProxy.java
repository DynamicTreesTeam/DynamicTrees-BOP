package dynamictreesbop.proxy;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;

import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.common.block.BlockBOPLeaves;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.items.ItemMagicSeed;
import dynamictreesbop.items.ItemMapleSeed;
import dynamictreesbop.models.ModelLoaderBlockBranchBamboo;
import dynamictreesbop.models.ModelLoaderBlockPalmFronds;
import dynamictreesbop.renderers.RenderMagicSeed;
import dynamictreesbop.renderers.RenderMapleSeed;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		super.preInit();
		registerClientEventHandlers();
		registerEntityRenderers();
		
		ModelLoaderRegistry.registerLoader(new ModelLoaderBlockBranchBamboo());
		ModelLoaderRegistry.registerLoader(new ModelLoaderBlockPalmFronds());
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
		
		final int magenta = 0x00FF00FF; // for errors.. because magenta sucks.
		
		ModelHelper.regColorHandler(ModContent.floweringOakLeaves, new IBlockColor() {
			@Override
			public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
				boolean inWorld = worldIn != null && pos != null;
				Block block = state.getBlock();
				
    			if (inWorld && tintIndex == 0 && TreeHelper.isLeaves(block)) {
					return ((BlockDynamicLeaves) block).getProperties(state).foliageColorMultiplier(state, worldIn, pos);
    			}
    			return 0xffffff;
			}
		});
		
		ModelHelper.regColorHandler(ModContent.palmFrondLeaves, new IBlockColor() {
			@Override
			public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
				boolean inWorld = worldIn != null && pos != null;
				Block block = state.getBlock();
				
    			if (inWorld && tintIndex == 0 && TreeHelper.isLeaves(block)) {
					return ((BlockDynamicLeaves) block).getProperties(state).foliageColorMultiplier(state, worldIn, pos);
    			}
    			return 0xffffff;
			}
		});
		
		for (BlockDynamicLeaves leaves: LeavesPaging.getLeavesMapForModId(DynamicTreesBOP.MODID).values()) {
			ModelHelper.regColorHandler(leaves, new IBlockColor() {
				@Override
				public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
					boolean inWorld = worldIn != null && pos != null;
					
					IBlockState primLeaves = leaves.getProperties(state).getPrimitiveLeaves();
					Block block = state.getBlock();
					
					if (primLeaves.getBlock() instanceof BlockBOPLeaves) {
		            	switch (BlockBOPLeaves.getColoringType((BOPTrees) primLeaves.getValue(((BlockBOPLeaves) primLeaves.getBlock()).variantProperty))) {
		            		case TINTED:
								if(inWorld && TreeHelper.isLeaves(block)) {
									return ((BlockDynamicLeaves) block).getProperties(state).foliageColorMultiplier(state, worldIn, pos);
								}
								return magenta;
		            		case OVERLAY:
		            			if (inWorld && tintIndex == 0) {
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
		}
		
	}
	
	public void registerClientEventHandlers() {
	}
	
	public void registerEntityRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(ItemMapleSeed.EntityItemMapleSeed.class, new RenderMapleSeed.Factory());
		RenderingRegistry.registerEntityRenderingHandler(ItemMagicSeed.EntityItemMagicSeed.class, new RenderMagicSeed.Factory());
	}
	
}
