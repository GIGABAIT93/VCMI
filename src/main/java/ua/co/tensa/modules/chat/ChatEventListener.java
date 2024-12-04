package ua.co.tensa.modules.chat;

import ua.co.tensa.config.Config;
import ua.co.tensa.config.data.ChatYAML;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import org.simpleyaml.configuration.file.YamlConfiguration;
import java.util.Set;

public class ChatEventListener {
    private static boolean chatEnabled = Config.getModules("chat-manager");
    private static YamlConfiguration chatConfig = ChatYAML.getInstance().getConfig();


    public static void reload() {
        chatEnabled = Config.getModules("chat-manager");
        if (chatEnabled) {
            chatConfig = ChatYAML.getInstance().getReloadedFile();
        }
    }

    @Subscribe
    public static void onPlayerMessage(PlayerChatEvent event) {
        if (!chatEnabled) {
            return;
        }
        Player player = event.getPlayer();
        Set<String> chatsKeys = chatConfig.getKeys(false);
        chatsKeys.forEach(key -> {
            String alias = chatConfig.getString(key + ".alias");
            String permission = chatConfig.getString(key + ".permission");
            if (!chatConfig.getBoolean(key + ".enabled") || (!permission.isEmpty() && !player.hasPermission(permission))) {
                return;
            }
            if (alias.isEmpty()){return;}

            String message = event.getMessage();
            if (message.startsWith(alias)) {
                event.setResult(PlayerChatEvent.ChatResult.denied());
                String messageFormat = chatConfig.getString(key + ".format");
                String messageFormatReplaced = messageFormat
                        .replace("{server}", player.getCurrentServer().isPresent() ? player.getCurrentServer().get().getServerInfo().getName() : "")
                        .replace("{player}", player.getUsername())
                        .replace("{message}", message.replace(chatConfig.getString(key + ".alias"), ""));
                if (chatConfig.getBoolean(key + ".see_all")) {
                    ChatModule.sendMessageToPermittedPlayers(messageFormatReplaced, "");
                    return;
                }
                ChatModule.sendMessageToPermittedPlayers(messageFormatReplaced, permission);
            }
        });
    }
}
