package com.vcmi;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.ConnectionRequestBuilder;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.crypto.IdentifiedKey;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.player.PlayerSettings;
import com.velocitypowered.api.proxy.player.ResourcePackInfo;
import com.velocitypowered.api.proxy.player.TabList;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.util.GameProfile;
import com.velocitypowered.api.util.ModInfo;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
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

        } catch (IOException exception) {
            exception.printStackTrace();
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

    public static class FakeConsolePlayer implements Player {
        private final GameProfile profile;

        public FakeConsolePlayer() {
            this.profile = GameProfile.forOfflinePlayer("Console");
        }

        @Override
        public GameProfile getGameProfile() {
            return profile;
        }

        @Override
        public void clearHeaderAndFooter() {

        }

        @Override
        public Component getPlayerListHeader() {
            return null;
        }

        @Override
        public Component getPlayerListFooter() {
            return null;
        }

        @Override
        public TabList getTabList() {
            return null;
        }

        @Override
        public void disconnect(Component component) {

        }

        @Override
        public void spoofChatInput(String s) {

        }

        @Override
        public void sendResourcePack(String s) {

        }

        @Override
        public void sendResourcePack(String s, byte[] bytes) {

        }

        @Override
        public void sendResourcePackOffer(ResourcePackInfo resourcePackInfo) {

        }

        @Override
        public @Nullable ResourcePackInfo getAppliedResourcePack() {
            return null;
        }

        @Override
        public @Nullable ResourcePackInfo getPendingResourcePack() {
            return null;
        }

        @Override
        public boolean sendPluginMessage(ChannelIdentifier channelIdentifier, byte[] bytes) {
            return false;
        }

        @Override
        public @Nullable String getClientBrand() {
            return null;
        }

        @Override
        public String getUsername() {
            return null;
        }

        @Override
        public @Nullable Locale getEffectiveLocale() {
            return null;
        }

        @Override
        public void setEffectiveLocale(Locale locale) {

        }

        @Override
        public UUID getUniqueId() {
            return null;
        }

        @Override
        public Optional<ServerConnection> getCurrentServer() {
            return Optional.empty();
        }

        @Override
        public PlayerSettings getPlayerSettings() {
            return null;
        }

        @Override
        public Optional<ModInfo> getModInfo() {
            return Optional.empty();
        }

        @Override
        public long getPing() {
            return 0;
        }

        @Override
        public boolean isOnlineMode() {
            return false;
        }

        @Override
        public ConnectionRequestBuilder createConnectionRequest(RegisteredServer registeredServer) {
            return null;
        }

        @Override
        public List<GameProfile.Property> getGameProfileProperties() {
            return null;
        }

        @Override
        public void setGameProfileProperties(List<GameProfile.Property> list) {

        }

        @Override
        public Tristate getPermissionValue(String s) {
            return null;
        }

        @Override
        public InetSocketAddress getRemoteAddress() {
            return null;
        }

        @Override
        public Optional<InetSocketAddress> getVirtualHost() {
            return Optional.empty();
        }

        @Override
        public boolean isActive() {
            return false;
        }

        @Override
        public ProtocolVersion getProtocolVersion() {
            return null;
        }

        @Override
        public IdentifiedKey getIdentifiedKey() {
            return null;
        }

        @Override
        public @NotNull Identity identity() {
            return null;
        }
    }
}
