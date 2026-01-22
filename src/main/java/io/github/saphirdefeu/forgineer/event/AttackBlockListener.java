package io.github.saphirdefeu.forgineer.event;

import io.github.saphirdefeu.forgineer.init.ForgineerBlocks;
import io.github.saphirdefeu.forgineer.init.ForgineerItems;
import io.github.saphirdefeu.forgineer.item.Drill;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

public class AttackBlockListener {

    public static ActionResult onAttackBlock(
            PlayerEntity playerEntity,
            World world,
            Hand hand,
            BlockPos blockPos,
            Direction direction
    ) {
        // Mandatory checks to avoid problematic handling of the event
        if(world.isClient()) return ActionResult.PASS;
        if(playerEntity.getGameMode() == GameMode.SPECTATOR) return ActionResult.PASS;

        if(playerEntity.getStackInHand(hand).isOf(ForgineerItems.DRILL)) {
            return Drill.onAttackBlock(playerEntity, world, hand, blockPos, direction);
        }

        return ActionResult.PASS;
    }
}
