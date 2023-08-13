package com.quantumgarbage.screenshotsettings.integrations;

import com.quantumgarbage.screenshotsettings.client.ScreenshotSettingsClient;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> ScreenshotSettingsClient.CONFIG.createGui(parent);
    }
}
