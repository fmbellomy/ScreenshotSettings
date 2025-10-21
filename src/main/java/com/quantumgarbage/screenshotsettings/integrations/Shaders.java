package com.quantumgarbage.screenshotsettings.integrations;
//? if neoforge {
import net.neoforged.fml.ModList;
//?} else {
/*import net.fabricmc.loader.api.FabricLoader;
*///?}

import net.irisshaders.iris.Iris;
import org.apache.commons.io.FilenameUtils;

public class Shaders {
    public static boolean irisPresent() {
        //? if fabric {
        /*return FabricLoader.getInstance().isModLoaded("iris");
        *///?} else {
        return ModList.get().isLoaded("iris");
        //?}
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
