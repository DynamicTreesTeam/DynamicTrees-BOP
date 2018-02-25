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
					&& biome != BOPBiomes.flower_island.orNull()
					&& biome != BOPBiomes.sacred_springs.orNull()
					&& biome != BOPBiomes.origin_island.orNull()
					&& biome != BOPBiomes.shrubland.orNull()
					&& biome != BOPBiomes.tundra.orNull()
					&& biome != BOPBiomes.bamboo_forest.orNull()
					&& biome != BOPBiomes.mangrove.orNull()
					&& biome != BOPBiomes.redwood_forest.orNull()
					&& biome != BOPBiomes.undergarden.orNull()
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
