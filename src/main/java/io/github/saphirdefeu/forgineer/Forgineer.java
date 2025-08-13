package io.github.saphirdefeu.forgineer;

import io.github.saphirdefeu.forgineer.init.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Forgineer implements ModInitializer {

    public static final String MOD_ID = "forgineer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final RegistryKey<PlacedFeature> GRAPHITE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(Forgineer.MOD_ID, "ore_graphite"));
    public static final RegistryKey<PlacedFeature> RUBY_ORE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(Forgineer.MOD_ID, "ore_ruby"));
    public static final RegistryKey<PlacedFeature> BLOODSTONE_ORE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(Forgineer.MOD_ID, "ore_bloodstone"));
    public static final RegistryKey<PlacedFeature> ONYX_ORE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(Forgineer.MOD_ID, "ore_onyx"));
    public static final RegistryKey<PlacedFeature> SAPPHIRE_ORE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(Forgineer.MOD_ID, "ore_sapphire"));
    public static final RegistryKey<PlacedFeature> AQUAMARINE_ORE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(Forgineer.MOD_ID, "ore_aquamarine"));

    @Override
    public void onInitialize() {

        LOGGER.info("Initializing Forgineer");

        ModItemGroup.initialize();

        ModBlocks.initialize();
        ModItems.initialize();
        ModEffects.initialize();

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, GRAPHITE_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(RegistryKey.of(RegistryKeys.BIOME, Identifier.of("minecraft:lush_caves"))), GenerationStep.Feature.UNDERGROUND_ORES, RUBY_ORE_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(RegistryKey.of(RegistryKeys.BIOME, Identifier.of("minecraft:dripstone_caves"))), GenerationStep.Feature.UNDERGROUND_ORES, BLOODSTONE_ORE_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(RegistryKey.of(RegistryKeys.BIOME, Identifier.of("minecraft:deep_dark"))), GenerationStep.Feature.UNDERGROUND_ORES, ONYX_ORE_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.tag(TagKey.of(RegistryKeys.BIOME, Identifier.of("c:is_cold"))), GenerationStep.Feature.UNDERGROUND_ORES, SAPPHIRE_ORE_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.tag(BiomeTags.IS_OCEAN), GenerationStep.Feature.UNDERGROUND_ORES, AQUAMARINE_ORE_PLACED_KEY);

        EventRegistrar.registerEvents();

        LOGGER.info("Forgineer initialized");
    }
}
