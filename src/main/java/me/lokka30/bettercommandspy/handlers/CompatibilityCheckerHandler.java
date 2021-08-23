/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy.handlers;

import me.lokka30.bettercommandspy.BetterCommandSpy;
import org.bukkit.command.CommandSender;

/**
 * This class hosts all the methods used to
 * try inform the user of any known incompatibilities
 * detected through their current server configuration.
 *
 * @author lokka30
 * @since v2.0.0
 */
public class CompatibilityCheckerHandler {

    private final BetterCommandSpy main;

    public CompatibilityCheckerHandler(BetterCommandSpy main) {
        this.main = main;
    }

    //TODO

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

    public void scan() {
        //TODO
    }

    public void presentFindings(CommandSender recipient) {
        //TODO
    }

    private boolean isCompatibilityCategoryEnabled(CompatibilityCategory category) {
        //TODO
        return true;
    }
}
