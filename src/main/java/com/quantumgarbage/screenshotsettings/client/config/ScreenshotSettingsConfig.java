package com.quantumgarbage.screenshotsettings.client.config;

import com.google.gson.*;
import dev.isxander.yacl.api.*;
import dev.isxander.yacl.config.ConfigEntry;
import dev.isxander.yacl.gui.controllers.ActionController;
import dev.isxander.yacl.gui.controllers.BooleanController;
import dev.isxander.yacl.gui.controllers.string.StringController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ScreenshotSettingsConfig {
    private static final String templatingTooltip = "Available templates:\n    <datetime> -> The default screenshot naming format.\n    <world> -> The name of the world in Single Player, and the name of the server in Multiplayer.\n    <version> -> The version of Minecraft the screenshot was taken on. (Ex. 1.19.2)\n    <x>, <y>, and <z> -> The respective coordinates where the screenshot was taken from.\n    <shader> -> The active Shaderpack (requires Iris Shaders)";

    public static final Path configFile = FabricLoader.getInstance().getConfigDir().resolve("screenshot-settings.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

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
    public boolean resourcePacks;
    @ConfigEntry
    public boolean seed;

    // Control how screenshots are named.
    @ConfigEntry
    public boolean useCustomScreenshotNamingSchema;
    @ConfigEntry
    public String screenshotNamingSchema;

    public void load() {
        try {
            if (Files.notExists(configFile)) {
                save();
                return;
            }

            JsonObject json = gson.fromJson(Files.readString(configFile), JsonObject.class);
            if (json.has("screenshot_directory")) {
                screenshotDirectory = json.getAsJsonPrimitive("screenshot_directory").getAsString();
            }
            if (json.has("use_custom_screenshot_directory")) {
                useCustomScreenshotDirectory = json.getAsJsonPrimitive("use_custom_screenshot_directory").getAsBoolean();
            }
            if (json.has("use_metadata")) {
                useMetadata = json.getAsJsonPrimitive("use_metadata").getAsBoolean();
            }
            if (json.has("world_name")) {
                worldName = json.getAsJsonPrimitive("world_name").getAsBoolean();
            }
            if (json.has("mc_version")) {
                mcVersion = json.getAsJsonPrimitive("mc_version").getAsBoolean();
            }
            if (json.has("shader_pack")) {
                shaderPack = json.getAsJsonPrimitive("shader_pack").getAsBoolean();
            }
            if (json.has("resource_packs")) {
                resourcePacks = json.getAsJsonPrimitive("resource_packs").getAsBoolean();
            }
            if (json.has("seed")) {
                seed = json.getAsJsonPrimitive("seed").getAsBoolean();
            }
            if (json.has("use_custom_screenshot_naming_schema")) {
                useCustomScreenshotNamingSchema = json.getAsJsonPrimitive("use_custom_screenshot_naming_schema").getAsBoolean();
            }
            if (json.has("screenshot_naming_schema")) {
                screenshotNamingSchema = json.getAsJsonPrimitive("screenshot_naming_schema").getAsString();
            }
            if (screenshotNamingSchema == null) {
                screenshotNamingSchema = "<timedate>";
            }
            if (screenshotDirectory == null) {
                screenshotDirectory = "/screenshots";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            Files.deleteIfExists(configFile);
            JsonObject json = new JsonObject();
            if (screenshotDirectory == null) {
                screenshotDirectory = "/screenshots";
            }
            if (screenshotNamingSchema == null) {
                screenshotNamingSchema = "<timedate>";
            }
            json.addProperty("screenshot_directory", screenshotDirectory);
            json.addProperty("use_custom_screenshot_directory", useCustomScreenshotDirectory);
            json.addProperty("use_metadata", useMetadata);
            json.addProperty("world_name", worldName);
            json.addProperty("mc_version", mcVersion);
            json.addProperty("shader_pack", shaderPack);
            json.addProperty("resource_packs", resourcePacks);
            json.addProperty("seed", seed);
            json.addProperty("use_custom_screenshot_naming_schema", useCustomScreenshotNamingSchema);
            json.addProperty("screenshot_naming_schema", screenshotNamingSchema);
            Files.writeString(configFile, gson.toJson(json));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getScreenshotDirectory(){
        if(!useCustomScreenshotDirectory){
            return "./screenshots/";
        }
        else {
            return screenshotDirectory + "/";
        }
    }
    public Screen createGui(Screen parent) {
        Option<String> screenshotDirectory = Option.createBuilder(String.class)
                .name(Text.of("Custom Screenshot Directory"))
                .tooltip(Text.of("Where screenshots will be saved to, provided Use Custom Screenshot Directory is enabled."))
                .binding(
                        "/screenshots",
                        () -> INSTANCE.screenshotDirectory,
                        dir -> INSTANCE.screenshotDirectory = dir

                ).controller(StringController::new)
                .build();
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
                        .option(screenshotDirectory)
                        .option(ButtonOption.createBuilder()
                                .name(Text.of("Choose Screenshots Folder"))
                                .action(((yaclScreen, buttonOption) -> {
                                    String currentSetting = screenshotDirectory.pendingValue();
                                    String newDir = TinyFileDialogs.tinyfd_selectFolderDialog("Select New Screenshots Directory", currentSetting);
                                    screenshotDirectory.requestSet(newDir);
                                }))
                                .controller((buttonOption) -> new ActionController(buttonOption, Text.of("Open File Dialog")))
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
                                        () -> INSTANCE.resourcePacks,
                                        val -> INSTANCE.resourcePacks = val
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
                .save(INSTANCE::save)
                .build()
                .generateScreen(parent);
    }
}
