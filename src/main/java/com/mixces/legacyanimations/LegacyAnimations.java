package com.mixces.legacyanimations;

import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class LegacyAnimations implements ModInitializer {

	@Override
	public void onInitialize() {
		LegacyAnimationsSettings.CONFIG.load();
		ClientTickEvents.END_WORLD_TICK.register(world -> {
			if (MinecraftClient.getInstance().player != null) {
				MinecraftClient.getInstance().player.calculateDimensions();
			}
		});
	}

}