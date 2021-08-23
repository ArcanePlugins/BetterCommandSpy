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
import java.util.Collections;
import java.util.List;

/**
 * @author lokka30
 * @since v2.0.0
 * <p>
 * This command reloads the plugin's configs.
 * <p>
 * cmd: /bcs reload
 * arg: -    0
 * len: 0    1
 */
public class ReloadSubcommand implements ISubcommand {

    /*
    TODO
        - Test the subcommand.
     */

    @Override
    public void parseCmd(@NotNull BetterCommandSpy main, @NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission("bettercommandspy.command.bettercommandspy.reload")) {
            new MultiMessage(main.messages.getConfig().getStringList("commands.common.no-permission"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix"), true),
                    new MultiMessage.Placeholder("permissions", main.messages.getConfig().getString("bettercommandspy.command.bettercommandspy.reload"), false)
            )).send(sender);
            return;
        }

        if (args.length != 1) {
            new MultiMessage(main.messages.getConfig().getStringList("commands.bettercommandspy.subcommands.reload.usage"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix"), true),
                    new MultiMessage.Placeholder("label", label, false)
            )).send(sender);
            return;
        }

        new MultiMessage(main.messages.getConfig().getStringList("commands.bettercommandspy.subcommands.reload.start"), Collections.singletonList(
                new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix"), true)
        )).send(sender);

        main.loadFiles();

        if (main.settings.getConfig().getBoolean("compatibility-checker.run-on-startup", true)) {
            main.compatibilityCheckerHandler.scan();
        }

        main.updateCheckerHandler.init(true);

        new MultiMessage(main.messages.getConfig().getStringList("commands.bettercommandspy.subcommands.reload.finish"), Collections.singletonList(
                new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix"), true)
        )).send(sender);
    }

    @Override
    public List<String> parseTabSuggestions(@NotNull BetterCommandSpy main, @NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
