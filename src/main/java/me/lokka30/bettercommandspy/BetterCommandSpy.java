package me.lokka30.bettercommandspy;

import me.lokka30.microlib.MicroLogger;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class BetterCommandSpy extends JavaPlugin {

    final MicroLogger logger = new MicroLogger("&b&lBetterCommandSpy: &7");

    final ArrayList<UUID> listeners = new ArrayList<>();

    final File settingsFile = new File(getDataFolder(), "settings.yml");
    final File messagesFile = new File(getDataFolder(), "messages.yml");
    final File dataFile = new File(getDataFolder(), "data.yml");
    YamlConfiguration settingsCfg, messagesCfg, dataCfg;

    @Override
    public void onEnable() {
        loadFiles();
        registerEvents();
        Objects.requireNonNull(getCommand("commandspy")).setExecutor(new BCSCommand(this));
        new Metrics(this, 8907);
    }

    public void loadFiles() {
        saveResource("license.txt", true);

        createIfNotExists(settingsFile);
        settingsCfg = YamlConfiguration.loadConfiguration(settingsFile);
        checkFileVersion(settingsCfg, "settings.yml", 2);

        createIfNotExists(messagesFile);
        messagesCfg = YamlConfiguration.loadConfiguration(messagesFile);
        checkFileVersion(messagesCfg, "messages.yml", 2);

        createIfNotExists(dataFile);
        dataCfg = YamlConfiguration.loadConfiguration(dataFile);
        checkFileVersion(dataCfg, "data.yml", 1);
    }

    private void createIfNotExists(File file) {
        if (!file.exists()) {
            saveResource(file.getName(), false);
        }
    }

    private void checkFileVersion(YamlConfiguration cfg, String name, int recommendedVersion) {
        if (cfg.getInt("advanced.file-version") != recommendedVersion) {
            logger.warning("&7Configuration file '&b" + name + "&7' is not running the correct right file version for this version of the plugin. Please regenerate or merge to the latest version of that file, else it is likely errors will occur!");
        }
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new BCSListeners(this), this);
    }

}
