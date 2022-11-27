package com.quantumgarbage.screenshotsettings.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.io.File;
import java.util.function.Consumer;

import static net.minecraft.client.util.ScreenshotRecorder.takeScreenshot;

@Mixin(ScreenshotRecorder.class)
public class ScreenshotDirectoryMixin {
    @Final
    @Shadow
    private static Logger LOGGER;
    private static File getScreenshotFilename(File directory) {
        String string = Util.getFormattedCurrentTime();
        int i = 1;

        while(true) {
            File file = new File(directory, string + (i == 1 ? "" : "_" + i) + ".png");
            if (!file.exists()) {
                return file;
            }

            ++i;
        }
    }
    private static void saveScreenshotInner(File gameDirectory, @Nullable String fileName, Framebuffer framebuffer, Consumer<Text> messageReceiver) {
        NativeImage nativeImage = takeScreenshot(framebuffer);
        String dir = "C:/Users/maxbe/Pictures/minecraft"; //TODO: make this read from a config.
        File file = new File(dir);
        file.mkdir();
        File file2;
        if (fileName == null) {
            file2 = getScreenshotFilename(file);
        } else {
            file2 = new File(file, fileName);
        }

        Util.getIoWorkerExecutor().execute(() -> {
            try {
                nativeImage.writeTo(file2);
                Text text = Text.literal(file2.getName()).formatted(Formatting.UNDERLINE).styled((style) -> {
                    return style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file2.getAbsolutePath()));
                });
                messageReceiver.accept(Text.translatable("screenshot.success", new Object[]{text}));
                LOGGER.info("Screenshot successfully saved to " + file2.getAbsolutePath() + "!");
            } catch (Exception var7) {
                LOGGER.warn("Couldn't save screenshot", var7);
                messageReceiver.accept(Text.translatable("screenshot.failure", new Object[]{var7.getMessage()}));
            } finally {
                nativeImage.close();
            }
        });
    }
}
