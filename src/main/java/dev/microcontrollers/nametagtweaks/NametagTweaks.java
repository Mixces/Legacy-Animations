package dev.microcontrollers.nametagtweaks;

import dev.microcontrollers.nametagtweaks.config.NametagTweaksConfig;
import net.fabricmc.api.ModInitializer;

public class NametagTweaks implements ModInitializer {

	@Override
	public void onInitialize() {
		NametagTweaksConfig.INSTANCE.load();
	}
}