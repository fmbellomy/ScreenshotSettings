//? if neoforge {
package com.quantumgarbage.screenshotsettings.platforms.neoforge.client;

import com.quantumgarbage.screenshotsettings.Config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.MinecraftClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(value = "screenshotsettings", dist = Dist.CLIENT)
public class ScreenshotSettingsClient{
    public static final Logger LOGGER = LoggerFactory.getLogger("ScreenshotSettings");
    public static Config CONFIG;
    public static MinecraftClient client;

    public ScreenshotSettingsClient(IEventBus bus, ModContainer container) {
        AutoConfig.register(Config.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(Config.class).getConfig();
        client = MinecraftClient.getInstance();
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (client, parent) -> (CONFIG.createGui(parent))

        );
        LOGGER.info("ScreenshotSettings loaded.");
    }

}
//?}
