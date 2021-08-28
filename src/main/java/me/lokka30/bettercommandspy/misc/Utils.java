/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy.misc;

import me.lokka30.bettercommandspy.BetterCommandSpy;
import me.lokka30.microlib.messaging.MessageUtils;
import me.lokka30.microlib.messaging.MicroLogger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;

/**
 * Class containing a bunch of utility methods and vars
 *
 * @author lokka30
 * @since v2.0.0
 */
public class Utils {

    private Utils() {
        throw new UnsupportedOperationException("Contents of the Utils class are static, access as such");
    }

    /**
     * This is the MicroLogger for BetterCommandSpy.
     * Used to send console logs with colors and auto-prefixing :)
     */
    @NotNull public static final MicroLogger LOGGER = new MicroLogger("&b&lBetterCommandSpy: &7");

    /**
     * Attempt to register a command from plugin.yml.
     *
     * @param main    BetterCommandSpy's main class.
     * @param clazz   TabExecutor class that handles execution of the command.
     * @param command Main label of the command (not any aliases).
     * @author lokka30
     * @since v2.0.0
     */
    public static void registerCommand(@NotNull final BetterCommandSpy main, @NotNull final TabExecutor clazz, @NotNull final String command) {
        Utils.LOGGER.info("&3Commands: &8[/" + command + "] &7Attempting to register command...");

        final PluginCommand pluginCommand = main.getCommand(command);

        if (pluginCommand == null) {
            Utils.LOGGER.error("&3Commands: &8[/" + command + "] &7Unable to register command - PluginCommand is null. Was plugin.yml incorrectly modified?");
            return;
        }

        pluginCommand.setExecutor(clazz);

        Utils.LOGGER.info("&3Commands: &8[/" + command + "] &7Command registered successfully.");
    }

    /**
     * Send a debug log to the console if the DebugCategory specified is enabled in settings.
     *
     * @param main          BetterCommandSpy's main class.
     * @param debugCategory Which debug category does the message fall under.
     * @param msg           Contents of the message to log.
     * @author lokka30
     * @since v2.0.0
     */
    public static void debugLog(@NotNull final BetterCommandSpy main, @NotNull final DebugCategory debugCategory, @NotNull final String msg) {
        if (!main.settings.getConfig().getStringList("debug").contains(debugCategory.toString())) return;
        Utils.LOGGER.info("&8[&3Debug &8| &3" + debugCategory + "&8]: &7" + msg);
    }

    /**
     * @param username username of the offlineplayer
     * @return the offlineplayer
     * @author lokka30
     * @since v2.0.0
     * Get the OfflinePlayer from a username.
     * Same as Bukkit#getOfflinePlayer, just bypasses the deprecation.
     */
    @SuppressWarnings("deprecation")
    public static @NotNull OfflinePlayer getOfflinePlayer(@NotNull final String username) {
        return Bukkit.getOfflinePlayer(username);
    }

    /**
     * @param sender the player being observed - if it's console, then all usernames are visible
     * @return a list of usernames of online players that the player knows are online
     * @author lokka30
     * @since v2.0.0
     * Get a list of usernames that the specified player can see (not vanished,
     * or so what Bukkit thinks - doesn't work for packet-only vanish plugins).
     * Used for tab-completion suggestions.
     */
    public static @NotNull HashSet<String> getVisibleOnlinePlayerUsernamesList(@NotNull CommandSender sender) {
        final HashSet<String> usernames = new HashSet<>();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!(sender instanceof Player) || ((Player) sender).canSee(onlinePlayer)) {
                usernames.add(onlinePlayer.getName());
            }
        }

        return usernames;
    }

    /**
     * @param main BetterCommandSpy's main class
     * @param list the list that should be formatted
     * @return the formatted list
     * @author lokka30
     * @since v2.0.0
     */
    public static @NotNull String getFormattedList(@NotNull BetterCommandSpy main, @NotNull List<String> list) {
        // yes - intentionally, only colorize the delimiter.
        return String.join(
                MessageUtils.colorizeAll(main.messages.getConfig().getString("commands.common.delimiter", ",")),
                list
        );
    }
}
