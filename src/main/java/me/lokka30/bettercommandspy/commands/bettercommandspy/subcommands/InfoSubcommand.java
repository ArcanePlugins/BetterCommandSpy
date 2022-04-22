/*
 * Copyright (c) 2020-2022  lokka30.
 * This file is/was present in BetterCommandSpy's source code.
 * Learn more about BetterCommandSpy and its licensing at:
 * <https://github.com/lokka30/BetterCommandSpy>
 */

package me.lokka30.bettercommandspy.commands.bettercommandspy.subcommands;

import java.util.Arrays;
import me.lokka30.bettercommandspy.BetterCommandSpy;
import me.lokka30.bettercommandspy.commands.Subcommand;
import me.lokka30.bettercommandspy.misc.Utils;
import me.lokka30.microlib.messaging.MultiMessage;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

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
public class InfoSubcommand implements Subcommand {

    @Override
    public void run(@NotNull BetterCommandSpy main, @NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
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
                new MultiMessage.Placeholder("contributors", Utils.getFormattedArray(main, BetterCommandSpy.CONTRIBUTORS), false)
        )).send(sender);
    }

}
