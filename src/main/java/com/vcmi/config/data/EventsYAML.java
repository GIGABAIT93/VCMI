package com.vcmi.config.data;

import com.vcmi.VCMI;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.simpleyaml.configuration.file.YamlFile;
import java.io.File;
import java.io.IOException;

public class EventsYAML {

    private static final String FILE_PATH = VCMI.pluginPath + File.separator + "events.yml";
    private static YamlFile yamlFile;

    private EventsYAML() {
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
                    "Events settings \n" +
                    "Placeholders: {player}, {server}, {fromServer}\n" +
                    "[console] - run console command\n" +
                    "[delay] (seconds) - delay seconds command"
            );

            setConfigValue("events.on_join_commands.enabled", true);
            setConfigValue("events.on_join_commands.commands", new String[]{
                    "[console] alert &6Player {player} join the game",
                    "[delay] 10",
                    "server vanilla"
            });

            setConfigValue("events.on_leave_commands.enabled", true);
            setConfigValue("events.on_leave_commands.commands", new String[]{
                    "[console] alert &6Player {player} left the game"
            });

            setConfigValue("events.on_server_switch.enabled", true);
            setConfigValue("events.on_server_switch.commands", new String[]{
                    "[console] alert &6Player {player} connected to server {server} from server {fromServer}"
            });

            setConfigValue("events.on_server_kick.enabled", true);
            setConfigValue("events.on_server_kick.commands", new String[]{
                    "[console] alert &6Player {player} kick the server {server}",
                    "[delay] 60",
                    "server {server}"
            });

            yamlFile.save();

        } catch (IOException e) {
            e.printStackTrace();
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
}
