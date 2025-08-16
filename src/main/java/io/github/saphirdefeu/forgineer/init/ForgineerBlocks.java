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

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ForgineerBlocks {

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

    public static final Block ONYX_ORE = register("onyx_ore", Block::new,
            AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)
                    .requiresTool()
                    .hardness(2.0f)
                    .resistance(7.5f)
                    .luminance(blockState -> 5),
            true
    );

    public static final Block SAPPHIRE_ORE = register("sapphire_ore", Block::new,
            AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)
                    .requiresTool()
                    .hardness(1.8f)
                    .resistance(7.0f),
            true
    );

    public static final Block AQUAMARINE_ORE = register("aquamarine_ore", Block::new,
            AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)
                    .requiresTool()
                    .hardness(1.6f)
                    .resistance(6.5f),
            true
    );

    public static final Block TOPAZ_ORE = register("topaz_ore", Block::new,
            AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)
                    .requiresTool()
                    .hardness(2.5f)
                    .resistance(10.0f)
                    .luminance(blockState -> 15),
            true
    );

    private static Block register(
            String name,
            @NotNull Function<AbstractBlock.Settings, Block> blockFactory,
            AbstractBlock.@NotNull Settings settings,
            boolean shouldRegisterItem
    ) {
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
                .modifyEntriesEvent(ForgineerItemGroup.FORGINEER_ITEM_GROUP_KEY)
                .register((itemGroup) -> {
                    itemGroup.add(ForgineerBlocks.GRAPHITE.asItem());
                    itemGroup.add(ForgineerBlocks.RUBY_ORE.asItem());
                    itemGroup.add(ForgineerBlocks.BLOODSTONE_ORE.asItem());
                    itemGroup.add(ForgineerBlocks.ONYX_ORE.asItem());
                    itemGroup.add(ForgineerBlocks.SAPPHIRE_ORE.asItem());
                    itemGroup.add(ForgineerBlocks.AQUAMARINE_ORE.asItem());
                    itemGroup.add(ForgineerBlocks.TOPAZ_ORE.asItem());
                });
    }

}
