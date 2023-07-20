package com.vcmi.config.data;

import com.vcmi.VCMI;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.simpleyaml.configuration.file.YamlFile;
import java.io.File;
import java.io.IOException;

public class RconServerYAML {
    private static final String FILE_PATH = VCMI.pluginPath + File.separator + "rcon" + File.separator + "rcon-server.yml";
    private static YamlFile yamlFile;

    private RconServerYAML() {
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
            e.printStackTrace();
        }
    }

    private static void populateConfigFile() {
        yamlFile.setHeader(
                "Rcon server settings\n" +
                "To receive RCON server connection notifications: velocityutil.rcon.notify"
        );

        yamlFile.setComment("port", "Rcon port");
        setConfigValue("port", "25570");

        yamlFile.setComment("password", "Rcon password");
        setConfigValue("password", "password");

        yamlFile.setComment("colored", "The response is colored or not");
        setConfigValue("colored", true);

    }
}
