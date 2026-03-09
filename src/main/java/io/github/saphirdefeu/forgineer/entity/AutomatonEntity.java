package io.github.saphirdefeu.forgineer.entity;

import io.github.saphirdefeu.forgineer.Forgineer;
import io.github.saphirdefeu.forgineer.entity.ai.goal.AutomatonAttackSuspect;
import io.github.saphirdefeu.forgineer.init.ForgineerEntities;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class AutomatonEntity extends GolemEntity implements Angerable {

    private static final UniformIntProvider ANGER_TIME_RANGE;

    private boolean isAngry;
    @Nullable
    private UUID angryAt;

    private int attackTicksLeft;
    private int angerTime;
    private int beamCooldown = 20;

    private HashMap<UUID, Integer> reputationMap = new HashMap<>();
    public static final int MAX_REPUTATION = 50000;
    private int highestReputation;

    public AutomatonEntity(EntityType<? extends GolemEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void initGoals() {
        this.goalSelector.add(1, new AutomatonAttackSuspect(this));
        this.goalSelector.add(2, new WanderNearTargetGoal(this, 0.5, 32.0f));
        this.goalSelector.add(3, new WanderAroundPointOfInterestGoal(this, 0.2, false));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(4, new UniversalAngerGoal<>(this, false));
    }

    public static DefaultAttributeContainer.Builder createAutomatonAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 100.0f)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.2f)
                .add(EntityAttributes.KNOCKBACK_RESISTANCE, 1.0f)
                .add(EntityAttributes.ATTACK_DAMAGE, 15.0f)
                .add(EntityAttributes.STEP_HEIGHT, 1.0f);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        if(this.getWorld().isClient()) return;

        Collection<ServerPlayerEntity> playersNear = PlayerLookup.around((ServerWorld) this.getWorld(), this.getPos(), 32.0f);

        updatePlayerReputation(playersNear);
        ServerPlayerEntity highestRepPlayer = getHighestReputation(playersNear);
        if(highestRepPlayer != null) {
            this.setTarget(highestRepPlayer);

            int playerRep = reputationMap.get(highestRepPlayer.getUuid());
            if(playerRep > MAX_REPUTATION / 2) {
                this.setAngryAt(highestRepPlayer.getUuid());
            }
        }

        // send colored laser towards player with different colors based on current stage of anger
        beamCooldown--;
        if(highestReputation > MAX_REPUTATION / 16) shootColorLaser(false);
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        NbtList nbtRepList = new NbtList();

        for(UUID uuid : reputationMap.keySet()) {
            // Convert UUID into a 4-sized integer array
            int[] uuidArray = Uuids.toIntArray(uuid);

            // Get corresponding value
            int reputation = reputationMap.get(uuid);

            NbtCompound keyValuePair = new NbtCompound();
            keyValuePair.putIntArray("uuid", uuidArray);
            keyValuePair.putInt("reputation", reputation);

            nbtRepList.add(keyValuePair);
        }

        nbt.put("reputations", nbtRepList);

        this.writeAngerToNbt(nbt);
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        NbtList nbtRepList = nbt.getList("reputations").orElse(new NbtList());
        int size = nbtRepList.size();
        for(int i = 0; i < size; i++) {
            // Any item in the reputations list is definitively a {uuid: int[4], reputation: int} element
            NbtCompound playerReputation = (NbtCompound) nbtRepList.get(i);
            Optional<int[]> uuidArrayOptional = playerReputation.getIntArray("uuid");
            if(uuidArrayOptional.isEmpty()) continue;

            int[] uuidArray = uuidArrayOptional.get();
            UUID uuid = Uuids.toUuid(uuidArray);
            int reputation = playerReputation.getInt("reputation", 0);

            reputationMap.put(uuid, reputation);
        }

        this.readAngerFromNbt(this.getWorld(), nbt);
    }

    private void updatePlayerReputation(Collection<ServerPlayerEntity> players) {
        for(ServerPlayerEntity player : players) {
            UUID uuid = player.getUuid();
            if(!reputationMap.containsKey(uuid)) reputationMap.put(uuid, 1);

            // +150 rep for every tick the player is running (eq. to +3k per second)
            if(!this.canSee(player)) return;
            if(player.isSprinting()) addReputation(uuid, 150); //150 per tick of sprinting
        }

        for(UUID uuid : reputationMap.keySet()) {
            int val = reputationMap.get(uuid);
            if(val <= 0) {
                reputationMap.put(uuid, 0);
            } else {
                reputationMap.put(uuid, val - 1);
            }
        }
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

        this.highestReputation = highestRep;
        return highestRepPlayer;
    }

    public void addReputation(UUID uuid, int val) {
        if(!reputationMap.containsKey(uuid)) return;
        int rep = reputationMap.get(uuid) + val;
        if(rep > MAX_REPUTATION) rep = MAX_REPUTATION;
        if(rep < 0) rep = 0;
        reputationMap.put(uuid, rep);
    }

    public void removeReputation(UUID uuid, int val) {
        if(!reputationMap.containsKey(uuid)) return;
        int rep = reputationMap.get(uuid) - val;
        if(rep> MAX_REPUTATION) rep = MAX_REPUTATION;
        if(rep < 0) rep = 0;
        reputationMap.put(uuid, rep);
    }

    @Override
    public boolean canTarget(EntityType<?> type) {
        return true;
    }

    private float getAttackDamage() {
        return (float)this.getAttributeValue(EntityAttributes.ATTACK_DAMAGE);
    }

    public boolean tryAttack(ServerWorld world, Entity target) {
        world.sendEntityStatus(this, (byte)4);
        float attackDamage = this.getAttackDamage();
        float g = (int) attackDamage > 0 ? attackDamage / 2.0F + (float)this.random.nextInt((int) attackDamage) : attackDamage;
        DamageSource damageSource = this.getDamageSources().mobAttack(this);
        boolean bl = target.damage(world, damageSource, g);
        if (bl) {
            EnchantmentHelper.onTargetDamaged(world, target, damageSource);
        }

        this.playSound(SoundEvents.ENTITY_GUARDIAN_ATTACK, 1.0F, 0.5F);
        if(bl) this.shootColorLaser(true);
        return bl;
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        boolean bl = super.damage(world, source, amount);
        Entity attacker = source.getAttacker();
        if(attacker == null) return bl;
        if(!(attacker instanceof PlayerEntity)) return bl;

        UUID uuid = attacker.getUuid();
        // update all automatons in the nearby area to ALSO shoot the player
        List<AutomatonEntity> automatons = ForgineerEntities.getEntitiesAround(world, this.getBlockPos(), 32.0f, TypeFilter.instanceOf(AutomatonEntity.class));
        for(AutomatonEntity automaton : automatons) {
            automaton.addReputation(uuid, MAX_REPUTATION);
        }
        return bl;
    }

    public int getAttackTicksLeft() {
        return this.attackTicksLeft;
    }

    public void setAttackTicksLeft(int attackTicksLeft) {
        this.attackTicksLeft = attackTicksLeft;
    }

    @Override
    public int getAngerTime() {
        return angerTime;
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
        this.isAngry = angryAt != null;
    }

    public boolean isAngry() {
        return isAngry;
    }

    @Override
    public void chooseRandomAngerTime() {
        this.setAngerTime(ANGER_TIME_RANGE.get(this.random));
    }

    public void shootColorLaser(boolean isAttackLaser) {
        if(beamCooldown > 0 && !isAttackLaser) return;
        if(beamCooldown == 0) beamCooldown = 20;

        World world = this.getWorld();
        LivingEntity target = this.getTarget();
        if(target == null || world == null) return;

        if(!this.canSee(target)) return;

        if(world.isClient()) return;
        ServerWorld serverWorld = (ServerWorld) world;

        double dx = (target.getX() - this.getX()) / 10.0f;
        double dy = (target.getY() - this.getY()) / 10.0f;
        double dz = (target.getZ() - this.getZ()) / 10.0f;

        double currX = this.getX();
        double currY = this.getY() + 1.0; // so it doesn't spawn on the ground and instead a bit in the air
        double currZ = this.getZ();

        int color = getBeamColor(isAttackLaser);
        DustColorTransitionParticleEffect particle = new DustColorTransitionParticleEffect(color, 0, 1.0f);

        for(int i = 0; i < 10; i++) {
            double vx = this.random.nextGaussian() * 0.02;
            double vy = this.random.nextGaussian() * 0.02;
            double vz = this.random.nextGaussian() * 0.02;
            serverWorld.spawnParticles(particle, currX, currY, currZ, 1, vx, vy, vz, 0);
            currX += dx;
            currY += dy;
            currZ += dz;
        }
    }

    private int getBeamColor(boolean isAttackLaser) {
        int color = 0;
        if(this.highestReputation > MAX_REPUTATION / 16) color = 3395962; // green
        if(this.highestReputation > MAX_REPUTATION / 8) color = 3507428; // blue
        if(this.highestReputation > MAX_REPUTATION / 4) color = 16175917; // yellow
        if(this.highestReputation > MAX_REPUTATION / 2) color = 14687012; // red
        if(isAttackLaser) color = 16777215; // white

        return color;
    }

    public void playerMiningGemstoneEvent(PlayerEntity player) {
        if(!this.canSee(player)) return;

        addReputation(player.getUuid(), 20000);
    }

    public void playerOpenChestEvent(PlayerEntity player) {
        if(!this.canSee(player)) return;

        addReputation(player.getUuid(), 10000);
    }

    static {
        ANGER_TIME_RANGE = TimeHelper.betweenSeconds(100, 200);
    }
}
