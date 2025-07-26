package io.github.saphirdefeu.forgineer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

import java.util.*;

public class Temperature {

    HashMap<UUID, Integer> temperatures = new HashMap<>();

    public Temperature() {
        ServerTickEvents.END_WORLD_TICK.register(this::onEndTick);
    }

    public void onEndTick(ServerWorld world) {
        List<ServerPlayerEntity> playerList = world.getPlayers();
        for(int i = 0; i < playerList.size(); i++) {
            ServerPlayerEntity player = world.getPlayers().get(i);
            UUID uuid = player.getUuid();

            try {
                Integer temperature = temperatures.get(uuid);
                temperature = updateTemperature(player, temperature);
                if(temperature == null) temperature = 0;
                temperatures.put(uuid, temperature);

                // Update effects depending on the current temperature of the player.
            } catch(NullPointerException e) {
                temperatures.put(uuid, 0);
            }
        }
    }

    private static Integer updateTemperature(ServerPlayerEntity player, Integer currentTemperature) {
        BlockPos position = player.getBlockPos();
        ServerWorld world = player.getWorld();

        if(world.getDimension().ultrawarm()) {
            return currentTemperature + 1;
        }

        Optional<RegistryKey<Biome>> biomeRegistryEntryOptional = world.getBiome(position).getKey();
        RegistryKey<Biome> biomeRegistryKey;

        if(biomeRegistryEntryOptional.isEmpty()) {
            return currentTemperature;
        } else {
            biomeRegistryKey = biomeRegistryEntryOptional.get();
        }

        final DynamicRegistryManager registryManager = world.getRegistryManager();

        Biome biome = registryManager.getOrThrow(RegistryKeys.BIOME).get(biomeRegistryKey);

        if(biome == null) {
            return currentTemperature;
        }

        if(biome.getTemperature() < 0.2) {
            return currentTemperature - 1;
        } else if(biome.getTemperature() > 0.8) {
            return currentTemperature + 1;
        }

        return currentTemperature;
    }
}
