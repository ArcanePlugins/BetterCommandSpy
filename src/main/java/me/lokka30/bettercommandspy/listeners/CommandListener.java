/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy.listeners;

import me.lokka30.bettercommandspy.BetterCommandSpy;
import me.lokka30.microlib.messaging.MultiMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class listens for whenever players execute a command.
 * BCS then sends command spy alerts out.
 *
 * @author lokka30
 * @since v2.0.0
 */
public class CommandListener implements Listener {

    private final @NotNull BetterCommandSpy main;

    public CommandListener(@NotNull final BetterCommandSpy main) {
        this.main = main;
    }

    /**
     * This listens for when a player runs a command.
     * This allows BCS to inform spying players about the event.
     *
     * @param event PlayerCommandPreprocessEvent
     * @author lokka30
     * @since v2.0.0
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onExecuteCommand(@NotNull final PlayerCommandPreprocessEvent event) {
        // check if sender has bypass permission
        if (event.getPlayer().hasPermission("bettercommandspy.bypass")) return;

        // check if command is ignored
        if(main.settings.getConfig().getBoolean("ignored-commands.enabled", false)) {
            final boolean isListed = main.settings.getConfig().getStringList("ignored-commands.list").contains(event.getMessage().split(" ")[0]);
            final boolean isListTypeWhitelist = main.settings.getConfig().getString("ignored-commands.type", "WHITELIST").equalsIgnoreCase("WHITELIST");

            if(isListTypeWhitelist) {
                if(isListed) return;
            } else {
                if(!isListed) return;
            }
        }

        // cache the alert message
        ArrayList<String> alertMessage = new MultiMessage(
                main.messages.getConfig().getStringList("alert"),
                Arrays.asList(
                        new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix"), true),
                        new MultiMessage.Placeholder("username", event.getPlayer().getName(), false),
                        new MultiMessage.Placeholder("displayname", event.getPlayer().getDisplayName(), false),
                        new MultiMessage.Placeholder("command", event.getMessage(), false)
                )
        ).getTranslatedContent();

        // check what online players should be alerted
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

            // don't allow players to alert themselves
            if(onlinePlayer.getUniqueId() == event.getPlayer().getUniqueId()) continue;

            // make sure the online player has the required status
            if (!main.userHandler.getStatus(onlinePlayer.getUniqueId())) continue;

            // send alert msg to these players
            alertMessage.forEach(onlinePlayer::sendMessage);
        }
    }
}
