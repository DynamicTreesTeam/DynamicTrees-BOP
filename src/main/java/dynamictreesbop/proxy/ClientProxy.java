package dynamictreesbop.proxy;

import java.util.function.Function;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;
import com.ferreusveritas.dynamictrees.models.blockmodels.ModelBlockBranchBasic;

import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.common.block.BlockBOPLeaves;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import dynamictreesbop.items.ItemMagicSeed;
import dynamictreesbop.items.ItemMapleSeed;
import dynamictreesbop.models.BakedModelBlockBranchBamboo;
import dynamictreesbop.models.ModelBlockBranchMangrove;
import dynamictreesbop.models.ModelBlockPalmFronds;
import dynamictreesbop.models.ModelLoaderDelegated;
import dynamictreesbop.renderers.RenderMagicSeed;
import dynamictreesbop.renderers.RenderMapleSeed;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		super.preInit();
		registerClientEventHandlers();
		registerEntityRenderers();
		
		ModelLoaderRegistry.registerLoader(
				new ModelLoaderDelegated(
						"dynamicbamboo", new ResourceLocation("dynamictrees", "block/smartmodel/branch"),
						(resloc, baseModelBlock) -> new ModelBlockBranchBasic(baseModelBlock) {
							@Override
							public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
								try {
									return new BakedModelBlockBranchBamboo(barkTexture, ringsTexture, bakedTextureGetter);
								} catch (Exception exception) {
									System.err.println("BambooModel.bake() failed due to exception:" + exception);
									return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
								}
							}
						}
						)
				);
		
		ModelLoaderRegistry.registerLoader(
				new ModelLoaderDelegated(
						"dynamicpalmfronds", new ResourceLocation("dynamictreesbop", "block/dynamicpalmfronds"),
						(resloc, baseModelBlock) -> new ModelBlockPalmFronds(baseModelBlock)
						)
				);
		
		ModelLoaderRegistry.registerLoader(
				new ModelLoaderDelegated(
						"dynamicmangrove", new ResourceLocation("dynamictrees", "block/smartmodel/branch"),
						(resloc, baseModelBlock) -> new ModelBlockBranchMangrove(baseModelBlock)
						)
				);
	}
	
	@Override
	public void init() {
		super.init();
		registerColorHandlers();
	}
	
	public void registerColorHandlers() {	
		
		final int magenta = 0x00FF00FF; // for errors.. because magenta sucks.
		
		ModelHelper.regColorHandler(ModContent.rootyWater, (state, world, pos, tintIndex) -> {
			int color = 0xFFFFFF;
			
			if(tintIndex != 2) {
				color = Minecraft.getMinecraft().getBlockColors().colorMultiplier(Blocks.WATER.getDefaultState(), world, pos, tintIndex);
			}
			
			return color;
		});
		
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
