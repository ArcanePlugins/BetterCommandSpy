/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy.listeners;

import me.lokka30.bettercommandspy.BetterCommandSpy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * This class listens for whenever players execute a command.
 * BCS then sends command spy alerts out.
 *
 * @author lokka30
 * @since v2.0.0
 */
public class CommandListener implements Listener {

    /*
    TODO
        listener.
     */

    private final BetterCommandSpy main;

    public CommandListener(final BetterCommandSpy main) {
        this.main = main;
    }

    /**
     * TODO
     *
     * @param event PlayerCommandPreprocessEvent
     * @author lokka30
     * @since v2.0.0
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onExecuteCommand(final PlayerCommandPreprocessEvent event) {
        // ...
    }
}
