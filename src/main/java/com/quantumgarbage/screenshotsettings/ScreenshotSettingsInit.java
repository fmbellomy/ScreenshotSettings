package com.quantumgarbage.screenshotsettings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScreenshotSettingsInit
{
	public static final String MODID = "assets/screenshotsettings";
	public static final Logger LOGGER = LoggerFactory.getLogger("ScreenshotSettings");
	public static ModPlatform PLATFORM = null;

	public static void entrypoint(ModPlatform platform) {
		ScreenshotSettingsInit.PLATFORM = platform;
    }
}