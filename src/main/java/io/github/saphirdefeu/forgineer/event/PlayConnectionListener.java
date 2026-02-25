package io.github.saphirdefeu.forgineer.event;

import io.github.saphirdefeu.forgineer.item.Gemstone;
import io.github.saphirdefeu.forgineer.state.StateManager;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public class PlayConnectionListener {
    public static void onJoin(
            ServerPlayNetworkHandler serverPlayNetworkHandler,
            PacketSender packetSender,
            MinecraftServer minecraftServer
    ) {
        StateManager state = StateManager.getServerState(minecraftServer);
        PlayerEntity player = serverPlayNetworkHandler.getPlayer();
        Gemstone.setPlayerAttributeModifiers(player, state);
    }
}
