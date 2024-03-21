package com.vcmi.modules.chat;

import com.vcmi.Message;
import com.vcmi.VCMI;
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
            VCMI.server.sendMessage(messageFormat);
            return;
        }
        VCMI.server.getAllPlayers().stream()
                .filter(player -> player.hasPermission(permission))
                .forEach(player -> player.sendMessage(messageFormat));
        VCMI.server.getConsoleCommandSource().sendMessage(messageFormat);

    }

}
