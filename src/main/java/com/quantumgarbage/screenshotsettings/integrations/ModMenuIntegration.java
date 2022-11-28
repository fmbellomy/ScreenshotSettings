package com.quantumgarbage.screenshotsettings.integrations;

import com.quantumgarbage.screenshotsettings.client.config.ScreenshotSettings;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> ScreenshotSettings.INSTANCE.createGui(parent);
    }
}
