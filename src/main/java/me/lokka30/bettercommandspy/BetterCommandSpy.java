/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy;

import me.lokka30.bettercommandspy.commands.bettercommandspy.BetterCommandSpyCommand;
import me.lokka30.bettercommandspy.handlers.FileHandler;
import me.lokka30.bettercommandspy.listeners.CommandListener;
import me.lokka30.bettercommandspy.misc.Utils;
import me.lokka30.microlib.QuickTimer;
import me.lokka30.microlib.YamlConfigFile;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Main class of the plugin, loaded by Bukkit.
 *
 * @author lokka30
 * @since v2.0.0
 */
public class BetterCommandSpy extends JavaPlugin {

    /* Handler Classes */
    public final FileHandler fileHandler = new FileHandler(this);

    /* Config Files */
    public final YamlConfigFile settings = new YamlConfigFile(this, new File(getDataFolder(), "settings.yml"));
    public final YamlConfigFile messages = new YamlConfigFile(this, new File(getDataFolder(), "messages.yml"));
    public final YamlConfigFile data = new YamlConfigFile(this, new File(getDataFolder(), "data.yml"));

    /**
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
        checkForUpdates();

        Utils.LOGGER.info("&3Start-up: &f~ &bStart-up complete&7, took &b" + quickTimer.getTimer() + "ms&f ~");
    }

    /**
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
     * @author lokka30
     * @since v2.0.0
     */
    protected void loadFiles() {
        Utils.LOGGER.info("&3Files: &7Loading files...");

        fileHandler.load();
    }

    /**
     * @author lokka30
     * @since v2.0.0
     */
    private void registerListeners() {
        Utils.LOGGER.info("&3Listeners: &7Registering listeners...");

        Bukkit.getPluginManager().registerEvents(new CommandListener(this), this);
    }

    /**
     * @author lokka30
     * @since v2.0.0
     */
    private void registerCommands() {
        Utils.LOGGER.info("&3Commands: &7Registering commands...");

        Utils.registerCommand(this, new BetterCommandSpyCommand(this), "bettercommandspy");
    }

    /**
     * @author lokka30
     * @since v2.0.0
     */
    private void startBStats() {
        new Metrics(this, 8907);
    }

    /**
     * TODO
     *
     * @author lokka30
     * @since v2.0.0
     */
    protected void checkForUpdates() {
        // ...
    }
}
