package com.fibermc.essentialcommands.commands;

import com.fibermc.essentialcommands.text.ECText;
import com.fibermc.essentialcommands.text.TextFormatType;
import org.spongepowered.include.com.google.common.base.Function;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SudoCommand implements Command<ServerCommandSource> {

    public SudoCommand() {
    }

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity targetPlayer = CommandUtil.getCommandTargetPlayer(context);

        String command = StringArgumentType.getString(context, "command");
        if (source.getEntity() instanceof ServerPlayerEntity && !(source.getEntity()).hasPermissionLevel(4)) {
            if (!(source.getEntity() instanceof Function)) {
                var ecText = ECText.access(source.getPlayerOrThrow());
                source.sendError(ecText.getText("cmd.sudo.error", TextFormatType.Error));
                return -1;
            }
        }
        exec(source, targetPlayer, command);
        return 0;
    }

    public static void exec(ServerCommandSource source, ServerPlayerEntity target, String command) throws CommandSyntaxException {
        if (target == null) {
            source.sendError(Text.of("Target player not found."));
            return;
        }

        String cmd = String.join(" ", command);
        cmd = cmd.replace("${target.name}", target.getName().getString());
        cmd = cmd.replace("${source.name}", source.getName());
        ServerCommandSource cmdSource;
        if (cmd.startsWith("?")) {
            cmd = cmd.substring(1);
            cmdSource = source.getServer().getCommandSource();
        } else {
            cmdSource = target.getCommandSource();
        }
        ParseResults<ServerCommandSource> parse = source.getServer().getCommandManager().getDispatcher().parse(cmd, cmdSource);
        source.getServer().getCommandManager().execute(parse, cmd);
        source.sendFeedback(Text.of("Command executed as: " + (cmdSource == source.getServer().getCommandSource() ? "console" : target.getName() ) + ": " + cmd), false);
    }
}
