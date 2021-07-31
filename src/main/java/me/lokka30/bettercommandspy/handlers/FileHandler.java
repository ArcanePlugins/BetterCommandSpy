/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy.handlers;

import me.lokka30.bettercommandspy.BetterCommandSpy;

/**
 * Handles file management (i.e. configs)
 *
 * @author lokka30
 * @since v2.0.0
 */
public class FileHandler {

    private final BetterCommandSpy main;
    public FileHandler(final BetterCommandSpy main) {
        this.main = main;
    }

    public enum Files {
        SETTINGS("settings.yml", 3), // Last modified v2.0.0
        MESSAGES("messages.yml", 3), // Last modified v2.0.0
        DATA("data.yml", 1), // Last modified v1.0.0
        LICENSE("license.txt", -1); // Last modified v1.0.0

        public final String fileName;
        public final int latestFileVersion; // -1 means that the file is not versioned.

        Files(String fileName, int latestFileVersion) {
            this.fileName = fileName;
            this.latestFileVersion = latestFileVersion;
        }
    }

    public void load() {
        //TODO
    }
}
