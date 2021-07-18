package me.lokka30.bettercommandspy.v1;

import me.lokka30.microlib.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BCSCommand implements TabExecutor {

    private final BCSMain instance;

    public BCSCommand(final BCSMain instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length == 0) {
            sender.sendMessage(" ");
            sender.sendMessage(MessageUtils.colorizeAll("&f&nAbout"));
            sender.sendMessage(MessageUtils.colorizeAll("&8 &m->&7 Running &b&lCommandSpy &bv" + instance.getDescription().getVersion() + "&7, developed by &flokka30&7."));
            sender.sendMessage(MessageUtils.colorizeAll("&8 &m->&7&o '" + instance.getDescription().getDescription() + "'"));
            sender.sendMessage(" ");
            sender.sendMessage(MessageUtils.colorizeAll("&f&nAvailable Commands"));
            sender.sendMessage(MessageUtils.colorizeAll("&8 &m->&b /commandspy on [player] &8(&7enable commandspy&8)"));
            sender.sendMessage(MessageUtils.colorizeAll("&8 &m->&b /commandspy off [player] &8(&7disable commandspy&8)"));
            sender.sendMessage(MessageUtils.colorizeAll("&8 &m->&b /commandspy reload &8(&7reload commandspy configuration files&8)"));
            sender.sendMessage(" ");
        } else {
            switch (args[0].toLowerCase()) {
                case "on":
                    if (sender.hasPermission("bettercommandspy.toggle")) {
                        if (args.length == 1) {
                            if (sender instanceof Player) {
                                final Player player = (Player) sender;
                                final UUID uuid = player.getUniqueId();
                                if (instance.listeners.contains(uuid)) {
                                    sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("command.toggle_on.self-already"))
                                            .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))));
                                } else {
                                    instance.listeners.add(uuid);
                                    instance.dataCfg.set("players." + uuid + ".state", true);
                                    try {
                                        instance.dataCfg.save(instance.dataFile);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("command.toggle_on.self"))
                                            .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))));
                                }
                            } else {
                                sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("command.toggle_on.usage-console"))
                                        .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))
                                        .replace("%label%", label)));
                            }
                        } else if (args.length == 2) {
                            if (sender.hasPermission("bettercommandspy.toggle.others")) {
                                @SuppressWarnings("deprecation") final OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                                if (target.hasPlayedBefore() || target.isOnline()) {
                                    UUID uuid = target.getUniqueId();
                                    String path = "players." + uuid + ".state";
                                    if (instance.dataCfg.contains(path)) {
                                        if (instance.dataCfg.getBoolean(path)) {
                                            sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("command.toggle_on.others-already"))
                                                    .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))
                                                    .replace("%player%", Objects.requireNonNull(target.getName()))));
                                        } else {
                                            instance.dataCfg.set(path, true);
                                            try {
                                                instance.dataCfg.save(instance.dataFile);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("command.toggle_on.others"))
                                                    .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))
                                                    .replace("%player%", Objects.requireNonNull(target.getName()))));
                                            if (target.isOnline()) {
                                                final Player player = target.getPlayer();
                                                assert player != null;
                                                instance.listeners.add(player.getUniqueId());
                                                if (!player.getName().equals(sender.getName())) {
                                                    player.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("command.toggle_on.by"))
                                                            .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))
                                                            .replace("%player%", sender.getName())));
                                                }
                                            }
                                        }
                                    } else {
                                        instance.dataCfg.set(path, true);
                                        try {
                                            instance.dataCfg.save(instance.dataFile);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("commandspy.toggle_on.others"))
                                                .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))
                                                .replace("%player%", Objects.requireNonNull(target.getName()))));
                                        if (target.isOnline()) {
                                            final Player player = target.getPlayer();
                                            assert player != null;
                                            instance.listeners.add(player.getUniqueId());
                                            if (!player.getName().equals(sender.getName())) {
                                                player.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(Objects.requireNonNull(instance.messagesCfg.getString("command.toggle_on.by"))
                                                        .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))
                                                        .replace("%player%", sender.getName()))));
                                            }
                                        }
                                    }
                                } else {
                                    sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("command.player-never-joined"))
                                            .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))
                                            .replace("%player%", args[1])));
                                }
                            }
                        } else {
                            sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(Objects.requireNonNull(instance.messagesCfg.getString("command.toggle_on.usage"))
                                    .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))
                                    .replace("%label%", label))));
                        }
                    } else {
                        sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("command.no-permission"))
                                .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))));
                    }
                    break;
                case "off":
                    if (sender.hasPermission("bettercommandspy.toggle")) {
                        if (args.length == 1) {
                            if (sender instanceof Player) {
                                final Player player = (Player) sender;
                                final UUID uuid = player.getUniqueId();
                                if (instance.listeners.contains(uuid)) {
                                    instance.listeners.remove(uuid);
                                    instance.dataCfg.set("players." + uuid + ".state", false);
                                    try {
                                        instance.dataCfg.save(instance.dataFile);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("command.toggle_off.self"))
                                            .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))));
                                } else {
                                    sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("command.toggle_off.self-already"))
                                            .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))));
                                }
                            } else {
                                sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("command.toggle_off.usage-console"))
                                        .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix"))))
                                        .replace("%label%", label));
                            }
                        } else if (args.length == 2) {
                            if (sender.hasPermission("bettercommandspy.toggle.others")) {
                                @SuppressWarnings("deprecation") final OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                                if (target.hasPlayedBefore() || target.isOnline()) {
                                    UUID uuid = target.getUniqueId();
                                    String path = "players." + uuid + ".state";
                                    if (instance.dataCfg.contains(path)) {
                                        if (instance.dataCfg.getBoolean(path)) {
                                            instance.dataCfg.set(path, false);
                                            try {
                                                instance.dataCfg.save(instance.dataFile);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("command.toggle_off.others"))
                                                    .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))
                                                    .replace("%player%", Objects.requireNonNull(target.getName()))));
                                            if (target.isOnline()) {
                                                final Player player = target.getPlayer();
                                                assert player != null;
                                                instance.listeners.remove(player.getUniqueId());
                                                if (!player.getName().equals(sender.getName())) {
                                                    player.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("command.toggle_off.by"))
                                                            .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))
                                                            .replace("%player%", sender.getName())));
                                                }
                                            }
                                        } else {
                                            sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("command.toggle_off.others-already"))
                                                    .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))
                                                    .replace("%player%", Objects.requireNonNull(target.getName()))));
                                        }
                                    } else {
                                        instance.dataCfg.set(path, false);
                                        try {
                                            instance.dataCfg.save(instance.dataFile);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("command.toggle_off.others"))
                                                .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))
                                                .replace("%player%", Objects.requireNonNull(target.getName()))));
                                        if (target.isOnline()) {
                                            final Player player = target.getPlayer();
                                            assert player != null;
                                            instance.listeners.remove(player.getUniqueId());
                                            if (!player.getName().equals(sender.getName())) {
                                                player.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("command.toggle_off.by"))
                                                        .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))
                                                        .replace("%player%", sender.getName())));
                                            }
                                        }
                                    }
                                } else {
                                    sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("command.player-never-joined"))
                                            .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))
                                            .replace("%player%", args[1])));
                                }
                            }
                        } else {
                            sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("command.toggle_off.usage"))
                                    .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))
                                    .replace("%label%", label)));
                        }
                    } else {
                        sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("command.no-permission"))
                                .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))));
                    }
                    break;
                case "reload":
                    if (sender.hasPermission("bettercommandspy.reload")) {
                        sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("command.reload.start"))
                                .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))));
                        instance.loadFiles();
                        sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("command.reload.complete"))
                                .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))));
                    } else {
                        sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("command.no-permission"))
                                .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))));
                    }
                    break;
                default:
                    sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(Objects.requireNonNull(instance.messagesCfg.getString("command.main-usage"))
                            .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("general.prefix")))
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
}
