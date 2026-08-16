package yeah_zero.subtitle_highlight.Keybind;

import yeah_zero.subtitle_highlight.Configure.Manager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class KeybindManager {
    private static KeyMapping toggleSubtitles;
    private static KeyMapping increaseScale;
    private static KeyMapping decreaseScale;
    private static KeyMapping reloadConfig;
    private static KeyMapping saveConfig;
    private static KeyMapping toggleIkunEasterEgg;
    private static KeyMapping increaseOpacity;
    private static KeyMapping decreaseOpacity;

    public static void registerKeybinds(RegisterKeyMappingsEvent event) {
        KeyMapping.Category category = new KeyMapping.Category(Identifier.fromNamespaceAndPath("subtitle_highlight", "keybinds"));
        event.registerCategory(category);

        toggleSubtitles = new KeyMapping(
                "key.subtitle_highlight.toggle_subtitles",
                GLFW.GLFW_KEY_UNKNOWN,
                category
        );

        increaseScale = new KeyMapping(
                "key.subtitle_highlight.increase_scale",
                GLFW.GLFW_KEY_UNKNOWN,
                category
        );

        decreaseScale = new KeyMapping(
                "key.subtitle_highlight.decrease_scale",
                GLFW.GLFW_KEY_UNKNOWN,
                category
        );

        reloadConfig = new KeyMapping(
                "key.subtitle_highlight.reload_config",
                GLFW.GLFW_KEY_UNKNOWN,
                category
        );

        saveConfig = new KeyMapping(
                "key.subtitle_highlight.save_config",
                GLFW.GLFW_KEY_UNKNOWN,
                category
        );

        toggleIkunEasterEgg = new KeyMapping(
                "key.subtitle_highlight.toggle_ikun",
                GLFW.GLFW_KEY_UNKNOWN,
                category
        );

        increaseOpacity = new KeyMapping(
                "key.subtitle_highlight.increase_opacity",
                GLFW.GLFW_KEY_UNKNOWN,
                category
        );

        decreaseOpacity = new KeyMapping(
                "key.subtitle_highlight.decrease_opacity",
                GLFW.GLFW_KEY_UNKNOWN,
                category
        );

        event.register(toggleSubtitles);
        event.register(increaseScale);
        event.register(decreaseScale);
        event.register(reloadConfig);
        event.register(saveConfig);
        event.register(toggleIkunEasterEgg);
        event.register(increaseOpacity);
        event.register(decreaseOpacity);
    }

    public static void handleKeybinds(Minecraft client) {
        LocalPlayer player = client.player;
        if (player == null) return;

        while (toggleSubtitles.consumeClick()) {
            Options options = client.options;
            OptionInstance<Boolean> showSubtitles = options.showSubtitles();
            boolean newValue = !showSubtitles.get();
            showSubtitles.set(newValue);
            options.save();
            player.displayClientMessage(net.minecraft.network.chat.Component.literal(I18n.get(newValue ? "subtitle_highlight.keybind.toggle_subtitles.on" : "subtitle_highlight.keybind.toggle_subtitles.off")), false);
        }

        while (increaseScale.consumeClick()) {
            Manager.settings.scale = Math.min(Manager.settings.scale + 0.1f, 3.0f);
            Manager.save();
            player.displayClientMessage(net.minecraft.network.chat.Component.literal(I18n.get("subtitle_highlight.keybind.scale.changed", String.format("%.1f", Manager.settings.scale))), false);
        }

        while (decreaseScale.consumeClick()) {
            Manager.settings.scale = Math.max(Manager.settings.scale - 0.1f, 0.5f);
            Manager.save();
            player.displayClientMessage(net.minecraft.network.chat.Component.literal(I18n.get("subtitle_highlight.keybind.scale.changed", String.format("%.1f", Manager.settings.scale))), false);
        }

        while (reloadConfig.consumeClick()) {
            Manager.load();
            player.displayClientMessage(net.minecraft.network.chat.Component.literal(I18n.get("subtitle_highlight.keybind.reload.success")), false);
        }

        while (saveConfig.consumeClick()) {
            Manager.save();
            player.displayClientMessage(net.minecraft.network.chat.Component.literal(I18n.get("subtitle_highlight.keybind.save.success")), false);
        }

        while (toggleIkunEasterEgg.consumeClick()) {
            Manager.settings.ikunEasterEgg = !Manager.settings.ikunEasterEgg;
            Manager.save();
            player.displayClientMessage(net.minecraft.network.chat.Component.literal(I18n.get(Manager.settings.ikunEasterEgg ? "subtitle_highlight.keybind.ikun.on" : "subtitle_highlight.keybind.ikun.off")), false);
        }

        while (increaseOpacity.consumeClick()) {
            int alpha = (Manager.settings.backgroundColor >>> 24) & 0xFF;
            alpha = Math.min(alpha + 17, 255);
            Manager.settings.backgroundColor = (alpha << 24) | (Manager.settings.backgroundColor & 0x00FFFFFF);
            Manager.save();
            player.displayClientMessage(net.minecraft.network.chat.Component.literal(I18n.get("subtitle_highlight.keybind.opacity.changed", String.format("%.0f%%", (alpha / 255.0) * 100))), false);
        }

        while (decreaseOpacity.consumeClick()) {
            int alpha = (Manager.settings.backgroundColor >>> 24) & 0xFF;
            alpha = Math.max(alpha - 17, 0);
            Manager.settings.backgroundColor = (alpha << 24) | (Manager.settings.backgroundColor & 0x00FFFFFF);
            Manager.save();
            player.displayClientMessage(net.minecraft.network.chat.Component.literal(I18n.get("subtitle_highlight.keybind.opacity.changed", String.format("%.0f%%", (alpha / 255.0) * 100))), false);
        }
    }
}
