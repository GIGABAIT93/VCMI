package com.vcmi.modules.playertime;

import com.vcmi.Message;
import com.vcmi.Util;
import com.vcmi.VCMI;
import com.vcmi.config.Config;
import com.vcmi.config.Database;
import com.vcmi.config.DatabaseInitializer;

public class PlayerTimeModule {

    private static PlayerEventListener eventListener;
    public static void enable() {
        Message.info("PlayerTime module enabled");
        initialize();
    }

    public static void disable() {
        PlayerTimeCommand.unregister();
//        if (eventListener != null) {
//            VCMI.server.getEventManager().unregister(eventListener);
//        }
        Message.warn("PlayerTime module disabled");
    }

    public static void initialize() {
        if (!Config.databaseEnable()){
            Message.warn("The PlayerTime module requires the use of a database, enable it in the configuration file");
            return;
        }
        Database database = new Database();
        if (database.connect()){
            if (!database.tableExists("player_times")){
                DatabaseInitializer databaseInitializer = new DatabaseInitializer(database);
                databaseInitializer.createPlayerTimeTable();
            }
            PlayerTimeTracker timeTracker = new PlayerTimeTracker(database);
            eventListener = new PlayerEventListener(timeTracker);
            VCMI.server.getEventManager().register(VCMI.pluginContainer, eventListener);
            Util.registerCommand("vplayertime", "vptime", new PlayerTimeCommand(timeTracker));
        } else {
            Message.warn("PlayerTime module. A database connection could not be established");
            disable();
        }
    }
}
