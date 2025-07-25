package io.github.saphirdefeu.forgineer;

import io.github.saphirdefeu.forgineer.init.ModBlocks;
import io.github.saphirdefeu.forgineer.init.ModItems;
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
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Forgineer implements ModInitializer {

    public static final String MOD_ID = "forgineer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final RegistryKey<ItemGroup> FORGINEER_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(Forgineer.MOD_ID, "item_group"));
    public static final ItemGroup FORGINEER_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.GRAPHITE_POWDER))
            .displayName(Text.translatable("itemGroup.forgineer"))
            .build();

    public static final RegistryKey<PlacedFeature> GRAPHITE_PLACED_KEY = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(Forgineer.MOD_ID, "ore_graphite"));

    @Override
    public void onInitialize() {

        LOGGER.info("Initializing Forgineer");

        Registry.register(Registries.ITEM_GROUP, FORGINEER_ITEM_GROUP_KEY, FORGINEER_ITEM_GROUP);

        ModItems.initialize();
        ModBlocks.initialize();

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, GRAPHITE_PLACED_KEY);
    }
}
