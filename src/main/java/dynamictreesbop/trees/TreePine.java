package dynamictreesbop.trees;

import java.util.List;

import com.ferreusveritas.dynamictrees.api.IGenFeature;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenBush;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenConiferTopper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.api.enums.BOPTrees;
import biomesoplenty.api.enums.BOPWoods;
import biomesoplenty.common.block.BlockBOPLeaves;
import biomesoplenty.common.block.BlockBOPLog;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class TreePine extends TreeFamily {
	
	public class SpeciesPine extends Species {
				
		SpeciesPine(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.pineLeavesProperties);
			
			setBasicGrowingParameters(0.3f, 16.0f, 4, 4, 0.9f);
			
			setDynamicSapling(new BlockDynamicSapling("pinesapling").getDefaultState());
			
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.25f);
			envFactor(Type.WET, 0.75f);
			
			addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
			
			generateSeed();
			
			setupStandardSeedDropping();
			
			addGenFeature(new FeatureGenConiferTopper(getLeavesProperties()));//Make a topper for this conifer tree
			addGenFeature(new FeatureGenBush(this).setBiomePredicate(b -> b == BOPBiomes.shield.orNull()), IGenFeature.POSTGEN);//Generate undergrowth
		}
		
		@Override
		public boolean isBiomePerfect(Biome biome) {
			return BiomeDictionary.hasType(biome, Type.CONIFEROUS);
		}
		
		@Override
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
			EnumFacing originDir = signal.dir.getOpposite();
			
			//Alter probability map for direction change
			probMap[0] = 0;//Down is always disallowed for spruce
			probMap[1] = signal.isInTrunk() ? getUpProbability(): 0;
			probMap[2] = probMap[3] = probMap[4] = probMap[5] = //Only allow turns when we aren't in the trunk(or the branch is not a twig and step is odd)
					!signal.isInTrunk() || (signal.isInTrunk() && signal.numSteps % 2 == 1 && radius > 1) ? 2 : 0;
			probMap[originDir.ordinal()] = 0;//Disable the direction we came from
			probMap[signal.dir.ordinal()] += signal.isInTrunk() ? 0 : signal.numTurns == 1 ? 2 : 1;//Favor current travel direction 
			
			return probMap;
		}
		
		@Override
		protected EnumFacing newDirectionSelected(EnumFacing newDir, GrowSignal signal) {
			if (signal.isInTrunk() && newDir != EnumFacing.UP) { // Turned out of trunk
				signal.energy /= 6.0f;
				if (signal.energy > 1.8f) signal.energy = 1.8f;
			}
			return newDir;
		}
		
		//Pine trees are so similar that it makes sense to randomize their height for a little variation
		//but we don't want the trees to always be the same height all the time when planted in the same location
		//so we feed the hash function the in-game month
		@Override
		public float getEnergy(World world, BlockPos pos) {
			long day = world.getTotalWorldTime() / 24000L;
			int month = (int)day / 30;//Change the hashs every in-game month
			
			return super.getEnergy(world, pos) * biomeSuitability(world, pos) + (coordHashCode(pos.up(month)) % 6);//Vary the height energy by a psuedorandom hash function
		}
		
	}
	
	public TreePine() {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "pine"));
		
		IBlockState primLog = BlockBOPLog.paging.getVariantState(BOPWoods.PINE);
		setPrimitiveLog(primLog, BlockBOPLog.paging.getVariantItem(BOPWoods.PINE));
		
		ModContent.pineLeavesProperties.setTree(this);
		hasConiferVariants = true;
		
		this.addConnectableVanillaLeaves((state) -> {
			return state.getBlock() instanceof BlockBOPLeaves && state.getValue(((BlockBOPLeaves) state.getBlock()).variantProperty) == BOPTrees.PINE;
		});
	}
	
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesPine(this));
	}
	
	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(getCommonSpecies().getDynamicSapling().getBlock());
		return super.getRegisterableBlocks(blockList);
	}

}
