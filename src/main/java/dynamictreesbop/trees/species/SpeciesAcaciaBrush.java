package dynamictreesbop.trees.species;

import java.util.List;
import java.util.Random;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorLogs;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.SpeciesRare;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.block.BOPBlocks;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class SpeciesAcaciaBrush extends SpeciesRare {
	
	public SpeciesAcaciaBrush(TreeFamily treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, "acaciabrush"), treeFamily, ModContent.acaciaBrushLeavesProperties);
		
		setBasicGrowingParameters(0.25f, 6.0f, 3, 4, 0.7f);
		
		setSoilLongevity(1);
		
		envFactor(Type.WET, 0.5f);
		envFactor(Type.COLD, 0.5f);
		envFactor(Type.SAVANNA, 1.05f);
		
		addAcceptableSoil(BOPBlocks.grass, BOPBlocks.dirt);
		
		setupStandardSeedDropping();
		remDropCreator(new ResourceLocation(ModConstants.MODID, "logs"));
		addDropCreator(new DropCreatorLogs() {
			@Override
			public List<ItemStack> getLogsDrop(World world, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, int volume) {
				int numLogs = volume / 1024;
				while(numLogs > 0) {
					dropList.add(species.getFamily().getPrimitiveLogItemStack(numLogs >= 64 ? 64 : numLogs)); // A log contains 4096 voxels of wood material(16x16x16 pixels) Drop vanilla logs or whatever
					numLogs -= 64;
				}
				dropList.add(species.getFamily().getStick((volume % 1024) / 128)); // A stick contains 512 voxels of wood (1/8th log) (1 log = 4 planks, 2 planks = 4 sticks) Give him the stick!
				return dropList;
			}
		});
		
		leavesProperties.setTree(treeFamily);
	}
	
	@Override
	public boolean isBiomePerfect(Biome biome) {
		return isOneOfBiomes(biome, BOPBiomes.brushland.orNull());
	}
	
	@Override
	public int getReinfTravel() {
		return 0;
	}
	
	@Override
	protected EnumFacing newDirectionSelected(EnumFacing newDir, GrowSignal signal) {
		if (signal.isInTrunk() && newDir != EnumFacing.UP) { // Turned out of trunk
			signal.energy *= 0.5f;
		}
		return newDir;
	}
	
	@Override
	public ItemStack getSeedStack(int qty) {
		return getFamily().getCommonSpecies().getSeedStack(qty);
	}
	
	@Override
	public Seed getSeed() {
		return getFamily().getCommonSpecies().getSeed();
	}

}
