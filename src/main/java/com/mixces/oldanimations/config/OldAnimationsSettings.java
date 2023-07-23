package com.mixces.oldanimations.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.ConfigEntry;
import dev.isxander.yacl3.config.ConfigInstance;
import dev.isxander.yacl3.config.GsonConfigInstance;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class OldAnimationsSettings {

    public static final ConfigInstance<OldAnimationsSettings> INSTANCE = GsonConfigInstance.createBuilder(OldAnimationsSettings.class)
            .setPath(FabricLoader.getInstance().getConfigDir().resolve("oldanimations.json"))
            .build();

    @ConfigEntry
    public boolean swordHitAnimation = true;

    @ConfigEntry
    public boolean bowHitAnimation = true;

    @ConfigEntry
    public boolean tridentHitAnimation = true;

    @ConfigEntry
    public boolean crossBowHitAnimation = true;

    @ConfigEntry
    public boolean eatDrinkHitAnimation = true;


    @SuppressWarnings("deprecation")
    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(INSTANCE, ((defaults, config, builder) -> builder
                .title(Text.literal("Old Animations"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Old Animations"))
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Sword Block-Hitting Animation"))
                                .description(OptionDescription.of(Text.of("Brings back the old block-hitting animation to swords!")))
                                .binding(defaults.swordHitAnimation, () -> config.swordHitAnimation, newVal -> config.swordHitAnimation = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Old Animations"))
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Bow Punch During Usage Animation"))
                                .description(OptionDescription.of(Text.of("Brings back the old bow pulling back whilst punching animation!")))
                                .binding(defaults.bowHitAnimation, () -> config.bowHitAnimation, newVal -> config.bowHitAnimation = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .build())
        )).generateScreen(parent);
    }
}
