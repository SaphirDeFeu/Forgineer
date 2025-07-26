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

        // Calculate a 'desired' temperature and move towards it
        // The 'desired' temperature transforms the biome temperature range 0.0-1.0 (float)
        // To a corporal temperature range (-10000) - (+10000) (int)
        int desiredTemperature = ((int) biome.getTemperature()) * 20000 - 10000;

        // If we're in water, the desired temperature will be multiplied by a constant factor
        // And clamped.
        if(world.isWater(position)) desiredTemperature = (int) (desiredTemperature * 1.5);
        desiredTemperature = Math.clamp(desiredTemperature, -10000, 10000);

        // Then, the difference between the desired temperature and current temperature is calculated
        // And used as a reference for the speed of the change
        int diffK = currentTemperature - desiredTemperature;
        int deltaK = diffK / 1000;

        // The result will be a negative (hotter environment) or positive (colder environment)
        // Used as the slope that will influence the temperature change
        return currentTemperature - deltaK;
    }
}
