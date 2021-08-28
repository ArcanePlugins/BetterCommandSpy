/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy;

import me.lokka30.bettercommandspy.commands.bettercommandspy.BetterCommandSpyCommand;
import me.lokka30.bettercommandspy.handlers.CompatibilityCheckerHandler;
import me.lokka30.bettercommandspy.handlers.FileHandler;
import me.lokka30.bettercommandspy.handlers.UpdateCheckerHandler;
import me.lokka30.bettercommandspy.handlers.UserHandler;
import me.lokka30.bettercommandspy.listeners.CommandListener;
import me.lokka30.bettercommandspy.listeners.JoinListener;
import me.lokka30.bettercommandspy.misc.Utils;
import me.lokka30.microlib.files.YamlConfigFile;
import me.lokka30.microlib.maths.QuickTimer;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Main class of the plugin, loaded by Bukkit.
 *
 * @author lokka30
 * @since v2.0.0
 */
public class BetterCommandSpy extends JavaPlugin {

    /*
    TODO - Testing To-do List
        - Command Spying (listener)
        - Compatibility Checker
        - Compatibility Subcommand + tab completion
        - Debug Subcommand + tab completion
        - Info Subcommand + tab completion
        - Off Subcommand + tab completion
        - On Subcommand + tab completion
        - Reload Subcommand + tab completion
        - Update Checker
        - Various config settings
        - Test file backups etc.
     */

    /* If you've contributed code to BCS, add your name to the end of this list ;) */
    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    @NotNull public final List<String> CONTRIBUTORS = Arrays.asList("None");

    /* Handler Classes */
    @NotNull public final FileHandler fileHandler = new FileHandler(this);
    @NotNull public final UserHandler userHandler = new UserHandler(this);
    @NotNull public final UpdateCheckerHandler updateCheckerHandler = new UpdateCheckerHandler(this);
    @NotNull public final CompatibilityCheckerHandler compatibilityCheckerHandler = new CompatibilityCheckerHandler(this);

    /* Config Files */
    @NotNull public final YamlConfigFile settings = new YamlConfigFile(this, new File(getDataFolder(), "settings.yml"));
    @NotNull public final YamlConfigFile messages = new YamlConfigFile(this, new File(getDataFolder(), "messages.yml"));
    @NotNull public final YamlConfigFile data = new YamlConfigFile(this, new File(getDataFolder(), "data.yml"));

    /**
     * Start-up sequence of the plugin. Called by Bukkit
     *
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public void onEnable() {
        final QuickTimer quickTimer = new QuickTimer();

        loadFiles();
        registerListeners();
        registerCommands();

        Utils.LOGGER.info("Running misc startup procedures...");
        startBStats();
        checkCompatibility();
        updateCheckerHandler.initStage1(UpdateCheckerHandler.UpdateCheckReason.FROM_STARTUP);

        Utils.LOGGER.info("&f~ &bStart-up complete&7, took &b" + quickTimer.getTimer() + "ms&f ~");
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

        /* Add any onDisable methods here. Nothing for now. */

        Utils.LOGGER.info("&f~ &bShut-down complete&7, took &b" + quickTimer.getTimer() + "ms&f ~");
    }

    /**
     * Load/reload the configs.
     *
     * @author lokka30
     * @since v2.0.0
     */
    public void loadFiles() {
        Utils.LOGGER.info("Loading files...");

        fileHandler.init();
    }

    /**
     * Run checks to see if the server is compatible or not with this version of the plugin
     *
     * @author lokka30
     * @since v2.0.0
     */
    public void checkCompatibility() {
        if (settings.getConfig().getBoolean("compatibility-checker.run-on-startup", true)) {
            compatibilityCheckerHandler.scan();

            if (settings.getConfig().getBoolean("compatibility-checker.notify.console-on-startup", true)) {
                compatibilityCheckerHandler.presentFindings(Bukkit.getConsoleSender());
            }
        }
    }

    /**
     * Register the listeners the plugin has.
     *
     * @author lokka30
     * @since v2.0.0
     */
    private void registerListeners() {
        Utils.LOGGER.info("Registering listeners...");

        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new CommandListener(this), this);
    }

    /**
     * Register the commands.
     *
     * @author lokka30
     * @since v2.0.0
     */
    private void registerCommands() {
        Utils.LOGGER.info("Registering commands...");

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
}
