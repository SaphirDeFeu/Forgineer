package io.github.saphirdefeu.forgineer.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;

public class HotHandEffect extends StatusEffect {

    public HotHandEffect() {
        super(StatusEffectCategory.HARMFUL, 0xff9a5f);
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        return super.applyUpdateEffect(world, entity, amplifier);
    }

}
