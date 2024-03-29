package com.quantumgarbage.screenshotsettings.client.config;

import com.quantumgarbage.screenshotsettings.client.ScreenshotSettingsClient;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

@Config(name = "screenshotsettings")
public class ScreenshotSettingsConfig implements ConfigData {
    private static final String templatingTooltip =
            """
                            Available templates:
                                <datetime> -> Minecraft's default screenshot naming format.
                                <world> -> The name of the world in Single Player, and the name of the server in Multiplayer.
                                <version> -> The version of Minecraft the screenshot was taken on. (Ex. 1.19.2)
                                <x>, <y>, and <z> -> The respective coordinates of where the screenshot was taken.
                                <shader> -> The active Shader pack. (requires Iris Shaders)
                                <player> -> Your Minecraft username.
                    """;

    // Change screenshot directory
    @ConfigEntry.Category("Screenshot Directory")
    public String screenshotDirectory;
    public boolean useCustomScreenshotDirectory;

    // Control metadata attached to screenshots.
    @ConfigEntry.Category("Metadata")
    public boolean useMetadata;

    public boolean coordinates;

    public boolean worldName;

    public boolean mcVersion;

    public boolean shaderPack;

    public boolean resourcePacks;

    public boolean seed;

    // Control how screenshots are named.
    @ConfigEntry.Category("Naming Scheme")
    public boolean useCustomScreenshotNamingSchema;
    public String screenshotNamingSchema;

    public String getScreenshotDirectory() {
        if (!this.useCustomScreenshotDirectory) {
            return "./screenshots/";
        } else {
            return this.screenshotDirectory + "/";
        }
    }

    @Override
    public void validatePostLoad() throws ValidationException {
        if (null == this.screenshotDirectory) {
            this.screenshotDirectory = "./screenshots";
        }
        if (null == this.screenshotNamingSchema) {
            this.screenshotNamingSchema = "<datetime>";
        }
        ConfigData.super.validatePostLoad();
    }

    public Screen createGui(final Screen parent) {
        final ScreenshotSettingsConfig INSTANCE = ScreenshotSettingsClient.CONFIG;
        final Option<String> screenshotDirectory = Option.<String>createBuilder()
                .name(Text.of("Custom Screenshot Directory"))
                .description(OptionDescription.of(Text.of("Where screenshots will be saved to, provided Use Custom Screenshot Directory is enabled.")))
                .binding(
                        "/screenshots",
                        () -> INSTANCE.screenshotDirectory,
                        dir -> INSTANCE.screenshotDirectory = dir

                ).controller(StringControllerBuilder::create)
                .build();
        final ConfigCategory dirConfig = ConfigCategory.createBuilder()
                .name(Text.of("Screenshot Directory"))
                .option(Option.<Boolean>createBuilder()
                        .name(Text.of("Use Custom Screenshot Directory"))
                        .description(OptionDescription.of(Text.of("Enabling this will set screenshots to be saved to the path specified in Custom Screenshot Directory")))
                        .binding(
                                false,
                                () -> INSTANCE.useCustomScreenshotDirectory,
                                val -> INSTANCE.useCustomScreenshotDirectory = val
                        ).controller(BooleanControllerBuilder::create)
                        .build())
                .group(OptionGroup.createBuilder()
                        .name(Text.of("Screenshot Directory"))
                        .collapsed(false)
                        .option(screenshotDirectory)
                        .option(ButtonOption.createBuilder()
                                .name(Text.of("Choose Screenshots Folder"))
                                .action(((yaclScreen, buttonOption) -> {
                                    final String currentSetting = screenshotDirectory.pendingValue();
                                    final String newDir = TinyFileDialogs.tinyfd_selectFolderDialog("Select New Screenshots Directory", currentSetting);
                                    if (newDir != null) {
                                        screenshotDirectory.requestSet(newDir);
                                    }
                                }))
                                .text(Text.of("Open File Chooser Dialog"))
                                .description(OptionDescription.of(Text.of("Opens a graphical file explorer to choose a new directory to save screenshots to.")))
                                .build())
                        .build())
                .build();
        final ConfigCategory metadata = ConfigCategory.createBuilder()
                .name(Text.of("Screenshot Metadata"))
                .option(Option.<Boolean>createBuilder()
                        .name(Text.of("Use Custom Metadata"))
                        .description(OptionDescription.of(Text.of("Enable or Disable embedding relevant in-game data into your screenshots' metadata")))
                        .binding(
                                false,
                                () -> INSTANCE.useMetadata,
                                val -> INSTANCE.useMetadata = val
                        ).controller(BooleanControllerBuilder::create)
                        .build())
                .group(OptionGroup.createBuilder()
                        .name(Text.of("Screenshot Metadata"))
                        .collapsed(false)
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.of("Include Coordinates"))
                                .binding(
                                        false,
                                        () -> INSTANCE.coordinates,
                                        val -> INSTANCE.coordinates = val
                                ).controller(BooleanControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.of("Include World/Server Name"))
                                .binding(
                                        false,
                                        () -> INSTANCE.worldName,
                                        val -> INSTANCE.worldName = val
                                ).controller(BooleanControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.of("Include World Seed"))
                                .binding(
                                        false,
                                        () -> INSTANCE.seed,
                                        val -> INSTANCE.seed = val
                                ).controller(BooleanControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.of("Include Texture Pack"))
                                .binding(
                                        false,
                                        () -> INSTANCE.resourcePacks,
                                        val -> INSTANCE.resourcePacks = val
                                ).controller(BooleanControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.of("Include Shader Pack (Requires Iris Shaders)"))
                                .binding(
                                        false,
                                        () -> INSTANCE.shaderPack,
                                        val -> INSTANCE.shaderPack = val
                                ).controller(BooleanControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.of("Include Game Version"))
                                .binding(
                                        false,
                                        () -> INSTANCE.mcVersion,
                                        val -> INSTANCE.mcVersion = val
                                ).controller(BooleanControllerBuilder::create)
                                .build())
                        .build())
                .build();

        final ConfigCategory screenshotNaming = ConfigCategory.createBuilder()
                .name(Text.of("Screenshot Naming"))
                .option(Option.<Boolean>createBuilder()
                        .name(Text.of("Enable Custom Screenshot Naming"))
                        .binding(
                                false,
                                () -> INSTANCE.useCustomScreenshotNamingSchema,
                                val -> INSTANCE.useCustomScreenshotNamingSchema = val
                        ).controller(BooleanControllerBuilder::create)
                        .build())
                .group(OptionGroup.createBuilder()
                        .name(Text.of("Screenshot Naming Scheme"))
                        .collapsed(false)
                        .option(Option.<String>createBuilder()
                                .name(Text.of("Naming Schema"))
                                .description(OptionDescription.of(Text.of(templatingTooltip)))
                                .binding(
                                        "<timedate>",
                                        () -> INSTANCE.screenshotNamingSchema,
                                        val -> INSTANCE.screenshotNamingSchema = val
                                ).controller(StringControllerBuilder::create)
                                .build())
                        .build())
                .build();

        return YetAnotherConfigLib.createBuilder()
                .title(Text.of("Screenshot Settings"))
                .category(dirConfig)
                .category(metadata)
                .category(screenshotNaming)
                .save(AutoConfig.getConfigHolder(ScreenshotSettingsConfig.class)::save)
                .build()
                .generateScreen(parent);
    }
}
