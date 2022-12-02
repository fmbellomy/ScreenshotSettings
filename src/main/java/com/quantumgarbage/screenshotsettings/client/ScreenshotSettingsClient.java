package com.quantumgarbage.screenshotsettings.client;

import com.quantumgarbage.screenshotsettings.client.config.ScreenshotSettingsConfig;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.MinecraftClient;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ScreenshotSettingsClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ScreenshotSettings");
    public static ScreenshotSettingsConfig CONFIG;
    public static MinecraftClient client;
    @Override
    public void onInitializeClient(final ModContainer mod) {
        AutoConfig.register(ScreenshotSettingsConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ScreenshotSettingsConfig.class).getConfig();
        client = MinecraftClient.getInstance();
        LOGGER.info("{} loaded.", mod.metadata().name());
    }
}
