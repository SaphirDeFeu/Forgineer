package io.github.saphirdefeu.forgineer;

import io.github.saphirdefeu.forgineer.init.*;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main Forgineer class
 */
public class Forgineer implements ModInitializer {

    public static final String MOD_ID = "forgineer";
    /**
     * Forgineer console logger
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /**
     * Entry point method
     */
    @Override
    public void onInitialize() {

        LOGGER.info("Initializing Forgineer");

        ForgineerItemGroup.initialize();

        ForgineerBlocks.initialize();
        ForgineerItems.initialize();
        ForgineerEffects.initialize();
        ForgineerWorldGen.initialize();
        ForgineerEnchantments.initialize();

        ForgineerEventRegistrar.registerEvents();

        LOGGER.info("Forgineer initialized");
    }
}
