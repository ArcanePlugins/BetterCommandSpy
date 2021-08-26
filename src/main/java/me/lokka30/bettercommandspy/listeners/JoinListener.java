/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy.listeners;

import me.lokka30.bettercommandspy.BetterCommandSpy;
import me.lokka30.bettercommandspy.misc.DebugCategory;
import me.lokka30.bettercommandspy.misc.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author lokka30
 * @see PlayerJoinEvent
 * @since v2.0.0
 * <p>
 * This class listens for whenever players
 * join the server. It runs methods based
 * off of PlayerJoinEvent.
 */
public class JoinListener implements Listener {

    private final BetterCommandSpy main;

    public JoinListener(BetterCommandSpy main) {
        this.main = main;
    }

    /**
     * @param event PlayerJoinEvent
     * @author lokka30
     * @see PlayerJoinEvent
     * @since v2.0.0
     * <p>
     * This method is called whenever
     * a player joins the server.
     * <p>
     */
    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        sendCompatibilityCheckerNotification(event.getPlayer());
        sendUpdateCheckerNotification(event.getPlayer());
    }

    /**
     * @param player who joined
     * @author lokka30
     * @since v2.0.0
     * <p>
     * Check if the player that joined
     * should receive a notification or not
     * <p>
     */
    private void sendCompatibilityCheckerNotification(final Player player) {

        // make sure the setting is enabled
        Utils.debugLog(main, DebugCategory.COMPATIBILITY_CHECKER_ON_JOIN_NOTIFY, "[" + player.getName() + "] (1/4) Checking if 'notify players with perms on join' is enabled.");
        if (!main.settings.getConfig().getBoolean("compatibility-checker.notify.players-with-perm-on-join", true))
            return;

        // check if it should be skipped due to incompatibilities possibly having a size of 0
        Utils.debugLog(main, DebugCategory.COMPATIBILITY_CHECKER_ON_JOIN_NOTIFY, "[" + player.getName() + "] (2/4) Checking size of incompatibilities list.");
        if (
                main.compatibilityCheckerHandler.getIncompatibilities().size() == 0 &&
                        main.settings.getConfig().getBoolean("compatibility-checker.notify.dont-notify-if-none-found", true)
        ) return;

        // Make sure the player has permission to receive the notification
        Utils.debugLog(main, DebugCategory.COMPATIBILITY_CHECKER_ON_JOIN_NOTIFY, "[" + player.getName() + "] (3/4) Checking if player has perms to receive notifications.");
        if (!player.hasPermission("bettercommandspy.notifications.compatibility-checker")) return;

        // Send the notice
        Utils.debugLog(main, DebugCategory.COMPATIBILITY_CHECKER_ON_JOIN_NOTIFY, "[" + player.getName() + "] (4/4) Presenting findings.");
        main.compatibilityCheckerHandler.presentFindings(player);
    }

    /**
     * @param player who joined
     * @author lokka30
     * @see PlayerJoinEvent
     * @since v2.0.0
     * This method checks and sends a
     * notification from the update checker
     * Intended to be fired from JoinListener#onJoin
     */
    private void sendUpdateCheckerNotification(final Player player) {
        // make sure update checker is enabled
        Utils.debugLog(main, DebugCategory.UPDATE_CHECKER_ON_JOIN_NOTIFY, "[" + player.getName() + "] (1/4) Checking update checker enabled");
        if (!main.settings.getConfig().getBoolean("update-checker.enabled", true)) return;

        // make sure notify players with perm is enabled
        Utils.debugLog(main, DebugCategory.UPDATE_CHECKER_ON_JOIN_NOTIFY, "[" + player.getName() + "] (2/4) Checking notify.players-with-perm");
        if (!main.settings.getConfig().getBoolean("update-checker.notify.players-with-perm", true)) return;

        // make sure the player has permission to receive the update notification
        Utils.debugLog(main, DebugCategory.UPDATE_CHECKER_ON_JOIN_NOTIFY, "[" + player.getName() + "] (3/4) Checking permission");
        if (!player.hasPermission("bettercommandspy.notifications.update-checker")) return;

        // send the update checker notice
        Utils.debugLog(main, DebugCategory.UPDATE_CHECKER_ON_JOIN_NOTIFY, "[" + player.getName() + "] (4/4) Sending notice");
        main.updateCheckerHandler.notify(player);
    }
}
