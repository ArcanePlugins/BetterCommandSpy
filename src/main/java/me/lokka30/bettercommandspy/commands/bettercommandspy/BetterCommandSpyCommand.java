/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy.commands.bettercommandspy;

import me.lokka30.bettercommandspy.BetterCommandSpy;
import me.lokka30.bettercommandspy.commands.bettercommandspy.subcommands.*;
import me.lokka30.microlib.messaging.MultiMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author lokka30
 * @since v2.0.0
 * <p>
 * This class handles the execution of the plugin's command '/bettercommandspy'.
 */
public class BetterCommandSpyCommand implements TabExecutor {

    private final @NotNull BetterCommandSpy main;

    public BetterCommandSpyCommand(@NotNull final BetterCommandSpy main) {
        this.main = main;
    }

    @NotNull final DebugSubcommand debugSubcommand = new DebugSubcommand();
    @NotNull final InfoSubcommand infoSubcommand = new InfoSubcommand();
    @NotNull final OffSubcommand offSubcommand = new OffSubcommand();
    @NotNull final OnSubcommand onSubcommand = new OnSubcommand();
    @NotNull final ReloadSubcommand reloadSubcommand = new ReloadSubcommand();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {

        if (args.length == 0) {
            sendUsageBanner(sender, label);
        } else {
            switch (args[0].toLowerCase(Locale.ROOT)) {
                case "on":
                    onSubcommand.run(main, sender, label, args);
                    break;
                case "off":
                    offSubcommand.run(main, sender, label, args);
                    break;
                case "reload":
                    reloadSubcommand.run(main, sender, label, args);
                    break;
                case "info":
                    infoSubcommand.run(main, sender, label, args);
                    break;
                case "debug":
                    debugSubcommand.run(main, sender, label, args);
                    break;
                default:
                    sendUsageBanner(sender, label);
                    break;
            }
        }

        return true;
    }

    void sendUsageBanner(@NotNull CommandSender sender, @NotNull String label) {
        new MultiMessage(main.messages.getConfig().getStringList("commands.bettercommandspy.usage"), Arrays.asList(
                new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix", "BCS:"), true),
                new MultiMessage.Placeholder("label", label, false)
        )).send(sender);
    }

    private static final List<String> SUBCOMMANDS_LIST = Arrays.asList("compatibility", "debug", "info", "off", "on", "reload");
    @NotNull
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            // Keep in alphabetical order
            return SUBCOMMANDS_LIST;
        }

        switch (args[0].toLowerCase(Locale.ROOT)) {
            // Keep in alphabetical order
            case "debug":
                return debugSubcommand.getSuggestions(main, sender, label, args);
            case "info":
                return infoSubcommand.getSuggestions(main, sender, label, args);
            case "off":
                return offSubcommand.getSuggestions(main, sender, label, args);
            case "on":
                return onSubcommand.getSuggestions(main, sender, label, args);
            case "reload":
                return reloadSubcommand.getSuggestions(main, sender, label, args);
            default:
                return Collections.emptyList();
        }
    }
}
