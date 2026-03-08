package io.github.saphirdefeu.forgineer.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;

public class AutomatonEntity extends MobEntity  {

    public AutomatonEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }
}
