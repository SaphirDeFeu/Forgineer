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

public class Topaz extends Item {

    public static final Settings settings = new Settings()
            .rarity(Rarity.UNCOMMON);

    public Topaz(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if(world.isClient) {
            return ActionResult.PASS;
        }

        double miningModifier = 0.0;

        // Check if the modifier forgineer:gemstone exists.
        // If true, then set modifier to that value
        // Otherwise, leave it at 0
        AttributeContainer attributes = user.getAttributes();
        if(attributes.hasModifierForAttribute(EntityAttributes.BLOCK_BREAK_SPEED, Identifier.of(Forgineer.MOD_ID, "gemstone"))) {
            miningModifier = user.getAttributes().getModifierValue(EntityAttributes.BLOCK_BREAK_SPEED, Identifier.of(Forgineer.MOD_ID, "gemstone"));
        }

        if(miningModifier > 0.45f) {
            user.sendMessage(
                    Text.translatable("forgineer.text.consume_gemstone_fail")
                            .formatted(Formatting.DARK_RED, Formatting.BOLD),
                    false
            );

            world.playSound(null, user.getBlockX(), user.getBlockY(), user.getBlockZ(), SoundEvents.BLOCK_ANVIL_FALL, SoundCategory.PLAYERS, 10f, 1f, 0L);

            return ActionResult.FAIL;
        }

        EntityAttributeModifier entityAttributeModifier = new EntityAttributeModifier(
                Identifier.of(Forgineer.MOD_ID, "gemstone"), miningModifier + 0.1f, EntityAttributeModifier.Operation.ADD_VALUE
        );
        Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> modifierMultimap = ArrayListMultimap.create();
        modifierMultimap.put(EntityAttributes.BLOCK_BREAK_SPEED, entityAttributeModifier);
        user.getAttributes().addTemporaryModifiers(modifierMultimap);

        Gemstone.saveAttributeModifier(world, user, entityAttributeModifier, EntityAttributes.BLOCK_BREAK_SPEED);

        user.sendMessage(
                Text.translatable("forgineer.text.consume_gemstone_success", user.getStackInHand(hand).getName())
                        .formatted(Formatting.YELLOW),
                false
        );

        world.playSound(null, user.getBlockX(), user.getBlockY(), user.getBlockZ(), SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS, 10f, 1f, 0L);

        user.getStackInHand(hand).decrement(1);

        return ActionResult.SUCCESS;
    }
}
