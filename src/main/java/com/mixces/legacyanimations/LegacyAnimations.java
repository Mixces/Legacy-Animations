package com.mixces.legacyanimations;

import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.hook.TransformHook;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;

public class LegacyAnimations implements ModInitializer
{

	@Override
	public void onInitialize()
	{
		LegacyAnimationsSettings.CONFIG.load();
//		ClientTickEvents.END_WORLD_TICK.register(world ->
//		{
//			if (MinecraftClient.getInstance().player == null)
//			{
//				return;
//			}
//
//			MinecraftClient.getInstance().player.calculateDimensions();
//		});

		CommandRegistrationCallback.EVENT.register(
				(dispatcher, registryAccess, environment) ->
				{
					dispatcher.register(CommandManager.literal("legacyanimations")
							.then(CommandManager.argument("x", FloatArgumentType.floatArg())
									.executes(context -> {
										final float value = FloatArgumentType.getFloat(context, "x");
										TransformHook.translationX = value;
										context.getSource().sendFeedback(() -> Text.literal("x: " + value), false);
										return 1;
									})
									.then(CommandManager.argument("y", FloatArgumentType.floatArg())
											.executes(context -> {
												final float value = FloatArgumentType.getFloat(context, "x");
												final float value2 = FloatArgumentType.getFloat(context, "y");
												TransformHook.translationX = value;
												TransformHook.translationY = value2;
												context.getSource().sendFeedback(() -> Text.literal("x: " + value + " y: " + value2), false);
												return 1;
											})
											.then(CommandManager.argument("z", FloatArgumentType.floatArg())
													.executes(context -> {
														final float value = FloatArgumentType.getFloat(context, "x");
														final float value2 = FloatArgumentType.getFloat(context, "y");
														final float value3 = FloatArgumentType.getFloat(context, "z");
														TransformHook.translationX = value;
														TransformHook.translationY = value2;
														TransformHook.translationZ = value3;
														context.getSource().sendFeedback(() -> Text.literal("x: " + value + " y: " + value2 + " z: " + value3), false);
														return 1;
													})
//													.then(CommandManager.argument("roll", FloatArgumentType.floatArg())
//															.executes(context -> {
//																final float value = FloatArgumentType.getFloat(context, "x");
//																final float value2 = FloatArgumentType.getFloat(context, "y");
//																final float value3 = FloatArgumentType.getFloat(context, "z");
//																final float value4 = FloatArgumentType.getFloat(context, "roll");
//																TransformHook.translationX = value;
//																TransformHook.translationY = value2;
//																TransformHook.translationZ = value3;
//																TransformHook.rotationY = value4;
//																context.getSource().sendFeedback(() -> Text.literal("x: " + value + " y: " + value2 + " z: " + value3 + " roll: " + value4), false);
//																return 1;
//															})
//													)
									))));
				});
	}

}