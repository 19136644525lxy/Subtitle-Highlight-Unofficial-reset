package yeah_zero.subtitle_highlight.api;

import yeah_zero.subtitle_highlight.Configure.Settings;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class SubtitleAPI {
    private static final List<SubtitleProcessor> processors = new ArrayList<>();

    public static void registerProcessor(SubtitleProcessor processor) {
        processors.add(processor);
    }

    public static Component processSubtitle(Component text, Settings settings) {
        Component processedText = text;
        for (SubtitleProcessor processor : processors) {
            processedText = processor.process(processedText, settings);
        }
        return processedText;
    }

    public interface SubtitleProcessor {
        Component process(Component text, Settings settings);
    }
}
