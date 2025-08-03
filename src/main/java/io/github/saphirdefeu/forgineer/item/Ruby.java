package io.github.saphirdefeu.forgineer.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

public class Ruby extends Item {

    private static final EntityAttributeModifier entityAttributeModifier = new EntityAttributeModifier(
        Identifier.of("minecraft:max_health"), 10.0, EntityAttributeModifier.Operation.ADD_VALUE
    );

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

        Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> modifierMultimap = ArrayListMultimap.create();
        modifierMultimap.put(EntityAttributes.MAX_HEALTH, entityAttributeModifier);
        user.getAttributes().addTemporaryModifiers(modifierMultimap);

        user.getStackInHand(hand).decrement(1);

        return ActionResult.SUCCESS;
    }


}
