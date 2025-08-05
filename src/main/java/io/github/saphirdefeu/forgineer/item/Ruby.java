package io.github.saphirdefeu.forgineer.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.github.saphirdefeu.forgineer.Forgineer;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class Ruby extends Item {

    public static final Settings settings = new Settings()
            .rarity(Rarity.UNCOMMON);

    public Ruby(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if(world.isClient) {
            return ActionResult.PASS;
        }

        double healthModifier = 0.0;

        // Check if the modifier forgineer:gemstone exists.
        // If true, then set modifier to that value
        // Otherwise, leave it at 0
        AttributeContainer attributes = user.getAttributes();
        if(attributes.hasModifierForAttribute(EntityAttributes.MAX_HEALTH, Identifier.of(Forgineer.MOD_ID, "gemstone"))) {
            healthModifier = user.getAttributes().getModifierValue(EntityAttributes.MAX_HEALTH, Identifier.of(Forgineer.MOD_ID, "gemstone"));
        }

        if(healthModifier >= 10.0f) {
            user.sendMessage(
                    Text.translatable("forgineer.text.consume_gemstone_fail")
                            .formatted(Formatting.DARK_RED, Formatting.BOLD),
                    false
            );

            user.playSoundToPlayer(SoundEvents.BLOCK_ANVIL_FALL, SoundCategory.PLAYERS, 10f, 1f);

            return ActionResult.FAIL;
        }

        EntityAttributeModifier entityAttributeModifier = new EntityAttributeModifier(
                Identifier.of(Forgineer.MOD_ID, "gemstone"), healthModifier + 2.0f, EntityAttributeModifier.Operation.ADD_VALUE
        );
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
