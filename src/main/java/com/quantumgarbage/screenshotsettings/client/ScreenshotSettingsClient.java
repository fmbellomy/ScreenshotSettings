package com.quantumgarbage.screenshotsettings.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@Environment(EnvType.CLIENT)
public class ScreenshotSettingsClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ScreenshotSettings");

    @Override
    public void onInitializeClient() {
        LOGGER.info("ScreenshotSettings loaded.");

    }
}
