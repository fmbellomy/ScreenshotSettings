package com.quantumgarbage.screenshotsettings.client;

import com.quantumgarbage.screenshotsettings.client.config.ScreenshotSettingsConfig;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class ScreenshotSettingsClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ScreenshotSettings");
    public static ScreenshotSettingsConfig CONFIG;
    public static MinecraftClient client;
    @Override
    public void onInitializeClient() {
        AutoConfig.register(ScreenshotSettingsConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ScreenshotSettingsConfig.class).getConfig();
        client = MinecraftClient.getInstance();
        LOGGER.info("ScreenshotSettings loaded.");
    }
}
