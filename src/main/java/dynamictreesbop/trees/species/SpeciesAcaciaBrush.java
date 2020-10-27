package dynamictreesbop.trees.species;

import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import biomesoplenty.api.biome.BOPBiomes;
import dynamictreesbop.DynamicTreesBOP;
import dynamictreesbop.ModContent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;

public class SpeciesAcaciaBrush extends Species {
	
	public SpeciesAcaciaBrush(TreeFamily treeFamily) {
		super(new ResourceLocation(DynamicTreesBOP.MODID, ModContent.ACACIABRUSH), treeFamily, ModContent.leaves.get(ModContent.ACACIABRUSH));
		
		setBasicGrowingParameters(0.25f, 6.0f, 3, 4, 0.7f);
		
		setSoilLongevity(1);
		
		envFactor(Type.WET, 0.5f);
		envFactor(Type.COLD, 0.5f);
		envFactor(Type.SAVANNA, 1.05f);
		
		setupStandardSeedDropping();
		
		setRequiresTileEntity(true);
		
		leavesProperties.setTree(treeFamily);
	}
	
	@Override
	public LogsAndSticks getLogsAndSticks(float volume) {
		return super.getLogsAndSticks(1.0f + volume);//Guarantee at least one log is produced
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
