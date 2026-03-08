package io.github.saphirdefeu.forgineer.client.render.entity;

import io.github.saphirdefeu.forgineer.Forgineer;
import io.github.saphirdefeu.forgineer.client.ForgineerClient;
import io.github.saphirdefeu.forgineer.client.render.entity.model.AutomatonEntityModel;
import io.github.saphirdefeu.forgineer.client.render.entity.state.AutomatonEntityRenderState;
import io.github.saphirdefeu.forgineer.entity.AutomatonEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class AutomatonEntityRenderer extends MobEntityRenderer<AutomatonEntity, AutomatonEntityRenderState, AutomatonEntityModel> {
    public AutomatonEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new AutomatonEntityModel(context.getPart(ForgineerClient.MODEL_CUBE_LAYER)), 0.5f);
    }

    @Override
    public Identifier getTexture(AutomatonEntityRenderState state) {
        return Identifier.of(Forgineer.MOD_ID, "textures/entity/automaton.png");
    }

    @Override
    public AutomatonEntityRenderState createRenderState() {
        return new AutomatonEntityRenderState();
    }
}
