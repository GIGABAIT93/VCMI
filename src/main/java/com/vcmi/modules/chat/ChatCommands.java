package com.vcmi.modules.chat;

import com.vcmi.Util;
import com.vcmi.config.Config;
import com.vcmi.config.Lang;
import com.vcmi.config.data.ChatYAML;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import org.simpleyaml.configuration.file.YamlConfiguration;
import java.util.Arrays;
import java.util.Set;

public class ChatCommands implements SimpleCommand {

    private static boolean chatEnabled = Config.getModules("chat-manager");
    private static YamlConfiguration chatConfig = ChatYAML.getConfig();

    public static void reload() {
        chatConfig = ChatYAML.getReloadedFile();
        unregister();
        register();
    }

    public static void register() {
        if (!chatEnabled) {
            return;
        }
        chatConfig.getKeys(false).stream()
                .filter(key -> chatConfig.getBoolean(key + ".enabled"))
                .forEach(key -> Util.registerCommand(chatConfig.getString(key + ".command"), "", new ChatCommands()));
    }

    public static void unregister() {
        boolean all = !chatEnabled;
        chatConfig.getKeys(false).stream()
                .filter(key -> !chatConfig.getBoolean(key + ".enabled") || all)
                .forEach(key -> Util.unregisterCommand(chatConfig.getString(key + ".command")));
    }

    @Override
    public void execute(Invocation invocation) {
        if (!chatEnabled) {
            return;
        }
        String server;
        String playerName;
        if (invocation.source() instanceof Player) {
            Player player = (Player) invocation.source();
            server = player.getCurrentServer().get().getServerInfo().getName();
            playerName = player.getUsername();
        } else {
            server = "";
            playerName = "";
        }

        Set<String> chats = chatConfig.getKeys(false);
        String command = invocation.alias();
        chats.forEach(key -> {
            if (chatConfig.getBoolean(key + ".enabled") && command.equals(chatConfig.getString(key + ".command"))) {
                String permission = chatConfig.getString(key + ".permission");
                if (!permission.isEmpty() && !invocation.source().hasPermission(permission)) {
                    invocation.source().sendMessage(Lang.no_perms.get());
                    return;
                }
                String message = Arrays.stream(invocation.arguments()).map(arg -> arg + " ").reduce("", String::concat);
                String messageFormat = chatConfig.getString(key + ".format");
                String messageFormatReplaced = messageFormat
                        .replace("{server}", server)
                        .replace("{player}", playerName)
                        .replace("{message}", message);
                if (chatConfig.getBoolean(key + ".see_all")) {
                    ChatModule.sendMessageToPermittedPlayers(messageFormatReplaced, "");
                    return;
                }
                ChatModule.sendMessageToPermittedPlayers(messageFormatReplaced, permission);
            }
        });
    }
}
