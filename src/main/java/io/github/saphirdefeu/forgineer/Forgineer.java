package io.github.saphirdefeu.forgineer;

import io.github.saphirdefeu.forgineer.init.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Forgineer implements ModInitializer {

    public static final String MOD_ID = "forgineer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final RegistryKey<PlacedFeature> GRAPHITE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(Forgineer.MOD_ID, "ore_graphite"));
    public static final RegistryKey<PlacedFeature> RUBY_ORE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(Forgineer.MOD_ID, "ore_ruby"));

    @Override
    public void onInitialize() {

        LOGGER.info("Initializing Forgineer");

        ModItemGroup.initialize();

        ModBlocks.initialize();
        ModItems.initialize();
        ModEffects.initialize();

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, GRAPHITE_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(RegistryKey.of(RegistryKeys.BIOME, Identifier.of("minecraft:lush_caves"))), GenerationStep.Feature.UNDERGROUND_ORES, RUBY_ORE_PLACED_KEY);

        EventRegistrar.registerEvents();
    }
}
