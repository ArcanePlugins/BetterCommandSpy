/*
 * Copyright (c) 2020-2022  lokka30.
 * This file is/was present in BetterCommandSpy's source code.
 * Learn more about BetterCommandSpy and its licensing at:
 * <https://github.com/lokka30/BetterCommandSpy>
 */

package me.lokka30.bettercommandspy.handlers;

import me.lokka30.bettercommandspy.BetterCommandSpy;
import me.lokka30.bettercommandspy.folia.FoliaRunnable;
import me.lokka30.bettercommandspy.folia.SchedulerUtils;
import me.lokka30.bettercommandspy.misc.DebugCategory;
import me.lokka30.bettercommandspy.misc.Utils;
import me.lokka30.microlib.messaging.MultiMessage;
import me.lokka30.microlib.other.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * This class contains a bunch of methods
 * making it easier to use the update checker
 *
 * @author lokka30
 * @since v2.0.0
 */
public class UpdateCheckerHandler {

    private final @NotNull BetterCommandSpy main;

    public UpdateCheckerHandler(@NotNull final BetterCommandSpy main) {
        this.main = main;
    }

    private @Nullable UpdateCheckerResult cachedUpdateCheckerResult = null;
    public @Nullable FoliaRunnable repeatingTask = null;

    /**
     * What type of result did the
     * update checker retrieve?
     */
    public enum ResultType {
        USING_LATEST_VERSION, // The user has the correct & latest version installed
        USING_DEVELOPMENT_VERSION, // The user is using an unreleased (future) version. Unused as of 2.0.5
        USING_OUTDATED_VERSION, // The user is using an outdated version of the plugin
        FAILED // Wasn't able to compare versions
    }

    public static class UpdateCheckerResult {

        private final @NotNull ResultType resultType;
        private final @Nullable String latestVersion;
        private final @Nullable String currentVersion;

        public UpdateCheckerResult(
                @NotNull ResultType resultType,
                @Nullable String latestVersion,
                @Nullable String currentVersion
        ) {
            this.resultType = resultType;
            this.latestVersion = latestVersion;
            this.currentVersion = currentVersion;
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
        public String getCurrentVersion() {
            return currentVersion;
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
            updateResult(UpdateCheckReason.FROM_RELOAD);
        }
        return cachedUpdateCheckerResult;
    }

    /**
     * Fetch the latest update result from Spigot.
     *
     * @author lokka30
     * @since v2.0.0
     */
    public void updateResult(UpdateCheckReason reason) {
        final UpdateChecker updateChecker = new UpdateChecker(main, 84030);

        try {
            updateChecker.getLatestVersion(latestVersion -> {
                final String currentVersion = updateChecker.getCurrentVersion();

                ResultType resultType;
                if (currentVersion.equals(latestVersion)) {
                    resultType = ResultType.USING_LATEST_VERSION;
                } else {
                    resultType = ResultType.USING_OUTDATED_VERSION;
                }

                cachedUpdateCheckerResult = new UpdateCheckerResult(
                        resultType,
                        latestVersion,
                        currentVersion
                );

                if(reason == UpdateCheckReason.FROM_STARTUP) {
                    initStage2();
                }
            });
        } catch (Exception ex) {
            main.getLogger().warning("Unable to check for updates - (>>> was your internet or SpigotMC.org down? <<<): " + ex.getMessage());
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
     * @param reason why the method is bieng ran
     * @author lokka30
     * @since v2.0.0
     */
    public void initStage1(UpdateCheckReason reason) {

        // make sure the update checker is enabled
        if (!main.settings.getConfig().getBoolean("update-checker.enabled", true)) {
            if (repeatingTask != null) repeatingTask.cancel(); // cancel it if it's already running
            return;
        }

        updateResult(reason);
    }

    /**
     * @author lokka30
     * @since v2.0.0
     * This method should only be called if the update
     * checker has completed its first check (on startup).
     */
    public void initStage2() {
        notify(Bukkit.getConsoleSender());

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

            repeatingTask = new FoliaRunnable() {

                @Override
                public void run() {
                    updateResult(UpdateCheckReason.FROM_REPEATING_TASK);
                }

            };
            SchedulerUtils.runTaskTimer(null, repeatingTask, repeatPeriod, repeatPeriod);
        }
    }

    public enum UpdateCheckReason {
        FROM_STARTUP,
        FROM_RELOAD,
        FROM_REPEATING_TASK
    }

}
