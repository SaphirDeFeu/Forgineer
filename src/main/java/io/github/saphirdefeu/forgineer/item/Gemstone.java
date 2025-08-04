package io.github.saphirdefeu.forgineer.item;

import io.github.saphirdefeu.forgineer.state.PlayerData;
import io.github.saphirdefeu.forgineer.state.StateManager;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Gemstone {
    public static void saveAttributeModifier(World world, PlayerEntity user, EntityAttributeModifier attribute) {
        // Retrieve Server State
        MinecraftServer server = world.getServer();
        assert server != null;
        StateManager serverState = StateManager.getServerState(server);

        // Modify the modifier on the server state
        Double attributeValue = attribute.value();
        String attributeID = attribute.id().toString();
        String UUID = user.getUuidAsString();

        HashMap<String, PlayerData> players = serverState.getPlayers();

        if(players.containsKey(UUID)) {
            PlayerData playerData = players.get(UUID);
            ArrayList<String> attributeIdentifiers = playerData.getAttributeIdentifiers();
            ArrayList<Double> attributeValues = playerData.getAttributeValues();
            boolean hasAttribute = false;
            for(int i = 0; i < attributeValues.size(); i++) {
                if(attributeID.equals(attributeIdentifiers.get(i))) {
                    hasAttribute = true;
                    attributeValues.set(i, attributeValue);
                    break;
                }
            }

            if(!hasAttribute) {
                attributeIdentifiers.add(attributeID);
                attributeValues.add(attributeValue);
            }

            playerData.setAttributeIdentifiers(attributeIdentifiers);
            playerData.setAttributeValues(attributeValues);
        } else {
            ArrayList<String> attributeIdentifiers = new ArrayList<>();
            ArrayList<Double> attributeValues = new ArrayList<>();
            attributeIdentifiers.add(attributeID);
            attributeValues.add(attributeValue);

            players.put(UUID, new PlayerData(attributeIdentifiers, attributeValues));
        }

        serverState.setPlayers(players);
    }
}
