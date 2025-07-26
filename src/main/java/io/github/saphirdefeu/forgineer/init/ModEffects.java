package io.github.saphirdefeu.forgineer.init;

import io.github.saphirdefeu.forgineer.Forgineer;
import io.github.saphirdefeu.forgineer.effects.HypothermiaEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModEffects {

    public static final RegistryEntry<StatusEffect> HYPOTHERMIA = Registry.registerReference(
            Registries.STATUS_EFFECT,
            Identifier.of(Forgineer.MOD_ID, "hypothermia"),
            new HypothermiaEffect()
    );

    public static void initialize() {

    }

}
