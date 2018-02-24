package dynamictreesbop.trees;

import java.util.List;
import java.util.Random;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorLogs;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.MathHelper;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreeBamboo extends TreeFamily {

	public class SpeciesBamboo extends Species {
		
		SpeciesBamboo(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.bambooLeavesProperties);
			
			setBasicGrowingParameters(0.2f, 13.0f, 4, 4, 1.5f);//Fastest growing "tree"
			
			setDynamicSapling(new BlockDynamicSapling("bamboosapling").getDefaultState());
			
			envFactor(Type.COLD, 0.25f);
			
			addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
			
			generateSeed();
			
			setupStandardSeedDropping();

			addDropCreator(new DropCreatorLogs() {
				@Override
				public List<ItemStack> getLogsDrop(World world, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, int volume) {
					dropList.add(species.getFamily().getPrimitiveLogItemStack(volume / 768));
					return dropList;
				}
			});
		}
		
		@Override
		public BlockRooty getRootyBlock() {
			return ModBlocks.blockRootyDirt;
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, BOPBiomes.bamboo_forest.get());
		}
		
		@Override
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
			EnumFacing originDir = signal.dir.getOpposite();
			
			// Alter probability map for direction change
			probMap[0] = 0; // Down is always disallowed for palm
			probMap[1] = 10;
			probMap[2] = probMap[3] = probMap[4] = probMap[5] =  0;	
			probMap[originDir.ordinal()] = 0; // Disable the direction we came from
			
			return probMap;
		}
		
		// Bamboo trees are so similar that it makes sense to randomize their height for a little variation
		// but we don't want the trees to always be the same height all the time when planted in the same location
		// so we feed the hash function the in-game month
		@Override
		public float getEnergy(World world, BlockPos pos) {
			long day = world.getTotalWorldTime() / 24000L;
			int month = (int) day / 30; // Change the hashs every in-game month
			
			return super.getEnergy(world, pos) * biomeSuitability(world, pos) + (CoordUtils.coordHashCode(pos.up(month), 3) % 3); // Vary the height energy by a psuedorandom hash function
		}
		
	}
	
	public TreeBamboo() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "bamboo"));
		setPrimitiveLog(Block.REGISTRY.getObject(new ResourceLocation("biomesoplenty", "bamboo")).getDefaultState());
		setStick(ItemStack.EMPTY);
		ModContent.bambooLeavesProperties.setTree(this);
	}
	
	@Override
	public BlockBranch createBranch() {
		
		return new BlockBranchBasic(getName() + "branch") {
			
			@Override
			public void setRadius(World world, BlockPos pos, int radius, EnumFacing dir, int flags) {
				super.setRadius(world, pos, MathHelper.clamp(radius, 1, 3), dir, flags);
			}
			

		};
		
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesBamboo(this));
	}
	
	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(getCommonSpecies().getDynamicSapling().getBlock());
		return super.getRegisterableBlocks(blockList);
	}
	
}
