/*
 * Copyright (c) 2020-2022  lokka30.
 * This file is/was present in BetterCommandSpy's source code.
 * Learn more about BetterCommandSpy and its licensing at:
 * <https://github.com/lokka30/BetterCommandSpy>
 */

package me.lokka30.bettercommandspy.listeners;

import me.lokka30.bettercommandspy.BetterCommandSpy;
import me.lokka30.bettercommandspy.misc.DebugCategory;
import me.lokka30.bettercommandspy.misc.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

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
    public void onJoin(final @NotNull PlayerJoinEvent event) {
        sendUpdateCheckerNotification(event.getPlayer());
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
    private void sendUpdateCheckerNotification(final @NotNull Player player) {
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
