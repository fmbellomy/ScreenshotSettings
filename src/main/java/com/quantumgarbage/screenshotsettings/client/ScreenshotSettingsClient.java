package com.quantumgarbage.screenshotsettings.client;

import com.quantumgarbage.screenshotsettings.client.config.ScreenshotSettingsConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class ScreenshotSettingsClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ScreenshotSettingsConfig");
    public static MinecraftClient client;
    @Override
    public void onInitializeClient() {
        ScreenshotSettingsConfig.INSTANCE.load();
        client = MinecraftClient.getInstance();
        LOGGER.info("ScreenshotSettingsConfig loaded.");
    }
}
