package io.github.saphirdefeu.forgineer.entity.ai.goal;

import io.github.saphirdefeu.forgineer.entity.AutomatonEntity;
import io.github.saphirdefeu.forgineer.init.ForgineerEntities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.TypeFilter;

import java.util.EnumSet;
import java.util.List;

public class AutomatonAttackSuspect extends Goal {
    private final AutomatonEntity mob;
    private LivingEntity target;

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
            List<AutomatonEntity> automatonsNearby = ForgineerEntities.getEntitiesAround(this.mob.getWorld(), this.mob.getBlockPos(), 32.0f, TypeFilter.instanceOf(AutomatonEntity.class));
            for(AutomatonEntity automaton : automatonsNearby) {
                automaton.removeReputation(this.target.getUuid(), AutomatonEntity.MAX_REPUTATION);
                automaton.setAngryAt(null);
            }
            return false;
        } else {
            return !this.mob.getNavigation().isIdle() || this.canStart();
        }
    }

    public void stop() {
        this.target = null;
    }

    public void tick() {
        if(!this.mob.canSee(this.target)) return;
        this.mob.getLookControl().lookAt(this.target, 200.0f, 200.0f);
        double attackDistanceSquared = this.mob.getWidth() * 2.0F * this.mob.getWidth() * 2.0F;
        double distanceSquared = this.mob.squaredDistanceTo(this.target.getX(), this.target.getY(), this.target.getZ());

        this.mob.getNavigation().startMovingTo(this.target, 0.1f);
        this.mob.setAttackTicksLeft(Math.max(this.mob.getAttackTicksLeft() - 1, 0));
        if (distanceSquared > attackDistanceSquared && this.mob.getAttackTicksLeft() <= 0 && distanceSquared <= 32.0f * 32.0f) {
            this.mob.setAttackTicksLeft(10);
            this.mob.tryAttack(getServerWorld(this.mob), this.target);
        }
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }
}
