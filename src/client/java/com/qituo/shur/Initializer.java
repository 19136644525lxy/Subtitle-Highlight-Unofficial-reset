package com.qituo.shur;

import com.qituo.shur.Command.CommandManager;
import com.qituo.shur.Configure.Manager;
import com.qituo.shur.Data.SubtitleTypeLoader;
import com.qituo.shur.Keybind.KeybindManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public class Initializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // 加载配置
        Manager.load();
        
        // 注册客户端命令
        ClientCommandRegistrationCallback.EVENT.register(CommandManager::registerCommands);
        
        // 注册数据加载器
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SubtitleTypeLoader());
        
        // 注册快捷键
        KeybindManager.registerKeybinds();
    }
}