package com.quantumgarbage.screenshotsettings.integrations;

import net.coderbot.iris.config.IrisConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.coderbot.iris.Iris;

public class ShaderIntegration {
    public static boolean irisPresent() {
        return FabricLoader.getInstance().isModLoaded("iris");
    }

    public static String getShaderMeta() {
        if (irisPresent()) {
            IrisConfig cfg = Iris.getIrisConfig();
            if (cfg.areShadersEnabled()) {
                return cfg.getShaderPackName().orElse("Unnamed Shader Pack");
            }
        }
        return "No Shaders in Use.";
    }
}
