package com.quantumgarbage.screenshotsettings.client;

import com.quantumgarbage.screenshotsettings.client.config.ScreenshotSettingsConfig;

import net.minecraft.client.MinecraftClient;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ScreenshotSettingsClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ScreenshotSettings");
    public static MinecraftClient client;
    @Override
    public void onInitializeClient(final ModContainer mod) {
        ScreenshotSettingsConfig.INSTANCE.load();
        client = MinecraftClient.getInstance();
        LOGGER.info("{} loaded.", mod.metadata().name());
    }
}
