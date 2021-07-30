/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy.commands.bettercommandspy.subcommands;

import me.lokka30.bettercommandspy.BetterCommandSpy;
import me.lokka30.bettercommandspy.commands.ISubcommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * @author lokka30
 * @since v2.0.0
 * <p>
 * TODO Describe.
 * <p>
 * cmd: /bcs
 * arg: -
 * len: 0
 */
public class OnSubcommand implements ISubcommand {

    @Override
    public void parseCmd(BetterCommandSpy main, CommandSender sender, String label, String[] args) {
        sender.sendMessage("Work in progress."); //TODO
    }

    @Override
    public List<String> parseTabSuggestions(BetterCommandSpy main, CommandSender sender, String label, String[] args) {
        return Collections.singletonList("Work in progress."); //TODO
    }
}