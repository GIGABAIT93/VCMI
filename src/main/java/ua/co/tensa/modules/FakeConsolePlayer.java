package ua.co.tensa.modules;

import com.velocitypowered.api.network.ProtocolState;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.ConnectionRequestBuilder;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.crypto.IdentifiedKey;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.PluginMessageEncoder;
import com.velocitypowered.api.proxy.player.PlayerSettings;
import com.velocitypowered.api.proxy.player.ResourcePackInfo;
import com.velocitypowered.api.proxy.player.TabList;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.util.GameProfile;
import com.velocitypowered.api.util.ModInfo;
import com.velocitypowered.api.util.ServerLink;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.*;

public class FakeConsolePlayer implements Player {
    private final GameProfile profile;

    public FakeConsolePlayer() {
        this.profile = GameProfile.forOfflinePlayer("Console");
    }

    @Override
    public GameProfile getGameProfile() {
        return profile;
    }


    @Override
    public void clearPlayerListHeaderAndFooter() {

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
    public @NotNull Collection<ResourcePackInfo> getAppliedResourcePacks() {
        return null;
    }

    @Override
    public @NotNull Collection<ResourcePackInfo> getPendingResourcePacks() {
        return null;
    }

    @Override
    public boolean sendPluginMessage(@NotNull ChannelIdentifier channelIdentifier, byte[] bytes) {
        return false;
    }

    @Override
    public boolean sendPluginMessage(@NotNull ChannelIdentifier identifier, @NotNull PluginMessageEncoder dataEncoder) {
        return false;
    }

    @Override
    public @Nullable String getClientBrand() {
        return null;
    }

    @Override
    public void addCustomChatCompletions(@NotNull Collection<String> collection) {

    }

    @Override
    public void removeCustomChatCompletions(@NotNull Collection<String> collection) {

    }

    @Override
    public void setCustomChatCompletions(@NotNull Collection<String> collection) {

    }

    @Override
    public void transferToHost(@NotNull InetSocketAddress address) {

    }

    @Override
    public void storeCookie(Key key, byte[] data) {

    }

    @Override
    public void requestCookie(Key key) {

    }

    @Override
    public void setServerLinks(@NotNull List<ServerLink> links) {

    }

    @Override
    public String getUsername() {
        return "Console";
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
    public boolean hasSentPlayerSettings() {
        return false;
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
    public Optional<String> getRawVirtualHost() {
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
    public ProtocolState getProtocolState() {
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
