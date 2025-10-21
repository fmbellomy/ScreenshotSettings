//? if fabric {
package com.quantumgarbage.screenshotsettings.platforms.fabric;

import com.quantumgarbage.screenshotsettings.platforms.fabric.client.ScreenshotSettingsClient;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> ScreenshotSettingsClient.CONFIG.createGui(parent);
    }
}
//?}
