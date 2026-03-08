package io.github.saphirdefeu.forgineer.entity.ai.goal;

import io.github.saphirdefeu.forgineer.entity.AutomatonEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class AutomatonAttackSuspect extends Goal {
    private final AutomatonEntity mob;
    private LivingEntity target;
    private int cooldown;

    private double x;
    private double y;
    private double z;

    public AutomatonAttackSuspect(AutomatonEntity mob) {
        this.mob = mob;
        this.setControls(EnumSet.of(Control.LOOK));
    }

    @Override
    public boolean canStart() {
        LivingEntity tmp = this.mob.getTarget();
        if(this.mob.isAngry() && tmp != null) {
            this.target = tmp;
            return true;
        }
        return false;
    }

    public boolean shouldContinue() {
        if (!this.target.isAlive()) {
            this.mob.removeReputation(this.target.getUuid(), AutomatonEntity.MAX_REPUTATION);
            this.mob.setAngryAt(null);
            return false;
        } else {
            return !this.mob.getNavigation().isIdle() || this.canStart();
        }
    }

    public void stop() {
        this.target = null;
    }

    public void tick() {
        this.mob.getLookControl().lookAt(this.target, 30.0F, 30.0F);
        double attackDistance = this.mob.getWidth() * 2.0F * this.mob.getWidth() * 2.0F;
        double distance = this.mob.squaredDistanceTo(this.target.getX(), this.target.getY(), this.target.getZ());

        this.mob.getNavigation().startMovingTo(this.target, 0.1f);
        this.cooldown = Math.max(this.cooldown - 1, 0);
        if (!(distance > attackDistance) && this.cooldown <= 0) {
            this.cooldown = 10;
            this.mob.tryAttack(getServerWorld(this.mob), this.target);
        }
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }
}
