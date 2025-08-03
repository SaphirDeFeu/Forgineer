package io.github.saphirdefeu.forgineer.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Rarity;
import org.jetbrains.annotations.Nullable;

public class Ruby extends Item {

    public static final Settings settings = new Settings().rarity(Rarity.UNCOMMON);

    public Ruby(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        if(entity instanceof LivingEntity) {
            StatusEffectInstance instance = new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 2, 1, false, false, true);
            ((LivingEntity) entity).addStatusEffect(instance);
        }

        super.inventoryTick(stack, world, entity, slot);
    }
}
