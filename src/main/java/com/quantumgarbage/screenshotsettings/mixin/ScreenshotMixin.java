package com.quantumgarbage.screenshotsettings.mixin;

import com.quantumgarbage.screenshotsettings.client.ScreenshotSettingsClient;
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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

@Mixin(ScreenshotRecorder.class)
public abstract class ScreenshotMixin {

    // method_68157 lambda (image) -> {File file2}
    @ModifyVariable(
            method = "method_68157",
            at = @At("STORE"),
            ordinal = 1
    )
    private static File modifyScreenshotFolder(File original) {
        return new File(ScreenshotSettingsClient.CONFIG.screenshotDirectory);
    }

    // Util.getIoWorkerExecutor().execute( method_22691 lambda )
    @Inject(method = "method_22691", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
    private static void onIoWorkerStart(NativeImage image, File file, Consumer<Text> consumer, CallbackInfo ci) {
        PNGMetadataManipulator.attachMetadata(file, GameMeta.getMetadata(ScreenshotSettingsClient.client));
    }

    @ModifyVariable(
            method = "saveScreenshot(Ljava/io/File;Ljava/lang/String;Lnet/minecraft/client/gl/Framebuffer;ILjava/util/function/Consumer;)V",
            argsOnly = true,
            at = @At("HEAD"),
            index = 1
    )
    private static String filenameInject(final String orig) {
        final String filename;
        if (orig == null) {
            filename = FileNameTemplateProcessor.format(ScreenshotSettingsClient.CONFIG.screenshotNamingSchema);
        } else {
            filename = orig;
        }
        final String dir = ScreenshotSettingsClient.CONFIG.getScreenshotDirectory();

        int i = 1;
        String tmp = filename;
        while (true) {
            final Path p = Path.of(dir + tmp + ".png");
            final File f = p.toFile();
            if(!f.getParentFile().mkdirs() && !f.getParentFile().exists()){
                throw new IllegalStateException("Couldn't create dir: " + f.getParentFile());
            }
            if (!Files.exists(p)) {
                break;
            }
            tmp = filename + "_(" + i + ")";
            ++i;
        }
        return tmp + ".png";
    }
}
