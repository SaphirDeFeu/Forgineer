package io.github.saphirdefeu.forgineer.init;

import io.github.saphirdefeu.forgineer.Forgineer;
import io.github.saphirdefeu.forgineer.item.MoltenMetal;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModItems {

    public static final Item GRAPHITE_POWDER = register("graphite_powder", Item::new, new Item.Settings());

    public static final Item USED_CASTING_MOLD = register("used_casting_mold", Item::new,
            new Item.Settings()
                    .fireproof()
                    .maxCount(1)
    );

    public static final Item CASTING_MOLD = register("casting_mold", Item::new,
            new Item.Settings()
                    .recipeRemainder(USED_CASTING_MOLD)
                    .fireproof()
                    .maxCount(1)
    );

    private static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        // Create item key, item instance, and register the item instance.
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Forgineer.MOD_ID, name));
        Item item = itemFactory.apply(settings.registryKey(itemKey));
        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }

    public static void initialize() {
        ItemGroupEvents
                .modifyEntriesEvent(ModItemGroup.FORGINEER_ITEM_GROUP_KEY)
                .register(
                (itemGroup) -> {
                    itemGroup.add(GRAPHITE_POWDER);
                    itemGroup.add(CASTING_MOLD);
                    itemGroup.add(USED_CASTING_MOLD);
                }
        );
    }
}
