package com.vcmi.config.data;

import com.vcmi.Message;
import com.vcmi.config.Config;
import com.vcmi.VCMI;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;

public class LangYAML {

    private static String FILE_PATH;
    private static YamlFile yamlFile;

    private LangYAML() {
        throw new IllegalStateException("Utility class");
    }

    public static void reload() {
        try {
            FILE_PATH = VCMI.pluginPath + File.separator + "lang" + File.separator + getLangFile() + ".yml";
            yamlFile = new YamlFile(FILE_PATH);
            if (!yamlFile.exists()) {
                yamlFile.createNewFile(true);
            }
            yamlFile.load();

            yamlFile.setHeader(getLangFile().toUpperCase() + " localization file");

            setConfigValue("prefix", "&f[&3VCMI&f] &7");
            setConfigValue("no_perms", "&cYou do not have permission to use this command");
            setConfigValue("unknown_error", "&cUnknown error");
            setConfigValue("unknown_request", "&cUnknown request");
            setConfigValue("error_executing", "&cError executing:");
            setConfigValue("no_command", "&cNo such command");
            setConfigValue("reload", "&aAll configurations reloaded");
            setConfigValue("enabled", "&aenabled");
            setConfigValue("disabled", "&cdisabled");
            setConfigValue("module_status", "&b{module} &6is {status}");

            yamlFile.setComment("rcon_manager_reload", "Rcon Manager");
            setConfigValue("rcon_manager_reload", "&aConfigurations Rcon Manager reloaded");
            setConfigValue("rcon_auth_error", "&6{server}: &cAuthentication Error. Please check your server configuration and ensure the server is available");
            setConfigValue("rcon_io_error", "&6{server}: &cIO Error. Please check your server configuration and ensure the server is available");
            setConfigValue("rcon_unknown_error", "&6{server}: &cUnknown host error. Please check the server IP address configuration");

            yamlFile.setComment("rcon_server_reload", "Rcon Server");
            setConfigValue("rcon_server_reload", "&aConfigurations Rcon Server reloaded");
            setConfigValue("rcon_connect_notify", "&aRcon connection from: &7[&3&l{address}&7] &aCommand: &3&l{command}");
            setConfigValue("rcon_usage", "&6Usage: rcon [server/all/reload] [command]");
            setConfigValue("rcon_empty_command", "&6Command is empty!");
            setConfigValue("rcon_invalid_command_or_server", "&6Invalid command or server name");
            setConfigValue("rcon_response", "&6{server}: &a{response}");
            setConfigValue("rcon_response_empty", "There is no response from the server");

            yamlFile.setComment("bash_usage", "Bash Module");
            setConfigValue("bash_usage", "&6Usage: bash [script/reload] [info/(script args)]");
            setConfigValue("bash_runner_reload", "&aConfigurations BASH Runner reloaded");
            setConfigValue("bash_out_script", "{response}");

            yamlFile.setComment("php_usage", "Php Module");
            setConfigValue("php_usage", "&6Usage: php [script/reload] [info/(script args)]");
            setConfigValue("php_runner_reload", "&aConfigurations PHP Runner reloaded");
            setConfigValue("php_out_script", "{response}");

            yamlFile.setComment("player_time_usage", "PlayerTime Module");
            setConfigValue("player_time_usage", "&6Usage: vptime [Player]");
            setConfigValue("player_time", "&aYour game time: {time}");
            setConfigValue("player_time_other", "&aGame time {player}: {time}");
            setConfigValue("player_not_found", "&c{player} not found");
            setConfigValue("player_time_days", " days ");
            setConfigValue("player_time_hours", " hours ");
            setConfigValue("player_time_minutes", " minutes ");
            setConfigValue("player_time_seconds", " seconds ");
            setConfigValue("player_time_top", "&aTop players by time:");
            setConfigValue("player_time_top_entry", "&a{position}. &6{player} - {time}");

            yamlFile.setComment("send_usage", "Send Module");
            setConfigValue("send_usage", "&6Usage: /psend &6{player} {server}");
            setConfigValue("send_success", "&aPlayer {player} sent to server {server}");
            setConfigValue("server_not_found", "&cServer {server} not found");


            yamlFile.setComment("help", "Help");
            setConfigValue("help", "&6Available commands:"
                    + "\n&6/vcmi &7- &6Show help."
                    + "\n&6/vcmireload &7- &6Reload all configurations."
                    + "\n&6/vcmimodules &7- &6Show all modules."
                    + "\n&6/vpl &7- &6Show all plugins."
                    + "\n&6/vptime &7- &6Returns your total playing time."
                    + "\n&6/vptime [player] &7- &6Returns the specified player's total playing time."
                    + "\n&6/rcon [server/all/reload] [command] &7- &6Sends the specified command to the specified server or all servers."
                    + "\n&6/php [script/reload] [args] &7- &6Executes the specified PHP script."
                    + "\n&6/bash [script/reload] [args] &7- &6Executes the specified Bash script."
                    + "\n&6/rules &7- &6Reads the specified text file."
                    + "\n&6/readme &7- &6Reads the specified text file."
                    + "\n&6/psend [player/all] [server]&7- &6Sends the specified player to the specified server."
            );


            yamlFile.save();

        } catch (IOException e) {
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


    private static String getLangFile() {
        if (Config.getLang().isEmpty()) {
            return "en.yml";
        }
        return Config.getLang();
    }
}
