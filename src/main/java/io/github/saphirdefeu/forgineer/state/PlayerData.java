package io.github.saphirdefeu.forgineer.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {

    public List<String> attributeIdentifiers;
    public List<Double> attributeValues;

    public PlayerData() {
        this.attributeIdentifiers = new ArrayList<>();
        this.attributeValues = new ArrayList<>();
    };

    public PlayerData(List<String> attributeIdentifiers, List<Double> attributeValues) {
        this.attributeIdentifiers = attributeIdentifiers;
        this.attributeValues = attributeValues;
    }

    public ArrayList<Double> getAttributeValues() {
        return new ArrayList<>(attributeValues);
    }

    public ArrayList<String> getAttributeIdentifiers() {
        return new ArrayList<>(attributeIdentifiers);
    }

    public void setAttributeIdentifiers(List<String> attributeIdentifiers) {
        this.attributeIdentifiers = attributeIdentifiers;
    }

    public void setAttributeValues(List<Double> attributeValues) {
        this.attributeValues = attributeValues;
    }

    public static final Codec<PlayerData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.listOf().fieldOf("attributeIdentifiers").forGetter(PlayerData::getAttributeIdentifiers),
            Codec.DOUBLE.listOf().fieldOf("attributeValues").forGetter(PlayerData::getAttributeValues)
    ).apply(instance, PlayerData::new));
}
