package com.quantumgarbage.screenshotsettings.util.getters;

import com.quantumgarbage.screenshotsettings.client.ScreenshotSettingsClient;
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
    public static String getCoordinatesMetadata(MinecraftClient client) {
        final var p = client.player;
        assert p != null;
        final Vec3d pos = getCoordinates(p);
        return String.format("X:[%.3f] Y:[%.3f] Z:[%.3f] Yaw:[%.3f] Pitch:[%.3f]", pos.x, pos.y, pos.z, p.getYaw(), p.getPitch());
    }

    public static Vec3d getCoordinates(ClientPlayerEntity p) {
        assert p != null;
        return p.getPos();
    }

    public static String getPlayerName(MinecraftClient client) {
        assert client.player != null;
        return client.player.getEntityName();
    }

    private static String getWorldNameSinglePlayer(MinecraftClient client) {

        assert client.getServer() != null;
        final ServerWorldProperties worldProperties = (ServerWorldProperties) client.getServer().getWorlds().iterator().next().getLevelProperties();
        return worldProperties.getLevelName();


    }

    private static String getWorldNameMultiplayer(MinecraftClient client) {
        final ServerInfo si = client.getCurrentServerEntry();
        assert si != null;
        return si.name;

    }

    public static String getWorldName(MinecraftClient client) {
        if (isSinglePlayer(client)) {
            return getWorldNameSinglePlayer(client);
        } else {
            return getWorldNameMultiplayer(client);
        }
    }

    public static String getSeed(MinecraftClient client) {
        if (isSinglePlayer(client)) {
            final IntegratedServer server = client.getServer();
            assert server != null;
            return Long.toString(server.getWorlds().iterator().next().getSeed());
        }
        return "Screenshot taken in Multiplayer -- Seed Unknown";
    }

    private static boolean isSinglePlayer(MinecraftClient client) {
        return client.isInSingleplayer();
    }

    public static String getResourcePacks(MinecraftClient client) {
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

    public static String getVersion(MinecraftClient client) {
        return client.getGame().getVersion().getName();
    }

    public static String timedate() {
        return Util.getFormattedCurrentTime();
    }

    public static HashMap<String, String> getMetadata() {
        final MinecraftClient client = ScreenshotSettingsClient.client;
        final HashMap<String, String> meta = new HashMap<>();
        if (!ScreenshotSettingsConfig.INSTANCE.useMetadata) {
            return meta;
        }
        if (ScreenshotSettingsConfig.INSTANCE.coordinates) {
            meta.put("Coordinates", getCoordinatesMetadata(client));
        }
        if (ScreenshotSettingsConfig.INSTANCE.worldName) {
            meta.put("World/Server Name", getWorldName(client));
        }
        if (ScreenshotSettingsConfig.INSTANCE.seed) {
            meta.put("World Seed", getSeed(client));
        }
        if (ScreenshotSettingsConfig.INSTANCE.resourcePacks) {
            meta.put("Resource Packs", getResourcePacks(client));
        }
        if (ScreenshotSettingsConfig.INSTANCE.shaderPack && ShaderIntegration.irisPresent()) {
            meta.put("Shader Pack", ShaderIntegration.getShaderName());
        }
        if (ScreenshotSettingsConfig.INSTANCE.mcVersion) {
            meta.put("Minecraft Version", getVersion(client));
        }
        return meta;
    }

}
