package io.github.saphirdefeu.forgineer.item;

import io.github.saphirdefeu.forgineer.state.PlayerData;
import io.github.saphirdefeu.forgineer.state.StateManager;
import net.minecraft.block.Block;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Gemstone {
    /**
     * Modifies the server's state to allow for cross-session saves of attribute modifiers
     * @param world this world's instance
     * @param user the player whose attribute modifier will be saved
     * @param attributeModifier the attribute modifier to be changed
     * @param attribute exactly what happens to {@code attributeModifier}
     */
    public static void saveAttributeModifier(World world, PlayerEntity user, EntityAttributeModifier attributeModifier, RegistryEntry<EntityAttribute> attribute) {
        // Retrieve Server State
        MinecraftServer server = world.getServer();
        assert server != null;
        StateManager serverState = StateManager.getServerState(server);

        // Modify the modifier on the server state
        Double attributeValue = attributeModifier.value();
        String attributeID = attribute.getIdAsString();
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

    /**
     * Is this block a Gemstone?
     * @param block the block
     * @return yes or no
     */
    public static boolean isGemstone(Block block) {
        Identifier id = Registries.BLOCK.getId(block);

        // Normally, we should be able to use a TagKey or TagEntry to check whether we are
        // detecting a gemstone in the vicinity
        // However, I cannot find such a way to make it work,
        // so it is now hardcoded.
        // good luck everyone else
        return switch (id.toString()) {
            case "forgineer:ruby_ore",
                 "forgineer:bloodstone_ore",
                 "forgineer:onyx_ore",
                 "forgineer:sapphire_ore",
                 "forgineer:aquamarine_ore",
                 "forgineer:topaz_ore" -> true;
            default -> false;
        };
    }
}
