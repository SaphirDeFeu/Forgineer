package io.github.saphirdefeu.forgineer.init;

import io.github.saphirdefeu.forgineer.Forgineer;
import io.github.saphirdefeu.forgineer.effect.HotHandEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModEffects {

    public static final RegistryEntry<StatusEffect> HOT_HAND = Registry.registerReference(
            Registries.STATUS_EFFECT,
            Identifier.of(Forgineer.MOD_ID, "hot_hand"),
            new HotHandEffect()
    );

    public static void initialize() {

    }

}
