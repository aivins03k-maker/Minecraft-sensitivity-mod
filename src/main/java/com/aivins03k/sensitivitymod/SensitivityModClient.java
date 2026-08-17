package com.aivins03k.sensitivitymod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class SensitivityModClient implements ClientModInitializer {
    private static final double[] SENSITIVITY_PRESETS = {
            0.20, 0.30, 0.40, 0.50, 0.60, 0.70, 0.80, 0.90, 1.00
    };

    private static int currentPreset = 3; // Starts at 0.50
    private static KeyBinding changeSensitivityKey;

    @Override
    public void onInitializeClient() {
        changeSensitivityKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sensitivitymod.change_sensitivity",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.sensitivitymod"
        ));

        net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (changeSensitivityKey.wasPressed()) {
                currentPreset = (currentPreset + 1) % SENSITIVITY_PRESETS.length;
                setSensitivity(client, SENSITIVITY_PRESETS[currentPreset]);
            }
        });
    }

    private static void setSensitivity(MinecraftClient client, double value) {
        client.options.getMouseSensitivity().setValue(value);
    }
}
