package io.github.saphirdefeu.forgineer.event;

import io.github.saphirdefeu.forgineer.entity.AutomatonEntity;
import io.github.saphirdefeu.forgineer.init.ForgineerItems;
import io.github.saphirdefeu.forgineer.item.Drill;
import io.github.saphirdefeu.forgineer.item.Gemstone;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

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

        if(Gemstone.isGemstone(world.getBlockState(blockPos).getBlock())) {
            // if this is a gemstone that we're mining, get all automatons within a 32 block radius and call the appropriate method
            TypeFilter<Entity, AutomatonEntity> filter = TypeFilter.instanceOf(AutomatonEntity.class);
            Vec3d pos1 = new Vec3d(blockPos.getX() - 32.0f, blockPos.getY() - 32.0f, blockPos.getZ() - 32.0f);
            Vec3d pos2 = new Vec3d(blockPos.getX() + 32.0f, blockPos.getY() + 32.0f, blockPos.getZ() + 32.0f);
            Box box = new Box(pos1, pos2);
            Predicate<Entity> predicate = EntityPredicates.maxDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 32.0f);
            List<AutomatonEntity> automatons = world.getEntitiesByType(filter, box, predicate);

            for(AutomatonEntity automaton : automatons) {
                automaton.playerMiningGemstoneEvent(playerEntity);
            }
        }

        return ActionResult.PASS;
    }
}
