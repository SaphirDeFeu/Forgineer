package io.github.saphirdefeu.forgineer.init;

import io.github.saphirdefeu.forgineer.Forgineer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ForgineerSounds {

    public static final SoundEvent ENTITY_AUTOMATON_FRIENDLY = register("automaton.friendly");
    public static final SoundEvent ENTITY_AUTOMATON_SUSPICIOUS = register("automaton.suspicious");
    public static final SoundEvent ENTITY_AUTOMATON_ANGERED = register("automaton.angered");
    public static final SoundEvent ENTITY_AUTOMATON_FURIOUS = register("automaton.furious");
    public static final SoundEvent ENTITY_AUTOMATON_HOSTILE = register("automaton.hostile");

    public static SoundEvent register(String name) {
        Identifier id = Identifier.of(Forgineer.MOD_ID, name);
        SoundEvent event = SoundEvent.of(id);
        Registry.register(Registries.SOUND_EVENT, id, event);

        return event;
    }

    public static void initialize() {}

}
