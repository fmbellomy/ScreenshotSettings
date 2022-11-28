package com.quantumgarbage.screenshotsettings.util;

import com.quantumgarbage.screenshotsettings.util.getters.GameMeta;
import org.stringtemplate.v4.ST;


public class FileNameTemplateProcessor {
    public static String format(String templateString) {
        ST filename = new ST(templateString);

        filename.add("timedate", GameMeta.timedate());
        filename.add("version", GameMeta.getVersion());
        filename.add("world", GameMeta.getWorldName());
        filename.add("coordinates", GameMeta.getCoordinatesFilename());

        return filename.render();
    }
}
