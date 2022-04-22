/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy;

import java.io.File;
import me.lokka30.bettercommandspy.commands.bettercommandspy.BetterCommandSpyCommand;
import me.lokka30.bettercommandspy.handlers.FileHandler;
import me.lokka30.bettercommandspy.handlers.UpdateCheckerHandler;
import me.lokka30.bettercommandspy.handlers.UserHandler;
import me.lokka30.bettercommandspy.listeners.CommandListener;
import me.lokka30.bettercommandspy.listeners.JoinListener;
import me.lokka30.bettercommandspy.misc.Utils;
import me.lokka30.microlib.files.YamlConfigFile;
import me.lokka30.microlib.maths.QuickTimer;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Main class of the plugin, loaded by Bukkit.
 *
 * @author lokka30
 * @since v2.0.0
 */
public class BetterCommandSpy extends JavaPlugin {

    /* If you've contributed code to BCS, add your name to the end of this list ;) */
    @NotNull public static final String[] CONTRIBUTORS = new String[]{"N/A"};

    /* Handler Classes */
    @NotNull public final FileHandler fileHandler = new FileHandler(this);
    @NotNull public final UserHandler userHandler = new UserHandler(this);
    @NotNull public final UpdateCheckerHandler updateCheckerHandler = new UpdateCheckerHandler(this);

    /* Config Files */
    @NotNull public final YamlConfigFile settings = new YamlConfigFile(this, new File(getDataFolder(), "settings.yml"));
    @NotNull public final YamlConfigFile messages = new YamlConfigFile(this, new File(getDataFolder(), "messages.yml"));
    @NotNull public final YamlConfigFile data = new YamlConfigFile(this, new File(getDataFolder(), "data.yml"));

    @Override
    public void onEnable() {
        final QuickTimer quickTimer = new QuickTimer();

        loadFiles();
        registerListeners();
        registerCommands();

        getLogger().info("Running misc startup procedures...");
        startMetrics();
        updateCheckerHandler.initStage1(UpdateCheckerHandler.UpdateCheckReason.FROM_STARTUP);

        getLogger().info("Start-up complete (took" + quickTimer.getTimer() + "ms).");
    }

    /**
     * Load/reload the configs.
     *
     * @author lokka30
     * @since v2.0.0
     */
    public void loadFiles() {
        getLogger().info("Loading files...");

        fileHandler.init();
    }

    /**
     * Register the listeners the plugin has.
     *
     * @author lokka30
     * @since v2.0.0
     */
    private void registerListeners() {
        getLogger().info("Registering listeners...");
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
        getLogger().info("Registering commands...");
        Utils.registerCommand(this, new BetterCommandSpyCommand(this), "bettercommandspy");
    }

    /**
     * Start logging bStats metrics.
     *
     * @author lokka30
     * @since v2.0.0
     */
    private void startMetrics() {
        final Metrics metrics = new Metrics(this, 8907);
        metrics.addCustomChart(new SimplePie("default_commandspy_state", () ->
            Boolean.toString(
                settings.getConfig().getBoolean("default-commandspy-state", true)
            )
        ));
        metrics.addCustomChart(new SimplePie("uses_ignored_commands", () ->
            Boolean.toString(
                settings.getConfig().getBoolean("ignored-commands.enabled", true)
            )
        ));
        metrics.addCustomChart(new SimplePie("uses_canlisten_permission", () ->
            Boolean.toString(
                settings.getConfig().getBoolean("use-canlisten-permission", true)
            )
        ));
        metrics.addCustomChart(new SimplePie("uses_update_checker", () ->
            Boolean.toString(
                settings.getConfig().getBoolean("update-checker.enabled", true)
            )
        ));
        metrics.addCustomChart(new SimplePie("debug_categories_enabled", () ->
            Integer.toString(
                settings.getConfig().getStringList("debug").size()
            )
        ));
    }
}
