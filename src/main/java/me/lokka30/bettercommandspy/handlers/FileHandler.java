/*
 * Copyright (c) 2020-2021 lokka30, All Rights Reserved. Use of this source code is governed by the GNU AGPL v3.0 license that can be found in BetterCommandSpy's LICENSE.md file.
 */

package me.lokka30.bettercommandspy.handlers;

import me.lokka30.bettercommandspy.BetterCommandSpy;
import me.lokka30.bettercommandspy.misc.Utils;
import me.lokka30.microlib.files.YamlConfigFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Handles file management (i.e. configs)
 *
 * @author lokka30
 * @since v2.0.0
 */
public class FileHandler {

    private final @NotNull BetterCommandSpy main;

    public FileHandler(@NotNull final BetterCommandSpy main) {
        this.main = main;
    }

    /**
     * This enum contains all the files, their file name, and their respective latest (as of this plugin's version) file-versions.
     * When file versions are changed, ensure the 'last modified' comment is also changed.
     *
     * @author lokka30
     * @since v2.0.0
     */
    public enum BCSFile {
        SETTINGS("settings.yml", 3), // Last modified v2.0.0
        MESSAGES("messages.yml", 3), // Last modified v2.0.0
        DATA("data.yml", 2), // Last modified v2.0.0
        LICENSE("license.txt", -1); // Last modified v1.0.0

        public final String fileName;
        public final int latestFileVersion; // -1 means that the file is not versioned.

        BCSFile(String fileName, int latestFileVersion) {
            this.fileName = fileName;
            this.latestFileVersion = latestFileVersion;
        }
    }

    /**
     * Start the procedure to load/reload all files from BCS.
     *
     * @author lokka30
     * @since v2.0.0
     */
    public void init() {
        for (BCSFile bcsFile : BCSFile.values()) {
            loadFile(bcsFile);
            checkFileVersion(bcsFile);
        }
    }

    private void loadFile(@NotNull BCSFile bcsFile) {
        Utils.LOGGER.info("Loading file '&b" + bcsFile.fileName + "&7'...");

        try {
            switch (bcsFile) {
                case SETTINGS:
                    main.settings.load();
                    break;
                case MESSAGES:
                    main.messages.load();
                    break;
                case DATA:
                    main.data.load();
                    break;
                case LICENSE:
                    main.saveResource("license.txt", true);
                    break;
                default:
                    throw new IllegalStateException("Unexpected state " + bcsFile);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Utils.LOGGER.info("File '&b" + bcsFile.fileName + "&7' has been loaded.");
    }

    /**
     * Back-up files that aren't the latest version.
     *
     * @param bcsFile The type of file and its version.
     * @author lokka30
     * @since v2.0.0
     */
    private void checkFileVersion(@NotNull BCSFile bcsFile) {
        YamlConfigFile configFile;

        switch (bcsFile) {
            case SETTINGS:
                configFile = main.settings;
                break;
            case MESSAGES:
                configFile = main.messages;
                break;
            case DATA:
                configFile = main.data;
                break;
            case LICENSE:
                return;
            default:
                throw new IllegalStateException("Unexpected state " + bcsFile);
        }

        if (configFile.getConfig().getInt("file.version", 0) != bcsFile.latestFileVersion) {
            Utils.LOGGER.error("File '&b" + bcsFile.fileName + "&7' is incompatible with this version of of the plugin. It has been backed up in the &b/plugins/BetterCommandSpy/backups/&7 folder, and your server will now run the latest default version of this file. You may want to consider configuring the newly generated file.");
            backup(configFile.getConfigFile());
            main.saveResource(bcsFile.fileName, true);
            loadFile(bcsFile);
        }
    }

    private void backup(@NotNull File source) {
        Utils.LOGGER.info("Starting backup of file '&b" + source.getName() + "&7'...");

        // Get the destination (backed up file) ready.
        File destination;
        final String destinationPath = main.getDataFolder() + File.separator + "backups" + File.separator + source.getName() + ".backup";

        // Don't want to overwrite an existing backup.
        int backupNumber = 1;
        while (true) {
            destination = new File(destinationPath + backupNumber);
            if (destination.exists()) {
                backupNumber++;
            } else {
                break;
            }
        }

        // Start transferring.
        try {

            // Create the backup folder if it doesn't exist.
            final File backupsFolder = new File(main.getDataFolder() + File.separator + "backups");
            if(!backupsFolder.exists() && !backupsFolder.mkdir()) {
                Utils.LOGGER.error("Unable to create backups folder. Backup for file '&b" + destination.getName() + "&7' cancelled.");
                return;
            }

            Files.copy(source.toPath(), destination.toPath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Utils.LOGGER.info("Backup complete for file '&b" + source.getName() + "&7'.");
    }
}
