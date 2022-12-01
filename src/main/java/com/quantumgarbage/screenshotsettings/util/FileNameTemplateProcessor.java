package com.quantumgarbage.screenshotsettings.util;

import com.quantumgarbage.screenshotsettings.integrations.ShaderIntegration;
import com.quantumgarbage.screenshotsettings.util.getters.GameMeta;
import org.stringtemplate.v4.ST;


public class FileNameTemplateProcessor {
    public static String format(String templateString) {
        ST nameBuilder = new ST(templateString);

        nameBuilder.add("datetime", GameMeta.timedate());
        nameBuilder.add("version", GameMeta.getVersion());
        nameBuilder.add("world", GameMeta.getWorldName());
        nameBuilder.add("x", String.format("%.0f",GameMeta.getCoordinates().x));
        nameBuilder.add("y", String.format("%.0f",GameMeta.getCoordinates().y));
        nameBuilder.add("z", String.format("%.0f",GameMeta.getCoordinates().z));
        nameBuilder.add("shader", ShaderIntegration.getShaderNameNoExtension());
        nameBuilder.add("player", GameMeta.getPlayerName());
        return nameBuilder.render();
    }
}
