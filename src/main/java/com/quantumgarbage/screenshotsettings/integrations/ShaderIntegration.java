package com.quantumgarbage.screenshotsettings.integrations;


import net.coderbot.iris.Iris;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FilenameUtils;

public class ShaderIntegration {
    public static boolean irisPresent() {
        return FabricLoader.getInstance().isModLoaded("iris");
    }

    public static String getShaderName() {
        if (irisPresent()) {
            return Iris.getCurrentPackName();
        }
        return "Iris Shaders not found -- could not resolve shader pack";
    }

    public static String getShaderNameNoExtension() {
        if (irisPresent()) {
            return FilenameUtils.removeExtension(getShaderName());
        } else {
            return "Unknown Shader";
        }
    }
}
