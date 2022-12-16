package com.quantumgarbage.screenshotsettings.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import com.quantumgarbage.screenshotsettings.client.config.ScreenshotSettingsConfig;

import static net.minecraft.server.command.CommandManager.*;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;

import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

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
        registerScreenshotCommands();
        LOGGER.info("ScreenshotSettings loaded.");
    }

    private void registerScreenshotCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> register(dispatcher));
    }

    private void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        // Believe me, I dislike this code duplication, but every single way of
        // "aliasing" that I attempted through brigadier
        // didn't work out so well.
        dispatcher.register(literal("screenshot")
                .executes(this::takeScreenshot)
                .then(argument("filename", StringArgumentType.string())
                        .executes(context ->
                                takeScreenshot(context, StringArgumentType.getString(context, "filename"))
                        )
                )
        );
        dispatcher.register(literal("ss")
                .executes(this::takeScreenshot)
                .then(argument("filename", StringArgumentType.string())
                        .executes(context ->
                                takeScreenshot(context, StringArgumentType.getString(context, "filename"))
                        )
                )
        );
    }

    private int takeScreenshot(CommandContext<ServerCommandSource> context) {
        if (context.getSource().isExecutedByPlayer()) {
            ScreenshotRecorder.saveScreenshot(
                    new File("."),
                    client.getFramebuffer(), 
                    context.getSource()::sendMessage
            );
        } else {
            context.getSource().sendMessage(Text.literal("This command can only be executed by players!"));
        }
        return 1;
    }

    private int takeScreenshot(CommandContext<ServerCommandSource> context, String filename) {
        if (context.getSource().isExecutedByPlayer()) {
            ScreenshotRecorder.saveScreenshot(
                    new File("."),
                    filename,
                    client.getFramebuffer(),
                    context.getSource()::sendMessage
            );
        } else {
            context.getSource().sendMessage(Text.literal("This command can only be executed by players!"));
        }
        return 1;
    }
}
