package me.lokka30.bettercommandspy.commands;

import me.lokka30.bettercommandspy.BetterCommandSpy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * TODO
 */
public class BetterCommandSpyCommand implements TabExecutor {

    //TODO

    private final BetterCommandSpy main;

    public BetterCommandSpyCommand(final BetterCommandSpy main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
