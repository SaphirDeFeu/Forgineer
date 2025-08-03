package io.github.saphirdefeu.forgineer.init;

import io.github.saphirdefeu.forgineer.event.PlayConnectionListener;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class EventRegistrar {

    public static void registerEvents() {
        ServerPlayConnectionEvents.JOIN.register(PlayConnectionListener::onJoin);
    }

}
