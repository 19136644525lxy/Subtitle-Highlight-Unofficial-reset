package com.qituo.shur.Keybind;

import com.qituo.shur.Configure.Manager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class KeybindManager {
    // 定义快捷键
    private static KeyBinding toggleSubtitles;
    private static KeyBinding increaseScale;
    private static KeyBinding decreaseScale;
    private static KeyBinding reloadConfig;
    private static KeyBinding saveConfig;
    private static KeyBinding toggleIkunEasterEgg;
    private static KeyBinding increaseOpacity;
    private static KeyBinding decreaseOpacity;

    public static void registerKeybinds() {
        // 创建分类
        KeyBinding.Category category = new KeyBinding.Category(Identifier.of("shur", "keybinds"));

        // 注册切换字幕显示快捷键
        toggleSubtitles = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.shur.toggle_subtitles",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                category
        ));

        // 注册增加字幕缩放快捷键
        increaseScale = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.shur.increase_scale",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                category
        ));

        // 注册减小字幕缩放快捷键
        decreaseScale = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.shur.decrease_scale",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                category
        ));

        // 注册重新加载配置快捷键
        reloadConfig = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.shur.reload_config",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                category
        ));

        // 注册保存配置快捷键
        saveConfig = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.shur.save_config",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                category
        ));

        // 注册切换 iKun 彩蛋快捷键
        toggleIkunEasterEgg = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.shur.toggle_ikun",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                category
        ));

        // 注册增加背景透明度快捷键
        increaseOpacity = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.shur.increase_opacity",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                category
        ));

        // 注册减小背景透明度快捷键
        decreaseOpacity = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.shur.decrease_opacity",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                category
        ));

        // 注册快捷键事件监听
        ClientTickEvents.END_CLIENT_TICK.register(KeybindManager::handleKeybinds);
    }

    private static void handleKeybinds(MinecraftClient client) {
        if (client.player == null) return;

        // 处理切换字幕显示
        while (toggleSubtitles.wasPressed()) {
            boolean newValue = !client.options.getShowSubtitles().getValue();
            client.options.getShowSubtitles().setValue(newValue);
            client.options.write();
            client.player.sendMessage(Text.translatable(newValue ? "shur.keybind.toggle_subtitles.on" : "shur.keybind.toggle_subtitles.off"), true);
        }

        // 处理增加字幕缩放
        while (increaseScale.wasPressed()) {
            Manager.settings.scale = Math.min(Manager.settings.scale + 0.1f, 3.0f);
            Manager.save();
            client.player.sendMessage(Text.translatable("shur.keybind.scale.changed", String.format("%.1f", Manager.settings.scale)), true);
        }

        // 处理减小字幕缩放
        while (decreaseScale.wasPressed()) {
            Manager.settings.scale = Math.max(Manager.settings.scale - 0.1f, 0.5f);
            Manager.save();
            client.player.sendMessage(Text.translatable("shur.keybind.scale.changed", String.format("%.1f", Manager.settings.scale)), true);
        }

        // 处理重新加载配置
        while (reloadConfig.wasPressed()) {
            Manager.load();
            client.player.sendMessage(Text.translatable("shur.keybind.reload.success"), true);
        }

        // 处理保存配置
        while (saveConfig.wasPressed()) {
            Manager.save();
            client.player.sendMessage(Text.translatable("shur.keybind.save.success"), true);
        }

        // 处理切换 iKun 彩蛋
        while (toggleIkunEasterEgg.wasPressed()) {
            Manager.settings.ikunEasterEgg = !Manager.settings.ikunEasterEgg;
            Manager.save();
            client.player.sendMessage(Text.translatable(Manager.settings.ikunEasterEgg ? "shur.keybind.ikun.on" : "shur.keybind.ikun.off"), true);
        }

        // 处理增加背景透明度
        while (increaseOpacity.wasPressed()) {
            int alpha = (Manager.settings.backgroundColor >>> 24) & 0xFF;
            alpha = Math.min(alpha + 17, 255); // 每次增加约 10% 透明度
            Manager.settings.backgroundColor = (alpha << 24) | (Manager.settings.backgroundColor & 0x00FFFFFF);
            Manager.save();
            client.player.sendMessage(Text.translatable("shur.keybind.opacity.changed", String.format("%.0f%%", (alpha / 255.0) * 100)), true);
        }

        // 处理减小背景透明度
        while (decreaseOpacity.wasPressed()) {
            int alpha = (Manager.settings.backgroundColor >>> 24) & 0xFF;
            alpha = Math.max(alpha - 17, 0); // 每次减小约 10% 透明度
            Manager.settings.backgroundColor = (alpha << 24) | (Manager.settings.backgroundColor & 0x00FFFFFF);
            Manager.save();
            client.player.sendMessage(Text.translatable("shur.keybind.opacity.changed", String.format("%.0f%%", (alpha / 255.0) * 100)), true);
        }
    }
}