/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy;

import me.lokka30.bettercommandspy.commands.bettercommandspy.BetterCommandSpyCommand;
import me.lokka30.bettercommandspy.handlers.FileHandler;
import me.lokka30.bettercommandspy.handlers.UserHandler;
import me.lokka30.bettercommandspy.listeners.CommandListener;
import me.lokka30.bettercommandspy.misc.Utils;
import me.lokka30.microlib.QuickTimer;
import me.lokka30.microlib.YamlConfigFile;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Main class of the plugin, loaded by Bukkit.
 *
 * @author lokka30
 * @since v2.0.0
 */
public class BetterCommandSpy extends JavaPlugin {

    /*
    TODO
        Compatibility Checker method
        Update Checker method
     */

    /* Handler Classes */
    public final FileHandler fileHandler = new FileHandler(this);
    public final UserHandler userHandler = new UserHandler(this);

    /* Config Files */
    public final YamlConfigFile settings = new YamlConfigFile(this, new File(getDataFolder(), "settings.yml"));
    public final YamlConfigFile messages = new YamlConfigFile(this, new File(getDataFolder(), "messages.yml"));
    public final YamlConfigFile data = new YamlConfigFile(this, new File(getDataFolder(), "data.yml"));

    /**
     * Start-up sequence of the plugin. Called by Bukkit
     *
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public void onEnable() {
        final QuickTimer quickTimer = new QuickTimer();
        Utils.LOGGER.info("&3Start-up: &f~ &7Initiating start-up procedure &f~");

        loadFiles();
        registerListeners();
        registerCommands();

        Utils.LOGGER.info("&3Start-up: &7Running misc startup procedures...");
        startBStats();
        checkCompatibility(Bukkit.getConsoleSender());
        checkForUpdates();

        Utils.LOGGER.info("&3Start-up: &f~ &bStart-up complete&7, took &b" + quickTimer.getTimer() + "ms&f ~");
    }

    /**
     * Shut-down sequence of the plugin. CAlled by Bukkit
     *
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public void onDisable() {
        final QuickTimer quickTimer = new QuickTimer();
        Utils.LOGGER.info("&3Shut-down: &f~ &7Initiating shut-down procedure &f~");

        /* Add any onDisable methods here. Nothing for now. */

        Utils.LOGGER.info("&3Shut-down: &f~ &bShut-down complete&7, took &b" + quickTimer.getTimer() + "ms&f ~");
    }

    /**
     * Load/reload the configs.
     *
     * @author lokka30
     * @since v2.0.0
     */
    protected void loadFiles() {
        Utils.LOGGER.info("&3Files: &7Loading files...");

        fileHandler.init();
    }

    /**
     * Run checks to see if the server is compatible or not with this version of the plugin
     *
     * @author lokka30
     * @since v2.0.0
     */
    private void checkCompatibility(@NotNull final CommandSender recipient) {
        //TODO
    }

    /**
     * Register the listeners the plugin has.
     *
     * @author lokka30
     * @since v2.0.0
     */
    private void registerListeners() {
        Utils.LOGGER.info("&3Listeners: &7Registering listeners...");

        Bukkit.getPluginManager().registerEvents(new CommandListener(this), this);
    }

    /**
     * Register the commands.
     *
     * @author lokka30
     * @since v2.0.0
     */
    private void registerCommands() {
        Utils.LOGGER.info("&3Commands: &7Registering commands...");

        Utils.registerCommand(this, new BetterCommandSpyCommand(this), "bettercommandspy");
    }

    /**
     * Start logging bStats metrics.
     *
     * @author lokka30
     * @since v2.0.0
     */
    private void startBStats() {
        new Metrics(this, 8907);
    }

    /**
     * TODO
     * <p>
     * Run the async update checker from MicroLib.
     *
     * @author lokka30
     * @since v2.0.0
     */
    protected void checkForUpdates() {
        // ...
    }
}
