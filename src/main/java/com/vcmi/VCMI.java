package com.vcmi;

import com.google.inject.Inject;
import com.vcmi.config.Config;
import com.vcmi.config.Database;
import com.vcmi.config.Lang;
import com.vcmi.modules.Modules;
import com.vcmi.modules.event.EventManager;
import com.vcmi.modules.rcon.server.RconServerModule;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import java.nio.file.Path;

@Plugin(
        id = "vcmi",
        name = "VCMI",
        version = "1.0.0",
        description = "VCMI - Velocity Content Manager Plugin",
        authors = { "GIGABAIT" }
)

public class VCMI {

    public static ProxyServer server;
    public static Path pluginPath;
    public static Database database;

    @Inject
    public VCMI(ProxyServer server, @DataDirectory Path dataDirectory) {
        VCMI.server = server;
        pluginPath = dataDirectory;
    }

    public static void loadPlugin() {
        Config.initialise();
        Lang.initialise();
        Modules.load();

//        if (database.exists("player_time", "name = ?", "GIGABAIT")){
//            database.deleteAsync("player_time", "name = ?", "GIGABAIT");
//        } else {
//            database.insertAsync("player_time", "name, uuid, play_time", "GIGABAIT", "SJDJDSAKLSD", "2312311212");
//        }


    }


    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {EventManager.onPlayerJoin(event);}

    @Subscribe
    public void onPlayerLeave(DisconnectEvent event) {
        EventManager.onPlayerLeave(event);
    }

    @Subscribe
    public void onPlayerKick(KickedFromServerEvent event) {
        EventManager.onPlayerKick(event);
    }

    @Subscribe
    public void onServerSwitch(ServerConnectedEvent event) {
        EventManager.onServerSwitch(event);
    }

    @Subscribe
    public void onEnable(ProxyInitializeEvent event) {
        loadPlugin();
        Message.logHeader();
    }

    @Subscribe
    public void onShutdown(ProxyShutdownEvent event) {
        RconServerModule.disable();
        if (database != null) {
            database.close();
        }
    }
}
