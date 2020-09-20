package me.lokka30.bettercommandspy;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BetterCommandSpy extends JavaPlugin implements Listener, TabExecutor {

    final ArrayList<UUID> listeners = new ArrayList<>();

    final File settingsFile = new File(getDataFolder(), "settings.yml");
    final File messagesFile = new File(getDataFolder(), "messages.yml");
    final File dataFile = new File(getDataFolder(), "data.yml");
    YamlConfiguration settingsCfg, messagesCfg, dataCfg;

    @Override
    public void onEnable() {
        loadFiles();
        registerEvents();
        Objects.requireNonNull(getCommand("commandspy")).setExecutor(this);
        new Metrics(this, 8907);
    }

    private void loadFiles() {
        saveResource("license.txt", true);

        createIfNotExists(settingsFile);
        settingsCfg = YamlConfiguration.loadConfiguration(settingsFile);
        checkFileVersion(settingsCfg, "settings.yml", 1);

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
            getLogger().warning(colorize("&7Configuration file '&b" + name + "&7' is not running the correct right file version for this version of BetterCommandSpy. Please regenerate or merge to the latest version of that file."));
        }
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) throws IOException {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        final String path = "players." + uuid.toString() + ".state";

        if (player.hasPermission("bettercommandspy.toggle")) {
            if (dataCfg.contains(path)) {
                if (dataCfg.getBoolean(path)) {
                    listeners.add(uuid);
                }
            } else {
                boolean state = settingsCfg.getBoolean("general.default-commandspy-state");
                dataCfg.set(path, state);
                dataCfg.save(dataFile);
                if (state) {
                    listeners.add(uuid);
                }
            }
        }
    }

    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        final String command = event.getMessage();
        if (!player.hasPermission("bettercommandspy.bypass")) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (player != onlinePlayer && listeners.contains(onlinePlayer.getUniqueId())) {
                    onlinePlayer.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("alert"))
                            .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))
                            .replace("%player%", player.getName())
                            .replace("%command%", command)));
                }
            }
        }
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length == 0) {
            sender.sendMessage(" ");
            sender.sendMessage(colorize("&f&nAbout"));
            sender.sendMessage(colorize("&8 &m->&7 Running &b&lCommandSpy &bv" + getDescription().getVersion() + "&7, developed by &flokka30&7."));
            sender.sendMessage(colorize("&8 &m->&7&o '" + getDescription().getDescription() + "'"));
            sender.sendMessage(" ");
            sender.sendMessage(colorize("&f&nAvailable Commands"));
            sender.sendMessage(colorize("&8 &m->&b /commandspy on [player] &8(&7enable commandspy&8)"));
            sender.sendMessage(colorize("&8 &m->&b /commandspy off [player] &8(&7disable commandspy&8)"));
            sender.sendMessage(colorize("&8 &m->&b /commandspy reload &8(&7reload commandspy configuration files&8)"));
            sender.sendMessage(" ");
        } else {
            switch (args[0].toLowerCase()) {
                case "on":
                    if (sender.hasPermission("bettercommandspy.toggle")) {
                        if (args.length == 1) {
                            if (sender instanceof Player) {
                                final Player player = (Player) sender;
                                final UUID uuid = player.getUniqueId();
                                if (listeners.contains(uuid)) {
                                    sender.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("command.toggle_on.self-already"))
                                            .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))));
                                } else {
                                    listeners.add(uuid);
                                    dataCfg.set("players." + uuid.toString() + ".state", true);
                                    try {
                                        dataCfg.save(dataFile);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    sender.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("command.toggle_on.self"))
                                            .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))));
                                }
                            } else {
                                sender.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("command.toggle_on.usage-console"))
                                        .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))
                                        .replace("%label%", label)));
                            }
                        } else if (args.length == 2) {
                            if (sender.hasPermission("bettercommandspy.toggle.others")) {
                                @SuppressWarnings("deprecation") final OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                                if (target.hasPlayedBefore() || target.isOnline()) {
                                    UUID uuid = target.getUniqueId();
                                    String path = "players." + uuid.toString() + ".state";
                                    if (dataCfg.contains(path)) {
                                        if (dataCfg.getBoolean(path)) {
                                            sender.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("command.toggle_on.others-already"))
                                                    .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))
                                                    .replace("%player%", Objects.requireNonNull(target.getName()))));
                                        } else {
                                            dataCfg.set(path, true);
                                            try {
                                                dataCfg.save(dataFile);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            sender.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("command.toggle_on.others"))
                                                    .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))
                                                    .replace("%player%", Objects.requireNonNull(target.getName()))));
                                            if (target.isOnline()) {
                                                final Player player = target.getPlayer();
                                                assert player != null;
                                                listeners.add(player.getUniqueId());
                                                if (!player.getName().equals(sender.getName())) {
                                                    player.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("command.toggle_on.by"))
                                                            .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))
                                                            .replace("%player%", sender.getName())));
                                                }
                                            }
                                        }
                                    } else {
                                        dataCfg.set(path, true);
                                        try {
                                            dataCfg.save(dataFile);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        sender.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("commandspy.toggle_on.others"))
                                                .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))
                                                .replace("%player%", Objects.requireNonNull(target.getName()))));
                                        if (target.isOnline()) {
                                            final Player player = target.getPlayer();
                                            assert player != null;
                                            listeners.add(player.getUniqueId());
                                            if (!player.getName().equals(sender.getName())) {
                                                player.sendMessage(colorize(Objects.requireNonNull(Objects.requireNonNull(messagesCfg.getString("command.toggle_on.by"))
                                                        .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))
                                                        .replace("%player%", sender.getName()))));
                                            }
                                        }
                                    }
                                } else {
                                    sender.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("command.player-never-joined"))
                                            .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))
                                            .replace("%player%", args[1])));
                                }
                            }
                        } else {
                            sender.sendMessage(colorize(Objects.requireNonNull(Objects.requireNonNull(messagesCfg.getString("command.toggle_on.usage"))
                                    .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))
                                    .replace("%label%", label))));
                        }
                    } else {
                        sender.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("command.no-permission"))
                                .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))));
                    }
                    break;
                case "off":
                    if (sender.hasPermission("bettercommandspy.toggle")) {
                        if (args.length == 1) {
                            if (sender instanceof Player) {
                                final Player player = (Player) sender;
                                final UUID uuid = player.getUniqueId();
                                if (listeners.contains(uuid)) {
                                    listeners.remove(uuid);
                                    dataCfg.set("players." + uuid.toString() + ".state", false);
                                    try {
                                        dataCfg.save(dataFile);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    sender.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("command.toggle_off.self"))
                                            .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))));
                                } else {
                                    sender.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("command.toggle_off.self-already"))
                                            .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))));
                                }
                            } else {
                                sender.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("command.toggle_off.usage-console"))
                                        .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix"))))
                                        .replace("%label%", label));
                            }
                        } else if (args.length == 2) {
                            if (sender.hasPermission("bettercommandspy.toggle.others")) {
                                @SuppressWarnings("deprecation") final OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                                if (target.hasPlayedBefore() || target.isOnline()) {
                                    UUID uuid = target.getUniqueId();
                                    String path = "players." + uuid.toString() + ".state";
                                    if (dataCfg.contains(path)) {
                                        if (dataCfg.getBoolean(path)) {
                                            dataCfg.set(path, false);
                                            try {
                                                dataCfg.save(dataFile);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            sender.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("command.toggle_off.others"))
                                                    .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))
                                                    .replace("%player%", Objects.requireNonNull(target.getName()))));
                                            if (target.isOnline()) {
                                                final Player player = target.getPlayer();
                                                assert player != null;
                                                listeners.remove(player.getUniqueId());
                                                if (!player.getName().equals(sender.getName())) {
                                                    player.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("command.toggle_off.by"))
                                                            .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))
                                                            .replace("%player%", sender.getName())));
                                                }
                                            }
                                        } else {
                                            sender.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("command.toggle_off.others-already"))
                                                    .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))
                                                    .replace("%player%", Objects.requireNonNull(target.getName()))));
                                        }
                                    } else {
                                        dataCfg.set(path, false);
                                        try {
                                            dataCfg.save(dataFile);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        sender.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("command.toggle_off.others"))
                                                .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))
                                                .replace("%player%", Objects.requireNonNull(target.getName()))));
                                        if (target.isOnline()) {
                                            final Player player = target.getPlayer();
                                            assert player != null;
                                            listeners.remove(player.getUniqueId());
                                            if (!player.getName().equals(sender.getName())) {
                                                player.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("command.toggle_off.by"))
                                                        .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))
                                                        .replace("%player%", sender.getName())));
                                            }
                                        }
                                    }
                                } else {
                                    sender.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("command.player-never-joined"))
                                            .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))
                                            .replace("%player%", args[1])));
                                }
                            }
                        } else {
                            sender.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("command.toggle_off.usage"))
                                    .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))
                                    .replace("%label%", label)));
                        }
                    } else {
                        sender.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("command.no-permission"))
                                .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))));
                    }
                    break;
                case "reload":
                    if (sender.hasPermission("bettercommandspy.reload")) {
                        sender.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("command.reload.start"))
                                .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))));
                        loadFiles();
                        sender.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("command.reload.complete"))
                                .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))));
                    } else {
                        sender.sendMessage(colorize(Objects.requireNonNull(messagesCfg.getString("command.no-permission"))
                                .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))));
                    }
                    break;
                case "debug_test":
                    if (messagesCfg.contains(args[1])) {
                        sender.sendMessage("Yes");
                    } else {
                        sender.sendMessage("No");
                    }
                    break;
                default:
                    sender.sendMessage(colorize(Objects.requireNonNull(Objects.requireNonNull(messagesCfg.getString("command.main-usage"))
                            .replace("%prefix%", Objects.requireNonNull(messagesCfg.getString("general.prefix")))
                            .replace("%label%", label))));
                    break;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 0) {
            suggestions.add("on");
            suggestions.add("off");
            suggestions.add("reload");
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (player.canSee(onlinePlayer)) {
                            suggestions.add(onlinePlayer.getName());
                        }
                    }
                } else {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        suggestions.add(onlinePlayer.getName());
                    }
                }
            }
        }
        return suggestions;
    }

    private String colorize(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
