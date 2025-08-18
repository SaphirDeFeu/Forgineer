package io.github.saphirdefeu.forgineer.item;

import io.github.saphirdefeu.forgineer.init.ForgineerToolMaterials;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class Drill extends Item {
    public static final Settings settings = new Settings()
            .maxCount(1)
            .enchantable(1)
            .pickaxe(ForgineerToolMaterials.DRILL_TOOL_MATERIAL, 8f, 1.5f);

    public Drill(Settings settings) {
        super(settings);
    }

    public static ActionResult onAttackBlock(
            PlayerEntity playerEntity,
            World world,
            Hand hand,
            BlockPos blockPos,
            Direction direction
    ) {
        // Because this is only called after client & gamemode check
        // We can assume the player is holding a drill, is not in spectator, and the event is checked server side

        ItemStack itemStack = playerEntity.getStackInHand(hand);
        int maxDamage = itemStack.getMaxDamage();
        int damage = itemStack.getDamage();

        if(maxDamage - damage < 2) return ActionResult.FAIL;

        return ActionResult.PASS;
    }
}
