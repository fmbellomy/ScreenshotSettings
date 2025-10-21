//? if fabric {
package com.quantumgarbage.screenshotsettings.platforms.fabric.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.quantumgarbage.screenshotsettings.Config;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.ScreenshotRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;


public class ScreenshotSettingsClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ScreenshotSettings");
    public static Config CONFIG;
    public static MinecraftClient client;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(Config.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(Config.class).getConfig();
        client = MinecraftClient.getInstance();
        registerScreenshotCommands();
        LOGGER.info("ScreenshotSettings loaded.");
    }

    private void registerScreenshotCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> register(dispatcher));
    }

    private void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        // Believe me, I dislike this code duplication, but every single way of
        // "aliasing" that I attempted through brigadier
        // didn't work out so well.
        dispatcher.register(literal("screenshot")
                .executes(context -> takeScreenshot())
                .then(argument("filename", StringArgumentType.string())
                        .executes(context ->
                                takeScreenshot(StringArgumentType.getString(context, "filename"))
                        )
                )
        );
        dispatcher.register(literal("ss")
                .executes( context ->takeScreenshot())
                .then(argument("filename", StringArgumentType.string())
                        .executes(context ->
                                takeScreenshot(StringArgumentType.getString(context, "filename"))
                        )
                )
        );
    }

    private int takeScreenshot() {
        client.execute(() -> ScreenshotRecorder.saveScreenshot(
                new File("."),
                client.getFramebuffer(),
                message -> client.inGameHud.getChatHud().addMessage(message)
        ));
        return 0;
    }

    private int takeScreenshot(String filename) {
        client.execute(() -> ScreenshotRecorder.saveScreenshot(
                new File("."),
                filename,
                client.getFramebuffer(),
                message -> client.inGameHud.getChatHud().addMessage(message)
        ));
        return 0;
    }
}
//?}
