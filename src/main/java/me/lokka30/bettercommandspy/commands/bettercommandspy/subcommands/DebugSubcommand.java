/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy.commands.bettercommandspy.subcommands;

import me.lokka30.bettercommandspy.BetterCommandSpy;
import me.lokka30.bettercommandspy.commands.ISubcommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

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

    /*
    TODO
        Command
        Test
     */

    @Override
    public void parseCmd(BetterCommandSpy main, CommandSender sender, String label, String[] args) {
        sender.sendMessage("Work in progress."); //TODO
    }

    @Override
    public List<String> parseTabSuggestions(BetterCommandSpy main, CommandSender sender, String label, String[] args) {
        return new ArrayList<>();
    }
}
