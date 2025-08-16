package io.github.saphirdefeu.forgineer.init;

import io.github.saphirdefeu.forgineer.Forgineer;
import io.github.saphirdefeu.forgineer.item.*;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ForgineerItems {

    public static final Item GRAPHITE_POWDER = register("graphite_powder", Item::new, new Item.Settings());

    public static final Item INGOT_CASTING_MOLD = register("ingot_casting_mold", CastingMold::new, CastingMold.settings);
    public static final Item PLATE_CASTING_MOLD = register("plate_casting_mold", CastingMold::new, CastingMold.settings);
    public static final Item BAR_CASTING_MOLD = register("bar_casting_mold", CastingMold::new, CastingMold.settings);

    public static final Item MOLTEN_IRON = register("molten_iron", MoltenMetal::new, MoltenMetal.settings);
    public static final Item MOLTEN_GOLD = register("molten_gold", MoltenMetal::new, MoltenMetal.settings);
    public static final Item MOLTEN_COPPER = register("molten_copper", MoltenMetal::new, MoltenMetal.settings);

    public static final Item UNCUT_RUBY = register("uncut_ruby", Item::new, new Item.Settings());
    public static final Item RUBY = register("ruby", Ruby::new, Ruby.settings);
    public static final Item RUBY_NETHERITE = register("ruby_netherite", RubyNetherite::new, RubyNetherite.settings);

    public static final Item UNCUT_BLOODSTONE = register("uncut_bloodstone", Item::new, new Item.Settings());
    public static final Item BLOODSTONE = register("bloodstone", Bloodstone::new, Bloodstone.settings);
    public static final Item BLOODSTONE_NETHERITE = register("bloodstone_netherite", BloodstoneNetherite::new, BloodstoneNetherite.settings);

    public static final Item UNCUT_ONYX = register("uncut_onyx", Item::new, new Item.Settings());
    public static final Item ONYX = register("onyx", Onyx::new, Onyx.settings);
    public static final Item ONYX_NETHERITE = register("onyx_netherite", OnyxNetherite::new, OnyxNetherite.settings);

    public static final Item UNCUT_SAPPHIRE = register("uncut_sapphire", Item::new, new Item.Settings());
    public static final Item SAPPHIRE = register("sapphire", Sapphire::new, Sapphire.settings);
    public static final Item SAPPHIRE_NETHERITE = register("sapphire_netherite", SapphireNetherite::new, SapphireNetherite.settings);

    public static final Item UNCUT_AQUAMARINE = register("uncut_aquamarine", Item::new, new Item.Settings());
    public static final Item AQUAMARINE = register("aquamarine", Aquamarine::new, Aquamarine.settings);
    public static final Item AQUAMARINE_NETHERITE = register("aquamarine_netherite", AquamarineNetherite::new, AquamarineNetherite.settings);

    public static final Item UNCUT_TOPAZ = register("uncut_topaz", Item::new, new Item.Settings());
    public static final Item TOPAZ = register("topaz", Topaz::new, Topaz.settings);
    public static final Item TOPAZ_NETHERITE = register("topaz_netherite", TopazNetherite::new, TopazNetherite.settings);

    public static final Item GEMSTONE_DETECTOR = register("gemstone_detector", GemstoneDetector::new, GemstoneDetector.settings);

    private static Item register(String name, @NotNull Function<Item.Settings, Item> itemFactory, Item.@NotNull Settings settings) {
        // Create item key, item instance, and register the item instance.
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Forgineer.MOD_ID, name));
        Item item = itemFactory.apply(settings.registryKey(itemKey));
        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }

    public static void initialize() {
        ItemGroupEvents
                .modifyEntriesEvent(ForgineerItemGroup.FORGINEER_ITEM_GROUP_KEY)
                .register(
                (itemGroup) -> {
                    itemGroup.add(GRAPHITE_POWDER);
                    itemGroup.add(INGOT_CASTING_MOLD);
                    itemGroup.add(PLATE_CASTING_MOLD);
                    itemGroup.add(BAR_CASTING_MOLD);

                    itemGroup.add(MOLTEN_IRON);
                    itemGroup.add(MOLTEN_GOLD);
                    itemGroup.add(MOLTEN_COPPER);

                    itemGroup.add(UNCUT_RUBY);
                    itemGroup.add(RUBY);
                    itemGroup.add(RUBY_NETHERITE);

                    itemGroup.add(UNCUT_BLOODSTONE);
                    itemGroup.add(BLOODSTONE);
                    itemGroup.add(BLOODSTONE_NETHERITE);

                    itemGroup.add(UNCUT_ONYX);
                    itemGroup.add(ONYX);
                    itemGroup.add(ONYX_NETHERITE);

                    itemGroup.add(UNCUT_SAPPHIRE);
                    itemGroup.add(SAPPHIRE);
                    itemGroup.add(SAPPHIRE_NETHERITE);

                    itemGroup.add(UNCUT_AQUAMARINE);
                    itemGroup.add(AQUAMARINE);
                    itemGroup.add(AQUAMARINE_NETHERITE);

                    itemGroup.add(UNCUT_TOPAZ);
                    itemGroup.add(TOPAZ);
                    itemGroup.add(TOPAZ_NETHERITE);

                    itemGroup.add(GEMSTONE_DETECTOR);
                }
        );
    }
}
