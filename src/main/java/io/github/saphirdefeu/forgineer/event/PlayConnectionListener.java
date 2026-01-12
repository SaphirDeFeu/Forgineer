package io.github.saphirdefeu.forgineer.event;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.github.saphirdefeu.forgineer.Forgineer;
import io.github.saphirdefeu.forgineer.state.PlayerData;
import io.github.saphirdefeu.forgineer.state.StateManager;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;

// TODO: move logic to apply attribute modifiers in onJoin() to a separate method to be called separately for better modularity
// TODO: in case something goes wrong (which it always does)
public class PlayConnectionListener {
    public static void onJoin(
            ServerPlayNetworkHandler serverPlayNetworkHandler,
            PacketSender packetSender,
            MinecraftServer minecraftServer
    ) {
        StateManager state = StateManager.getServerState(minecraftServer);

        // Find the player who just joined and if their UUID is contained in the list of attribute modifiers on the server state
        PlayerEntity player = serverPlayNetworkHandler.getPlayer();
        HashMap<String, PlayerData> players = state.getPlayers();
        String uuid = player.getUuidAsString();
        if(!players.containsKey(uuid)) {
            return;
        }

        // Make a list of this player's modifications to apply them after
        PlayerData playerData = players.get(uuid);
        ArrayList<String> attributeIdentifiers = playerData.getAttributeIdentifiers();
        ArrayList<Double> attributeValues = playerData.getAttributeValues();

        for(int i = 0; i < attributeValues.size(); i++) {
            // For each instance of a modified attribute, apply it to the player
            String attributeIdentifier = attributeIdentifiers.get(i);
            double attributeValue = attributeValues.get(i);

            EntityAttributeModifier entityAttributeModifier = new EntityAttributeModifier(
                    Identifier.of(Forgineer.MOD_ID, "gemstone"), attributeValue, EntityAttributeModifier.Operation.ADD_VALUE
            );

            Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> modifierMultimap = ArrayListMultimap.create();
            // Convert Attribute ID to RegistryEntry
            RegistryEntry<EntityAttribute> attributeEntry = Registries.ATTRIBUTE.getEntry(Registries.ATTRIBUTE.get(Identifier.of(attributeIdentifier)));
            modifierMultimap.put(attributeEntry, entityAttributeModifier);
            player.getAttributes().addTemporaryModifiers(modifierMultimap);
        }
    }
}
