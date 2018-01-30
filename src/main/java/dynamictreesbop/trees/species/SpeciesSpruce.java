package dynamictreesbop.trees.species;

import java.util.Collections;
import java.util.List;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockRootyDirt;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.DynamicTree;
import com.ferreusveritas.dynamictrees.trees.Species;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import dynamictreesbop.DynamicTreesBOP;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class SpeciesSpruce extends Species {
	
	Species baseSpecies;
	
	public SpeciesSpruce(DynamicTree treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "spruce"), treeFamily);
		
		baseSpecies = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "spruce"));
		
		//Spruce are conical thick slower growing trees
		setBasicGrowingParameters(0.25f, 16.0f, 3, 3, 0.9f);
		
		setDynamicSapling(baseSpecies.getDynamicSapling());
		setSeedStack(baseSpecies.getSeedStack(1));
		
		envFactor(Type.HOT, 0.50f);
		envFactor(Type.DRY, 0.25f);
		
		setupStandardSeedDropping();
	}
	
	@Override
	public boolean isBiomePerfect(Biome biome) {
		return BiomeDictionary.hasType(biome, Type.CONIFEROUS) || isOneOfBiomes(biome, BOPBiomes.wetland.get(), BOPBiomes.land_of_lakes.get(), BOPBiomes.boreal_forest.get(), BOPBiomes.shield.get(), BOPBiomes.dead_forest.get(), BOPBiomes.maple_woods.get(), BOPBiomes.meadow.get());
	}
	
	@Override
	public boolean isAcceptableSoil(World world, BlockPos pos, IBlockState soilBlockState) {
		Block soilBlock = soilBlockState.getBlock();
		return soilBlock == Blocks.DIRT || soilBlock == Blocks.GRASS || soilBlock == Blocks.MYCELIUM || soilBlock instanceof BlockRootyDirt || soilBlock == BOPBlocks.grass || soilBlock == BOPBlocks.dirt;
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
		if(signal.isInTrunk() && newDir != EnumFacing.UP){//Turned out of trunk
			signal.energy /= 3.0f;
		}
		return newDir;
	}
	
	//Spruce trees are so similar that it makes sense to randomize their height for a little variation
	//but we don't want the trees to always be the same height all the time when planted in the same location
	//so we feed the hash function the in-game month
	@Override
	public float getEnergy(World world, BlockPos pos) {
		long day = world.getTotalWorldTime() / 24000L;
		int month = (int)day / 30;//Change the hashs every in-game month
		
		return super.getEnergy(world, pos) * biomeSuitability(world, pos) + (coordHashCode(pos.up(month)) % 5);//Vary the height energy by a psuedorandom hash function
	}
	
	public int coordHashCode(BlockPos pos) {
		int hash = (pos.getX() * 9973 ^ pos.getY() * 8287 ^ pos.getZ() * 9721) >> 1;
		return hash & 0xFFFF;
	}
	
	@Override
	public void postGeneration(World world, BlockPos rootPos, Biome biome, int radius, List<BlockPos> endPoints, boolean worldGen) {
		//Manually place the highest few blocks of the conifer since the leafCluster voxmap won't handle it
		BlockPos highest = Collections.max(endPoints, (a, b) -> a.getY() - b.getY());
		world.setBlockState(highest.up(1), getTree().getDynamicLeavesState(4));
		world.setBlockState(highest.up(2), getTree().getDynamicLeavesState(3));
		world.setBlockState(highest.up(3), getTree().getDynamicLeavesState(1));
	}
	
}
