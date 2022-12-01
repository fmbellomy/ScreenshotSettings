package com.quantumgarbage.screenshotsettings.util;

import com.quantumgarbage.screenshotsettings.client.ScreenshotSettingsClient;
import com.quantumgarbage.screenshotsettings.integrations.ShaderIntegration;
import com.quantumgarbage.screenshotsettings.util.getters.GameMeta;
import net.minecraft.client.MinecraftClient;
import org.stringtemplate.v4.ST;


public class FileNameTemplateProcessor {
    public static String format(String templateString) {
        MinecraftClient client = ScreenshotSettingsClient.client;
        ST nameBuilder = new ST(templateString);

        nameBuilder.add("datetime", GameMeta.timedate());
        nameBuilder.add("version", GameMeta.getVersion(client));
        nameBuilder.add("world", GameMeta.getWorldName(client));
        nameBuilder.add("x", String.format("%.0f",GameMeta.getCoordinates(client.player).x));
        nameBuilder.add("y", String.format("%.0f",GameMeta.getCoordinates(client.player).y));
        nameBuilder.add("z", String.format("%.0f",GameMeta.getCoordinates(client.player).z));
        nameBuilder.add("shader", ShaderIntegration.getShaderNameNoExtension());
        nameBuilder.add("player", GameMeta.getPlayerName(client));
        return nameBuilder.render();
    }
}
