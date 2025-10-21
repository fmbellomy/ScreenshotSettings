//? if neoforge {
/*package com.quantumgarbage.screenshotsettings.platforms.neoforge;

import com.quantumgarbage.screenshotsettings.Config;
import com.quantumgarbage.screenshotsettings.ScreenshotSettingsInit;
import com.quantumgarbage.screenshotsettings.ModPlatform;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod("screenshotsettings")
public class ScreenshotSettingsNeoForge {
    public static Config CONFIG = new Config();
	public ScreenshotSettingsNeoForge() {
		ScreenshotSettingsInit.entrypoint(new NeoForgePlatform());
	}
    public static class NeoForgePlatform implements ModPlatform {
        @Override
        public String getModloader() {
            return "NeoForge";
        }

        @Override
        public boolean isModLoaded(String modId) {
            return ModList.get().isLoaded(modId);
        }
    }
}
*///?}