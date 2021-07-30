/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy.misc;

import me.lokka30.bettercommandspy.BetterCommandSpy;
import me.lokka30.microlib.MicroLogger;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;

/**
 * Class containing a bunch of utility methods and vars
 *
 * @author lokka30
 * @since v2.0.0
 */
public class Utils {

    private Utils() {
        throw new UnsupportedOperationException("Contents are static, access as such");
    }

    public static final MicroLogger LOGGER = new MicroLogger("&b&lBetterCommandSpy: &7");

    public static void registerCommand(final BetterCommandSpy main, final TabExecutor clazz, final String command) {
        Utils.LOGGER.info("&3Commands: &8[/" + command + "] &7Attempting to register command...");

        final PluginCommand pluginCommand = main.getCommand(command);

        if (pluginCommand == null) {
            Utils.LOGGER.error("&3Commands: &8[/" + command + "] &7Unable to register command - PluginCommand is null. Was plugin.yml incorrectly modified?");
            return;
        }

        pluginCommand.setExecutor(clazz);

        Utils.LOGGER.info("&3Commands: &8[/" + command + "] &7Command registered successfully.");
    }
}
