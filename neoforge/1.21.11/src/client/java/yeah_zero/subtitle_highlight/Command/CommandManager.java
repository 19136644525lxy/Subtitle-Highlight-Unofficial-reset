package yeah_zero.subtitle_highlight.Command;

import yeah_zero.subtitle_highlight.Configure.Manager;
import yeah_zero.subtitle_highlight.Configure.Settings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

@EventBusSubscriber(modid = "subtitle_highlight", value = Dist.CLIENT)
public class CommandManager {
    @SubscribeEvent
    public static void registerCommands(RegisterClientCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("subtitle_highlight")
            .then(Commands.literal("reload")
                .executes(CommandManager::reloadConfig)
            )
            .then(Commands.literal("save")
                .executes(CommandManager::saveConfig)
            )
            .then(Commands.literal("maxDuration")
                .then(Commands.argument("value", LongArgumentType.longArg(0))
                    .executes(CommandManager::setMaximumDuration)
                )
                .executes(CommandManager::getMaximumDuration)
            )
            .then(Commands.literal("scale")
                .then(Commands.argument("value", FloatArgumentType.floatArg(0.1f, 5.0f))
                    .executes(CommandManager::setScale)
                )
                .executes(CommandManager::getScale)
            )
            .then(Commands.literal("bottomMargin")
                .then(Commands.argument("value", FloatArgumentType.floatArg(0))
                    .executes(CommandManager::setBottomMargin)
                )
                .executes(CommandManager::getBottomMargin)
            )
            .then(Commands.literal("sideMargin")
                .then(Commands.argument("value", FloatArgumentType.floatArg(0))
                    .executes(CommandManager::setSideMargin)
                )
                .executes(CommandManager::getSideMargin)
            )
            .then(Commands.literal("backgroundColor")
                .then(Commands.argument("value", IntegerArgumentType.integer(0, 0xffffffff))
                    .executes(CommandManager::setBackgroundColor)
                )
                .executes(CommandManager::getBackgroundColor)
            )
            .then(Commands.literal("startRatio")
                .then(Commands.argument("value", FloatArgumentType.floatArg(0, 1))
                    .executes(CommandManager::setStartRatio)
                )
                .executes(CommandManager::getStartRatio)
            )
            .then(Commands.literal("endRatio")
                .then(Commands.argument("value", FloatArgumentType.floatArg(0, 1))
                    .executes(CommandManager::setEndRatio)
                )
                .executes(CommandManager::getEndRatio)
            )
            .then(Commands.literal("ikunEasterEgg")
                .then(Commands.argument("value", StringArgumentType.string())
                    .executes(CommandManager::setIkuneasterEgg)
                )
                .executes(CommandManager::getIkuneasterEgg)
            )
            .executes(CommandManager::showHelp)
        );
    }

    private static int reloadConfig(CommandContext<CommandSourceStack> context) {
        Manager.load();
        context.getSource().sendSystemMessage(Component.literal("§aSubtitle Highlight: Configuration reloaded"));
        return 1;
    }

    private static int saveConfig(CommandContext<CommandSourceStack> context) {
        Manager.save();
        context.getSource().sendSystemMessage(Component.literal("§aSubtitle Highlight: Configuration saved"));
        return 1;
    }

    private static int setMaximumDuration(CommandContext<CommandSourceStack> context) {
        long value = LongArgumentType.getLong(context, "value");
        Manager.settings.maxDuration = value;
        context.getSource().sendSystemMessage(Component.literal("§aSubtitle Highlight: Maximum duration set to " + value + "ms"));
        return 1;
    }

    private static int getMaximumDuration(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSystemMessage(Component.literal("§aSubtitle Highlight: Maximum duration: " + Manager.settings.maxDuration + "ms"));
        return 1;
    }

    private static int setScale(CommandContext<CommandSourceStack> context) {
        float value = FloatArgumentType.getFloat(context, "value");
        Manager.settings.scale = value;
        context.getSource().sendSystemMessage(Component.literal("§aSubtitle Highlight: Scale set to " + value));
        return 1;
    }

    private static int getScale(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSystemMessage(Component.literal("§aSubtitle Highlight: Scale: " + Manager.settings.scale));
        return 1;
    }

    private static int setBottomMargin(CommandContext<CommandSourceStack> context) {
        float value = FloatArgumentType.getFloat(context, "value");
        Manager.settings.bottomMargin = value;
        context.getSource().sendSystemMessage(Component.literal("§aSubtitle Highlight: Bottom margin set to " + value));
        return 1;
    }

    private static int getBottomMargin(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSystemMessage(Component.literal("§aSubtitle Highlight: Bottom margin: " + Manager.settings.bottomMargin));
        return 1;
    }

    private static int setSideMargin(CommandContext<CommandSourceStack> context) {
        float value = FloatArgumentType.getFloat(context, "value");
        Manager.settings.sideMargin = value;
        context.getSource().sendSystemMessage(Component.literal("§aSubtitle Highlight: Side margin set to " + value));
        return 1;
    }

    private static int getSideMargin(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSystemMessage(Component.literal("§aSubtitle Highlight: Side margin: " + Manager.settings.sideMargin));
        return 1;
    }

    private static int setBackgroundColor(CommandContext<CommandSourceStack> context) {
        int value = IntegerArgumentType.getInteger(context, "value");
        Manager.settings.backgroundColor = value;
        context.getSource().sendSystemMessage(Component.literal("§aSubtitle Highlight: Background color set to " + Integer.toHexString(value)));
        return 1;
    }

    private static int getBackgroundColor(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSystemMessage(Component.literal("§aSubtitle Highlight: Background color: " + Integer.toHexString(Manager.settings.backgroundColor)));
        return 1;
    }

    private static int setStartRatio(CommandContext<CommandSourceStack> context) {
        float value = FloatArgumentType.getFloat(context, "value");
        Manager.settings.startRatio = value;
        context.getSource().sendSystemMessage(Component.literal("§aSubtitle Highlight: Start opacity ratio set to " + value));
        return 1;
    }

    private static int getStartRatio(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSystemMessage(Component.literal("§aSubtitle Highlight: Start opacity ratio: " + Manager.settings.startRatio));
        return 1;
    }

    private static int setEndRatio(CommandContext<CommandSourceStack> context) {
        float value = FloatArgumentType.getFloat(context, "value");
        Manager.settings.endRatio = value;
        context.getSource().sendSystemMessage(Component.literal("§aSubtitle Highlight: End opacity ratio set to " + value));
        return 1;
    }

    private static int getEndRatio(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSystemMessage(Component.literal("§aSubtitle Highlight: End opacity ratio: " + Manager.settings.endRatio));
        return 1;
    }

    private static int setIkuneasterEgg(CommandContext<CommandSourceStack> context) {
        String value = StringArgumentType.getString(context, "value");
        boolean boolValue = value.equalsIgnoreCase("true") || value.equalsIgnoreCase("on") || value.equalsIgnoreCase("yes");
        Manager.settings.ikunEasterEgg = boolValue;
        String status = boolValue ? "enabled" : "disabled";
        context.getSource().sendSystemMessage(Component.literal("§aSubtitle Highlight: iKun Easter Egg " + status));
        return 1;
    }

    private static int getIkuneasterEgg(CommandContext<CommandSourceStack> context) {
        String status = Manager.settings.ikunEasterEgg ? "enabled" : "disabled";
        context.getSource().sendSystemMessage(Component.literal("§aSubtitle Highlight: iKun Easter Egg: " + status));
        return 1;
    }

    private static int showHelp(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSystemMessage(Component.literal("§aSubtitle Highlight Command Help:"));
        context.getSource().sendSystemMessage(Component.literal("§7/subtitle_highlight reload - Reload configuration"));
        context.getSource().sendSystemMessage(Component.literal("§7/subtitle_highlight save - Save configuration"));
        context.getSource().sendSystemMessage(Component.literal("§7/subtitle_highlight maxDuration [value] - Set or get maximum duration"));
        context.getSource().sendSystemMessage(Component.literal("§7/subtitle_highlight scale [value] - Set or get scale"));
        context.getSource().sendSystemMessage(Component.literal("§7/subtitle_highlight bottomMargin [value] - Set or get bottom margin"));
        context.getSource().sendSystemMessage(Component.literal("§7/subtitle_highlight sideMargin [value] - Set or get side margin"));
        context.getSource().sendSystemMessage(Component.literal("§7/subtitle_highlight backgroundColor [value] - Set or get background color"));
        context.getSource().sendSystemMessage(Component.literal("§7/subtitle_highlight startRatio [value] - Set or get start opacity ratio"));
        context.getSource().sendSystemMessage(Component.literal("§7/subtitle_highlight endRatio [value] - Set or get end opacity ratio"));
        context.getSource().sendSystemMessage(Component.literal("§7/subtitle_highlight ikunEasterEgg [true/false] - Set or get iKun Easter Egg"));
        return 1;
    }
}
