package com.quantumgarbage.screenshotsettings.client;

import com.quantumgarbage.screenshotsettings.client.config.ScreenshotSettingsConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class ScreenshotSettingsClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ScreenshotSettingsConfig");

    @Override
    public void onInitializeClient() {
        ScreenshotSettingsConfig.INSTANCE.load();
        LOGGER.info("ScreenshotSettingsConfig loaded.");

    }
}
