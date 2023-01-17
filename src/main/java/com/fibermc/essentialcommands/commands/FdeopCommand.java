package com.fibermc.essentialcommands.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class FdeopCommand implements Command<ServerCommandSource> {
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity targetPlayer = CommandUtil.getCommandTargetPlayer(context);

        if (targetPlayer == null) {
            source.sendError(Text.literal("Target player not found."));
            return 0;
        }
        var sourceName = source.getName();
        var targetName = targetPlayer.getName();

        targetPlayer.sendMessage(Text.literal("[").formatted(Formatting.GRAY, Formatting.ITALIC)
            .append(sourceName).formatted(Formatting.GRAY, Formatting.ITALIC)
            .append(": Made ").formatted(Formatting.GRAY, Formatting.ITALIC)
            .append(targetName).formatted(Formatting.GRAY, Formatting.ITALIC)
            .append(" no longer a server operator]").formatted(Formatting.GRAY, Formatting.ITALIC), false);
        return 0;
    }
}
