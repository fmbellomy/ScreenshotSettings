package com.quantumgarbage.screenshotsettings.integrations;

import com.quantumgarbage.screenshotsettings.client.config.ScreenshotSettingsConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> ScreenshotSettingsConfig.INSTANCE.createGui(parent);
    }
}
