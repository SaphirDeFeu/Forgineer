package io.github.saphirdefeu.forgineer.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.github.saphirdefeu.forgineer.Forgineer;
import io.github.saphirdefeu.forgineer.state.PlayerData;
import io.github.saphirdefeu.forgineer.state.StateManager;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;

public class Ruby extends Item {

    private static final EntityAttributeModifier entityAttributeModifier = new EntityAttributeModifier(
        Identifier.of("minecraft:max_health"), 10.0, EntityAttributeModifier.Operation.ADD_VALUE
    );

    public static final Settings settings = new Settings()
            .rarity(Rarity.UNCOMMON);

    public Ruby(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if(world.isClient) {
            return ActionResult.PASS;
        }

        final double healthModifier = user.getAttributes().getValue(EntityAttributes.MAX_HEALTH);

        if(healthModifier >= 40.0f) {
            user.sendMessage(
                    Text.translatable("forgineer.text.consume_gemstone_fail", user.getStackInHand(hand).getName())
                            .formatted(Formatting.DARK_RED, Formatting.BOLD),
                    false
            );

            user.playSoundToPlayer(SoundEvents.BLOCK_ANVIL_FALL, SoundCategory.PLAYERS, 10f, 1f);

            return ActionResult.FAIL;
        }

        // Retrieve Server State
        MinecraftServer server = world.getServer();
        assert server != null;
        StateManager serverState = StateManager.getServerState(server);

        Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> modifierMultimap = ArrayListMultimap.create();
        modifierMultimap.put(EntityAttributes.MAX_HEALTH, entityAttributeModifier);
        user.getAttributes().addTemporaryModifiers(modifierMultimap);

        // Modify the modifier on the server state
        Double attributeValue = entityAttributeModifier.value();
        String attributeID = entityAttributeModifier.id().toString();
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

        user.sendMessage(
                Text.translatable("forgineer.text.consume_gemstone_success", user.getStackInHand(hand).getName())
                        .formatted(Formatting.RED),
                false
        );

        user.playSoundToPlayer(SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS, 10f, 1f);

        user.getStackInHand(hand).decrement(1);

        return ActionResult.SUCCESS;
    }
}
