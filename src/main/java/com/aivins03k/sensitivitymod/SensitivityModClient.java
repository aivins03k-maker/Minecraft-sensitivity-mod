package com.aivins03k.sensitivitymod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class SensitivityModClient implements ClientModInitializer {
    public static final double MIN_SENSITIVITY = 0.01;
    public static final double MAX_SENSITIVITY = 1.00;
    public static final double STEP = 0.01;

    private static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sensitivitymod.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.sensitivitymod"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new SensitivityScreen(null));
                }
            }
        });
    }

    public static double getSensitivity(MinecraftClient client) {
        return client.options.getMouseSensitivity().getValue();
    }

    public static void setSensitivity(MinecraftClient client, double value) {
        value = Math.max(MIN_SENSITIVITY, Math.min(MAX_SENSITIVITY, value));
        client.options.getMouseSensitivity().setValue(value);
    }

    public static void adjustSensitivity(MinecraftClient client, double amount) {
        setSensitivity(client, getSensitivity(client) + amount);
    }
}
