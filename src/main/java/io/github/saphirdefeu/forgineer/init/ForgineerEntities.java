package io.github.saphirdefeu.forgineer.init;

import io.github.saphirdefeu.forgineer.Forgineer;
import io.github.saphirdefeu.forgineer.entity.AutomatonEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class ForgineerEntities {

    public static final EntityType<AutomatonEntity> AUTOMATON = register("automaton", EntityType.Builder.create(
            AutomatonEntity::new,
            SpawnGroup.MISC
            )
            .dimensions(1f, 1f)
    );

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> entityBuilder) {
        Identifier id = Identifier.of(Forgineer.MOD_ID, name);
        RegistryKey<EntityType<?>> entityKey = RegistryKey.of(RegistryKeys.ENTITY_TYPE, id);

        return Registry.register(
                Registries.ENTITY_TYPE,
                id,
                entityBuilder.build(entityKey)
        );
    }

    public static void initialize() {
        FabricDefaultAttributeRegistry.register(AUTOMATON, AutomatonEntity.createAutomatonAttributes());
    }

    public static <T extends Entity> List<T> getEntitiesAround(World world, BlockPos center, float radius, TypeFilter<Entity, T> filter) {
        Vec3d pos1 = new Vec3d(center.getX() - radius, center.getY() - radius, center.getZ() - radius);
        Vec3d pos2 = new Vec3d(center.getX() + radius, center.getY() + radius, center.getZ() + radius);
        Box box = new Box(pos1, pos2);
        Predicate<Entity> predicate = EntityPredicates.maxDistance(center.getX(), center.getY(), center.getZ(), radius);

        return world.getEntitiesByType(filter, box, predicate);
    }
}
