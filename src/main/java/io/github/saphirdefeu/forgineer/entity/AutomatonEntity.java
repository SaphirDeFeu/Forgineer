package io.github.saphirdefeu.forgineer.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.world.World;

public class AutomatonEntity extends GolemEntity {

    public AutomatonEntity(EntityType<? extends GolemEntity> entityType, World world) {
        super(entityType, world);
    }
}
