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
    public boolean punchDuringUsage = true;
    @ConfigEntry
    public boolean oldSwordBlock = true;
    @ConfigEntry
    public boolean hideShields = true;
    @ConfigEntry
    public boolean noCooldown = true;
    @ConfigEntry
    public boolean oldSneaking = true;
    @ConfigEntry
    public boolean oldWalking = true;
    @ConfigEntry
    public boolean oldDeath = true;


    @SuppressWarnings("deprecation")
    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(INSTANCE, ((defaults, config, builder) -> builder
                .title(Text.literal("Old Animations"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Old Animations"))
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Punching Blocks Whilst Using Items Animation"))
                                .description(OptionDescription.of(Text.of("Brings back the old block-hitting animation to swords/bows/consumable/blocks!")))
                                .binding(defaults.punchDuringUsage, () -> config.punchDuringUsage, newVal -> config.punchDuringUsage = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Old Sword Blocking"))
                                .description(OptionDescription.of(Text.of("Adds the ability to block with your sword! This option is purely visual.")))
                                .binding(defaults.oldSwordBlock, () -> config.oldSwordBlock, newVal -> config.oldSwordBlock = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Hide Shield Model"))
                                .description(OptionDescription.of(Text.of("Hides the shield model from rendering.")))
                                .binding(defaults.hideShields, () -> config.hideShields, newVal -> config.hideShields = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Remove Swing Cooldown Animation"))
                                .description(OptionDescription.of(Text.of("Visually removes the swing cooldown animation!")))
                                .binding(defaults.noCooldown, () -> config.noCooldown, newVal -> config.noCooldown = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Old Sneaking Animation Descent"))
                                .description(OptionDescription.of(Text.of("Makes the descent of the sneaking animation faster than it's ascent.")))
                                .binding(defaults.oldSneaking, () -> config.oldSneaking, newVal -> config.oldSneaking = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Old Backwards Walking Animation"))
                                .description(OptionDescription.of(Text.of("Reverts the backwards walking animation to it's former glory.")))
                                .binding(defaults.oldWalking, () -> config.oldWalking, newVal -> config.oldWalking = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Old Entity Death Animation"))
                                .description(OptionDescription.of(Text.of("Allows dead entity corpse limbs to move.")))
                                .binding(defaults.oldDeath, () -> config.oldDeath, newVal -> config.oldDeath = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .build())
        )).generateScreen(parent);
    }
}
