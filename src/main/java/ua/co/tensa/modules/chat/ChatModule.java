package ua.co.tensa.modules.chat;

import ua.co.tensa.Message;
import ua.co.tensa.Tensa;
import net.kyori.adventure.text.Component;

public class ChatModule {

    public static void initialise() {
         ChatEventListener.reload();
         ChatCommands.reload();
    }

    public static void reload() {
        initialise();
    }

    public static void enable() {
        initialise();
        Message.info("Chat Manager module enabled");
    }

    public static void disable() {
        initialise();
    }

    public static void sendMessageToPermittedPlayers(String message, String permission) {
        Component messageFormat = Message.convert(message);
        if (permission.isEmpty()){
            Tensa.server.sendMessage(messageFormat);
            return;
        }
        Tensa.server.getAllPlayers().stream()
                .filter(player -> player.hasPermission(permission))
                .forEach(player -> player.sendMessage(messageFormat));
        Tensa.server.getConsoleCommandSource().sendMessage(messageFormat);

    }

}
