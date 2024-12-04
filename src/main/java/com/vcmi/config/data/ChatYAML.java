package com.vcmi.config.data;

public class ChatYAML extends BaseYAMLConfig {

    private static ChatYAML instance;

    private ChatYAML() {
        super("chats.yml");
    }

    public static ChatYAML getInstance() {
        if (instance == null) {
            instance = new ChatYAML();
        }
        return instance;
    }

    @Override
    protected void populateConfigFile() {
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
    }
}
