/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy.handlers;

import me.lokka30.bettercommandspy.BetterCommandSpy;
import me.lokka30.bettercommandspy.events.CommandSpyToggleEvent;
import me.lokka30.bettercommandspy.misc.DebugCategory;
import me.lokka30.bettercommandspy.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

/**
 * Contains methods to get & set the spy status of
 * users.
 *
 * @author lokka30
 * @since v2.0.0
 */
public class UserHandler {

    private final @NotNull BetterCommandSpy main;

    public UserHandler(@NotNull final BetterCommandSpy main) {
        this.main = main;
    }

    /**
     * Get the spy status of a player.
     *
     * @param uuid player to get command spy status of
     * @return command spying status of player
     * @author lokka30
     * @since v2.0.0
     */
    public boolean getStatus(@NotNull final UUID uuid) {

        /* Check status */
        final boolean defaultCommandSpyStatus = main.settings.getConfig().getBoolean("default-commandspy-state", true);
        final String dataPath = "players." + uuid + ".state";

        final boolean statusInData = main.data.getConfig().getBoolean(dataPath, defaultCommandSpyStatus);

        final Player player = Bukkit.getPlayer(uuid);

        /* Check permission */
        if (player != null && main.settings.getConfig().getBoolean("use-canlisten-permission", true)) {

            if (player.hasPermission("bettercommandspy.canListen")) {
                return statusInData;
            } else {
                if (statusInData) setStatus(uuid, false, ChangedStatusCause.NO_CAN_LISTEN_PERMISSION);
                return false;
            }
        } else {
            return statusInData;
        }
    }

    /**
     * This enum contains possible reasons as to why
     * a player's command spy status was changed.
     *
     * @author lokka30
     * @since v2.0.0
     */
    public enum ChangedStatusCause {
        COMMAND,
        NO_CAN_LISTEN_PERMISSION // The player's status was changed by lacking the 'canListen' permission.
    }

    /**
     * Change the spy status of a player.
     *
     * @param playerUuid    player to set command spy status of
     * @param originalState state that should be set
     * @param cause         the cause of this method being ran
     * @author lokka30
     * @since v2.0.0
     */
    public void setStatus(@NotNull final UUID playerUuid, final boolean originalState, @NotNull final ChangedStatusCause cause) {
        /* Call the event */
        CommandSpyToggleEvent event = new CommandSpyToggleEvent(playerUuid, originalState, cause);
        Bukkit.getPluginManager().callEvent(event);

        /* Set & save the new value into the data file. */
        main.data.getConfig().set("players." + playerUuid + ".state", event.getState());
        try {
            main.data.save();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        /* Send a debug log regarding the method being ran */
        Utils.debugLog(main, DebugCategory.USER_HANDLER_STATUS_CHANGED, "Player '" + playerUuid + "' spy status changing to state '" + event.getState() + "' (modified by external plugin: " + event.getWasStateModified() + ") with cause '" + cause + "'.");
    }
}
