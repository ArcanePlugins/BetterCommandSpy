package me.lokka30.bettercommandspy.OLD_CODE_REMOVE_ME;

import me.lokka30.microlib.MessageUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BCSListeners implements Listener {

    private final BCSMain instance;

    public BCSListeners(final BCSMain instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) throws IOException {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        final String path = "players." + uuid + ".state";

        if (player.hasPermission("bettercommandspy.toggle")) {
            if (instance.dataCfg.contains(path)) {
                if (instance.dataCfg.getBoolean(path)) {
                    instance.listeners.add(uuid);
                }
            } else {
                boolean state = instance.settingsCfg.getBoolean("general.default-commandspy-state");
                instance.dataCfg.set(path, state);
                instance.dataCfg.save(instance.dataFile);
                if (state) {
                    instance.listeners.add(uuid);
                }
            }
        }
    }

    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        final String command = event.getMessage();

        if (player.hasPermission("bettercommandspy.bypass")) {
            return;
        }

        if (isCommandIgnored(command)) {
            return;
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (player != onlinePlayer && instance.listeners.contains(onlinePlayer.getUniqueId())) {

                String alertMessage = instance.messagesCfg.getString("alert");
                Validate.notNull(alertMessage, "Message 'alert' in the 'messages.yml' file must be specified");

                onlinePlayer.sendMessage(MessageUtils.colorizeAll(alertMessage)
                        .replace("%prefix%", MessageUtils.colorizeAll(Objects.requireNonNull(instance.messagesCfg.getString("general.prefix"))))
                        .replace("%player%", player.getName())
                        .replace("%displayname%", player.getDisplayName())
                        .replace("%command%", command));
            }
        }
    }

    public boolean isCommandIgnored(final String command) {
        if (!instance.settingsCfg.getBoolean("general.ignored-commands.enabled")) {
            return false;
        }

        final List<String> commandsList = instance.settingsCfg.getStringList("general.ignored-commands.list");
        final String listType = instance.settingsCfg.getString("general.ignored-commands.type");
        Validate.notNull(listType, "List type must be 'BLACKLIST' or 'WHITELIST'.");

        switch (listType.toUpperCase()) {
            case "WHITELIST":
                return commandsList.contains(command);
            case "BLACKLIST":
                return !commandsList.contains(command);
            default:
                instance.getLogger().severe("The 'ignored commands' section in the 'settings.yml' file has an invalid 'type' specified - must be 'BLACKLIST' or 'WHITELIST'. The ignored commands function will be disabled until you fix your config!");
                return false;
        }
    }

}
