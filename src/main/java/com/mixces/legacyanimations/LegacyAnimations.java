package com.mixces.legacyanimations;

import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import net.fabricmc.api.ModInitializer;

public class LegacyAnimations implements ModInitializer {

	@Override
	public void onInitialize() {
		LegacyAnimationsSettings.CONFIG.load();
	}

}