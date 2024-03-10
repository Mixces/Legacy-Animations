package com.mixces.legacyanimations.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class LegacyAnimationsSettings {

    public static final ConfigClassHandler<LegacyAnimationsSettings> CONFIG = ConfigClassHandler.createBuilder(LegacyAnimationsSettings.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                .setPath(FabricLoader.getInstance().getConfigDir().resolve("legacyanimations.json"))
                .build())
            .build();

    @SerialEntry public boolean punchDuringUsage = true;
    @SerialEntry public boolean oldSwordBlock = true; // TODO: this prevents an item in offhand (e.g., bow) from being used. Also causes to walk slow despite not actually blocking, may trigger ACs
    @SerialEntry public boolean blockWithShieldOnly = true; // this fixes the aforementioned issue by requiring a shield in offhand
    @SerialEntry public boolean hideShields = true;
    @SerialEntry public boolean hideShieldHotbar = true;
    @SerialEntry public boolean noCooldown = true;
    @SerialEntry public boolean oldSneaking = true;
    @SerialEntry public boolean oldWalking = true;
    @SerialEntry public boolean oldDeath = true;
    @SerialEntry public boolean oldSwim = true;
    @SerialEntry public boolean oldHearts = true;
    @SerialEntry public boolean oldDebug = true;
    @SerialEntry public boolean perspectiveCrosshair = true;
    @SerialEntry public boolean oldProjectiles = true;
    @SerialEntry public boolean oldViewBob = true;
    @SerialEntry public boolean noDropSwing = true;
    @SerialEntry public boolean fastItems = true;
    @SerialEntry public boolean armorTint = true;


    @SuppressWarnings("deprecation")
    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Text.literal("Old Animations"))

                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Block-Hitting"))

                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Punching Blocks Whilst Using Items Animation"))
                                .description(OptionDescription.of(Text.of("Brings back the old block-hitting animation to swords/bows/consumable/blocks!")))
                                .binding(defaults.punchDuringUsage, () -> config.punchDuringUsage, newVal -> config.punchDuringUsage = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Old Sword Blocking"))
                                .description(OptionDescription.of(Text.of("Adds the ability to block with your sword! This option is purely visual (not really pls fix).")))
                                .binding(defaults.oldSwordBlock, () -> config.oldSwordBlock, newVal -> config.oldSwordBlock = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Block Only when Holding Shield"))
                                .description(OptionDescription.of(Text.of("Only blocks with your sword when you hold a shield in your offhand.")))
                                .binding(defaults.blockWithShieldOnly, () -> config.blockWithShieldOnly, newVal -> config.blockWithShieldOnly = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Hide Shield Model"))
                                .description(OptionDescription.of(Text.of("Hides the shield model from rendering.")))
                                .binding(defaults.hideShields, () -> config.hideShields, newVal -> config.hideShields = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Hide Shield Offhand Hotbar"))
                                .description(OptionDescription.of(Text.of("Hides the offhand hotbar if the item held is a shield.")))
                                .binding(defaults.hideShieldHotbar, () -> config.hideShieldHotbar, newVal -> config.hideShieldHotbar = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .build())

                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Sneaking Mechanics"))

                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Old Sneaking Animation Descent"))
                                .description(OptionDescription.of(Text.of("Makes the descent of the sneaking animation faster than it's ascent.")))
                                .binding(defaults.oldSneaking, () -> config.oldSneaking, newVal -> config.oldSneaking = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .build())

                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("GUI Tweaks"))

                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Old Debug Menu"))
                                .description(OptionDescription.of(Text.of("Removes the background of the F3 menu text and uses text shadow instead.")))
                                .binding(defaults.oldDebug, () -> config.oldDebug, newVal -> config.oldDebug = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Old Health Flashing"))
                                .description(OptionDescription.of(Text.of("Brings back the old hotbar health flashing appearance.")))
                                .binding(defaults.oldHearts, () -> config.oldHearts, newVal -> config.oldHearts = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Third Person Mode Crosshair"))
                                .description(OptionDescription.of(Text.of("Brings back the crosshair in third person mode.")))
                                .binding(defaults.perspectiveCrosshair, () -> config.perspectiveCrosshair, newVal -> config.perspectiveCrosshair = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .build())

                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Entity Animations"))

                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Armor Damage Tint"))
                                .description(OptionDescription.of(Text.of("Tints the armor while taking damage.")))
                                .binding(defaults.armorTint, () -> config.armorTint, newVal -> config.armorTint = newVal)
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
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Old Swimming Animation"))
                                .description(OptionDescription.of(Text.of("Reverts the swimming animation to it's former glory.")))
                                .binding(defaults.oldSwim, () -> config.oldSwim, newVal -> config.oldSwim = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .build())

                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Miscellaneous"))

                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Remove Swing Cooldown Animation"))
                                .description(OptionDescription.of(Text.of("Removes the swing  animation played when dropping/throwing items!")))
                                .binding(defaults.noDropSwing, () -> config.noDropSwing, newVal -> config.noDropSwing = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Remove Swing Animation on Throwing Items"))
                                .description(OptionDescription.of(Text.of("Visually removes the swing cooldown animation!")))
                                .binding(defaults.noCooldown, () -> config.noCooldown, newVal -> config.noCooldown = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Old View Bobbing"))
                                .description(OptionDescription.of(Text.of("Changes the held item's position while moving based on the player's yaw")))
                                .binding(defaults.oldViewBob, () -> config.oldViewBob, newVal -> config.oldViewBob = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Old Projectiles"))
                                .description(OptionDescription.of(Text.of("Reverts the projectile's positions back to the older style.")))
                                .binding(defaults.oldProjectiles, () -> config.oldProjectiles, newVal -> config.oldProjectiles = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Fast/2D Items"))
                                .description(OptionDescription.of(Text.of("Reverts the projectile's positions back to the older style.")))
                                .binding(defaults.fastItems, () -> config.fastItems, newVal -> config.fastItems = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .build())

        )).generateScreen(parent);
    }

}
