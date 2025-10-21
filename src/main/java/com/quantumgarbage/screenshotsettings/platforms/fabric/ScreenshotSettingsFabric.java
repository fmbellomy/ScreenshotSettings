//? if fabric {
package com.quantumgarbage.screenshotsettings.platforms.fabric;

import com.quantumgarbage.screenshotsettings.ModPlatform;
import net.fabricmc.api.ModInitializer;
import com.quantumgarbage.screenshotsettings.ScreenshotSettingsInit;
import net.fabricmc.loader.api.FabricLoader;

public class ScreenshotSettingsFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		ScreenshotSettingsInit.entrypoint(new FabricPlatform());
	}
	public static class FabricPlatform implements ModPlatform{

		@Override
		public String getModloader() {
			return "Fabric";
		}

		@Override
		public boolean isModLoaded(String modloader) {
			return FabricLoader.getInstance().isModLoaded(modloader);
		}
	}
}
//?}