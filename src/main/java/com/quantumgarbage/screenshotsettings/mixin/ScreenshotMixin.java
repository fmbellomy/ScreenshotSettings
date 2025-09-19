package com.quantumgarbage.screenshotsettings.mixin;


import com.mojang.logging.LogUtils;
import com.quantumgarbage.screenshotsettings.client.ScreenshotSettingsClient;
import com.quantumgarbage.screenshotsettings.util.FileNameTemplateProcessor;
import com.quantumgarbage.screenshotsettings.util.PNGMetadataManipulator;
import com.quantumgarbage.screenshotsettings.util.getters.GameMeta;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

@Mixin(ScreenshotRecorder.class)
public class ScreenshotMixin {

    @Shadow
    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * @author Gahaha
     * @reason Fk it
     */
    @Overwrite
    public static void saveScreenshot(File gameDirectory, @Nullable String fileName, Framebuffer framebuffer, int downscaleFactor, Consumer<Text> messageReceiver) {
        ScreenshotRecorder.takeScreenshot(framebuffer, downscaleFactor, image -> {
            File file2 = new File(ScreenshotSettingsClient.CONFIG.screenshotDirectory);

            // this is to create any necessary subdirectories for the screenshots.
            file2.mkdirs();
            File file3 = new File(file2, filenameInject(fileName));
            Util.getIoWorkerExecutor().execute(() -> {
                try (NativeImage nativeImage2 = image;){
                    image.writeTo(file3);
                    MutableText text = Text.literal(file3.getName()).formatted(Formatting.UNDERLINE).styled(style -> style.withClickEvent(new ClickEvent.OpenFile(file3.getAbsoluteFile())));
                    PNGMetadataManipulator.attachMetadata(file3, GameMeta.getMetadata(ScreenshotSettingsClient.client));
                    messageReceiver.accept(Text.translatable("screenshot.success", text));
                } catch (Exception exception) {
                    LOGGER.warn("Couldn't save screenshot", exception);
                    messageReceiver.accept(Text.translatable("screenshot.failure", exception.getMessage()));
                }
            });
        });
    }

    @Unique
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
            if (!Files.exists(p)) {
                break;
            }
            tmp = filename + "_(" + i + ")";
            ++i;
        }
        return tmp + ".png";
    }
}
