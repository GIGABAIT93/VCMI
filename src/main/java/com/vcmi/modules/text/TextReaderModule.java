package com.vcmi.modules.text;

import com.vcmi.Message;
import com.vcmi.Util;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import static com.vcmi.VCMI.pluginPath;


public class TextReaderModule {

    private static final Path dir = pluginPath.resolve("text");

    public static void load() {
        if (dir.toFile().mkdirs()){
            Util.copyFile(dir.toString(), "rules.txt");
        }
        Message.info("Text Reader module enabled");
    }

    public static void enable() {
        load();
        for (String cmd: getTxtFileNamesWithoutExtension()) {
            Util.registerCommand(cmd, cmd, new TextReaderCommand());
        }
    }

    public static void disable() {
        TextReaderCommand.unregister();
    }

    public static String readTxt(String filename) throws IOException {
        return Files.readString(dir.resolve(filename + ".txt"), StandardCharsets.UTF_8);
    }

    public static String[] getTxtFileNamesWithoutExtension() {
        File directory = new File(dir.toUri());
        if (directory.exists() && directory.isDirectory()) {
            String[] fileNames = directory.list((dir, name) -> name.endsWith(".txt"));
            if (fileNames != null) {
                for (int i = 0; i < fileNames.length; i++) {
                    fileNames[i] = fileNames[i].substring(0, fileNames[i].length() - 4);
                }
                return fileNames;
            }
        }
        return new String[]{};
    }
}
