/*
 * Copyright (c) 2020-2022  lokka30.
 * This file is/was present in BetterCommandSpy's source code.
 * Learn more about BetterCommandSpy and its licensing at:
 * <https://github.com/lokka30/BetterCommandSpy>
 */

package me.lokka30.bettercommandspy.events;

import me.lokka30.bettercommandspy.handlers.UserHandler;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * This is an Event which fires when a player's command spy state is toggled.
 * It allows other plugins to check when this is done, and it also allows
 * plugins to override the result of the event.
 *
 * @author lokka30
 * @see Event
 * @see UserHandler#setStatus(UUID, boolean, UserHandler.ChangedStatusCause)
 * @since v2.0.0
 */
@SuppressWarnings("unused")
public class CommandSpyToggleEvent extends Event {

    private final UUID uuid;
    private boolean state;
    private final UserHandler.ChangedStatusCause cause;

    public CommandSpyToggleEvent(UUID uuid, boolean state, UserHandler.ChangedStatusCause cause) {
        this.uuid = uuid;
        this.state = state;
        this.cause = cause;
    }

    private boolean wasStateModified = false;

    /* Getters & setters */

    public UUID getPlayerUUID() {
        return uuid;
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
