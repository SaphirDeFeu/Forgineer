package io.github.saphirdefeu.forgineer.init;

import io.github.saphirdefeu.forgineer.event.AttackBlockListener;
import io.github.saphirdefeu.forgineer.event.PlayConnectionListener;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class ForgineerEventRegistrar {

    public static void registerEvents() {

        ServerPlayConnectionEvents.JOIN.register(PlayConnectionListener::onJoin);

        AttackBlockCallback.EVENT.register(AttackBlockListener::onAttackBlock);

    }

}
