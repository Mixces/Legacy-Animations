package dev.microcontrollers.nametagtweaks.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.ConfigEntry;
import dev.isxander.yacl3.config.ConfigInstance;
import dev.isxander.yacl3.config.GsonConfigInstance;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class NametagTweaksConfig {
    public static final ConfigInstance<NametagTweaksConfig> INSTANCE = GsonConfigInstance.createBuilder(NametagTweaksConfig.class)
            .setPath(FabricLoader.getInstance().getConfigDir().resolve("nametagtweaks.json"))
            .build();

    @ConfigEntry
    public boolean showOwnNametags = false;

    @ConfigEntry
    public boolean showNametagsInHiddenHud = false;

    @ConfigEntry
    public boolean nametagTextShadow = false;

    @ConfigEntry
    public float nametagOpacity = 25F;

    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(INSTANCE, ((defaults, config, builder) -> builder
                .title(Text.literal("Nametag Tweaks"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Nametag Tweaks"))
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Show Own Nametag"))
                                .description(OptionDescription.of(Text.of("Show Own Nametag")))
                                .binding(defaults.showOwnNametags, () -> config.showOwnNametags, newVal -> config.showOwnNametags = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Show Nametags in F1 Mode"))
                                .description(OptionDescription.of(Text.of("Show Nametags in F1 Mode")))
                                .binding(defaults.showNametagsInHiddenHud, () -> config.showNametagsInHiddenHud, newVal -> config.showNametagsInHiddenHud = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(float.class)
                                .name(Text.literal("Custom Nametag Background Opacity"))
                                .description(OptionDescription.of(Text.of("The value for custom nametag background opacity.")))
                                .binding(25F, () -> config.nametagOpacity, newVal -> config.nametagOpacity = newVal)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .valueFormatter(value -> Text.of(String.format("%,.0f", value) + " %"))
                                        .range(0F, 100F)
                                        .step(1F))
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Nametag Text Shadow"))
                                .description(OptionDescription.of(Text.of("Adds a textshadow to nametags. The shadow renders in front of the regular text, which seems to be a vanilla Minecraft Bug.")))
                                .binding(defaults.nametagTextShadow, () -> config.nametagTextShadow, newVal -> config.nametagTextShadow = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .build())
        )).generateScreen(parent);
    }
}
