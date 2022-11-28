package com.quantumgarbage.screenshotsettings.mixin;


import com.quantumgarbage.screenshotsettings.client.config.ScreenshotSettingsConfig;
import com.quantumgarbage.screenshotsettings.util.FileNameTemplateProcessor;
import com.quantumgarbage.screenshotsettings.util.PNGMetadataManipulator;
import com.quantumgarbage.screenshotsettings.util.getters.GameMeta;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

@Mixin(ScreenshotRecorder.class)
public class ScreenshotMixin {
    @ModifyVariable(
            method = "saveScreenshotInner",
            at = @At(value = "STORE"),
            index = 5)
    private static File directoryInject(File f) {
        return new File(ScreenshotSettingsConfig.INSTANCE.screenshotDirectory);
    }

    @ModifyVariable(
            method = "saveScreenshotInner",
            argsOnly = true,
            at = @At(value = "HEAD"),
            index = 1
    )
    private static String filenameInject(String orig) {
        String filename = FileNameTemplateProcessor.format(ScreenshotSettingsConfig.INSTANCE.screenshotNamingSchema);
        // base filename
        String filenameChecked = filename + ".png";
        // check if another file with the given name already exists.
        // if it does, add a number suffix.
        for (int i = 1; Files.exists(Path.of(filenameChecked)); ++i) {
            filenameChecked = filename + "_" + i + ".png";
        }
        return filenameChecked;
    }

    // inject into the lambda in Util.getServiceWorker.execute()
    // this is to create any necessary subdirectories for the screenshots.
    @Inject(
            method = "method_1661",
            at = @At(value = "HEAD"),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private static void createDirs(NativeImage _nativeImage, File file, Consumer<Text> _consumer, CallbackInfo _ci) {
        file.getParentFile().mkdirs();
    }

    @Inject(
            method = "method_1661",
            at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private static void attachMetadata(NativeImage nativeImage, File file, Consumer<Text> consumer, CallbackInfo ci) {
        PNGMetadataManipulator.attachMetadata(file, GameMeta.getMetadata());
    }
}
