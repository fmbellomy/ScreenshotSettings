package com.quantumgarbage.screenshotsettings.util.getters;

import net.fabricmc.loader.api.FabricLoader;

public class ShaderIntegration {
    public static boolean irisPresent() {
        return FabricLoader.getInstance().isModLoaded("iris");
    }

    public static String get() {
        //TODO: Iris integration?
        return "unimplemented";
    }
}
