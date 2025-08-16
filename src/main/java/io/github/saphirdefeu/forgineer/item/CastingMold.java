package io.github.saphirdefeu.forgineer.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CastingMold extends Item {
    public static final Settings settings = new Settings()
            .fireproof()
            .maxCount(1);

    public CastingMold(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return new ItemStack(this);
    }
}
