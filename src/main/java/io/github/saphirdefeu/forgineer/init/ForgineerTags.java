package io.github.saphirdefeu.forgineer.init;

import io.github.saphirdefeu.forgineer.Forgineer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public final class ForgineerTags {
    public static final class Items {
        public static final TagKey<Item> DRILL_TOOL_MATERIALS = TagKey.of(RegistryKeys.ITEM, Identifier.of(Forgineer.MOD_ID, "drill_tool_materials"));
        public static final TagKey<Item> AQUAMARINES = TagKey.of(RegistryKeys.ITEM, Identifier.of(Forgineer.MOD_ID, "aquamarines"));
        public static final TagKey<Item> BLOODSTONES = TagKey.of(RegistryKeys.ITEM, Identifier.of(Forgineer.MOD_ID, "bloodstones"));
        public static final TagKey<Item> ONYXES = TagKey.of(RegistryKeys.ITEM, Identifier.of(Forgineer.MOD_ID, "onyxes"));
        public static final TagKey<Item> RUBIES = TagKey.of(RegistryKeys.ITEM, Identifier.of(Forgineer.MOD_ID, "rubies"));
        public static final TagKey<Item> SAPPHIRES = TagKey.of(RegistryKeys.ITEM, Identifier.of(Forgineer.MOD_ID, "sapphires"));
        public static final TagKey<Item> TOPAZES = TagKey.of(RegistryKeys.ITEM, Identifier.of(Forgineer.MOD_ID, "topazes"));
        public static final TagKey<Item> UNCUT_GEMS = TagKey.of(RegistryKeys.ITEM, Identifier.of(Forgineer.MOD_ID, "uncut_gems"));
        public static final TagKey<Item> CUT_GEMS = TagKey.of(RegistryKeys.ITEM, Identifier.of(Forgineer.MOD_ID, "cut_gems"));
        public static final TagKey<Item> NETHERITE_GEMS = TagKey.of(RegistryKeys.ITEM, Identifier.of(Forgineer.MOD_ID, "netherite_gems"));
        public static final TagKey<Item> MOLTEN_METALS = TagKey.of(RegistryKeys.ITEM, Identifier.of(Forgineer.MOD_ID, "molten_metals"));
        public static final TagKey<Item> CASTING_MOLDS = TagKey.of(RegistryKeys.ITEM, Identifier.of(Forgineer.MOD_ID, "casting_molds"));

        public static final class CanBeSmeltInto {
            public static final TagKey<Item> MOLTEN_COPPER = TagKey.of(RegistryKeys.ITEM, Identifier.of(Forgineer.MOD_ID, "can_be_smelt_into/molten_copper"));
            public static final TagKey<Item> MOLTEN_GOLD = TagKey.of(RegistryKeys.ITEM, Identifier.of(Forgineer.MOD_ID, "can_be_smelt_into/molten_gold"));
            public static final TagKey<Item> MOLTEN_IRON = TagKey.of(RegistryKeys.ITEM, Identifier.of(Forgineer.MOD_ID, "can_be_smelt_into/molten_iron"));
        }
    }

    public static final class Blocks {
        public static final TagKey<Block> INCORRECT_FOR_DRILL = TagKey.of(RegistryKeys.BLOCK, Identifier.of(Forgineer.MOD_ID, "incorrect_for_drill"));
        public static final TagKey<Block> NEEDS_DRILL = TagKey.of(RegistryKeys.BLOCK, Identifier.of(Forgineer.MOD_ID, "needs_drill"));
        public static final TagKey<Block> GEMSTONES = TagKey.of(RegistryKeys.BLOCK, Identifier.of(Forgineer.MOD_ID, "gemstones"));
    }
}
