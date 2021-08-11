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
@SuppressWarnings("unused")
public class CommandSpyToggleEvent extends Event {

    private final Player player;
    private boolean state;
    private final UserHandler.ChangedStatusCause cause;

    public CommandSpyToggleEvent(Player player, boolean state, UserHandler.ChangedStatusCause cause) {
        this.player = player;
        this.state = state;
        this.cause = cause;
    }

    private boolean wasStateModified = false;

    /* Getters & setters */

    public Player getPlayer() {
        return player;
    }

    public boolean getState() {
        return state;
    }

    public boolean getWasStateModified() {
        return wasStateModified;
    }

    public UserHandler.ChangedStatusCause getCause() {
        return cause;
    }

    public void setState(boolean state) {
        this.state = state;
        this.wasStateModified = true;
    }

    /* Event handler list */

    @NotNull
    private static final HandlerList HANDLERS = new HandlerList();

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
