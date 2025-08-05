package io.github.saphirdefeu.forgineer.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.github.saphirdefeu.forgineer.Forgineer;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class RubyNetherite extends Item {

    private static final EntityAttributeModifier entityAttributeModifier = new EntityAttributeModifier(
            Identifier.of(Forgineer.MOD_ID, "gemstone"), 20.0, EntityAttributeModifier.Operation.ADD_VALUE
    );

    public static final Settings settings = new Settings()
            .maxCount(1)
            .rarity(Rarity.EPIC);

    public RubyNetherite(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if(world.isClient) {
            return ActionResult.PASS;
        }

        final double healthModifier = user.getAttributes().getModifierValue(EntityAttributes.MAX_HEALTH, Identifier.of(Forgineer.MOD_ID, "gemstone"));

        if(healthModifier >= 20.0f) {
            user.sendMessage(
                    Text.translatable("forgineer.text.consume_gemstone_fail")
                            .formatted(Formatting.DARK_RED, Formatting.BOLD),
                    false
            );

            user.playSoundToPlayer(SoundEvents.BLOCK_ANVIL_FALL, SoundCategory.PLAYERS, 10f, 1f);

            return ActionResult.FAIL;
        }

        Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> modifierMultimap = ArrayListMultimap.create();
        modifierMultimap.put(EntityAttributes.MAX_HEALTH, entityAttributeModifier);
        user.getAttributes().addTemporaryModifiers(modifierMultimap);

        Gemstone.saveAttributeModifier(world, user, entityAttributeModifier, EntityAttributes.MAX_HEALTH);

        user.sendMessage(
                Text.translatable("forgineer.text.consume_gemstone_success", user.getStackInHand(hand).getName())
                        .formatted(Formatting.RED),
                false
        );

        user.playSoundToPlayer(SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS, 10f, 1f);

        user.getStackInHand(hand).decrement(1);

        return ActionResult.SUCCESS;
    }
}
