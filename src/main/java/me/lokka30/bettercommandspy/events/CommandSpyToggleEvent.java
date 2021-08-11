/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy.events;

import me.lokka30.bettercommandspy.handlers.UserHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This is an Event which fires when a player's command spy state is toggled.
 * <p>
 * It allows other plugins to check when this is done, and it also allows
 * plugins to override the result of the event.
 *
 * @author lokka30
 * @see Event
 * @see me.lokka30.bettercommandspy.handlers.UserHandler#setStatus(Player, boolean, UserHandler.ChangedStatusCause)
 * @since v2.0.0
 */
public class CommandSpyToggleEvent extends Event {

    /*
    TODO
        Add handler list variable
        Add constructor
        Add method that allows plugins to override the state of the player.
        Add method to get changed reason, etc.
        Fire event from UserHandler#setStatus. Add another debug log that runs only if the state was changed through the event.
     */

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
