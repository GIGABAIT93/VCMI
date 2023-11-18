package com.vcmi.config.data;

import com.vcmi.VCMI;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.simpleyaml.configuration.file.YamlFile;
import java.io.File;
import java.io.IOException;

public class ConfigYAML {
    private static final String FILE_PATH = VCMI.pluginPath + File.separator + "config.yml";
    private static YamlFile yamlFile;

    private ConfigYAML() {
        throw new IllegalStateException("Utility class");
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
            e.printStackTrace();
        }
    }

    private static void populateConfigFile() {
        yamlFile.setHeader("General Configuration File");

        setConfigValue("language", "en");

        yamlFile.setComment("use_uuid", "Use UUID to store player data. If false, the nickname will be used");
        setConfigValue("use_uuid", false);
        populateModules();
        populateDatabase();
    }

    private static void populateModules() {
        yamlFile.setBlankLine("modules");

        yamlFile.setComment("modules.rcon-manager", "Rcon manager. Allows sending RCON commands to other servers");
        setConfigValue("modules.rcon-manager", true);

        yamlFile.setComment("modules.rcon-server", "Rcon server. Enables Velocity RCON server to run");
        setConfigValue("modules.rcon-server", true);

        yamlFile.setComment("modules.php-runner", "PHP Runner. Allows you to download the PHP script");
        setConfigValue("modules.php-runner", true);

        yamlFile.setComment("modules.bash-runner", "BASH Runner. Allows you to download the BASH script");
        setConfigValue("modules.bash-runner", true);

        yamlFile.setComment("modules.events-manager", "Events manager. Allows you to use an event handler");
        setConfigValue("modules.events-manager", true);

        yamlFile.setComment("modules.request-module", "Request module. Allows you to perform HTTP requests");
        setConfigValue("modules.request-module", true);

        yamlFile.setComment("modules.player-time", "Player Time module. Keeps statistics of the player's playing time");
        setConfigValue("modules.player-time", true);

        yamlFile.setComment("modules.text-reader", "Text Reader module. Allows reading and outputting text files and chat");
        setConfigValue("modules.text-reader", true);


    }

    private static void populateDatabase() {
        yamlFile.setBlankLine("database");
        yamlFile.setComment("database.type", "only \"mysql\"");
        setConfigValue("database.enable", false);
        setConfigValue("database.type", "sqlite");
        setConfigValue("database.name", "server");
        setConfigValue("database.user", "root");
        setConfigValue("database.password", "password");
        setConfigValue("database.host", "localhost");
        setConfigValue("database.port", "3306");
        setConfigValue("database.use_ssl", false);
        setConfigValue("database.table_prefix", "vcmi_");
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
}