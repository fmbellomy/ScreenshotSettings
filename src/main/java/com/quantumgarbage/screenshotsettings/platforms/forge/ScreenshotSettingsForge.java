//? if forge {
/*package com.quantumgarbage.screenshotsettings.platforms.forge;

import com.quantumgarbage.screenshotsettings.ConfigScreen;
import com.quantumgarbage.screenshotsettings.ScreenshotSettingsInit;
import com.quantumgarbage.screenshotsettings.ModPlatform;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod("template")
public class ScreenshotSettingsForge {
	public ScreenshotSettingsForge() {
		ScreenshotSettingsInit.entrypoint(new ForgePlatform());
        MinecraftForge.registerConfigScreen(ConfigScreen::createConfigScreen);
	}
	public static class ForgePlatform implements ModPlatform {
		@Override
		public String getModloader() {
			return "LexForge";
		}

		@Override
		public boolean isModLoaded(String modId) {
			return ModList.get().isLoaded(modId);
		}
	}

}
*///?}