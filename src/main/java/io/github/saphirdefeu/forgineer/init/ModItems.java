package io.github.saphirdefeu.forgineer.init;

import io.github.saphirdefeu.forgineer.Forgineer;
import io.github.saphirdefeu.forgineer.item.MoltenMetal;
import io.github.saphirdefeu.forgineer.item.Ruby;
import io.github.saphirdefeu.forgineer.item.RubyNetherite;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

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

    public static final Item MOLTEN_IRON = register("molten_iron", MoltenMetal::new, MoltenMetal.settings);
    public static final Item MOLTEN_GOLD = register("molten_gold", MoltenMetal::new, MoltenMetal.settings);
    public static final Item MOLTEN_COPPER = register("molten_copper", MoltenMetal::new, MoltenMetal.settings);

    public static final Item UNCUT_RUBY = register("uncut_ruby", Item::new, new Item.Settings());
    public static final Item RUBY = register("ruby", Ruby::new, Ruby.settings);
    public static final Item RUBY_NETHERITE = register("ruby_netherite", RubyNetherite::new, RubyNetherite.settings);

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

                    itemGroup.add(MOLTEN_IRON);
                    itemGroup.add(MOLTEN_GOLD);
                    itemGroup.add(MOLTEN_COPPER);

                    itemGroup.add(UNCUT_RUBY);
                    itemGroup.add(RUBY);
                    itemGroup.add(RUBY_NETHERITE);
                }
        );
    }
}
