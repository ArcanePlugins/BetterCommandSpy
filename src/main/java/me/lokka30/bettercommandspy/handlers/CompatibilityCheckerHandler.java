/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy.handlers;

import me.lokka30.bettercommandspy.BetterCommandSpy;
import me.lokka30.bettercommandspy.misc.Utils;
import me.lokka30.microlib.messaging.MultiMessage;
import me.lokka30.microlib.other.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author lokka30
 * @since v2.0.0
 * <p>
 * This class hosts all the methods used to
 * try inform the user of any known incompatibilities
 * detected through their current server configuration.
 * <p>
 * At the moment the class is future-proofed for any
 * possible new incompatibility detections, as of v2.0.0
 * it only contains one check: server's Minecraft version.
 */
public class CompatibilityCheckerHandler {

    private final BetterCommandSpy main;

    public CompatibilityCheckerHandler(BetterCommandSpy main) {
        this.main = main;
    }

    @NotNull private final HashSet<Incompatibility> incompatibilities = new HashSet<>();

    @NotNull public HashSet<Incompatibility> getIncompatibilities() {
        return incompatibilities;
    }

    /**
     * @author lokka30
     * @since v2.0.0
     * <p>
     * This enum contains constants that
     * are used by the compatibility checker
     * to see if they are disabled by the user
     * in their configuration - for example,
     * the compatibility checker can check if
     * the MC version of the server is supported
     * or not - the user can even suppress any
     * Category of the compatibility checker's
     * findings in case they know possible
     * consequences of continuing use of BCS.
     */
    public enum CompatibilityCategory {
        SERVER_MINECRAFT_VERSION // Whether the MC version of the server is supported or not
    }

    /**
     * @author lokka30
     * @since v2.0.0
     * <p>
     * This sub-class is used as an object to
     * contain the information of each incompatibility
     * detected by the compatibility checker, stored in
     * a list of Incompatibilities, and then it is iterated
     * through to present all the findings to the user.
     */
    public static class Incompatibility {
        private final CompatibilityCategory category;
        private final String reason;
        private final String otherInfo;

        public Incompatibility(CompatibilityCategory category, String reason, String otherInfo) {
            this.category = category;
            this.reason = reason;
            this.otherInfo = otherInfo;
        }

        public CompatibilityCategory getCategory() {
            return category;
        }

        public String getReason() {
            return reason;
        }

        public String getOtherInfo() {
            return otherInfo;
        }
    }

    /**
     * @author lokka30
     * @since v2.0.0
     * <p>
     * Scan for any incompatibilities known
     */
    public void scan() {
        Utils.LOGGER.info("&3Compatibility Checker: &7Starting compatibility checker...");

        scanServerMCVersion();

        Utils.LOGGER.info("&3Compatibility Checker: &7Checks completed.");
    }

    /**
     * @author lokka30
     * @since v2.0.0
     * <p>
     * Check if the server's MC version is compatible
     */
    private void scanServerMCVersion() {
        // Make sure this compatibility is not suppressed
        if (isCompatibilityCategorySuppressed(CompatibilityCategory.SERVER_MINECRAFT_VERSION)) return;

        // Log what the compat checker is doing
        Utils.LOGGER.info("&3Compatibility Checker: &7Checking category '&b" + CompatibilityCategory.SERVER_MINECRAFT_VERSION + "&7'...");

        // Make sure the server is at least MC 1.7
        if (VersionUtils.isOneSeven()) return;

        // Add the incompatibility to the list
        incompatibilities.add(new Incompatibility(
                CompatibilityCategory.SERVER_MINECRAFT_VERSION,
                "Versions older than MC 1.7 have not been tested, and are unsupported by the developers. Consider updating your server to a newer version of Minecraft.",
                "Detected version: &b" + Bukkit.getVersion()
        ));
    }

    /**
     * @param recipient who the findings should be presented to
     * @author lokka30
     * @since v2.0.0
     * Reports any findings gathered from the latest scan.
     */
    public void presentFindings(CommandSender recipient) {
        int amount = incompatibilities.size();
        if (amount == 0) {
            new MultiMessage(main.messages.getConfig().getStringList("compatibility-checker.no-incompatibilities-found"), Collections.singletonList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix"), true)
            )).send(recipient);
            return;
        } else if (amount == 1) {
            new MultiMessage(main.messages.getConfig().getStringList("compatibility-checker.one-incompatibility-found"), Collections.singletonList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix"), true)
            )).send(recipient);
        } else {
            new MultiMessage(main.messages.getConfig().getStringList("compatibility-checker.one-incompatibility-found"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix"), true),
                    new MultiMessage.Placeholder("amount", incompatibilities.size() + "", false)
            )).send(recipient);
        }

        int index = 1;
        for (Incompatibility incompatibility : incompatibilities) {
            new MultiMessage(main.messages.getConfig().getStringList("compatibility-checker.entry"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix"), true),
                    new MultiMessage.Placeholder("index", index + "", false),
                    new MultiMessage.Placeholder("category", incompatibility.getCategory().toString(), false),
                    new MultiMessage.Placeholder("reason", incompatibility.getReason(), true),
                    new MultiMessage.Placeholder("other-info", incompatibility.getOtherInfo(), true)
            )).send(recipient);

            if (index != amount) {
                new MultiMessage(main.messages.getConfig().getStringList("compatibility-checker.entry-spacer"), Collections.singletonList(
                        new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix"), true)
                )).send(recipient);
            }

            index++;
        }
    }

    /**
     * @param category what category should be checked
     * @return if it is suppressed or not
     * @author lokka30
     * @since v2.0.0
     * Checks if the administrator has configured BCS to suppress
     * the specified Compatibility Category.
     */
    private boolean isCompatibilityCategorySuppressed(@SuppressWarnings("SameParameterValue") @NotNull CompatibilityCategory category) {
        return main.settings.getConfig().getStringList("compatibility-checker.suppressed-categories").contains(category.toString());
    }
}
