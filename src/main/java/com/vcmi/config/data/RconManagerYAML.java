package com.vcmi.config.data;

import com.vcmi.Message;
import com.vcmi.VCMI;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.simpleyaml.configuration.file.YamlFile;
import java.io.File;
import java.io.IOException;

public class RconManagerYAML {

    private static final String FILE_PATH = VCMI.pluginPath + File.separator + "rcon" + File.separator + "rcon-manager.yml";
    private static YamlFile yamlFile;

    private RconManagerYAML() {
        throw new IllegalStateException("Utility class");
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

    public static void reload() {
        try {
            yamlFile = new YamlFile(FILE_PATH);
            if (!yamlFile.exists()) {
                yamlFile.createNewFile(true);
            }
            yamlFile.load();
            populateConfigFile();
            yamlFile.save();
        } catch (IOException e) {
            Message.error(e.getMessage());
        }
    }

    private static void populateConfigFile() {
        yamlFile.setHeader(
                "Rcon servers\n" +
                "To allow the use of a separate server for a player, use permission:\n" +
                "vcmi.rcon.serve_name\n" +
                "Examples: vcmi.rcon.lobby, vcmi.rcon.vanilla"
        );

        setConfigValue("servers.lobby.ip", "0.0.0.0");
        setConfigValue("servers.lobby.port", 25566);
        setConfigValue("servers.lobby.pass", "rcon password");

        yamlFile.setComment("tab-complete-list", "List of rcon server command arguments");
        setConfigValue("tab-complete-list", new String[]{
                "alert",
                "list",
                "tps"
        });
    }
}
