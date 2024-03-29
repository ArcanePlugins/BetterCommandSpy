/*
 * Copyright (c) 2020-2022  lokka30.
 * This file is/was present in BetterCommandSpy's source code.
 * Learn more about BetterCommandSpy and its licensing at:
 * <https://github.com/lokka30/BetterCommandSpy>
 */

package me.lokka30.bettercommandspy.commands.bettercommandspy.subcommands;

import me.lokka30.bettercommandspy.BetterCommandSpy;
import me.lokka30.bettercommandspy.commands.Subcommand;
import me.lokka30.bettercommandspy.handlers.UpdateCheckerHandler;
import me.lokka30.microlib.maths.QuickTimer;
import me.lokka30.microlib.messaging.MultiMessage;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author lokka30
 * @since v2.0.0
 * <p>
 * This command reloads the plugin's configs.
 * <p>
 * cmd: /bcs reload
 * arg: -    0
 * len: 0    1
 */
public class ReloadSubcommand implements Subcommand {

    @Override
    public void run(@NotNull BetterCommandSpy main, @NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {

        // ensure sender has required permission
        if (!sender.hasPermission("bettercommandspy.command.bettercommandspy.reload")) {
            new MultiMessage(main.messages.getConfig().getStringList("commands.common.no-permission"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix", "BCS:"), true),
                    new MultiMessage.Placeholder("permission", "bettercommandspy.command.bettercommandspy.reload", false)
            )).send(sender);
            return;
        }

        // ensure correct usage
        if (args.length != 1) {
            new MultiMessage(main.messages.getConfig().getStringList("commands.bettercommandspy.subcommands.reload.usage"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix", "BCS:"), true),
                    new MultiMessage.Placeholder("label", label, false)
            )).send(sender);
            return;
        }

        // start timer
        final QuickTimer timer = new QuickTimer(TimeUnit.MILLISECONDS);

        // send 'starting' msg
        new MultiMessage(main.messages.getConfig().getStringList("commands.bettercommandspy.subcommands.reload.start"), Collections.singletonList(
                new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix", "BCS:"), true)
        )).send(sender);

        // reload files
        main.loadFiles();

        // reload update checker
        main.updateCheckerHandler.initStage1(UpdateCheckerHandler.UpdateCheckReason.FROM_RELOAD);

        // send 'done' msg
        new MultiMessage(main.messages.getConfig().getStringList("commands.bettercommandspy.subcommands.reload.finish"), Arrays.asList(
                new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("prefix", "BCS:"), true),
                new MultiMessage.Placeholder("time", timer.getDuration() + "", false)
        )).send(sender);
    }

}
