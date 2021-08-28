/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy.handlers;

import me.lokka30.bettercommandspy.BetterCommandSpy;
import me.lokka30.bettercommandspy.misc.DebugCategory;
import me.lokka30.bettercommandspy.misc.Utils;
import me.lokka30.microlib.exceptions.OutdatedServerVersionException;
import me.lokka30.microlib.messaging.MultiMessage;
import me.lokka30.microlib.other.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Arrays;

/**
 * This class contains a bunch of methods
 * making it easier to use the update checker
 *
 * @author lokka30
 * @since v2.0.0
 */
public class UpdateCheckerHandler {

    private final BetterCommandSpy main;

    public UpdateCheckerHandler(@NotNull final BetterCommandSpy main) {
        this.main = main;
    }

    private UpdateCheckerResult cachedUpdateCheckerResult = null;
    public BukkitTask repeatingTask = null;

    /**
     * What type of result did the
     * update checker retrieve?
     */
    public enum ResultType {
        USING_LATEST_VERSION, // The user has the correct & latest version installed
        USING_DEVELOPMENT_VERSION, // The user is using an unreleased (future) version
        USING_OUTDATED_VERSION, // The user is using an outdated version of the plugin
        FAILED // Wasn't able to compare versions
    }

    public static class UpdateCheckerResult {

        private final ResultType resultType;
        private final String latestVersion;
        private final Integer latestBuild;
        private final String currentVersion;
        private final Integer currentBuild;

        public UpdateCheckerResult(
                @NotNull ResultType resultType,
                @Nullable String latestVersion,
                @Nullable Integer latestBuild,
                @Nullable String currentVersion,
                @Nullable Integer currentBuild
        ) {
            this.resultType = resultType;
            this.latestVersion = latestVersion;
            this.latestBuild = latestBuild;
            this.currentVersion = currentVersion;
            this.currentBuild = currentBuild;
        }

        @SuppressWarnings("unused")
        @NotNull
        public ResultType getResultType() {
            return resultType;
        }

        @SuppressWarnings("unused")
        @Nullable
        public String getLatestVersion() {
            return latestVersion;
        }

        @SuppressWarnings("unused")
        @Nullable
        public Integer getLatestBuild() {
            return latestBuild;
        }

        @SuppressWarnings("unused")
        @Nullable
        public String getCurrentVersion() {
            return currentVersion;
        }

        @SuppressWarnings("unused")
        @Nullable
        public Integer getCurrentBuild() {
            return currentBuild;
        }
    }

    /**
     * @return the cached update checker result
     * @author lokka30
     * @since v2.0.0
     */
    @SuppressWarnings("unused")
    @NotNull
    public final UpdateCheckerResult getResult() {
        if (cachedUpdateCheckerResult == null) {
            updateResult();
        }
        return cachedUpdateCheckerResult;
    }

    /**
     * Fetch the latest update result from Spigot.
     *
     * @author lokka30
     * @since v2.0.0
     */
    public void updateResult() {
        final UpdateChecker updateChecker = new UpdateChecker(main, 84030);

        try {
            updateChecker.getLatestVersion(detectedVersionUnsplit -> {
                final String currentVersionUnsplit = updateChecker.getCurrentVersion();

                String[] currentVersionSplit = currentVersionUnsplit.split("-");
                String[] latestVersionSplit = detectedVersionUnsplit.split("-");

                if (latestVersionSplit.length != 2 || currentVersionSplit.length != 2) {
                    cachedUpdateCheckerResult = new UpdateCheckerResult(
                            ResultType.FAILED,
                            null,
                            null,
                            null,
                            null
                    );

                    Utils.LOGGER.warning(
                            "Unable to check for updates - please inform a BetterCommandSpy developer, and send them this log." +
                                    " &8[&7CVS &8= &b" + currentVersionSplit.length + "&7, LVS &8= &b" + currentVersionSplit.length + "&8]&7.");

                    if (repeatingTask != null) repeatingTask.cancel();

                    return;
                }

                final String latestVersion = latestVersionSplit[0];
                final int latestBuild = Integer.parseInt(latestVersionSplit[1]);

                final String currentVersion = currentVersionSplit[0];
                final int currentBuild = Integer.parseInt(currentVersionSplit[1]);

                ResultType resultType;

                if (currentBuild == latestBuild) {
                    resultType = ResultType.USING_LATEST_VERSION;
                } else if (currentBuild > latestBuild) {
                    resultType = ResultType.USING_DEVELOPMENT_VERSION;
                } else {
                    resultType = ResultType.USING_OUTDATED_VERSION;
                }

                cachedUpdateCheckerResult = new UpdateCheckerResult(
                        resultType,
                        latestVersion,
                        latestBuild,
                        currentVersion,
                        currentBuild
                );

                notify(Bukkit.getConsoleSender());
                Bukkit.getOnlinePlayers().forEach(this::notify);
            });
        } catch (OutdatedServerVersionException ignored) {
            Utils.LOGGER.warning(
                    "The update checker was unable to run as your server is not MC 1.11 or newer." +
                            " BetterCommandSpy will now disable the update checker."
            );

            main.settings.getConfig().set("update-checker.enabled", false);

            try {
                main.settings.save();
            } catch (IOException ex) {
                Utils.LOGGER.error("Unable to disable update checker in settings.yml. Stack trace:");
                ex.printStackTrace();
            }

            if (repeatingTask != null) repeatingTask.cancel();
        }
    }

