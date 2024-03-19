package com.mixces.legacyanimations.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;

public class ServerUtils {

    public static ServerUtils INSTANCE = new ServerUtils();

    public final ServerInfo server = MinecraftClient.getInstance().getCurrentServerEntry();

    public boolean isOnHypixel() {
        return server != null && server.address.endsWith("hypixel.net");
    }

}
