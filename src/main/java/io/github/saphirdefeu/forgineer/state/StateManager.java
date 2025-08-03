package io.github.saphirdefeu.forgineer.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.saphirdefeu.forgineer.Forgineer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

/**
 * All-in-one class to manage the persistent (across reloads) Server State.
 * @see PersistentState
 * @implNote Made thanks to <a href="https://wiki.fabricmc.net/tutorial:persistent_states">Fabric Wiki Page on Persistent States</a> although Codecs are still required unlike what it is shown in the last code example.
 */
public class StateManager extends PersistentState {

    private Map<String, PlayerData> players;

    /**
     * Creates a blank {@link StateManager}
     */
    public StateManager() {
        this.players = new HashMap<>();
    }

    /**
     * Creates a new {@link StateManager} from an existing {@link Map} object
     * @param players A compatible {@link Map} object
     */
    public StateManager(Map<String, PlayerData> players) {
        this.players = players;
    }

    /**
     * Getter function for the {@link StateManager#players} {@link Map}.
     * @return A mutable copy of the internal {@link StateManager#players} field as a {@link HashMap}
     * @see StateManager#setPlayers(Map) 
     */
    public HashMap<String, PlayerData> getPlayers() {
        return new HashMap<>(this.players);
    }

    /**
     * Setter function for the {@link StateManager#players} {@link Map}
     * @param players Any implementation of {@link Map}.
     * @see StateManager#getPlayers()
     */
    public void setPlayers(Map<String, PlayerData> players) {
        this.players = players;
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

    /**
     * Retrieves an instance of {@link StateManager} from a corresponding {@link MinecraftServer}
     * @param server A {@link MinecraftServer} instance
     * @return The corresponding Forgineer {@link StateManager}
     * @throws NullPointerException if {@code server} does not have a valid {@link World#OVERWORLD}
     */
    public static StateManager getServerState(MinecraftServer server) throws NullPointerException {
        // (Note: arbitrary choice to use 'World.OVERWORLD' instead of 'World.END' or 'World.NETHER'.  Any work)
        ServerWorld serverWorld = server.getWorld(World.OVERWORLD);
        if(serverWorld == null) {
            throw new NullPointerException("serverWorld is empty");
        }

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
}
