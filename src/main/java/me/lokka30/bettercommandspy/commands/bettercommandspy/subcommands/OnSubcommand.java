/*
 * Copyright (c) 2020-2022  lokka30.
 * This file is/was present in BetterCommandSpy's source code.
 * Learn more about BetterCommandSpy and its licensing at:
 * <https://github.com/lokka30/BetterCommandSpy>
 */

package me.lokka30.bettercommandspy.commands.bettercommandspy.subcommands;

import me.lokka30.bettercommandspy.BetterCommandSpy;
import me.lokka30.bettercommandspy.commands.Subcommand;
import me.lokka30.bettercommandspy.handlers.UserHandler;
import me.lokka30.bettercommandspy.misc.Utils;
import me.lokka30.microlib.messaging.MultiMessage;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author lokka30
 * @since v2.0.0
 * <p>
 * This command enables the sender or target player's
 * command spy status.
 * <p>
 * cmd: /bcs on [player]
 * arg: -    0   1
 * len: 0    1   2
 */
public class OnSubcommand implements Subcommand {

    @Override
    public void run(@NotNull BetterCommandSpy main, @NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission("bettercommandspy.command.bettercommandspy.toggle")) {
            new MultiMessage(main.messages.getConfig().getStringList("commands.common.no-permission"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix", "BCS:"), true),
                    new MultiMessage.Placeholder("permission", "bettercommandspy.command.bettercommandspy.toggle", false)
            )).send(sender);
            return;
        }

        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                new MultiMessage(main.messages.getConfig().getStringList("commands.bettercommandspy.subcommands.toggle-on.usage-console"), Arrays.asList(
                        new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix", "BCS:"), true),
                        new MultiMessage.Placeholder("label", label, false)
                )).send(sender);
                return;
            }

            final UUID uuid = ((Player) sender).getUniqueId();

            if (main.userHandler.getStatus(uuid)) {
                new MultiMessage(main.messages.getConfig().getStringList("commands.bettercommandspy.subcommands.toggle-on.self-already-enabled"), Collections.singletonList(
                        new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix", "BCS:"), true)
                )).send(sender);
            } else {
                main.userHandler.setStatus(uuid, true, UserHandler.ChangedStatusCause.COMMAND);
                new MultiMessage(main.messages.getConfig().getStringList("commands.bettercommandspy.subcommands.toggle-on.self-success"), Collections.singletonList(
                        new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix", "BCS:"), true)
                )).send(sender);
            }
        } else if (args.length == 2) {
            if (!sender.hasPermission("bettercommandspy.command.bettercommandspy.toggle.others")) {
                // requires '.others' permission.
                new MultiMessage(main.messages.getConfig().getStringList("commands.common.no-permission"), Arrays.asList(
                        new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix", "BCS:"), true),
                        new MultiMessage.Placeholder("permissions", main.messages.getConfig().getString("bettercommandspy.command.bettercommandspy.toggle.others"), false)
                )).send(sender);
                return;
            }

            final OfflinePlayer target = Utils.getOfflinePlayer(args[1]);

            if (!target.hasPlayedBefore() && !target.isOnline()) {
                new MultiMessage(main.messages.getConfig().getStringList("commands.common.specified-player-never-joined"), Arrays.asList(
                        new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix", "BCS:"), true),
                        new MultiMessage.Placeholder("username", args[1], false)
                )).send(sender);
                return;
            }

            final String username = target.getName();
            final UUID uuid = target.getUniqueId();

            if (main.userHandler.getStatus(uuid)) {
                new MultiMessage(main.messages.getConfig().getStringList("commands.bettercommandspy.subcommands.toggle-on.target-already-enabled"), Arrays.asList(
                        new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix", "BCS:"), true),
                        new MultiMessage.Placeholder("username", username, false)
                )).send(sender);
                return;
            }

            main.userHandler.setStatus(uuid, true, UserHandler.ChangedStatusCause.COMMAND);

            new MultiMessage(main.messages.getConfig().getStringList("commands.bettercommandspy.subcommands.toggle-on.target-success"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix", "BCS:"), true),
                    new MultiMessage.Placeholder("username", username, false)
            )).send(sender);
        } else {
            // send incorrect usage message.

            new MultiMessage(main.messages.getConfig().getStringList("commands.bettercommandspy.subcommands.toggle-on.usage"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix", "BCS:"), true),
                    new MultiMessage.Placeholder("label", label, false)
            )).send(sender);
        }
    }

    @Override
    public @NotNull List<String> getSuggestions(@NotNull BetterCommandSpy main, @NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 2) {
            return new ArrayList<>(Utils.getVisibleOnlinePlayerUsernamesList(sender));
        } else {
            return Collections.emptyList();
        }
    }
}
