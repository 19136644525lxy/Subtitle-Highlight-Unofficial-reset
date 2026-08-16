package yeah_zero.subtitle_highlight;

import yeah_zero.subtitle_highlight.Configure.Manager;
import yeah_zero.subtitle_highlight.Data.SubtitleTypeLoader;
import yeah_zero.subtitle_highlight.Keybind.KeybindManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(modid = "subtitle_highlight")
public class Initializer {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        Manager.load();
        SubtitleTypeLoader.init();
        SubtitleHighlight.LOGGER.info("Subtitle Highlight Client initialized");
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        KeybindManager.registerKeybinds(event);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        KeybindManager.handleKeybinds(net.minecraft.client.Minecraft.getInstance());
    }
}
