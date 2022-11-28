package com.quantumgarbage.screenshotsettings.client.config;

import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.OptionGroup;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.config.ConfigEntry;
import dev.isxander.yacl.config.GsonConfigInstance;
import dev.isxander.yacl.gui.controllers.BooleanController;
import dev.isxander.yacl.gui.controllers.string.StringController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.nio.file.Path;

public class ScreenshotSettingsConfig {
    private static String templatingTooltip = "Available templates:\n    <timedate> -> The default Time and Date function.\n    <world> -> The name of the world in Single Player, and the name of the server in Multiplayer.\n    <version> -> The version of Minecraft the screenshot was taken on. (Ex. 1.19.2)\n    <coordinates> -> The XYZ coordinates the screenshot was taken at. Does not include player rotation.";

    public static final Path configFile = FabricLoader.getInstance().getConfigDir().resolve("screenshot-settings.json");
    public static ScreenshotSettingsConfig INSTANCE = new ScreenshotSettingsConfig();
    // Change screenshot directory
    @ConfigEntry
    public String screenshotDirectory;
    @ConfigEntry
    public boolean useCustomScreenshotDirectory;

    // Control metadata attached to screenshots.
    @ConfigEntry
    public boolean useMetadata;
    @ConfigEntry
    public boolean coordinates;
    @ConfigEntry
    public boolean worldName;
    @ConfigEntry
    public boolean mcVersion;
    @ConfigEntry
    public boolean shaderPack;
    @ConfigEntry
    public boolean texturePack;
    @ConfigEntry
    public boolean seed;

    // Control how screenshots are named.
    @ConfigEntry
    public boolean useCustomScreenshotNamingSchema;
    @ConfigEntry
    public String screenshotNamingSchema;

    public static void load() {
        GsonConfigInstance<ScreenshotSettingsConfig> configInstance = new GsonConfigInstance<>(ScreenshotSettingsConfig.class, configFile);
        configInstance.load();
        INSTANCE = configInstance.getConfig();
        if (INSTANCE.screenshotDirectory == null) {
            INSTANCE.screenshotDirectory = "/screenshots";
        }
        if (INSTANCE.screenshotNamingSchema == null) {
            INSTANCE.screenshotNamingSchema = "<timedate>";
        }
    }

    public String getScreenshotDirectory() {
        if (useCustomScreenshotDirectory) {
            return screenshotDirectory;
        } else {
            return FabricLoader.getInstance().getGameDir() + "/screenshots";
        }
    }

    public String getScreenshotNamingSchema() {
        if (useCustomScreenshotNamingSchema) {
            return screenshotNamingSchema;
        } else {
            return "<timedate>";
        }
    }

    public void save() {
        GsonConfigInstance<ScreenshotSettingsConfig> configInstance = new GsonConfigInstance<>(ScreenshotSettingsConfig.class, configFile);
        configInstance.save();
    }

    public Screen createGui(Screen parent) {
        ConfigCategory dirConfig = ConfigCategory.createBuilder()
                .name(Text.of("Screenshot Directory"))
                .option(Option.createBuilder(boolean.class)
                        .name(Text.of("Use Custom Screenshot Directory"))
                        .tooltip(Text.of("Enabling this will set screenshots to be saved to the path specified in Custom Screenshot Directory"))
                        .binding(
                                false,
                                () -> INSTANCE.useCustomScreenshotDirectory,
                                val -> INSTANCE.useCustomScreenshotDirectory = val
                        ).controller(BooleanController::new)
                        .build())
                .group(OptionGroup.createBuilder()
                        .name(Text.of("Screenshot Directory"))
                        .collapsed(false)
                        .option(Option.createBuilder(String.class)
                                .name(Text.of("Custom Screenshot Directory"))
                                .tooltip(Text.of("Where screenshots will be saved to, provided Use Custom Screenshot Directory is enabled."))
                                .binding(
                                        "/screenshots",
                                        () -> INSTANCE.screenshotDirectory,
                                        dir -> INSTANCE.screenshotDirectory = dir

                                ).controller(StringController::new)
                                .build())
                        .build())
                .build();
        ConfigCategory metadata = ConfigCategory.createBuilder()
                .name(Text.of("Screenshot Metadata"))
                .option(Option.createBuilder(boolean.class)
                        .name(Text.of("Use Custom Metadata"))
                        .tooltip(Text.of("Enable or Disable embedding relevant in-game data into your screenshots' metadata"))
                        .binding(
                                false,
                                () -> INSTANCE.useMetadata,
                                val -> INSTANCE.useMetadata = val
                        ).controller(BooleanController::new)
                        .build())
                .group(OptionGroup.createBuilder()
                        .name(Text.of("Screenshot Metadata"))
                        .collapsed(false)
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.of("Include Coordinates"))
                                .binding(
                                        false,
                                        () -> INSTANCE.coordinates,
                                        val -> INSTANCE.coordinates = val
                                ).controller(BooleanController::new)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.of("Include World/Server Name"))
                                .binding(
                                        false,
                                        () -> INSTANCE.worldName,
                                        val -> INSTANCE.worldName = val
                                ).controller(BooleanController::new)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.of("Include World Seed"))
                                .binding(
                                        false,
                                        () -> INSTANCE.seed,
                                        val -> INSTANCE.seed = val
                                ).controller(BooleanController::new)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.of("Include Texture Pack"))
                                .binding(
                                        false,
                                        () -> INSTANCE.texturePack,
                                        val -> INSTANCE.texturePack = val
                                ).controller(BooleanController::new)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.of("Include Shader Pack (Requires Iris Shaders)"))
                                .binding(
                                        false,
                                        () -> INSTANCE.shaderPack,
                                        val -> INSTANCE.shaderPack = val
                                ).controller(BooleanController::new)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.of("Include Game Version"))
                                .binding(
                                        false,
                                        () -> INSTANCE.mcVersion,
                                        val -> INSTANCE.mcVersion = val
                                ).controller(BooleanController::new)
                                .build())
                        .build())
                .build();

        ConfigCategory screenshotNaming = ConfigCategory.createBuilder()
                .name(Text.of("Screenshot Naming"))
                .option(Option.createBuilder(boolean.class)
                        .name(Text.of("Enable Custom Screenshot Naming"))
                        .binding(
                                false,
                                () -> INSTANCE.useCustomScreenshotNamingSchema,
                                val -> INSTANCE.useCustomScreenshotNamingSchema = val
                        ).controller(BooleanController::new)
                        .build())
                .group(OptionGroup.createBuilder()
                        .name(Text.of("Screenshot Naming Scheme"))
                        .collapsed(false)
                        .option(Option.createBuilder(String.class)
                                .name(Text.of("Naming Schema"))
                                .tooltip(Text.of(templatingTooltip))
                                .binding(
                                        "<timedate>",
                                        () -> INSTANCE.screenshotNamingSchema,
                                        val -> INSTANCE.screenshotNamingSchema = val
                                ).controller(StringController::new)
                                .build())
                        .build())
                .build();

        return YetAnotherConfigLib.createBuilder()
                .title(Text.of("Screenshot Settings"))
                .category(dirConfig)
                .category(metadata)
                .category(screenshotNaming)
                .save(this::save)
                .build()
                .generateScreen(parent);
    }
}
