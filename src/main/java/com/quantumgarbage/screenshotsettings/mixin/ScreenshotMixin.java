package com.quantumgarbage.screenshotsettings.mixin;


import com.quantumgarbage.screenshotsettings.client.ScreenshotSettingsClient;
import com.quantumgarbage.screenshotsettings.client.config.ScreenshotSettingsConfig;
import com.quantumgarbage.screenshotsettings.util.FileNameTemplateProcessor;
import com.quantumgarbage.screenshotsettings.util.PNGMetadataManipulator;
import com.quantumgarbage.screenshotsettings.util.getters.GameMeta;

import com.mojang.blaze3d.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
    @Shadow @Final private static Logger LOGGER;

    @ModifyVariable(
            method = "saveScreenshotInner",
            at = @At("STORE"),
            index = 5)
    private static File directoryInject(final File f) {
        return new File(ScreenshotSettingsConfig.INSTANCE.screenshotDirectory);
    }

    @ModifyVariable(
            method = "saveScreenshotInner",
            argsOnly = true,
            at = @At("HEAD"),
            index = 1
    )
    private static String filenameInject(final String orig) {
        final String filename = FileNameTemplateProcessor.format(ScreenshotSettingsConfig.INSTANCE.screenshotNamingSchema);
        final String dir = ScreenshotSettingsConfig.INSTANCE.getScreenshotDirectory();

        int i = 1;
        String tmp = filename;
        while(true){
            final Path p = Path.of(dir + tmp + ".png");
            if(!Files.exists(p)){
                break;
            }
            tmp = filename + "_(" + i + ")";
            ++i;
        }
        LOGGER.info("Saving screenshot as " + tmp + ".png");
        return tmp + ".png";
    }

    // inject into the lambda in Util.getServiceWorker.execute()
    // this is to create any necessary subdirectories for the screenshots.
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Inject(
            method = "m_laykisam",
            at = @At("HEAD"),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private static void createDirs(final NativeImage _nativeImage, final File file, final Consumer<Text> _consumer, final CallbackInfo _ci) {
        file.getParentFile().mkdirs();
    }

    @Inject(
            method = "m_laykisam",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/texture/NativeImage;writeFile(Ljava/io/File;)V", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private static void attachMetadata(final NativeImage nativeImage, final File file, final Consumer<Text> consumer, final CallbackInfo ci) {
        PNGMetadataManipulator.attachMetadata(file, GameMeta.getMetadata(ScreenshotSettingsClient.client));
    }
}
