/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy.commands.bettercommandspy;

import me.lokka30.bettercommandspy.BetterCommandSpy;
import me.lokka30.bettercommandspy.commands.bettercommandspy.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author lokka30
 * @since v2.0.0
 * <p>
 * This class handles the execution of the plugin's command '/bettercommandspy'.
 */
public class BetterCommandSpyCommand implements TabExecutor {

    private final BetterCommandSpy main;

    public BetterCommandSpyCommand(final BetterCommandSpy main) {
        this.main = main;
    }

    final CompatibilitySubcommand compatibilitySubcommand = new CompatibilitySubcommand();
    final InfoSubcommand infoSubcommand = new InfoSubcommand();
    final OffSubcommand offSubcommand = new OffSubcommand();
    final OnSubcommand onSubcommand = new OnSubcommand();
    final ReloadSubcommand reloadSubcommand = new ReloadSubcommand();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0) {
            sendUsageBanner(sender, label);
        } else {
            switch (args[0].toLowerCase(Locale.ROOT)) {
                case "on":
                    onSubcommand.parseCmd(main, sender, label, args);
                    break;
                case "off":
                    offSubcommand.parseCmd(main, sender, label, args);
                    break;
                case "reload":
                    reloadSubcommand.parseCmd(main, sender, label, args);
                    break;
                case "info":
                    infoSubcommand.parseCmd(main, sender, label, args);
                    break;
                case "compatibility":
                    compatibilitySubcommand.parseCmd(main, sender, label, args);
                    break;
                default:
                    sendUsageBanner(sender, label);
                    break;
            }
        }

        return true;
    }

    void sendUsageBanner(CommandSender sender, String label) {
        //TODO customisable msg
        sender.sendMessage("Invalid usage. label: " + label);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) return new ArrayList<>();

        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "on":
                return onSubcommand.parseTabSuggestions(main, sender, label, args);
            case "off":
                return offSubcommand.parseTabSuggestions(main, sender, label, args);
            case "reload":
                return reloadSubcommand.parseTabSuggestions(main, sender, label, args);
            case "info":
                return infoSubcommand.parseTabSuggestions(main, sender, label, args);
            case "compatibility":
                return compatibilitySubcommand.parseTabSuggestions(main, sender, label, args);
            default:
                return new ArrayList<>();
        }
    }
}
