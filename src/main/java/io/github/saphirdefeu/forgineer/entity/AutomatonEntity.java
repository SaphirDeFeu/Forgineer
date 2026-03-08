package io.github.saphirdefeu.forgineer.entity;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class AutomatonEntity extends GolemEntity implements Angerable {

    private static final UniformIntProvider ANGER_TIME_RANGE;
    private int attackTicksLeft;
    private int angerTime;
    @Nullable
    private UUID angryAt;

    private HashMap<UUID, Integer> reputationMap = new HashMap<>();
    private int maxReputation = 50000;

    public AutomatonEntity(EntityType<? extends GolemEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void initGoals() {
        this.goalSelector.add(2, new WanderNearTargetGoal(this, 0.2, 32.0f));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
        this.targetSelector.add(4, new UniversalAngerGoal(this, false));
    }

    public static DefaultAttributeContainer.Builder createIronGolemAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 100.0F)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.25F)
                .add(EntityAttributes.KNOCKBACK_RESISTANCE, 1.0F)
                .add(EntityAttributes.ATTACK_DAMAGE, 15.0F)
                .add(EntityAttributes.STEP_HEIGHT, 1.0F);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.attackTicksLeft > 0) {
            --this.attackTicksLeft;
        }

        if(this.getWorld().isClient()) return;

        Collection<ServerPlayerEntity> playersNear = PlayerLookup.around((ServerWorld) this.getWorld(), this.getPos(), 32.0f);

        updatePlayerReputation(playersNear);
        ServerPlayerEntity highestRepPlayer = getHighestReputation(playersNear);
        if(highestRepPlayer != null) {
            this.setTarget(highestRepPlayer);

            int playerRep = reputationMap.get(highestRepPlayer.getUuid());
            if(playerRep > maxReputation / 2) this.setAngryAt(highestRepPlayer.getUuid());
        }

        this.tickAngerLogic((ServerWorld)this.getWorld(), true);
    }

    private void updatePlayerReputation(Collection<ServerPlayerEntity> players) {
        for(ServerPlayerEntity player : players) {
            UUID uuid = player.getUuid();
            if(!reputationMap.containsKey(uuid)) reputationMap.put(uuid, 1);
        }

        reputationMap.replaceAll((u, v) -> reputationMap.get(u) - 1);
    }

    private @Nullable ServerPlayerEntity getHighestReputation(Collection<ServerPlayerEntity> players) {
        if(this.getWorld().isClient()) return null;

        ServerPlayerEntity highestRepPlayer = null;
        int highestRep = -1;
        for(ServerPlayerEntity player : players) {
            if(!reputationMap.containsKey(player.getUuid())) continue;

            int rep = reputationMap.get(player.getUuid());
            if(rep > highestRep) {
                highestRepPlayer = player;
                highestRep = rep;
            }
        }

        return highestRepPlayer;
    }

    @Override
    public boolean canTarget(EntityType<?> type) {
        return true;
    }

    private float getAttackDamage() {
        return (float)this.getAttributeValue(EntityAttributes.ATTACK_DAMAGE);
    }

    public boolean tryAttack(ServerWorld world, Entity target) {
        this.attackTicksLeft = 2;
        world.sendEntityStatus(this, (byte)4);
        float f = this.getAttackDamage();
        float g = (int)f > 0 ? f / 2.0F + (float)this.random.nextInt((int)f) : f;
        DamageSource damageSource = this.getDamageSources().mobAttack(this);
        boolean bl = target.damage(world, damageSource, g);
        if (bl) {
            EnchantmentHelper.onTargetDamaged(world, target, damageSource);
        }

        this.playSound(SoundEvents.ENTITY_IRON_GOLEM_DAMAGE, 1.0F, 1.0F);
        return bl;
    }

    public int getAttackTicksLeft() {
        return this.attackTicksLeft;
    }

    @Override
    public int getAngerTime() {
        return 0;
    }

    @Override
    public void setAngerTime(int angerTime) {
        this.angerTime = angerTime;
    }

    @Override
    public @Nullable UUID getAngryAt() {
        return angryAt;
    }

    @Override
    public void setAngryAt(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }

    @Override
    public void chooseRandomAngerTime() {
        this.setAngerTime(ANGER_TIME_RANGE.get(this.random));
    }

    static {
        ANGER_TIME_RANGE = TimeHelper.betweenSeconds(100, 200);
    }
}
