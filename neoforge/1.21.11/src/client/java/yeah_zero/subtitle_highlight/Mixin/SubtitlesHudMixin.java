package yeah_zero.subtitle_highlight.Mixin;

import yeah_zero.subtitle_highlight.Configure.Manager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.SubtitleOverlay;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SubtitleOverlay.class)
public class SubtitlesHudMixin {
    @Shadow
    @Final
    private List<SubtitleOverlay.Subtitle> subtitles;

    @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/gui/GuiGraphics;)V", cancellable = true)
    private void renderSubtitles(GuiGraphics guiGraphics, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (!client.options.showSubtitles().get()) {
            ci.cancel();
            return;
        }

        if (subtitles.isEmpty()) {
            ci.cancel();
            return;
        }

        Font font = client.font;
        float scale = Manager.settings.scale;

        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();
        int bottomMargin = (int) (Manager.settings.bottomMargin * scale);
        int sideMargin = (int) (Manager.settings.sideMargin * scale);

        int y = screenHeight - bottomMargin;
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().scale(scale, scale);

        for (int i = subtitles.size() - 1; i >= 0; --i) {
            SubtitleOverlay.Subtitle subtitle = subtitles.get(i);
            
            if (!subtitle.isStillActive()) continue;

            float opacity = calculateOpacity(subtitle);
            if (opacity <= 0) continue;

            Component textComponent = subtitle.getText();
            int textWidth = font.width(textComponent);
            int x = (screenWidth / (int) scale) - textWidth - sideMargin;

            int bgColor = Manager.settings.backgroundColor;
            int bgAlpha = ((bgColor >> 24) & 0xFF);
            int adjustedBgColor = (Mth.clamp((int) (bgAlpha * opacity), 0, 255) << 24) | (bgColor & 0x00FFFFFF);

            guiGraphics.fill(x - 2, y - 2, x + textWidth + 2, y + font.lineHeight + 2, adjustedBgColor);
            guiGraphics.drawString(font, textComponent, x, y, (int) (0xFFFFFF * opacity), false);

            y -= font.lineHeight + 2;
        }

        guiGraphics.pose().popMatrix();
        ci.cancel();
    }

    private float calculateOpacity(SubtitleOverlay.Subtitle subtitle) {
        List<SubtitleOverlay.SoundPlayedAt> playedAt = subtitle.playedAt;
        if (playedAt.isEmpty()) return 0;

        long age = System.currentTimeMillis() - playedAt.getFirst().time();
        long duration = Manager.settings.maxDuration;
        double progress = (double) age / duration;
        
        float startRatio = (float) Manager.settings.startRatio;
        float endRatio = (float) Manager.settings.endRatio;
        
        if (progress <= 0.1) {
            float fadeInProgress = (float) (progress / 0.1);
            return Mth.lerp(fadeInProgress, 0.0F, startRatio);
        } else if (progress >= 0.9) {
            float fadeOutProgress = (float) ((progress - 0.9) / 0.1);
            return Mth.lerp(fadeOutProgress, startRatio, endRatio);
        } else {
            return startRatio;
        }
    }
}