package com.aivins03k.sensitivitymod;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.Locale;

public class SensitivityScreen extends Screen {
    private static final double[] PRESETS = {0.10, 0.25, 0.50, 0.75, 1.00};
    private static final String[] PRESET_NAMES = {"Low", "Medium-Low", "Normal", "High", "Maximum"};

    private final Screen parent;
    private ButtonWidget valueButton;

    public SensitivityScreen(Screen parent) {
        super(Text.translatable("screen.sensitivitymod.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        int y = this.height / 2 - 70;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("-0.10"), button ->
                SensitivityModClient.adjustSensitivity(client, -0.10)).dimensions(center - 155, y, 75, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("-0.01"), button ->
                SensitivityModClient.adjustSensitivity(client, -SensitivityModClient.STEP)).dimensions(center - 75, y, 75, 20).build());

        valueButton = this.addDrawableChild(ButtonWidget.builder(valueText(), button -> {})
                .dimensions(center + 5, y, 150, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("+0.01"), button ->
                SensitivityModClient.adjustSensitivity(client, SensitivityModClient.STEP)).dimensions(center - 75, y + 30, 75, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("+0.10"), button ->
                SensitivityModClient.adjustSensitivity(client, 0.10)).dimensions(center + 5, y + 30, 75, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("screen.sensitivitymod.reset"), button ->
                SensitivityModClient.setSensitivity(client, 0.50)).dimensions(center - 155, y + 30, 75, 20).build());

        for (int i = 0; i < PRESETS.length; i++) {
            final double preset = PRESETS[i];
            int x = center - 155 + (i % 3) * 105;
            int presetY = y + 70 + (i / 3) * 25;
            this.addDrawableChild(ButtonWidget.builder(Text.literal(PRESET_NAMES[i]), button ->
                    SensitivityModClient.setSensitivity(client, preset)).dimensions(x, presetY, 100, 20).build());
        }

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.done"), button -> close())
                .dimensions(center - 50, y + 125, 100, 20).build());
    }

    private Text valueText() {
        double value = client == null ? 0.50 : SensitivityModClient.getSensitivity(client);
        return Text.literal(String.format(Locale.ROOT, "Sensitivity: %.2f", value));
    }

    @Override
    public void tick() {
        if (valueButton != null) {
            valueButton.setMessage(valueText());
        }
    }

    @Override
    public void close() {
        if (client != null) {
            client.setScreen(parent);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 2 - 105, 0xFFFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer,
                Text.literal("Adjust mouse sensitivity"), width / 2, height / 2 - 90, 0xFFCCCCCC);
        super.render(context, mouseX, mouseY, delta);
    }
}
