package io.github.saphirdefeu.forgineer.item;

import io.github.saphirdefeu.forgineer.init.ModEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

public class MoltenMetal extends Item {

    public MoltenMetal(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        if(entity instanceof LivingEntity) {
            var hot_hand_instance = new StatusEffectInstance(ModEffects.HOT_HAND, 20, 0, false, true, true);
            ((LivingEntity) entity).addStatusEffect(hot_hand_instance);
        }
        super.inventoryTick(stack, world, entity, slot);
    }
}
