package ua.co.tensa.modules.event;

import ua.co.tensa.Message;
import ua.co.tensa.config.Config;
import ua.co.tensa.Util;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.proxy.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static ua.co.tensa.modules.event.EventsModule.Events.*;

public class EventManager {

    private static boolean eventsEnabled = Config.getModules("events-manager");
    private static final String DELAY = "[delay]";
    private static final String CONSOLE = "[console]";
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void reload() {
        eventsEnabled = Config.getModules("events-manager");
    }

    private static void sendCommand(Player player, String command, boolean console) {
        if (console) {
            Util.executeCommand(command);
        } else {
            Util.executeCommand(player, command);
        }
    }

    private static List<String> commandsPrepare(List<String> commands, String player, String server, String preServer) {
        List<String> cmd = new ArrayList<>();
        for (Object command : commands) {
            cmd.add(command.toString().replace("{player}", player).replace("{server}", server).replace("{fromServer}",
                    preServer));
        }
        return cmd;
    }

    private static void runnable(Player player, List<String> commands, String currentServerName, String preServer) {
        executorService.submit(() -> {
            for (String command : commandsPrepare(commands, player.getUsername(), currentServerName, preServer)) {
                if (command.contains(DELAY)) {
                    try {
                        TimeUnit.SECONDS.sleep(Integer.parseInt(command.replace(DELAY, "").trim()));
                    } catch (InterruptedException e1) {
                        Message.error(e1.getMessage());
                    }
                    continue;
                }
                if (command.contains(CONSOLE)) {
                    sendCommand(player, command.replace(CONSOLE, "").trim(), true);
                } else {
                    sendCommand(player, command, false);
                }
            }
        });
    }

    private static void executeCommands(List<String> commands) {
        for (String command : commandsPrepare(commands, "", "", "")) {
            if (command.contains(DELAY)) {
                try {
                    TimeUnit.SECONDS.sleep(Integer.parseInt(command.replace(DELAY, "").trim()));
                } catch (InterruptedException e) {
                    Message.error(e.getMessage());
                }
                continue;
            }
            Util.executeCommand(command.replace(CONSOLE, "").trim());
        }
    }

    private static String getCurrentServerName(Player player) {
        return player.getCurrentServer().map(serverConnection -> serverConnection.getServerInfo().getName()).orElse("");
    }

    @SuppressWarnings("unchecked")
    public static void onPlayerJoin(PostLoginEvent event) {
        if (!eventsEnabled || !on_join_commands.enabled()) {
            return;
        }
        runnable(event.getPlayer(), on_join_commands.commands(), getCurrentServerName(event.getPlayer()), "");
    }

    @SuppressWarnings("unchecked")
    public static void onPlayerLeave(DisconnectEvent event) {
        if (!eventsEnabled || !on_leave_commands.enabled()) {
            return;
        }
        runnable(event.getPlayer(), on_leave_commands.commands(), getCurrentServerName(event.getPlayer()), "");
    }

    @SuppressWarnings("unchecked")
    public static void onPlayerKick(KickedFromServerEvent event) {
        if (!eventsEnabled || !on_server_kick.enabled()) {
            return;
        }
        runnable(event.getPlayer(), on_server_kick.commands(), event.getServer().getServerInfo().getName(), "");
    }

    @SuppressWarnings("unchecked")
    public static void onServerSwitch(ServerConnectedEvent event) {
        if (!eventsEnabled || !on_server_switch.enabled() || event.getPreviousServer().isEmpty()) {
            return;
        }
        String currentServerName = event.getServer().getServerInfo().getName();
        String preServer = event.getPreviousServer().map(serverConnection -> serverConnection.getServerInfo().getName())
                .orElse("");
        runnable(event.getPlayer(), on_server_switch.commands(), currentServerName, preServer);
    }

    @SuppressWarnings("unchecked")
    public static void onServerRunning(ProxyInitializeEvent event) {
        if (!eventsEnabled || !on_server_running.enabled()) {
            return;
        }
        executeCommands(on_server_running.commands());
    }

    @SuppressWarnings("unchecked")
    public static void onServerStop(ProxyShutdownEvent event) {
        if (!eventsEnabled || !on_server_stop.enabled()) {
            return;
        }
        executeCommands(on_server_stop.commands());
    }

}
