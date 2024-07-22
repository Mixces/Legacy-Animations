package com.mixces.legacyanimations.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;

public class ServerUtils
{

    public static ServerUtils INSTANCE = new ServerUtils();

    private final ServerInfo server = MinecraftClient.getInstance().getCurrentServerEntry();

    public boolean isValidServer()
    {
        return server != null && (server.address.endsWith("hypixel.net") || server.address.endsWith("bedwarspractice.club"));
    }

}
