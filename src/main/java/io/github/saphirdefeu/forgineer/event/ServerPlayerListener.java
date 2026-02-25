package io.github.saphirdefeu.forgineer.event;

import io.github.saphirdefeu.forgineer.item.Gemstone;
import io.github.saphirdefeu.forgineer.state.StateManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class ServerPlayerListener {

    public static void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        ServerWorld serverWorld = oldPlayer.getServerWorld();
        MinecraftServer server = serverWorld.getServer();
        StateManager state = StateManager.getServerState(server);
        Gemstone.setPlayerAttributeModifiers(newPlayer, state);
    }
}