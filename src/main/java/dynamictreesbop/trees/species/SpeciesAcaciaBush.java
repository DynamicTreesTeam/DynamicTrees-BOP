package dynamictreesbop.trees.species;

import java.util.Random;

import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenBush;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;

import dynamictreesbop.DynamicTreesBOP;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class SpeciesAcaciaBush extends Species {
	
	FeatureGenBush bushGen;
	
	public SpeciesAcaciaBush() {
		super();
		setRegistryName(new ResourceLocation(DynamicTreesBOP.MODID, "acaciabush"));
		
		setStandardSoils();
		addAcceptableSoil(Blocks.SAND);
		
		this.bushGen = new FeatureGenBush(this)
				.setLeavesState(Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.ACACIA))
				.setLogState(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA));
	}
	
	@Override
	public boolean generate(World world, BlockPos rootPos, Biome biome, Random random, int radius, SafeChunkBounds safeBounds) {
		bushGen.generate(world, rootPos, biome, random, radius, safeBounds);
		return true;
	}

}
