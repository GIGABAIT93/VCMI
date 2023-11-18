package com.vcmi.modules.playertime;

import com.vcmi.VCMI;
import com.vcmi.config.Lang;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class PlayerTimeCommand implements SimpleCommand {

    private final PlayerTimeTracker timeTracker;

    public PlayerTimeCommand(PlayerTimeTracker timeTracker) {
        this.timeTracker = timeTracker;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();

        if (!sender.hasPermission("VCMI.playertime")) {
            sender.sendMessage(Lang.no_perms.get());
            return;
        }

        if (args.length == 0 && sender instanceof Player) {
            Player player = (Player) sender;
            long onlineTime = timeTracker.getOnlineTime(player.getUniqueId());
            sender.sendMessage(Lang.player_time.replace("{time}", formatTime(onlineTime)));
        } else if (args.length == 1 && sender.hasPermission("VCMI.playertime.admin")) {
            String playerName = args[0];
            long onlineTime = timeTracker.getPlayerTimeByName(playerName);
            if (onlineTime == 0) {
                sender.sendMessage(Lang.player_not_found.replace("{player}", playerName));
            } else {
                sender.sendMessage(Lang.player_time_other.replace("{player}", playerName, "{time}", formatTime(onlineTime)));
            }
        } else {
            sender.sendMessage(Lang.player_time_usage.get());
        }
    }

    public static void unregister() {
        CommandManager manager = VCMI.server.getCommandManager();
        manager.unregister("vplayertime");
        manager.unregister("vptime");
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        ArrayList<String> suggestions = new ArrayList<>();
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length == 0 && hasAdminPermission(source)) {
            for (Player player : VCMI.server.getAllPlayers()) {
                suggestions.add(player.getUsername().trim());
            }
        }
        return CompletableFuture.completedFuture(suggestions);
    }

    private boolean hasAdminPermission(CommandSource source) {
        return source.hasPermission("VCMI.playertime.admin");
    }

    private String formatTime(long timeMillis) {
        long seconds = timeMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        seconds %= 60;
        minutes %= 60;
        hours %= 24;

        StringBuilder timeBuilder = new StringBuilder();
        if (days > 0) {
            timeBuilder.append(days).append(Lang.player_time_days.getClean());
        }
        if (hours > 0) {
            timeBuilder.append(hours).append(Lang.player_time_hours.getClean());
        }
        if (minutes > 0) {
            timeBuilder.append(minutes).append(Lang.player_time_minutes.getClean());
        }
        if (seconds > 0 || timeBuilder.length() == 0) {
            timeBuilder.append(seconds).append(Lang.player_time_seconds.getClean());
        }
        return timeBuilder.toString();
    }
}
