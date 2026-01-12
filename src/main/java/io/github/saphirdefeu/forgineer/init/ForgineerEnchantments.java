package io.github.saphirdefeu.forgineer.init;

import io.github.saphirdefeu.forgineer.Forgineer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ForgineerEnchantments {
    public static final RegistryKey<Enchantment> PRISTINE = of("pristine");
    public static final RegistryKey<Enchantment> GEMFINDER = of("gemfinder");

    /**
     * Returns an enchantment's registry key
     * @param name enchantment identifier without mod id
     * @return its registry key
     */
    private static RegistryKey<Enchantment> of(String name) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Forgineer.MOD_ID, name));
    }

    public static void initialize() {
    }
}
