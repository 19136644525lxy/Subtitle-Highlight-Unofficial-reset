package yeah_zero.subtitle_highlight.Mixin;

import yeah_zero.subtitle_highlight.Configure.Manager;
import yeah_zero.subtitle_highlight.Configure.Settings;
import yeah_zero.subtitle_highlight.Data.SubtitleTypeLoader;
import yeah_zero.subtitle_highlight.Util.ColorCode;
import net.minecraft.client.gui.components.SubtitleOverlay;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SubtitleOverlay.Subtitle.class)
public class SubtitleEntryMixin {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubtitleEntryMixin.class);

    @Shadow
    @Final
    private Component text;

    @Inject(at = @At("RETURN"), method = "getText()Lnet/minecraft/network/chat/Component;", cancellable = true)
    private void colorizeSubtitle(CallbackInfoReturnable<Component> cir) {
        try {
            Component originalText = cir.getReturnValue();
            if (originalText == null) return;

            String translationKey = text.getString();
            
            for (Settings.Custom custom : Manager.settings.customList) {
                if (custom.translationKey.equals(translationKey)) {
                    MutableComponent styledText = Component.literal(originalText.getString())
                            .setStyle(originalText.getStyle()
                                    .withColor(custom.color)
                                    .withObfuscated(custom.obfuscated)
                                    .withBold(custom.bold)
                                    .withStrikethrough(custom.strikethrough)
                                    .withUnderlined(custom.underline)
                                    .withItalic(custom.italic));
                    cir.setReturnValue(styledText);
                    return;
                }
            }

            if (Manager.settings.ikunEasterEgg) {
                if (translationKey.startsWith("subtitles.entity.chicken")) {
                    String newKey = translationKey.replace("chicken", "kun");
                    cir.setReturnValue(Component.translatable(newKey));
                    return;
                }
            }

            ColorCode color = SubtitleTypeLoader.getColor(translationKey);
            if (color != null) {
                MutableComponent coloredText = Component.literal(originalText.getString())
                        .setStyle(originalText.getStyle().withColor(color.getColor()));
                cir.setReturnValue(coloredText);
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to colorize subtitle: " + e.getMessage());
        }
    }
}