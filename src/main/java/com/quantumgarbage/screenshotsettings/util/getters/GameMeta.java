package com.quantumgarbage.screenshotsettings.util.getters;


import com.quantumgarbage.screenshotsettings.client.ScreenshotSettingsClient;
import com.quantumgarbage.screenshotsettings.integrations.ShaderIntegration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.resource.ResourcePack;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.level.ServerWorldProperties;

import java.util.HashMap;
import java.util.Iterator;

import static com.quantumgarbage.screenshotsettings.client.ScreenshotSettingsClient.LOGGER;

public class GameMeta {
    public static String getCoordinatesMetadata(final MinecraftClient client) {
        final var p = client.player;
        assert null != p;
        final Vec3d pos = getCoordinates(p);
        return String.format("X:[%.3f] Y:[%.3f] Z:[%.3f] Yaw:[%.3f] Pitch:[%.3f]", pos.x, pos.y, pos.z, p.getYaw(), p.getPitch());
    }

    public static Vec3d getCoordinates(final ClientPlayerEntity p) {
        try {
            return p.getPos();
        } catch (NullPointerException e) {
            LOGGER.error("Unable to get player coordinates. Did you try to take the screenshot before you finished loading in?");
            e.printStackTrace();
            return new Vec3d(0, 0, 0);
        }
    }

    public static String getPlayerName(final MinecraftClient client) {
        return client.getSession().getUsername();
    }

    private static String getWorldNameSinglePlayer(final MinecraftClient client) {
        assert null != client.getServer();
        try {
            final ServerWorldProperties worldProperties = (ServerWorldProperties) client.getServer().getWorlds().iterator().next().getLevelProperties();

            return worldProperties.getLevelName();
        } catch (Exception e) {
            com.quantumgarbage.screenshotsettings.client.ScreenshotSettingsClient.LOGGER.error("Unable to get world name for a screenshot.\n" + e);
            return "Unable to determine world name.";
        }


    }

    private static String getWorldNameMultiplayer(final MinecraftClient client) {
        try {
            final ServerInfo si = client.getCurrentServerEntry();
            assert si != null;
            return si.name;
        } catch (Exception e) {
            LOGGER.error("Unable to get world name for a screenshot.\n" + e);
            return "Unable to determine world name.";
        }
    }

    public static String getWorldName(final MinecraftClient client) {
        if (isSinglePlayer(client)) {
            return getWorldNameSinglePlayer(client);
        } else {
            return getWorldNameMultiplayer(client);
        }
    }

    public static String getSeed(final MinecraftClient client) {
        if (isSinglePlayer(client)) {
            final IntegratedServer server = client.getServer();
            assert null != server;
            return Long.toString(server.getWorlds().iterator().next().getSeed());
        }
        return "Screenshot taken in Multiplayer -- Seed Unknown";
    }

    private static boolean isSinglePlayer(final MinecraftClient client) {
        return client.isInSingleplayer();
    }

    public static String getResourcePacks(final MinecraftClient client) {
        final StringBuilder base = new StringBuilder("[");
        final Iterator<ResourcePack> resourcePackIterator = client.getResourceManager().streamResourcePacks().iterator();
        while (resourcePackIterator.hasNext()) {
            base.append(String.format("%s", resourcePackIterator.next().getName()));
            if (resourcePackIterator.hasNext()) {
                base.append(", ");
            }
        }
        base.append("]");
        return base.toString();
    }

    public static String getVersion(final MinecraftClient client) {
        return client.getGameVersion();
    }

    public static String timedate() {
        return Util.getFormattedCurrentTime();
    }

    public static HashMap<String, String> getMetadata(final MinecraftClient client) {
        final HashMap<String, String> meta = new HashMap<>();
        // make sure that metadata even is possible to obtain.
        if (null == client.player || null == client.world) {
            return meta;
        }

        if (!ScreenshotSettingsClient.CONFIG.useMetadata) {
            return meta;
        }
        if (ScreenshotSettingsClient.CONFIG.coordinates) {
            meta.put("Coordinates", getCoordinatesMetadata(client));
        }
        if (ScreenshotSettingsClient.CONFIG.worldName) {
            meta.put("World/Server Name", getWorldName(client));
        }
        if (ScreenshotSettingsClient.CONFIG.seed) {
            meta.put("World Seed", getSeed(client));
        }
        if (ScreenshotSettingsClient.CONFIG.resourcePacks) {
            meta.put("Resource Packs", getResourcePacks(client));
        }
        if (ScreenshotSettingsClient.CONFIG.shaderPack && ShaderIntegration.irisPresent()) {
            meta.put("Shader Pack", ShaderIntegration.getShaderName());
        }
        if (ScreenshotSettingsClient.CONFIG.mcVersion) {
            meta.put("Minecraft Version", getVersion(client));
        }
        return meta;
    }

}
