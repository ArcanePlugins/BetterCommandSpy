/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy.commands.bettercommandspy.subcommands;

import me.lokka30.bettercommandspy.BetterCommandSpy;
import me.lokka30.bettercommandspy.commands.ISubcommand;
import me.lokka30.microlib.messaging.MultiMessage;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author lokka30
 * @since v2.0.0
 * <p>
 * This command contains various debug functions
 * which should only be used if a developer of BCS
 * tells a user to run it.
 * <p>
 * cmd: /bcs debug <...>
 * arg: -    0     1+
 * len: 0    1     2+
 */
public class DebugSubcommand implements ISubcommand {

    @Override
    public void parseCmd(@NotNull BetterCommandSpy main, @NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission("bettercommandspy.command.bettercommandspy.debug")) {
            new MultiMessage(main.messages.getConfig().getStringList("commands.common.no-permission"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix", "BCS:"), true),
                    new MultiMessage.Placeholder("permissions", main.messages.getConfig().getString("bettercommandspy.command.bettercommandspy.debug"), false)
            )).send(sender);
            return;
        }

        if (args.length != 2) {
            new MultiMessage(main.messages.getConfig().getStringList("commands.bettercommandspy.subcommands.debug.usage"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix", "BCS:"), true),
                    new MultiMessage.Placeholder("label", label, false)
            )).send(sender);
            return;
        }

        switch (args[1].toLowerCase(Locale.ROOT)) {
            case "example1":
                sender.sendMessage("Example 1"); // Placeholder for any future debug-methods.
                break;
            case "example2":
                sender.sendMessage("Example 2"); // Placeholder for any future debug-methods.
                break;
            default:
                new MultiMessage(main.messages.getConfig().getStringList("commands.bettercommandspy.subcommands.debug.invalid-method"), Arrays.asList(
                        new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix", "BCS:"), true),
                        new MultiMessage.Placeholder("method", args[1], false),
                        new MultiMessage.Placeholder("label", label, false)
                )).send(sender);
                break;
        }
    }

    @Override
    public @NotNull List<String> parseTabSuggestions(@NotNull BetterCommandSpy main, @NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
