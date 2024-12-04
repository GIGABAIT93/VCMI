package ua.co.tensa.config.data;

import ua.co.tensa.Message;
import ua.co.tensa.Tensa;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;

public abstract class BaseYAMLConfig {
    protected YamlFile yamlFile;
    protected final String FILE_PATH;

    protected BaseYAMLConfig(String relativePath) {
        this.FILE_PATH = Tensa.pluginPath + File.separator + relativePath;
        this.yamlFile = new YamlFile(FILE_PATH);
    }

    public void reload() {
        try {
            if (!yamlFile.exists()) {
                yamlFile.createNewFile(true);
                yamlFile.load();
                populateConfigFile();
                yamlFile.save();
            } else {
                yamlFile.load();
            }
        } catch (IOException e) {
            Message.error(e.getMessage());
        }
    }

    protected abstract void populateConfigFile();

    protected void setConfigValue(String path, Object defaultValue) {
        if (!yamlFile.contains(path)) {
            yamlFile.set(path, defaultValue);
        }
    }

    public YamlConfiguration getReloadedFile() {
        reload();
        return new YamlConfiguration(yamlFile);
    }

    public YamlConfiguration getConfig() {
        return new YamlConfiguration(yamlFile);
    }
}
