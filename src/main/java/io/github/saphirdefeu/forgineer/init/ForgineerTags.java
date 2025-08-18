package io.github.saphirdefeu.forgineer.init;

import io.github.saphirdefeu.forgineer.Forgineer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public final class ForgineerTags {
    public static final class Items {
        public static final TagKey<Item> DRILL_TOOL_MATERIALS = new TagKey<>(RegistryKeys.ITEM, Identifier.of(Forgineer.MOD_ID, "drill_tool_materials"));
    }

    public static final class Blocks {
        public static final TagKey<Block> INCORRECT_FOR_DRILL = new TagKey<>(RegistryKeys.BLOCK, Identifier.of(Forgineer.MOD_ID, "incorrect_for_drill"));
    }
}
