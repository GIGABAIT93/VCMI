package com.vcmi;

import com.velocitypowered.api.plugin.Plugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;


public class Message {
    private static final String prefix = "<white>[<dark_aqua>VCMI<white>] <gray>";
    private static final String warningSuffix = "<yellow>[WARNING] <gold>";
    private static final String errorSuffix = "<yellow>[ERROR] <dark_red>";

    public static Component convert(String message) {

        if (message.contains("<") && message.contains(">")) {
            return MiniMessage.miniMessage().deserialize(message);
        } else {
            return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
        }
    }

    public static String convertLegacyToMiniMessage(String legacyMessage) {
        Component legacyComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(legacyMessage);
        return MiniMessage.miniMessage().serialize(legacyComponent);
    }


    private static void sendMessageWithPrefix(String prefix, String message) {
        message = prefix + message;
        for (String string : message.split("\n")) {
            VCMI.server.getConsoleCommandSource().sendMessage(convert(string));
        }
    }

    private static void send(String message) {
        for (String string : message.split("\n")) {
            VCMI.server.getConsoleCommandSource().sendMessage(convert(string));
        }
    }

    public static void info(String message) {
        sendMessageWithPrefix(prefix, message);
    }

    public static void info(String message, boolean removePrefix) {
        if (removePrefix) {
            send(message);
        } else {
            sendMessageWithPrefix(prefix, message);
        }
    }

    public static void warn(String message) {
        sendMessageWithPrefix(prefix + warningSuffix, message);
    }

    public static void error(String message) {
        sendMessageWithPrefix(prefix + errorSuffix, message);
    }

    public static void logHeader() {
        String header = "<green>----------------------------------";
        info(header);
        info("<green>    +==================+");
        info("<green>    |       VCMI       |");
        info("<green>    +==================+");
        info(header);
        info("<green>    <dark_aqua>Current version: <green>" + VCMI.class.getAnnotation(Plugin.class).version());
        info("<green>    <dark_aqua>Author: <green>GIGABAIT");
        info(header);
    }
}
