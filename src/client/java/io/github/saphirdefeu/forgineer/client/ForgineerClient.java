package io.github.saphirdefeu.forgineer.client;

import io.github.saphirdefeu.forgineer.Forgineer;
import io.github.saphirdefeu.forgineer.client.render.entity.AutomatonEntityRenderer;
import io.github.saphirdefeu.forgineer.client.render.entity.model.AutomatonEntityModel;
import io.github.saphirdefeu.forgineer.init.ForgineerEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.EntityRenderers;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ForgineerClient implements ClientModInitializer {
    public static final EntityModelLayer MODEL_AUTOMATON_LAYER = new EntityModelLayer(Identifier.of(Forgineer.MOD_ID, "automaton"), "main");

    @Override
    public void onInitializeClient() {
        EntityRenderers.register(ForgineerEntities.AUTOMATON, AutomatonEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(MODEL_AUTOMATON_LAYER, AutomatonEntityModel::getTexturedModelData);
    }
}
