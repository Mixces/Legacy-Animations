package com.mixces.oldanimations;

import com.mixces.oldanimations.config.OldAnimationsSettings;
import net.fabricmc.api.ModInitializer;

public class OldAnimations implements ModInitializer {

	@Override
	public void onInitialize() {
		OldAnimationsSettings.load();
	}

}