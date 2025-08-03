package io.github.saphirdefeu.forgineer.state;

import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.saphirdefeu.forgineer.Forgineer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StateManager extends PersistentState {

    /**
     * USE THE CORRESPONDING GETTER FUNCTION AS IT CONVERTS THE MAP INTO A USABLE HASHMAP
     */
    public Map<String, PlayerData> players;

    public StateManager() {
        this.players = new HashMap<>();
    }

    public StateManager(Map<String, PlayerData> players) {
        this.players = players;
    }

    public HashMap<String, PlayerData> getPlayers() {
        return new HashMap<>(this.players);
    }

    public void setPlayers(Map<String, PlayerData> players) {
        this.players = players;
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound playersNbt = new NbtCompound();
        players.forEach((uuid, playerData) -> {
            NbtCompound playerNbt = new NbtCompound();

            NbtList attributeIDsList = new NbtList();
            playerData.attributeIdentifiers.forEach(id -> {
                NbtString nbtString = NbtString.of(id);
                attributeIDsList.add(nbtString);
            });

            NbtList attributeValuesList = new NbtList();
            playerData.attributeValues.forEach(value -> {
                NbtDouble nbtDouble = NbtDouble.of(value);
                attributeValuesList.add(nbtDouble);
            });

            playerNbt.put("attributeIdentifiers", attributeIDsList);
            playerNbt.put("attributeValues", attributeValuesList);

            playersNbt.put(uuid, playerNbt);
        });

        nbt.put("players", playersNbt);

        return nbt;
    }

    public static StateManager createFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        StateManager state = new StateManager();
        NbtCompound players = tag.getCompound("players").orElseThrow();
        players.getKeys().forEach(key -> {
            PlayerData playerData = new PlayerData();

            NbtList IDsList = players.getCompound(key).orElseThrow().getList("attributeIdentifiers").orElseThrow();
            NbtList valuesList = players.getCompound(key).orElseThrow().getList("attributeValues").orElseThrow();
            ArrayList<String> attributeIdentifiers = new ArrayList<>();
            ArrayList<Double> attributeValues = new ArrayList<>();
            for(int i = 0; i < IDsList.size(); i++) {
                attributeIdentifiers.add(IDsList.getString(i, ""));
                attributeValues.add(valuesList.getDouble(i, 1.0));
            }

            playerData.attributeIdentifiers = attributeIdentifiers;
            playerData.attributeValues = attributeValues;

            UUID uuid = UUID.fromString(key);

            state.players.put(uuid.toString(), playerData);
        });

        return state;
    }

    public static StateManager createNew() {
        StateManager state = new StateManager();
        state.players = new HashMap<>();
        return state;
    }

    public static final Codec<StateManager> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(Codec.STRING, PlayerData.CODEC).fieldOf("players").forGetter(StateManager::getPlayers)
    ).apply(instance, StateManager::new));

    private static final PersistentStateType<StateManager> type = new PersistentStateType<>(
            Forgineer.MOD_ID,
            StateManager::new, // If there's no 'StateSaverAndLoader' yet create one and refresh variables
            CODEC, // If there is a 'StateSaverAndLoader' NBT, parse it with 'createFromNbt'
            null // Supposed to be an 'DataFixTypes' enum, but we can just pass null
    );

    public static StateManager getServerState(MinecraftServer server) {
        // (Note: arbitrary choice to use 'World.OVERWORLD' instead of 'World.END' or 'World.NETHER'.  Any work)
        ServerWorld serverWorld = server.getWorld(World.OVERWORLD);
        assert serverWorld != null;

        // The first time the following 'getOrCreate' function is called, it creates a brand new 'StateManager' and
        // stores it inside the 'PersistentStateManager'. The subsequent calls to 'getOrCreate' pass in the saved
        // 'StateManager' NBT on disk to the codec in our type, using the codec to decode the nbt into our state
        StateManager state = serverWorld.getPersistentStateManager().getOrCreate(type);

        // If state is not marked dirty, nothing will be saved when Minecraft closes.
        // Technically it's 'cleaner' if you only mark state as dirty when there was actually a change, but the vast majority
        // of mod writers are just going to be confused when their data isn't being saved, and so it's best just to 'markDirty' for them.
        // Besides, it's literally just setting a bool to true, and the only time there's a 'cost' is when the file is written to disk when
        // there were no actual change to any of the mods state (INCREDIBLY RARE).
        state.markDirty();

        return state;
    }

    public static PlayerData getPlayerState(LivingEntity player) {
        StateManager serverState = getServerState(player.getWorld().getServer());

        // Either get the player by the uuid, or we don't have data for him yet, make a new player state
        PlayerData playerState = serverState.players.computeIfAbsent(player.getUuid().toString(), uuid -> new PlayerData());

        return playerState;
    }
}
