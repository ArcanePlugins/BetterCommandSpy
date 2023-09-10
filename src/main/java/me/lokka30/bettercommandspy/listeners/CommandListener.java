/*
 * Copyright (c) 2020-2022  lokka30.
 * This file is/was present in BetterCommandSpy's source code.
 * Learn more about BetterCommandSpy and its licensing at:
 * <https://github.com/lokka30/BetterCommandSpy>
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

    public CommandListener(
        @NotNull final BetterCommandSpy main
    ) {
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
    public void onExecuteCommand(
        @NotNull final PlayerCommandPreprocessEvent event
    ) {
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

            // execute commands
            for (String command : main.settings.getConfig().getStringList("ignored-commands.execute-commands")) {
                String replacedCommand = command;

                if (replacedCommand.startsWith("/")) {
                    replacedCommand = replacedCommand.substring(1);
                }

                replacedCommand = replacedCommand
                        .replace("%username%", event.getPlayer().getName())
                        .replace("%displayname%", event.getPlayer().getDisplayName())
                        .replace("%command%", event.getMessage());

                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), replacedCommand);
            }
        }

        // cache the alert message
        ArrayList<String> alertMessage = (ArrayList<String>) new MultiMessage(
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

            // vanished player support
            if(!onlinePlayer.canSee(event.getPlayer())) continue;

            // send alert msg to the online player
            alertMessage.forEach(onlinePlayer::sendMessage);
        }
    }
}
