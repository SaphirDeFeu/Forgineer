package io.github.saphirdefeu.forgineer.init;

import io.github.saphirdefeu.forgineer.Forgineer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModBlocks {

    public static final Block GRAPHITE = register(
            "graphite",
            Block::new,
            AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.STONE)
                    .requiresTool()
                    .hardness(1.4f)
                    .resistance(5.0f),
            true
    );

    public static final Block RUBY_ORE = register("ruby_ore", Block::new,
            AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)
                    .requiresTool()
                    .hardness(2.0f)
                    .resistance(7.5f)
                    .luminance(blockState -> 9),
            true
    );

    public static final Block BLOODSTONE_ORE = register("bloodstone_ore", Block::new,
            AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)
                    .requiresTool()
                    .hardness(2.0f)
                    .resistance(7.5f)
                    .luminance(blockState -> 9),
            true
    );

    private static Block register(
            String name,
            Function<AbstractBlock.Settings, Block> blockFactory,
            AbstractBlock.Settings settings,
            boolean shouldRegisterItem) {
        // Register block key and block instance
        RegistryKey<Block> blockKey = keyOfBlock(name);
        Block block = blockFactory.apply(settings.registryKey(blockKey));

        // Some blocks may not require a corresponding item
        if(shouldRegisterItem) {
            RegistryKey<Item> itemKey = keyOfItem(name);

            BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(itemKey));
            Registry.register(Registries.ITEM, itemKey, blockItem);
        }

        return Registry.register(Registries.BLOCK, blockKey, block);
    }

    private static RegistryKey<Block> keyOfBlock(String name) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Forgineer.MOD_ID, name));
    }

    private static RegistryKey<Item> keyOfItem(String name) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Forgineer.MOD_ID, name));
    }

    public static void initialize() {
        ItemGroupEvents
                .modifyEntriesEvent(ModItemGroup.FORGINEER_ITEM_GROUP_KEY)
                .register((itemGroup) -> {
                    itemGroup.add(ModBlocks.GRAPHITE.asItem());
                    itemGroup.add(ModBlocks.RUBY_ORE.asItem());
                    itemGroup.add(ModBlocks.BLOODSTONE_ORE.asItem());
                });
    }

}
