package com.quantumgarbage.screenshotsettings.util;

import com.quantumgarbage.screenshotsettings.integrations.ShaderIntegration;
import com.quantumgarbage.screenshotsettings.util.getters.GameMeta;
import org.stringtemplate.v4.ST;


public class FileNameTemplateProcessor {
    public static String format(String templateString) {
        ST filename = new ST(templateString);

        filename.add("datetime", GameMeta.timedate());
        filename.add("version", GameMeta.getVersion());
        filename.add("world", GameMeta.getWorldName());
        filename.add("x", String.format("%.0f",GameMeta.getCoordinates().x));
        filename.add("y", String.format("%.0f",GameMeta.getCoordinates().y));
        filename.add("z", String.format("%.0f",GameMeta.getCoordinates().z));
        filename.add("shader", ShaderIntegration.getShaderNameNoExtension());
        return filename.render();
    }
}
