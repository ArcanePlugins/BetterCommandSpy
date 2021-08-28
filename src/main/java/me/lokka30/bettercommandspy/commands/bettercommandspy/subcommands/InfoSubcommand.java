/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy.commands.bettercommandspy.subcommands;

import me.lokka30.bettercommandspy.BetterCommandSpy;
import me.lokka30.bettercommandspy.commands.ISubcommand;
import me.lokka30.bettercommandspy.misc.Utils;
import me.lokka30.microlib.messaging.MultiMessage;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lokka30
 * @since v2.0.0
 * <p>
 * This command prints information about the
 * installed version of the plugin.
 * <p>
 * cmd: /bcs info
 * arg: -    0
 * len: 0    1
 */
public class InfoSubcommand implements ISubcommand {

    @Override
    public void parseCmd(@NotNull BetterCommandSpy main, @NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission("bettercommandspy.command.bettercommandspy.info")) {
            new MultiMessage(main.messages.getConfig().getStringList("commands.common.no-permission"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix", "BCS:"), true),
                    new MultiMessage.Placeholder("permission", "bettercommandspy.command.bettercommandspy.info", false)
            )).send(sender);
            return;
        }

        if (args.length != 1) {
            new MultiMessage(main.messages.getConfig().getStringList("commands.bettercommandspy.subcommands.info.usage"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix", "BCS:"), true),
                    new MultiMessage.Placeholder("label", label, false)
            )).send(sender);
            return;
        }

        new MultiMessage(main.messages.getConfig().getStringList("commands.bettercommandspy.subcommands.info.print"), Arrays.asList(
                new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix", "BCS:"), true),
                new MultiMessage.Placeholder("version", main.getDescription().getVersion(), false),
                new MultiMessage.Placeholder("authors", Utils.getFormattedList(main, main.getDescription().getAuthors()), false),
                new MultiMessage.Placeholder("contributors", Utils.getFormattedList(main, main.CONTRIBUTORS), false)
        )).send(sender);
    }

    @Override
    public @NotNull List<String> parseTabSuggestions(@NotNull BetterCommandSpy main, @NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
