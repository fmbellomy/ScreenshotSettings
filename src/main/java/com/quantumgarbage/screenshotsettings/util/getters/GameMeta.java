package com.quantumgarbage.screenshotsettings.util.getters;

import com.quantumgarbage.screenshotsettings.client.config.ScreenshotSettingsConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.resource.ResourcePack;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.Util;
import net.minecraft.world.level.ServerWorldProperties;

import java.util.HashMap;
import java.util.Iterator;

public class GameMeta {
    public static String getCoordinates() {
        if (MinecraftClient.getInstance().player == null) {
            return "Failed to get Coordinates (Screenshot appears to have been taken by a null player?)";
        }
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();
        double pitch = player.getPitch();
        double yaw = player.getYaw();

        return String.format("X:[%.3f] Y:[%.3f] Z:[%.3f] Yaw:[%.3f] Pitch:[%.3f]", x, y, z, yaw, pitch);
    }

    public static String getCoordinatesFilename() {
        if (MinecraftClient.getInstance().player == null) {
            return "Failed to get Coordinates (Screenshot appears to have been taken by a null player?)";
        }
        double x = MinecraftClient.getInstance().player.getX();
        double y = MinecraftClient.getInstance().player.getY();
        double z = MinecraftClient.getInstance().player.getZ();
        return String.format("[%.2f][%.2f][%.2f]", x, y, z);
    }

    private static String getWorldNameSinglePlayer() {
        try {
            ServerWorldProperties sprops = (ServerWorldProperties) MinecraftClient.getInstance().getServer().getWorlds().iterator().next().getLevelProperties();
            return sprops.getLevelName();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            return "Unable to get world name due to an error. [Single Player]";
        }
    }

    private static String getWorldNameMultiplayer() {
        try {
            ServerInfo si = MinecraftClient.getInstance().getCurrentServerEntry();
            if(si == null){
                return "Couldn't get server name.";
            }
            return si.name;
        } catch (Exception e) {
            e.printStackTrace();
            return "Unable to get world name due to an error. [Multiplayer]";
        }
    }

    public static String getWorldName() {
        if (isSinglePlayer()) {
            return getWorldNameSinglePlayer();
        } else {
            return getWorldNameMultiplayer();
        }
    }

    public static String getSeed() {
        if (isSinglePlayer()) {
            IntegratedServer server = MinecraftClient.getInstance().getServer();
            if (server == null) {
                return "Unable to get world seed due to an error.";
            }
            return Long.toString(server.getWorlds().iterator().next().getSeed());
        }
        return "Screenshot taken in Multiplayer -- Seed Unknown"; //TODO
    }

    private static boolean isSinglePlayer() {
        return MinecraftClient.getInstance().isInSingleplayer();
    }

    public static String getResourcePacks() {
        StringBuilder base = new StringBuilder("[");
        try {
            Iterator<ResourcePack> resourcePackIterator = MinecraftClient.getInstance().getResourceManager().streamResourcePacks().iterator();
            while (resourcePackIterator.hasNext()) {
                base.append(String.format("%s", resourcePackIterator.next().getName()));
                if (resourcePackIterator.hasNext()) {
                    base.append(", ");
                }
            }
            base.append("]");
            return base.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Unable to get active resource packs due to an error.";
        }
    }

    public static String getVersion() {
        return MinecraftClient.getInstance().getGame().getVersion().getName();
    }

    public static String timedate() {
        return Util.getFormattedCurrentTime();
    }

    public static HashMap<String, String> getMetadata() {
        HashMap<String, String> meta = new HashMap<>();
        if (!ScreenshotSettingsConfig.INSTANCE.useMetadata) {
            return meta;
        }
        if (ScreenshotSettingsConfig.INSTANCE.coordinates) {
            meta.put("Coordinates", getCoordinates());
        }
        if (ScreenshotSettingsConfig.INSTANCE.worldName) {
            meta.put("World/Server Name", getWorldName());
        }
        if (ScreenshotSettingsConfig.INSTANCE.seed) {
            meta.put("World Seed", getSeed());
        }
        if (ScreenshotSettingsConfig.INSTANCE.resourcePacks) {
            meta.put("Resource Packs", getResourcePacks());
        }
        if (ScreenshotSettingsConfig.INSTANCE.shaderPack && ShaderIntegration.irisPresent()) {
            meta.put("Shader Pack", ShaderIntegration.get());
        }
        if (ScreenshotSettingsConfig.INSTANCE.mcVersion) {
            meta.put("Minecraft Version", getVersion());
        }
        return meta;
    }

}
