package io.github.saphirdefeu.forgineer;

import io.github.saphirdefeu.forgineer.init.*;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Forgineer implements ModInitializer {

    public static final String MOD_ID = "forgineer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


    @Override
    public void onInitialize() {

        LOGGER.info("Initializing Forgineer");

        ModItemGroup.initialize();

        ModBlocks.initialize();
        ModItems.initialize();
        ModEffects.initialize();
        ModWorldGen.initialize();

        EventRegistrar.registerEvents();

        LOGGER.info("Forgineer initialized");
    }
}
