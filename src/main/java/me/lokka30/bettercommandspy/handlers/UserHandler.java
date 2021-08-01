/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy.handlers;

import me.lokka30.bettercommandspy.BetterCommandSpy;
import me.lokka30.bettercommandspy.misc.DebugCategory;
import me.lokka30.bettercommandspy.misc.Utils;
import org.bukkit.entity.Player;

/**
 * Contains methods to get & set the spy status of
 * users.
 *
 * @author lokka30
 * @since v2.0.0
 */
public class UserHandler {

    /*
    TODO
        setStatus method
     */

    private final BetterCommandSpy main;

    public UserHandler(final BetterCommandSpy main) {
        this.main = main;
    }

    /**
     * Get the spy status of a player.
     *
     * @param player player to get command spy status of
     * @return command spy status of player
     * @author lokka30
     * @since v2.0.0
     */
    public boolean getStatus(final Player player) {

        /* Check status */
        final boolean defaultCommandSpyStatus = main.settings.getConfig().getBoolean("default-commandspy-state", true);
        final String dataPath = "players." + player.getUniqueId() + ".state";

        final boolean statusInData = main.data.getConfig().getBoolean(dataPath, defaultCommandSpyStatus);

        /* Check permission */
        if (main.settings.getConfig().getBoolean("use-canlisten-permission", true)) {
            if (player.hasPermission("bettercommandspy.canListen")) {
                return statusInData;
            } else {
                if (statusInData) {
                    setStatus(player, false, ChangedStatusCause.NO_CAN_LISTEN_PERMISSION);
                }
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
        COMMAND, // The player's status was changed from the on/off command.
        NO_CAN_LISTEN_PERMISSION // The player's status was changed by lacking the 'canListen' permission.
    }

    /**
     * Change the spy status of a player.
     *
     * @param player player to set command spy status of
     * @param state  state that should be set
     * @param cause  the cause of this method being ran
     *
     * @author lokka30
     * @since v2.0.0
     */
    public void setStatus(final Player player, final boolean state, final ChangedStatusCause cause) {
        Utils.debugLog(main, DebugCategory.STATUS_CHANGED, "Player '" + player.getName() + "' spy status changing to state '" + state + "' with cause '" + cause + "'.");

        //TODO
    }
}
