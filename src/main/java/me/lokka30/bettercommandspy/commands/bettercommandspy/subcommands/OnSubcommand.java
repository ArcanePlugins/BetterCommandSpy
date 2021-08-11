/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy.commands.bettercommandspy.subcommands;

import me.lokka30.bettercommandspy.BetterCommandSpy;
import me.lokka30.bettercommandspy.commands.ISubcommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

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
public class OnSubcommand implements ISubcommand {

    /*
    TODO
        Command
        Test
        Tab Completion
        Test
     */

    @Override
    public void parseCmd(@NotNull BetterCommandSpy main, @NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        sender.sendMessage("Work in progress."); //TODO
    }

    @Override
    public List<String> parseTabSuggestions(@NotNull BetterCommandSpy main, @NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        return Collections.singletonList("Work in progress."); //TODO
    }
}
