package dynamictreesbop.trees;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.blocks.BlockRootyDirt;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.DynamicTree;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.MathHelper;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.item.BOPItems;
import biomesoplenty.common.block.BlockBOPLeaves;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.dropcreators.DropCreatorFruit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.registries.IForgeRegistry;

public class TreeFloweringOak extends DynamicTree {
	
	public class SpeciesFloweringOak extends Species {
		
		SpeciesFloweringOak(DynamicTree treeFamily) {
			super(treeFamily.getName(), treeFamily);
			
			setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.85f);
			
			setDynamicSapling(new BlockDynamicSapling("floweringoaksapling").getDefaultState());
			
			envFactor(Type.COLD, 0.75f);
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);
			
			addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
			
			addDropCreator(new DropCreatorFruit(BOPItems.peach));
			setupStandardSeedDropping();
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return BiomeDictionary.hasType(biome, Type.FOREST);
		}
		
	}
	
	public class SpeciesFloweringOakLarge extends Species {
		
		SpeciesFloweringOakLarge(DynamicTree treeFamily) {
			super(new ResourceLocation(treeFamily.getName().getResourceDomain(), treeFamily.getName().getResourcePath() + "large"), treeFamily);
			
			setBasicGrowingParameters(0.35f, 18.0f, 6, 6, 0.9f);
			
			setSoilLongevity(12);
			
			envFactor(Type.COLD, 0.75f);
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);
			
			addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
			
			addDropCreator(new DropCreatorFruit(BOPItems.peach));
			setupStandardSeedDropping();
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return BiomeDictionary.hasType(biome, Type.FOREST);
		}
		
		@Override
		public ItemStack getSeedStack(int qty) {
			return getCommonSpecies().getSeedStack(qty);
		}
		
		@Override
		public Seed getSeed() {
			return getCommonSpecies().getSeed();
		}
		
	}
	
	Species largeSpecies;
	
	public TreeFloweringOak() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "floweringoak"), -1);
		
		BlockDynamicLeavesFlowering leaves = new BlockDynamicLeavesFlowering();
		
		setDynamicLeaves(leaves, 0);
		
		IBlockState primLog = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);		
		setPrimitiveLog(primLog, new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.OAK.ordinal() & 3));
		
		IBlockState primLeaves = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK);
		setPrimitiveLeaves(primLeaves, new ItemStack(primLeaves.getBlock(), 1, primLeaves.getValue(BlockOldLeaf.VARIANT).getMetadata() & 3));
		//IBlockState primLeaves = BlockBOPLeaves.paging.getVariantState(BOPTrees.FLOWERING);
		//setPrimitiveLeaves(primLeaves, BlockBOPLeaves.paging.getVariantItem(BOPTrees.FLOWERING));
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesFloweringOak(this));
		largeSpecies = new SpeciesFloweringOakLarge(this);
	}
	
	@Override
	public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
		super.registerSpecies(speciesRegistry);
		speciesRegistry.register(largeSpecies);
		
		getCommonSpecies().generateSeed();
	}
	
	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(getDynamicLeaves());
		blockList.add(getCommonSpecies().getDynamicSapling().getBlock());
		
		return super.getRegisterableBlocks(blockList);
	}
	
	@Override
	public List<Item> getRegisterableItems(List<Item> itemList) {
		itemList.add(new ItemBlock(getDynamicLeaves()).setRegistryName(getDynamicLeaves().getRegistryName()));
		
		return super.getRegisterableItems(itemList);
	}
	
	@Override
	public IBlockState getDynamicLeavesState() {
		return getDynamicLeaves().getDefaultState();
	}
	
	@Override
	public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random) {
		if(super.rot(world, pos, neighborCount, radius, random)) {
			if(radius > 4 && TreeHelper.isRootyDirt(world, pos.down()) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
				world.setBlockState(pos, random.nextInt(3) == 0 ? ModBlocks.blockStates.redMushroom : ModBlocks.blockStates.brownMushroom);//Change branch to a mushroom
				world.setBlockState(pos.down(), ModBlocks.blockStates.podzol);//Change rooty dirt to Podzol
			}
			return true;
		}
		
		return false;
	}
	
	public static class BlockDynamicLeavesFlowering extends BlockDynamicLeaves { 
		
		public static final PropertyBool FLOWERING = PropertyBool.create("flowering");
		public static final PropertyBool CAN_FLOWER = PropertyBool.create("can_flower");
		
		private DynamicTree tree;
		
		public BlockDynamicLeavesFlowering() {
			setDefaultState(this.blockState.getBaseState().withProperty(HYDRO, 4).withProperty(FLOWERING, false).withProperty(CAN_FLOWER, false));
			setRegistryName(DynamicTreesBOP.MODID, "leaves_flowering");
			setUnlocalizedName("leaves_flowering");
		}
		
		@Override
		protected BlockStateContainer createBlockState() {
			return new BlockStateContainer(this, new IProperty[] {HYDRO, FLOWERING, CAN_FLOWER, TREE});//TREE is unused, but it has to be there to prevent a crash when the constructor of BlockDynamicLeaves sets the default state
		}
		
		@Override
		public IBlockState getStateFromMeta(int meta) {
			return this.getDefaultState().withProperty(FLOWERING, ((meta >> 2) & 1) > 0).withProperty(CAN_FLOWER, ((meta >> 3) & 1) > 0).withProperty(HYDRO, (meta & 3) + 1);
		}
		
		@Override
		public int getMetaFromState(IBlockState state) {
			return (state.getValue(HYDRO) - 1) | (state.getValue(FLOWERING) ? 4 : 0) | (state.getValue(CAN_FLOWER) ? 8 : 0);
		}
		
		@Override
		public void setTree(int treeNum, DynamicTree tree) {
			this.tree = tree;
			for (int i = 0; i < 4; i++) super.setTree(i, tree);
		}
		
		@Override
		public DynamicTree getTree(int treeNum) {
			return tree;
		}
		
		@Override
		public DynamicTree getTree(IBlockState blockState) {
			return tree;
		}
		
		@Override
		public boolean age(World world, BlockPos pos, IBlockState state, Random rand, boolean rapid) {
			DynamicTree tree = getTree(state);
			int preHydro = getHydrationLevel(state);
			
			//Check hydration level.  Dry leaves are dead leaves.
			int hydro = getHydrationLevelFromNeighbors(world, pos, tree);
			if(hydro == 0 || (!rapid && !hasAdequateLight(world, tree, pos))) { //Light doesn't work right during worldgen so we'll just disable it during worldgen for now.
				removeLeaves(world, pos);//No water, no light .. no leaves
				return true;//Leaves were destroyed
			} else { 
				//Encode new hydration level in metadata for this leaf
				if(preHydro != hydro) {//A little performance gain
					if(setHydrationLevel(world, pos, hydro, state)) {
						return true;//Leaves were destroyed
					}
				}
			}
			
			// If this block can flower, check if flowers should grow or decay
			if (canFlower(state)) {
				boolean flowering = rapid || world.getLight(pos) >= 14;
				if (isFlowering(state) != flowering) {
					setFlowering(world, pos, flowering, state);
				}
			}
			
			//We should do this even if the hydro is only 1.  Since there could be adjacent branch blocks that could use a leaves block
			for(EnumFacing dir: EnumFacing.VALUES) {//Go on all 6 sides of this block
				if(hydro > 1 || rand.nextInt(4) == 0 ) {//we'll give it a 1 in 4 chance to grow leaves if hydro is low to help performance
					growLeaves(world, tree, pos.offset(dir));//Attempt to grow new leaves
				}
			}
			
			return false;//Leaves were not destroyed
		}
		
		@Override
		public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
			BlockPos deltaPos = pos.offset(facing.getOpposite());
			DynamicTree tree = TreeHelper.getSafeTreePart(world, deltaPos).getTree(world, deltaPos);
			
			if (tree.getDynamicLeaves() == this) {//The tree being clicked on is a flowering oak, add a chance for the placed leaves to be flowering
				boolean flowers = world.rand.nextInt(3) == 0;
				return getDefaultState().withProperty(CAN_FLOWER, flowers).withProperty(FLOWERING, flowers);
			}
			
			return getDefaultState();
		}
		
		@Override
		public boolean setBlockToLeaves(World world, DynamicTree tree, BlockPos pos, int hydro) {
			hydro = MathHelper.clamp(hydro, 0, 4);
			if (hydro != 0) {
				boolean canFlower = world.rand.nextInt(4) == 0;
				world.setBlockState(pos, tree.getDynamicLeavesState().withProperty(CAN_FLOWER, canFlower).withProperty(FLOWERING, canFlower && world.getLight(pos) >= 14).withProperty(HYDRO, hydro), 2);//Removed Notify Neighbors Flag for performance
				return true;
			} else {
				removeLeaves(world, pos);
				return false;
			}
		}
		
		public boolean isFlowering(IBlockState blockState) {
			return blockState.getValue(FLOWERING);
		}
		
		public boolean isFlowering(IBlockAccess blockAccess, BlockPos pos) {
			return isFlowering(blockAccess.getBlockState(pos));
		}
		
		public boolean canFlower(IBlockState blockState) {
			return blockState.getValue(CAN_FLOWER);
		}
		
		public boolean canFlower(IBlockAccess blockAccess, BlockPos pos) {
			return canFlower(blockAccess.getBlockState(pos));
		}
		
		public static void setFlowering(World world, BlockPos pos, boolean flowering, IBlockState currentBlockState) {
			world.setBlockState(pos, currentBlockState.withProperty(FLOWERING, flowering), 2);
		}
		
		@Override
		public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess blockAccess, BlockPos pos, int fortune) {
			IBlockState state = blockAccess.getBlockState(pos);
			DynamicTree tree = getTree(state);
			ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
			
			if (state.getBlock() == this) {
				ret.add(isFlowering(state)
						? BlockBOPLeaves.paging.getVariantItem(BOPTrees.FLOWERING)
						: tree.getPrimitiveLeavesItemStack(1)
				);
			}
			
			return ret;
		}
		
	}

}
