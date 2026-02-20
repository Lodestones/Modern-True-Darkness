package grondag.darkness.config;

import grondag.darkness.DarknessInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.IntConsumer;

public class DarknessConfigScreen extends Screen {
    private final Screen parent;
    private final DarknessSettings config = DarknessInit.CONFIG;
    private DarknessOptionsList list;

    public DarknessConfigScreen(Screen parent) {
        super(Component.translatable("text.config.darkness.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        //? if <=1.20.2 {
        /*this.list = new DarknessOptionsList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
        this.addWidget(this.list);
        *///?} else {
        this.list = new DarknessOptionsList(this.minecraft, this.width, this.height - 64, 32, 25);
        this.addRenderableWidget(this.list);
        //?}

        // Toggle buttons
        list.addWidgetEntry(createToggle("darkOverworld", config.darkOverworld, (b, v) -> config.darkOverworld = v));
        list.addWidgetEntry(createToggle("darkNether", config.darkNether, (b, v) -> config.darkNether = v));
        list.addWidgetEntry(createToggle("darkEnd", config.darkEnd, (b, v) -> config.darkEnd = v));
        list.addWidgetEntry(createToggle("darkCreative", config.darkCreative, (b, v) -> config.darkCreative = v));
        list.addWidgetEntry(createToggle("requireMod", config.requireMod, (b, v) -> config.requireMod = v));

        // Sliders
        list.addWidgetEntry(new BrightnessSlider("fullMoonBrightness", config.fullMoonBrightness, v -> config.fullMoonBrightness = v));
        list.addWidgetEntry(new BrightnessSlider("gibbousMoonBrightness", config.gibbousMoonBrightness, v -> config.gibbousMoonBrightness = v));
        list.addWidgetEntry(new BrightnessSlider("quarterMoonBrightness", config.quarterMoonBrightness, v -> config.quarterMoonBrightness = v));
        list.addWidgetEntry(new BrightnessSlider("crescentMoonBrightness", config.crescentMoonBrightness, v -> config.crescentMoonBrightness = v));
        list.addWidgetEntry(new BrightnessSlider("newMoonBrightness", config.newMoonBrightness, v -> config.newMoonBrightness = v));
        list.addWidgetEntry(new BrightnessSlider("caveBrightness", config.caveBrightness, v -> config.caveBrightness = v));

        // Done button (fixed at bottom, outside scrollable area)
        this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> {
            config.save();
            this.minecraft.setScreen(this.parent);
        }).bounds(this.width / 2 - 100, this.height - 28, 200, 20).build());
    }

    private CycleButton<Boolean> createToggle(String key, boolean initial, CycleButton.OnValueChange<Boolean> onChange) {
        return CycleButton.onOffBuilder(initial)
                .create(0, 0, 200, 20,
                        Component.translatable("text.config.darkness.option." + key),
                        onChange);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        //? if <=1.20.2 {
        /*this.list.render(g, mouseX, mouseY, delta);
        *///?}
        super.render(g, mouseX, mouseY, delta);
        g.drawCenteredString(this.font, this.title, this.width / 2, 12, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        config.save();
        this.minecraft.setScreen(this.parent);
    }

    static class DarknessOptionsList extends ContainerObjectSelectionList<DarknessOptionsList.WidgetEntry> {

        //? if <=1.20.2 {
        /*public DarknessOptionsList(Minecraft client, int width, int height, int top, int bottom, int itemHeight) {
            super(client, width, height, top, bottom, itemHeight);
        }
        *///?} else {
        public DarknessOptionsList(Minecraft client, int width, int height, int top, int itemHeight) {
            super(client, width, height, top, itemHeight);
        }
        //?}

        public void addWidgetEntry(AbstractWidget widget) {
            this.addEntry(new WidgetEntry(widget));
        }

        @Override
        public int getRowWidth() {
            return 400;
        }

        static class WidgetEntry extends ContainerObjectSelectionList.Entry<WidgetEntry> {
            private final AbstractWidget widget;

            WidgetEntry(AbstractWidget widget) {
                this.widget = widget;
            }

            @Override
            public List<? extends GuiEventListener> children() {
                return List.of(widget);
            }

            @Override
            public List<? extends NarratableEntry> narratables() {
                return List.of(widget);
            }

            //? if >=1.21.9 {
            /*@Override
            public void renderContent(GuiGraphics g, int mouseX, int mouseY, boolean isHovered, float delta) {
                widget.setX(getX() + getWidth() / 2 - 100);
                widget.setY(getY());
                widget.render(g, mouseX, mouseY, delta);
            }
            *///?} else {
            @Override
            public void render(GuiGraphics g, int index, int top, int left, int width, int height,
                               int mouseX, int mouseY, boolean isHovered, float delta) {
                widget.setX(left + width / 2 - 100);
                widget.setY(top);
                widget.render(g, mouseX, mouseY, delta);
            }
            //?}
        }
    }

    static class BrightnessSlider extends AbstractSliderButton {
        private final String translationKey;
        private final IntConsumer setter;

        BrightnessSlider(String key, int initial, IntConsumer setter) {
            super(0, 0, 200, 20, Component.empty(), initial / 100.0);
            this.translationKey = "text.config.darkness.option." + key;
            this.setter = setter;
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            int val = (int) Math.round(this.value * 100);
            this.setMessage(Component.translatable(translationKey).append(": " + val + "%"));
        }

        @Override
        protected void applyValue() {
            setter.accept((int) Math.round(this.value * 100));
        }
    }
}
