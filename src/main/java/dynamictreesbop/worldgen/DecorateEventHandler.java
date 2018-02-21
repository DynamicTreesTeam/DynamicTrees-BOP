package dynamictreesbop.worldgen;

import com.ferreusveritas.dynamictrees.ModConfigs;

import biomesoplenty.api.biome.BOPBiomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DecorateEventHandler {
	
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(DecorateBiomeEvent.Decorate event) {
		if (event.getType() == EventType.TREE) {
			Biome biome = event.getWorld().getBiome(event.getPos());
			ResourceLocation resloc = biome.getRegistryName();
			if (resloc.getResourceDomain().equals("biomesoplenty")
					&& biome != BOPBiomes.flower_island.get()
					&& biome != BOPBiomes.sacred_springs.get()
					&& biome != BOPBiomes.origin_island.get()
					&& biome != BOPBiomes.shrubland.get()
					&& biome != BOPBiomes.tundra.get()
					&& biome != BOPBiomes.meadow.get()
					&& biome != BOPBiomes.eucalyptus_forest.get()
					&& biome != BOPBiomes.bamboo_forest.get()
					&& biome != BOPBiomes.mangrove.get()
					&& biome != BOPBiomes.redwood_forest.get()
					&& biome != BOPBiomes.overgrown_cliffs.get()
					&& biome != BOPBiomes.undergarden.get()
					&& biome != BOPBiomes.tropical_rainforest.get()
					&& biome != BOPBiomes.brushland.get()
			) {
				event.setResult(Result.DENY);
			}
		} else if (!ModConfigs.vanillaCactusWorldGen && event.getType() == EventType.CACTUS){
			Biome biome = event.getWorld().getBiome(event.getPos());
			ResourceLocation resloc = biome.getRegistryName();
			if(resloc.getResourceDomain().equals("biomesoplenty")) {
				event.setResult(Result.DENY);
			}
		}
	}

}
