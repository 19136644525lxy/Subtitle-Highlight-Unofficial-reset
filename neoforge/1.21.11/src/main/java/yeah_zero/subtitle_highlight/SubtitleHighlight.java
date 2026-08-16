package yeah_zero.subtitle_highlight;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import yeah_zero.subtitle_highlight.Configure.ScreenAPI;
import net.minecraft.client.gui.screens.Screen;

@Mod(SubtitleHighlight.MODID)
public class SubtitleHighlight {
    public static final String MODID = "subtitle_highlight";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SubtitleHighlight(IEventBus modEventBus, ModContainer container) {
        LOGGER.info("Subtitle Highlight loaded");
        container.registerExtensionPoint(IConfigScreenFactory.class, (mc, parent) -> ScreenAPI.createConfigScreen(parent));
    }
}
