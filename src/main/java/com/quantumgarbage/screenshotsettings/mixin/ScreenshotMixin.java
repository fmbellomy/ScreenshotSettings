package com.quantumgarbage.screenshotsettings.mixin;


import com.quantumgarbage.screenshotsettings.client.config.ScreenshotSettingsConfig;
import com.quantumgarbage.screenshotsettings.util.FileNameTemplateProcessor;
import com.quantumgarbage.screenshotsettings.util.PNGMetadataManipulator;
import com.quantumgarbage.screenshotsettings.util.getters.GameMeta;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.util.function.Consumer;

import static net.minecraft.client.util.ScreenshotRecorder.takeScreenshot;


@Mixin(ScreenshotRecorder.class)
public class ScreenshotMixin {

    @Final
    @Shadow
    private static Logger LOGGER;
    private static void saveScreenshotInner(File gameDirectory, @Nullable String fileName, Framebuffer framebuffer, Consumer<Text> messageReceiver) {
        NativeImage nativeImage = takeScreenshot(framebuffer);
        String dir = ScreenshotSettingsConfig.INSTANCE.getScreenshotDirectory();
        File file = new File(dir);
        //noinspection ResultOfMethodCallIgnored
        file.mkdir();
        File file2;
        String filename = FileNameTemplateProcessor.format(ScreenshotSettingsConfig.INSTANCE.screenshotNamingSchema);
        file2 = new File(file, filename);
        int i = 1;
        while (new File(file2 + ".png").exists()) {
            ++i;
            file2 = new File(file, filename + "_(" + i + ")");
        }
        final File file3 = new File(file2 + ".png");
        Util.getIoWorkerExecutor().execute(() -> {
            try {
                nativeImage.writeTo(file3);
                Text text = Text.literal(file3.getName()).formatted(Formatting.UNDERLINE).styled((style) -> {
                    return style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file3.getAbsolutePath()));
                });
                messageReceiver.accept(Text.translatable("screenshot.success", text));
                LOGGER.info("Screenshot successfully saved to " + file3.getAbsolutePath() + "!");

                PNGMetadataManipulator.attachMetadata(file3, GameMeta.getMetadata());

            } catch (Exception var7) {
                LOGGER.warn("Couldn't save screenshot", var7);
                messageReceiver.accept(Text.translatable("screenshot.failure", var7.getMessage()));
            } finally {
                nativeImage.close();
            }
        });
    }
}
