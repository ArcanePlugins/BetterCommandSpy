/*
 * Copyright (c) 2020-2022  lokka30.
 * This file is/was present in BetterCommandSpy's source code.
 * Learn more about BetterCommandSpy and its licensing at:
 * <https://github.com/lokka30/BetterCommandSpy>
 */

package me.lokka30.bettercommandspy;

import me.lokka30.bettercommandspy.commands.bettercommandspy.BetterCommandSpyCommand;
import me.lokka30.bettercommandspy.handlers.FileHandler;
import me.lokka30.bettercommandspy.handlers.UpdateCheckerHandler;
import me.lokka30.bettercommandspy.handlers.UserHandler;
import me.lokka30.bettercommandspy.listeners.CommandListener;
import me.lokka30.bettercommandspy.listeners.JoinListener;
import me.lokka30.bettercommandspy.misc.Utils;
import me.lokka30.microlib.files.YamlConfigFile;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
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

    /* If you've contributed code to BCS, add your name to the end of this list ;) */
    @NotNull public static final String[] CONTRIBUTORS = new String[]{"ArcanePlugins"};

    /* Handler Classes */
    @NotNull public final FileHandler fileHandler = new FileHandler(this);
    @NotNull public final UserHandler userHandler = new UserHandler(this);
    @NotNull public final UpdateCheckerHandler updateCheckerHandler = new UpdateCheckerHandler(this);

    /* Config Files */
    @NotNull
    public final YamlConfigFile settings =
            new YamlConfigFile(this, new File(getDataFolder(), "settings.yml"));

    @NotNull
    public final YamlConfigFile messages =
            new YamlConfigFile(this, new File(getDataFolder(), "messages.yml"));

    @NotNull
    public final YamlConfigFile data =
            new YamlConfigFile(this, new File(getDataFolder(), "data.yml"));

    @Override
    public void onEnable() {
        loadFiles();
        registerListeners();
        registerCommands();
        startMetrics();
        updateCheckerHandler.initStage1(UpdateCheckerHandler.UpdateCheckReason.FROM_STARTUP);
    }

    /**
     * Load/reload the configs.
     *
     * @author lokka30
     * @since v2.0.0
     */
    public void loadFiles() {
        fileHandler.init();
    }

    /**
     * Register the listeners the plugin has.
     *
     * @author lokka30
     * @since v2.0.0
     */
    private void registerListeners() {
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
