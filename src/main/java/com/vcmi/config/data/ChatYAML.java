package com.vcmi.config.data;

import com.vcmi.Message;
import com.vcmi.VCMI;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;

public class ChatYAML {
    private static final String FILE_PATH = VCMI.pluginPath + File.separator + "chats.yml";
    private static YamlFile yamlFile;

    private ChatYAML() {
        throw new IllegalStateException("Utility class");
    }

    public static void reload() {
        try {
            yamlFile = new YamlFile(FILE_PATH);
            if (!yamlFile.exists()) {
                yamlFile.createNewFile(true);
            }
            yamlFile.load();

            yamlFile.setHeader(
                    "Chat Manager \n" + "Placeholders: {player}, {server}, {message}"
            );

            yamlFile.setComment("global", "Global chat");
            setConfigValue("global.enabled", true);
            setConfigValue("global.alias", "!");
            setConfigValue("global.command", "g");
            yamlFile.setComment("global.permission", "If empty, everyone can use this chat and see the messages");
            setConfigValue("global.permission", "");
            setConfigValue("global.see_all", true);
            setConfigValue("global.format", "&8[&6G&8] &a{player} &6=> &f{message}");

            yamlFile.setComment("staff", "Staff chat");
            setConfigValue("staff.enabled", true);
            setConfigValue("staff.alias", "@");
            setConfigValue("staff.command", "s");
            setConfigValue("staff.permission", "vcmi.chat.staff");
            setConfigValue("staff.see_all", false);
            setConfigValue("staff.format", "&8&l[&4&lS&8&l] &b&l{server} &a&l{player} &6&l=> &f&l{message}");

            yamlFile.setComment("alert", "Alert chat");
            setConfigValue("alert.enabled", true);
            setConfigValue("alert.alias", "");
            setConfigValue("alert.command", "alert");
            setConfigValue("alert.permission", "");
            setConfigValue("alert.see_all", true);
            setConfigValue("alert.format", "&8[&4ALERT&8] &f{message}");

            yamlFile.save();
        } catch (Exception e) {
            Message.error(e.getMessage());
        }
    }

    private static void setConfigValue(String path, Object defaultValue) {
        if (!yamlFile.contains(path)) {
            yamlFile.set(path, defaultValue);
        }
    }

    public static YamlConfiguration getReloadedFile() {
        reload();
        return new YamlConfiguration(yamlFile);
    }

    public static YamlConfiguration getConfig() {
        return new YamlConfiguration(yamlFile);
    }
}
