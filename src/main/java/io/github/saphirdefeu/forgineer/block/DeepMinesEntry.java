package io.github.saphirdefeu.forgineer.block;

import io.github.saphirdefeu.forgineer.init.ForgineerDimensions;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;

public class DeepMinesEntry extends Block {
    public static final AbstractBlock.Settings settings = Settings.create()
            .noCollision()
            .hardness(-1);

    public DeepMinesEntry(Block.Settings settings) {
        super(settings);
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler) {
        // Get the current world
        if(world.isClient()) return;

        RegistryEntry<DimensionType> currentDimension = world.getDimensionEntry();

        boolean isInDeepMines = currentDimension.getIdAsString().equals(ForgineerDimensions.DEEP_MINES.getValue().toString());
        boolean isInOverworld = currentDimension.getIdAsString().equals("minecraft:overworld");

        if(!isInDeepMines && !isInOverworld) return; // portal should not work in the end or nether or any other place for that matter

        // Select the world we are looking to teleport to
        String lookingFor;
        if(isInDeepMines) lookingFor = "minecraft:overworld";
        else lookingFor = ForgineerDimensions.DEEP_MINES.getValue().toString();

        MinecraftServer server = world.getServer();
        if(server == null) return;

        // Find the deep mines world
        Iterable<ServerWorld> worlds = server.getWorlds();
        AtomicReference<ServerWorld> desiredWorld = new AtomicReference<>();

        worlds.forEach(w -> {
            if(w.getDimensionEntry().getIdAsString().equals(lookingFor)) {
                desiredWorld.set(w);
            }
        });

        if(desiredWorld.get() == null) return;

        // setup teleport target, fix the height at 300 blocks up
        // TODO: check if this is not game breaking & unbalanced :)
        entity.teleport(
                desiredWorld.get(),
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                new HashSet<>(),
                entity.lastYaw,
                entity.lastPitch,
                true // tf does "reset camera" mean?
        );
    }
}
