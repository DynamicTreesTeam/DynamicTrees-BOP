//package therealeststu.dtbop.trees;
//
//import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
//import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
//import com.ferreusveritas.dynamictrees.trees.Family;
//import com.ferreusveritas.dynamictrees.trees.Species;
//import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
//import com.ferreusveritas.dynamictrees.worldgen.JoCode;
//import net.minecraft.block.Blocks;
//import net.minecraft.util.Direction;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IWorld;
//import net.minecraft.world.World;
//import net.minecraft.world.biome.Biome;
//
//public class TempSpecies extends Species {
//
//    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(TempSpecies::new);
//
//    public TempSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
//        super(name, family, leavesProperties);
//    }
//
//    @Override
//    public JoCode getJoCode(String joCodeString) {
//        return new JoCode(joCodeString){
//            @Override
//            public void generate(World worldObj, IWorld world, Species species, BlockPos rootPosIn, Biome biome, Direction facing, int radius, SafeChunkBounds safeBounds) {
//                worldObj.setBlockAndUpdate(rootPosIn.below(), Blocks.AIR.defaultBlockState());
//                super.generate(worldObj, world, species, rootPosIn, biome, facing, radius, safeBounds);
//            }
//        };
//    }
//}
