package io.github.saphirdefeu.forgineer.event;

import io.github.saphirdefeu.forgineer.entity.AutomatonEntity;
import io.github.saphirdefeu.forgineer.init.ForgineerEntities;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.util.List;

public class UseBlockListener {

    public static ActionResult onUseBlock(PlayerEntity playerEntity, World world, Hand hand, BlockHitResult blockHitResult) {
        if(world.isClient()) return ActionResult.PASS;
        if(playerEntity.getGameMode() == GameMode.SPECTATOR) return ActionResult.PASS;

        BlockPos blockPos = blockHitResult.getBlockPos();
        if(blockPos == null) return ActionResult.PASS;

        if(world.getBlockState(blockPos).isOf(Blocks.CHEST)) {
            // if this is a gemstone that we're mining, get all automatons within a 32 block radius and call the appropriate method
            TypeFilter<Entity, AutomatonEntity> filter = TypeFilter.instanceOf(AutomatonEntity.class);
            List<AutomatonEntity> automatons = ForgineerEntities.getEntitiesAround(world, blockPos, 32.0f, filter);
            for(AutomatonEntity automaton : automatons) {
                automaton.playerOpenChestEvent(playerEntity);
            }
        }

        return ActionResult.PASS;
    }
}
