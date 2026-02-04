package grondag.darkness.config;

import grondag.darkness.DarknessInit;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class DarknessConfigScreen extends Screen {
    private final Screen parent;
    private final DarknessSettings config = DarknessInit.CONFIG;

    public DarknessConfigScreen(Screen parent) {
        super(Component.translatable("text.config.darkness.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int y = this.height / 6;

        this.addRenderableWidget(CycleButton.onOffBuilder(config.darkOverworld)
                .create(centerX - 100, y, 200, 20,
                        Component.translatable("text.config.darkness.option.darkOverworld"),
                        (button, value) -> config.darkOverworld = value));

        this.addRenderableWidget(CycleButton.onOffBuilder(config.darkNether)
                .create(centerX - 100, y + 28, 200, 20,
                        Component.translatable("text.config.darkness.option.darkNether"),
                        (button, value) -> config.darkNether = value));

        this.addRenderableWidget(CycleButton.onOffBuilder(config.darkEnd)
                .create(centerX - 100, y + 56, 200, 20,
                        Component.translatable("text.config.darkness.option.darkEnd"),
                        (button, value) -> config.darkEnd = value));

        this.addRenderableWidget(CycleButton.onOffBuilder(config.requireMod)
                .create(centerX - 100, y + 84, 200, 20,
                        Component.translatable("text.config.darkness.option.requireMod"),
                        (button, value) -> config.requireMod = value));

        //? if >=1.21.11 {
        /*this.addRenderableWidget(CycleButton.<Integer>builder(value -> Component.literal(value + "%"), config.darknessIntensity)
                .withValues(0, 25, 50, 75, 100)
                .create(centerX - 100, y + 112, 200, 20,
                        Component.translatable("text.config.darkness.option.darknessIntensity"),
                        (button, value) -> config.darknessIntensity = value));
        *///?} else {
        this.addRenderableWidget(CycleButton.<Integer>builder(value -> Component.literal(value + "%"))
                .withValues(0, 25, 50, 75, 100)
                .withInitialValue(config.darknessIntensity)
                .create(centerX - 100, y + 112, 200, 20,
                        Component.translatable("text.config.darkness.option.darknessIntensity"),
                        (button, value) -> config.darknessIntensity = value));
        //?}

        this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> {
            config.save();
            this.minecraft.setScreen(this.parent);
        }).bounds(centerX - 100, y + 156, 200, 20).build());
    }

    @Override
    public void onClose() {
        config.save();
        this.minecraft.setScreen(this.parent);
    }
}
