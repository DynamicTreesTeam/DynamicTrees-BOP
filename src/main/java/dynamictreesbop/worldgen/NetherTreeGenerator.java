package dynamictreesbop.worldgen;

import java.util.Random;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.common.block.BlockBOPGrass;
import dynamictreesbop.ModContent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class NetherTreeGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		switch (world.provider.getDimension()) {
		case 0: //Overworld
			break;
		case -1: //Nether
			// generateNether(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
			break;
		case 1: //End
			break;
		}
	}
	
	@SuppressWarnings("unused")
	private void generateNether(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		for (int i = 0; i < 15; i++) { 
			int x = chunkX + random.nextInt(14) + 1;
			int z = chunkZ + random.nextInt(14) + 1;
			BlockPos pos = new BlockPos(x, 127, z);
			BlockPos upPos = new BlockPos(x, 127, z);
			
			for (; pos.getY() > 0; pos = pos.down()) {
				IBlockState state = world.getBlockState(pos);
				if (state.getBlock() == BOPBlocks.grass && (world.isAirBlock(upPos) || world.getBlockState(upPos).getBlock().isReplaceable(world, upPos)) &&
						state.getValue(BlockBOPGrass.VARIANT) == BlockBOPGrass.BOPGrassType.OVERGROWN_NETHERRACK) {
					ModContent.hellbarkTree.getCommonSpecies().generate(world, pos, world.getBiomeForCoordsBody(pos), random, 2);
					break;
				}
				
				upPos = pos;
			}
		}
	}

}
