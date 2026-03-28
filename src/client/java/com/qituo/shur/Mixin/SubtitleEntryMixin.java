package com.qituo.shur.Mixin;

import com.qituo.shur.Configure.Manager;
import com.qituo.shur.Configure.Settings;
import com.qituo.shur.Util.SplitKeyArrays;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.SubtitlesHud;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(targets = "net.minecraft.client.gui.hud.SubtitlesHud$SubtitleEntry")
public class SubtitleEntryMixin {
    @Shadow
    @Final
    private Text text;

    @Inject(at = @At("RETURN"), method = "getText()Lnet/minecraft/text/Text;", cancellable = true)
    private void colorizeSubtitle(CallbackInfoReturnable<Text> cir) {
        MutableText subtitleText = ((MutableText) this.text).formatted(Formatting.RESET);
        if (subtitleText.getContent() instanceof TranslatableTextContent) {
            for (Settings.Custom custom : Manager.settings.customList) {
                if (((TranslatableTextContent) subtitleText.getContent()).getKey().equals(custom.translationKey)) {
                    cir.setReturnValue(subtitleText.setStyle(subtitleText.getStyle().withColor(custom.color).withObfuscated(custom.obfuscated).withBold(custom.bold).withStrikethrough(custom.strikethrough).withUnderline(custom.underline).withItalic(custom.italic)));
                    return;
                }
            }
            String[] keyParts = ((TranslatableTextContent) subtitleText.getContent()).getKey().split("\\.");
            if (keyParts[0].equals("subtitles")) {
                switch (keyParts[1]) {
                    case "ambient", "weather" -> {
                        cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.ambient.getFormatting()));
                        return;
                    }
                    case "block" -> {
                        if (keyParts[2].equals("generic")) {
                            cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.block.generic.getFormatting()));
                            return;
                        }
                        for (String element : SplitKeyArrays.interact) {
                            if (keyParts[2].equals(element)) {
                                if ((keyParts[2].equals("anvil") && keyParts[3].equals("land")) || (keyParts[2].equals("tripwire") && keyParts[3].equals("click"))) {
                                    cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.block.dangerous.getFormatting()));
                                    return;
                                }
                                cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.block.interact.getFormatting()));
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.working) {
                            if (keyParts[2].equals(element)) {
                                if ((keyParts[2].equals("beacon") && keyParts[3].equals("power_select")) || (keyParts[2].equals("beehive") && keyParts[3].equals("shear"))) {
                                    cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.block.interact.getFormatting()));
                                    return;
                                }
                                if (keyParts[2].equals("pointed_dripstone") && (keyParts[3].startsWith("drip_lava") || keyParts[3].equals("land"))) {
                                    cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.block.dangerous.getFormatting()));
                                    return;
                                }
                                cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.block.working.getFormatting()));
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.dangerousBlocks) {
                            if (keyParts[2].equals(element)) {
                                cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.block.dangerous.getFormatting()));
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.crops) {
                            if (keyParts[2].equals(element)) {
                                cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.block.crop.getFormatting()));
                                return;
                            }
                        }
                        cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.block.other.getFormatting()));
                        return;
                    }
                    case "chiseled_bookshelf", "ui" -> {
                        cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.block.interact.getFormatting()));
                        return;
                    }
                    case "enchant", "particle" -> {
                        cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.enchant.getFormatting()));
                        return;
                    }
                    case "entity" -> {
                        if (keyParts[2].equals("generic") || keyParts[2].equals("player")) {
                            if (keyParts[3].equals("attack")) {
                                cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.mob.player.attack.getFormatting()));
                                return;
                            }
                            for (String element : SplitKeyArrays.hurt) {
                                if (keyParts[3].equals(element)) {
                                    cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.mob.player.hurt.getFormatting()));
                                    return;
                                }
                            }
                            cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.mob.player.other.getFormatting()));
                            return;
                        }
                        for (String element : SplitKeyArrays.friendlyMobs) {
                            if (keyParts[2].equals(element)) {
                                if (keyParts[2].equals("chicken") && Manager.settings.ikunEasterEgg) {
                                    cir.setReturnValue(Text.translatable("subtitles.entity.kun." + keyParts[3]).setStyle(subtitleText.getStyle().withColor(TextColor.fromFormatting(Formatting.GRAY)).withBold(true)));
                                    return;
                                }
                                cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.mob.passive.getFormatting()));
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.neutralMobs) {
                            if (keyParts[2].equals(element)) {
                                cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.mob.neutral.getFormatting()));
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.hostileMobs) {
                            if (keyParts[2].equals(element)) {
                                cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.mob.hostile.getFormatting()));
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.bossMobs) {
                            if (keyParts[2].equals(element)) {
                                cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.mob.boss.getFormatting()));
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.vehicles) {
                            if (keyParts[2].equals(element)) {
                                cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.vehicle.getFormatting()));
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.projectiles) {
                            if (keyParts[2].equals(element)) {
                                cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.projectile.getFormatting()));
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.explosives) {
                            if (keyParts[2].equals(element)) {
                                cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.explosive.getFormatting()));
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.decorations) {
                            if (keyParts[2].equals(element)) {
                                cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.decoration.getFormatting()));
                                return;
                            }
                        }
                        cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.other.getFormatting()));
                        return;
                    }
                    case "event" -> {
                        cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.mob.hostile.getFormatting()));
                        return;
                    }
                    case "item" -> {
                        for (String element : SplitKeyArrays.weapons) {
                            if (keyParts[2].equals(element)) {
                                cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.item.weapon.getFormatting()));
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.armors) {
                            if (keyParts[2].equals(element)) {
                                cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.item.armor.getFormatting()));
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.tools) {
                            if (keyParts[2].equals(element)) {
                                cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.item.tool.getFormatting()));
                                return;
                            }
                        }
                        cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.item.other.getFormatting()));
                        return;
                    }
                }
            }
        }
        cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.other.getFormatting()));
    }
}