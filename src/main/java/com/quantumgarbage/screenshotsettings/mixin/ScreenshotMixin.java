package com.quantumgarbage.screenshotsettings.mixin;

import com.quantumgarbage.screenshotsettings.ScreenshotSettingsInit;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class ScreenshotMixin {

    @Inject(method = "init",at=@At("HEAD"))
    void init(CallbackInfo ci){
        ScreenshotSettingsInit.LOGGER.info("Stonecutter example mixin init in %s".formatted(ScreenshotSettingsInit.PLATFORM.getModloader()));
    }

}
