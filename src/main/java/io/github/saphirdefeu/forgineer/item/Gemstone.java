package io.github.saphirdefeu.forgineer.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.github.saphirdefeu.forgineer.Forgineer;
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

    public static boolean isGemstone(Block block) {
        Identifier id = Registries.BLOCK.getId(block);

        // Normally, we should be able to use a TagKey or TagEntry to check whether we are
        // detecting a gemstone in the vicinity
        // However, I cannot find such a way to make it work,
        // so it is now hardcoded.
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

    public static void setPlayerAttributeModifiers(PlayerEntity player, StateManager stateManager) {
        // Find player out of the saved players in the state
        HashMap<String, PlayerData> players = stateManager.getPlayers();
        String uuid = player.getUuidAsString();
        if(!players.containsKey(uuid)) {
            Forgineer.LOGGER.debug("cannot find player " + uuid);
            return;
        }

        PlayerData playerData = players.get(uuid);
        ArrayList<String> attributeIdentifiers = playerData.getAttributeIdentifiers();
        ArrayList<Double> attributeValues = playerData.getAttributeValues();

        for(int i = 0; i < attributeValues.size(); i++) {
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
