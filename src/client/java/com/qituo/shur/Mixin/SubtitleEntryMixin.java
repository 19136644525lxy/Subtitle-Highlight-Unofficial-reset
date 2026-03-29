package com.qituo.shur.Mixin;

import com.qituo.shur.Configure.Manager;
import com.qituo.shur.Configure.Settings;
import com.qituo.shur.Data.SubtitleTypeLoader;
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
                        com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("", "ambient");
                        if (colorCode != null) {
                            cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                        } else {
                            cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.ambient.getFormatting()));
                        }
                        return;
                    }
                    case "block" -> {
                        if (keyParts[2].equals("generic")) {
                            com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("block", "generic");
                            if (colorCode != null) {
                                cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                            } else {
                                cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.block.generic.getFormatting()));
                            }
                            return;
                        }
                        for (String element : SplitKeyArrays.interact) {
                            if (keyParts[2].equals(element)) {
                                if ((keyParts[2].equals("anvil") && keyParts[3].equals("land")) || (keyParts[2].equals("tripwire") && keyParts[3].equals("click"))) {
                                    com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("block", "dangerous");
                                    if (colorCode != null) {
                                        cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                                    } else {
                                        cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.block.dangerous.getFormatting()));
                                    }
                                    return;
                                }
                                com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("block", "interact");
                                if (colorCode != null) {
                                    cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                                } else {
                                    cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.block.interact.getFormatting()));
                                }
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.working) {
                            if (keyParts[2].equals(element)) {
                                if ((keyParts[2].equals("beacon") && keyParts[3].equals("power_select")) || (keyParts[2].equals("beehive") && keyParts[3].equals("shear"))) {
                                    com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("block", "interact");
                                    if (colorCode != null) {
                                        cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                                    } else {
                                        cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.block.interact.getFormatting()));
                                    }
                                    return;
                                }
                                if (keyParts[2].equals("pointed_dripstone") && (keyParts[3].startsWith("drip_lava") || keyParts[3].equals("land"))) {
                                    com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("block", "dangerous");
                                    if (colorCode != null) {
                                        cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                                    } else {
                                        cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.block.dangerous.getFormatting()));
                                    }
                                    return;
                                }
                                com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("block", "working");
                                if (colorCode != null) {
                                    cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                                } else {
                                    cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.block.working.getFormatting()));
                                }
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.dangerousBlocks) {
                            if (keyParts[2].equals(element)) {
                                com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("block", "dangerous");
                                if (colorCode != null) {
                                    cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                                } else {
                                    cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.block.dangerous.getFormatting()));
                                }
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.crops) {
                            if (keyParts[2].equals(element)) {
                                com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("block", "crop");
                                if (colorCode != null) {
                                    cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                                } else {
                                    cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.block.crop.getFormatting()));
                                }
                                return;
                            }
                        }
                        com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("block", "other");
                        if (colorCode != null) {
                            cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                        } else {
                            cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.block.other.getFormatting()));
                        }
                        return;
                    }
                    case "chiseled_bookshelf", "ui" -> {
                        cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.block.interact.getFormatting()));
                        return;
                    }
                    case "enchant", "particle" -> {
                        com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("", "enchant");
                        if (colorCode != null) {
                            cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                        } else {
                            cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.enchant.getFormatting()));
                        }
                        return;
                    }
                    case "entity" -> {
                        if (keyParts[2].equals("generic") || keyParts[2].equals("player")) {
                            if (keyParts[3].equals("attack")) {
                                com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("entity.mob.player", "attack");
                                if (colorCode != null) {
                                    cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                                } else {
                                    cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.mob.player.attack.getFormatting()));
                                }
                                return;
                            }
                            for (String element : SplitKeyArrays.hurt) {
                                if (keyParts[3].equals(element)) {
                                    com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("entity.mob.player", "hurt");
                                    if (colorCode != null) {
                                        cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                                    } else {
                                        cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.mob.player.hurt.getFormatting()));
                                    }
                                    return;
                                }
                            }
                            com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("entity.mob.player", "other");
                            if (colorCode != null) {
                                cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                            } else {
                                cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.mob.player.other.getFormatting()));
                            }
                            return;
                        }
                        for (String element : SplitKeyArrays.friendlyMobs) {
                            if (keyParts[2].equals(element)) {
                                if (keyParts[2].equals("chicken") && Manager.settings.ikunEasterEgg) {
                                    cir.setReturnValue(Text.translatable("subtitles.entity.kun." + keyParts[3]).setStyle(subtitleText.getStyle().withColor(TextColor.fromFormatting(Formatting.GRAY)).withBold(true)));
                                    return;
                                }
                                com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("entity.mob", "passive");
                                if (colorCode != null) {
                                    cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                                } else {
                                    cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.mob.passive.getFormatting()));
                                }
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.neutralMobs) {
                            if (keyParts[2].equals(element)) {
                                com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("entity.mob", "neutral");
                                if (colorCode != null) {
                                    cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                                } else {
                                    cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.mob.neutral.getFormatting()));
                                }
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.hostileMobs) {
                            if (keyParts[2].equals(element)) {
                                com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("entity.mob", "hostile");
                                if (colorCode != null) {
                                    cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                                } else {
                                    cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.mob.hostile.getFormatting()));
                                }
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.bossMobs) {
                            if (keyParts[2].equals(element)) {
                                com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("entity.mob", "boss");
                                if (colorCode != null) {
                                    cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                                } else {
                                    cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.mob.boss.getFormatting()));
                                }
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.vehicles) {
                            if (keyParts[2].equals(element)) {
                                com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("entity", "vehicle");
                                if (colorCode != null) {
                                    cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                                } else {
                                    cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.vehicle.getFormatting()));
                                }
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.projectiles) {
                            if (keyParts[2].equals(element)) {
                                com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("entity", "projectile");
                                if (colorCode != null) {
                                    cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                                } else {
                                    cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.projectile.getFormatting()));
                                }
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.explosives) {
                            if (keyParts[2].equals(element)) {
                                com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("entity", "explosive");
                                if (colorCode != null) {
                                    cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                                } else {
                                    cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.explosive.getFormatting()));
                                }
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.decorations) {
                            if (keyParts[2].equals(element)) {
                                com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("entity", "decoration");
                                if (colorCode != null) {
                                    cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                                } else {
                                    cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.decoration.getFormatting()));
                                }
                                return;
                            }
                        }
                        com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("entity", "other");
                        if (colorCode != null) {
                            cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                        } else {
                            cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.other.getFormatting()));
                        }
                        return;
                    }
                    case "event" -> {
                        com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("entity.mob", "hostile");
                        if (colorCode != null) {
                            cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                        } else {
                            cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.entity.mob.hostile.getFormatting()));
                        }
                        return;
                    }
                    case "item" -> {
                        for (String element : SplitKeyArrays.weapons) {
                            if (keyParts[2].equals(element)) {
                                com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("item", "weapon");
                                if (colorCode != null) {
                                    cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                                } else {
                                    cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.item.weapon.getFormatting()));
                                }
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.armors) {
                            if (keyParts[2].equals(element)) {
                                com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("item", "armor");
                                if (colorCode != null) {
                                    cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                                } else {
                                    cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.item.armor.getFormatting()));
                                }
                                return;
                            }
                        }
                        for (String element : SplitKeyArrays.tools) {
                            if (keyParts[2].equals(element)) {
                                com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("item", "tool");
                                if (colorCode != null) {
                                    cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                                } else {
                                    cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.item.tool.getFormatting()));
                                }
                                return;
                            }
                        }
                        com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("item", "other");
                        if (colorCode != null) {
                            cir.setReturnValue(subtitleText.formatted(colorCode.getFormatting()));
                        } else {
                            cir.setReturnValue(subtitleText.formatted(Manager.settings.colorSettings.item.other.getFormatting()));
                        }
                        return;
                    }
                }
            }
        }
        // 使用API处理字幕
        Text processedText = com.qituo.shur.api.SubtitleAPI.processSubtitle(subtitleText, Manager.settings);
        
        // 应用其他类型的颜色
        com.qituo.shur.Util.ColorCode colorCode = SubtitleTypeLoader.getColor("", "other");
        if (colorCode != null) {
            processedText = ((MutableText) processedText).formatted(colorCode.getFormatting());
        } else {
            processedText = ((MutableText) processedText).formatted(Manager.settings.colorSettings.other.getFormatting());
        }
        
        cir.setReturnValue(processedText);
    }
}