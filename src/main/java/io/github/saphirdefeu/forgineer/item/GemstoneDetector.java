package io.github.saphirdefeu.forgineer.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.LinkedList;

public class GemstoneDetector extends Item {
    private static final int RADIUS = 50;

    public static final Settings settings = new Settings()
            .rarity(Rarity.UNCOMMON)
            .maxDamage(RADIUS * 10);

    public GemstoneDetector(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if(world.isClient()) return ActionResult.PASS;
        if(world.getServer() == null) return ActionResult.PASS;

        BlockPos centerPosition = user.getBlockPos();
        LinkedList<BlockPos> ores = new LinkedList<>();
        for (int x = centerPosition.getX() - RADIUS; x <= centerPosition.getX() + RADIUS; x++) {
            for (int y = centerPosition.getY() - RADIUS; y <= centerPosition.getY() + RADIUS; y++) {
                for (int z = centerPosition.getZ() - RADIUS; z <= centerPosition.getZ() + RADIUS; z++) {
                    BlockPos blockPos = new BlockPos(x, y, z);
                    Block block = world.getBlockState(blockPos).getBlock();
                    if (!Gemstone.isGemstone(block)) continue;

                    ores.add(blockPos);
                }
            }
        }

        if(ores.isEmpty()) {
            user.getStackInHand(hand).setDamage(user.getStackInHand(hand).getMaxDamage() - 1);
            return ActionResult.PASS;
        }

        Vec3d exactPosition = new Vec3d(user.getX(), user.getY(), user.getZ()); // avoids Mojang's renaming of getPos() to getEntityPos()
        double minDist = RADIUS + 10;
        BlockPos minDistBlockPos = new BlockPos(0, 0, 0);
        for(BlockPos blockPos : ores) {
            double dX = exactPosition.getX() - blockPos.getX();
            double dY = exactPosition.getY() - blockPos.getY();
            double dZ = exactPosition.getZ() - blockPos.getZ();
            // Expensive & Scary !!
            double dist = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
            if(dist >= RADIUS) continue;
            if(dist >= minDist) continue;

            minDist = dist;
            minDistBlockPos = blockPos;
        }

        if(minDist == RADIUS + 10) {
            user.getStackInHand(hand).setDamage(user.getStackInHand(hand).getMaxDamage() - 1);
            return ActionResult.PASS;
        }

        user.getStackInHand(hand).setDamage((int) (minDist * 10));
        world.playSound(null, minDistBlockPos, SoundEvents.BLOCK_NOTE_BLOCK_CHIME.value(), SoundCategory.BLOCKS, RADIUS, 0f);

        return ActionResult.SUCCESS;
    }
}