    /**
     * Sends an update notification to whoever
     * is configured to receive it.
     *
     * @param recipient who to send the update checker notice to
     * @author lokka30
     * @since v2.0.0
     */
    public void notify(@NotNull CommandSender recipient) {
        if (!recipient.hasPermission("bettercommandspy.notifications.update-checker")) return;

        Utils.debugLog(main, DebugCategory.UPDATE_CHECKER_OPERATIONS, "Notifying recipient '" + recipient.getName() + "', result is " + getResult().getResultType() + ".");

        switch (getResult().getResultType()) {
            case USING_LATEST_VERSION:
            case FAILED:
                // if the update checker failed, don't notify anyone.
                // also don't notify if they're using the latest version
                return;
            case USING_DEVELOPMENT_VERSION:
                new MultiMessage(main.messages.getConfig().getStringList("update-checker.using-development-version"), Arrays.asList(
                        new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix"), true),
                        new MultiMessage.Placeholder("installed-version", getResult().getCurrentVersion(), false),
                        new MultiMessage.Placeholder("release-version", getResult().getLatestVersion(), false)
                )).send(recipient);
                break;
            case USING_OUTDATED_VERSION:
                new MultiMessage(main.messages.getConfig().getStringList("update-checker.using-outdated-version"), Arrays.asList(
                        new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix"), true),
                        new MultiMessage.Placeholder("installed-version", getResult().getCurrentVersion(), false),
                        new MultiMessage.Placeholder("release-version", getResult().getLatestVersion(), false)
                )).send(recipient);
                break;
            default:
                throw new IllegalStateException("Unexpected state '" + getResult().getResultType() + "'!");
        }
    }

    /**
     * start the update checker systems
     *
     * @param calledFromReloadSubcommand if this method was called due to the reload command being ran
     * @author lokka30
     * @since v2.0.0
     */
    public void init(boolean calledFromReloadSubcommand) {

        // make sure the update checker is enabled
        if (!main.settings.getConfig().getBoolean("update-checker.enabled", true)) {
            if (repeatingTask != null) repeatingTask.cancel(); // cancel it if it's already running
            return;
        }

        // inform console if needed. if calledFromReloadSubcommand
        // then don't notify again.
        if (!calledFromReloadSubcommand) {
            notify(Bukkit.getConsoleSender());
        }

        // check if it should repeatedly use the update checker
        final int repeatPeriod = Math.max(main.settings.getConfig().getInt("update-checker.repeat-period", 0), 0) // don't allow numbers below zero
                * 20  // now it's in seconds,
                * 60  // now it's in minutes,
                * 60; // now it's in hours.

        if (repeatPeriod != 0) {
            // if it's still zero, then the user has disabled it. otherwise, start the task

            if (repeatingTask != null) {
                repeatingTask.cancel();
            } // cancel existing repeating task - if it exists of course

            repeatingTask = new BukkitRunnable() {

                @Override
                public void run() {
                    updateResult();
                }

            }.runTaskTimer(main, repeatPeriod, repeatPeriod);
        }
    }

}
