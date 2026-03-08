package io.github.saphirdefeu.forgineer.init;

import io.github.saphirdefeu.forgineer.Forgineer;
import io.github.saphirdefeu.forgineer.entity.AutomatonEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;

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
        FabricDefaultAttributeRegistry.register(AUTOMATON, AutomatonEntity.createMobAttributes());
    }
}
