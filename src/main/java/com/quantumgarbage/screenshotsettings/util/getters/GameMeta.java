package com.quantumgarbage.screenshotsettings.util.getters;

import com.quantumgarbage.screenshotsettings.client.config.ScreenshotSettingsConfig;
import com.quantumgarbage.screenshotsettings.integrations.ShaderIntegration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.resource.ResourcePack;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.level.ServerWorldProperties;

import java.util.HashMap;
import java.util.Iterator;

public class GameMeta {
    public static String getCoordinatesMetadata() {
        Vec3d pos = getCoordinates();
        var p = MinecraftClient.getInstance().player;
        if(pos == null || p == null){
            return "Failed to get Coordinates due to an error.";
        }
        return String.format("X:[%.3f] Y:[%.3f] Z:[%.3f] Yaw:[%.3f] Pitch:[%.3f]", pos.x, pos.y, pos.z, p.getYaw(), p.getPitch());
    }

    public static Vec3d getCoordinates() {
        if (MinecraftClient.getInstance().player == null) {
            return new Vec3d(0,0,0);
        }
            return MinecraftClient.getInstance().player.getPos();
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
            meta.put("Coordinates", getCoordinatesMetadata());
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
            meta.put("Shader Pack", ShaderIntegration.getShaderName());
        }
        if (ScreenshotSettingsConfig.INSTANCE.mcVersion) {
            meta.put("Minecraft Version", getVersion());
        }
        return meta;
    }

}
