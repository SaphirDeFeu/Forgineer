package io.github.saphirdefeu.forgineer.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {

    private List<String> attributeIdentifiers;
    private List<Double> attributeValues;

    public PlayerData() {
        this.attributeIdentifiers = new ArrayList<>();
        this.attributeValues = new ArrayList<>();
    };

    public PlayerData(List<String> attributeIdentifiers, List<Double> attributeValues) {
        this.attributeIdentifiers = attributeIdentifiers;
        this.attributeValues = attributeValues;
    }

    /**
     * Getter function for the {@link PlayerData#attributeValues} {@link List}
     * @return A mutable copy of {@link PlayerData#attributeValues} in form of an {@link ArrayList}
     * @see PlayerData#setAttributeValues(List)
     */
    public ArrayList<Double> getAttributeValues() {
        return new ArrayList<>(attributeValues);
    }

    /**
     * Getter function for the {@link PlayerData#attributeIdentifiers} {@link List}
     * @return A mutable copy of {@link PlayerData#attributeIdentifiers} in form of an {@link ArrayList}
     * @see PlayerData#setAttributeIdentifiers(List)
     */
    public ArrayList<String> getAttributeIdentifiers() {
        return new ArrayList<>(attributeIdentifiers);
    }

    /**
     * Setter function for the {@link PlayerData#attributeIdentifiers} {@link List}
     * @param attributeIdentifiers Any instance of classes implementing {@link List}
     * @see PlayerData#getAttributeIdentifiers()
     */
    public void setAttributeIdentifiers(List<String> attributeIdentifiers) {
        this.attributeIdentifiers = attributeIdentifiers;
    }

    /**
     * Setter function for the {@link PlayerData#attributeValues} {@link List}
     * @param attributeValues Any instance of classes implementing {@link List}
     * @see PlayerData#getAttributeValues()
     */
    public void setAttributeValues(List<Double> attributeValues) {
        this.attributeValues = attributeValues;
    }

    public static final Codec<PlayerData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.listOf().fieldOf("attributeIdentifiers").forGetter(PlayerData::getAttributeIdentifiers),
            Codec.DOUBLE.listOf().fieldOf("attributeValues").forGetter(PlayerData::getAttributeValues)
    ).apply(instance, PlayerData::new));
}
