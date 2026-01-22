package io.github.saphirdefeu.forgineer.init;

import io.github.saphirdefeu.forgineer.Forgineer;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionType;

public class ForgineerDimensions {

    public static final RegistryKey<DimensionType> DEEP_MINES = RegistryKey.of(RegistryKeys.DIMENSION_TYPE, Identifier.of(Forgineer.MOD_ID, "deep_mines"));

}
