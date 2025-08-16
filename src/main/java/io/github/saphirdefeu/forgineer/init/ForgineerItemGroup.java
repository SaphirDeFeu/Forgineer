package io.github.saphirdefeu.forgineer.init;

import io.github.saphirdefeu.forgineer.Forgineer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ForgineerItemGroup {

    public static RegistryKey<ItemGroup> FORGINEER_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(Forgineer.MOD_ID, "main_group"));
    public static final ItemGroup FORGINEER_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ForgineerItems.GRAPHITE_POWDER))
            .displayName(Text.translatable("itemGroup.forgineer"))
            .build();

    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, FORGINEER_ITEM_GROUP_KEY, FORGINEER_ITEM_GROUP);
    }

}
