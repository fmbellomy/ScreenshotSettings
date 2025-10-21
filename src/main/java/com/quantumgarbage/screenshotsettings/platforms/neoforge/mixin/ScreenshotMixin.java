//? if neoforge {
package com.quantumgarbage.screenshotsettings.platforms.neoforge.mixin;

import com.quantumgarbage.screenshotsettings.platforms.neoforge.client.ScreenshotSettingsClient;
import com.quantumgarbage.screenshotsettings.util.PNGMetadataManipulator;
import com.quantumgarbage.screenshotsettings.util.getters.GameMeta;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.neoforged.neoforge.client.event.ScreenshotEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.function.Consumer;

@Mixin(ScreenshotRecorder.class)
public abstract class ScreenshotMixin {
    // Util.getIoWorkerExecutor().execute( method_22691 lambda )
    @Inject(method = "lambda$_grab$2", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
    private static void onIoWorkerStart(NativeImage nativeimage, File file, ScreenshotEvent event, Consumer consumer, CallbackInfo ci) {
        PNGMetadataManipulator.attachMetadata(file, GameMeta.getMetadata(ScreenshotSettingsClient.client));
    }
}
//?}