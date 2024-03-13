package com.vcmi;

import com.vcmi.modules.FakeConsolePlayer;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.vcmi.VCMI.server;

public class Util {

    public static void executeCommand(final String command) {
        server.getCommandManager().executeAsync(server.getConsoleCommandSource(), command);
    }

    public static void executeCommand(Player player, final String command) {
        server.getCommandManager().executeAsync(player, command);
    }

    public static void registerCommand(String command, String alias, SimpleCommand CommandClass) {
        CommandManager commandManager = server.getCommandManager();
        CommandMeta commandMeta = commandManager.metaBuilder(command).aliases(alias).plugin(server).build();
        commandManager.register(commandMeta, CommandClass);

    }

    public static String capitalize(String text) {
        return text.toUpperCase().charAt(0) + text.substring(1);
    }

    public static void copyFile(String toPath, String fileName) {
        File file = new File(toPath + File.separator + fileName);

        if (file.exists()) {
            return;
        }

        try (InputStream input = VCMI.class.getResourceAsStream("/" + file.getName())) {

            if (input != null) {
                Files.copy(input, file.toPath());
            } else {
                file.createNewFile();
            }

        } catch (IOException e) {
            Message.error(e.getMessage());
        }
    }

    public static void createDir(String path) {
        File dir = Path.of(path).toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static Map<String, ScriptsData> getScriptsData(Path dirPath, String isDirFileName) {

        ArrayList<ScriptsData> data = new ArrayList<>();
        getScripts(dirPath, true).forEach(fileName -> {
            String path = dirPath.toString();
            String absolutePath, commandName, extension;
            File file = new File(path + File.separator + fileName);
            if (file.exists()) {
                path = file.toPath().toString();
            }
            if (Files.isDirectory(Paths.get(path))) {
                commandName = fileName;
                absolutePath = path + File.separator + isDirFileName;
                Path run = Path.of(absolutePath);
                if (!run.toFile().exists()) {
                    copyFile(path, isDirFileName);
                }
                data.add(new ScriptsData(commandName, path, getFileExtension(absolutePath), absolutePath, true));
            } else {
                commandName = fileName.replace("." + getFileExtension(fileName), "");
                absolutePath = path;
                path = path.replace("." + fileName, "");
                extension = getFileExtension(fileName);
                if (extension.isEmpty()) {
                    extension = null;
                }
                data.add(new ScriptsData(commandName, path, extension, absolutePath, false));
            }
        });

        Map<String, ScriptsData> scriptsData = new HashMap<>();
        data.forEach(res -> scriptsData.put(res.cmd_name, res));
        return scriptsData;
    }

    public static ArrayList<String> getScripts(Path dirPath, boolean exception) {
        String[] scripts = dirPath.toFile().list();
        assert scripts != null;
        if (!exception) {
            ArrayList<String> args = new ArrayList<>();
            for (String fileName : scripts) {
                File file = new File(dirPath.toUri() + File.separator + fileName);
                if (file.isDirectory()) {
                    args.add(fileName);
                    continue;
                }
                args.add(fileName.replace(getFileExtension(fileName), "").replace(".", ""));
            }
            return args;
        }
        return new ArrayList<>(Arrays.asList(scripts));
    }

    public static ArrayList<String> getScripts(Path dirPath) {
        return getScripts(dirPath, false);
    }

    public static String getFileExtension(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("fileName must not be null!");
        }

        String extension = "";

        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            extension = fileName.substring(index + 1);
        }

        return extension;

    }

    public static Player getFakeConsolePlayer() {
        return new FakeConsolePlayer();
    }

    public static class ScriptsData {
        public String cmd_name;
        public String path;
        public String exception;
        public String absolutePath;
        public boolean isDir;

        public ScriptsData(String cmd_name, String path, String exception, String absolutePath, boolean isDir) {
            this.cmd_name = cmd_name;
            this.path = path;
            this.exception = exception;
            this.absolutePath = absolutePath;
            this.isDir = isDir;
        }
    }


}
