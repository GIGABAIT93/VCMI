package ua.co.tensa.modules.playertime;

import ua.co.tensa.Message;
import ua.co.tensa.Tensa;
import ua.co.tensa.config.Lang;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

import java.sql.SQLException;
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

        if (!sender.hasPermission("tensa.playertime")) {
            sender.sendMessage(Lang.no_perms.get());
            return;
        }

        if (args.length == 0 && sender instanceof Player) {
            Player player = (Player) sender;
            try {
                timeTracker.getCurrentPlayerTime(player.getUniqueId()).thenAccept(result -> {
                    try {
                        if (result.next()) {
                            sender.sendMessage(Lang.player_time.replace("{time}", PlayerTimeModule.formatTime(Long.parseLong(result.getString(1)))));
                        }
                        result.close();
                    } catch (SQLException e) {
                        Message.error(e.getMessage());
                    }
                });
            } catch (SQLException e) {
                Message.error(e.getMessage());
            }
        } else if (args.length == 1 && sender.hasPermission("TENSA.playertime.admin")) {
            String playerName = args[0];
            timeTracker.getPlayerTimeByName(playerName).thenAccept(result -> {
                try {
                    if (result.next()) {
                        sender.sendMessage(Lang.player_time_other.replace("{player}", playerName, "{time}", PlayerTimeModule.formatTime(Long.parseLong(result.getString(1)))));
                    } else {
                        sender.sendMessage(Lang.player_not_found.replace("{player}", playerName));
                    }
                    result.close();
                } catch (SQLException e) {
                    Message.error(e.getMessage());
                }
            });
        } else {
            sender.sendMessage(Lang.player_time_usage.get());
        }
    }

    public static void unregister() {
        CommandManager manager = Tensa.server.getCommandManager();
        manager.unregister("vplayertime");
        manager.unregister("vptime");
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        ArrayList<String> suggestions = new ArrayList<>();
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (args.length == 0 && hasAdminPermission(source)) {
            for (Player player : Tensa.server.getAllPlayers()) {
                suggestions.add(player.getUsername().trim());
            }
        }
        return CompletableFuture.completedFuture(suggestions);
    }

    private boolean hasAdminPermission(CommandSource source) {
        return source.hasPermission("tensa.playertime.admin");
    }
}
