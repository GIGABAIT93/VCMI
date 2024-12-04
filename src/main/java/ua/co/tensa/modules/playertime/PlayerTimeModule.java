package ua.co.tensa.modules.playertime;

import ua.co.tensa.Message;
import ua.co.tensa.Util;
import ua.co.tensa.Tensa;
import ua.co.tensa.config.Config;
import ua.co.tensa.config.Database;
import ua.co.tensa.config.DatabaseInitializer;
import ua.co.tensa.config.Lang;

import java.util.concurrent.TimeUnit;

public class PlayerTimeModule {

    public static void enable() {
        Message.info("PlayerTime module enabled");
        initialize();
    }

    public static void disable() {
        PlayerTimeCommand.unregister();
        PlayerTimeTopCommand.unregister();
        Message.warn("PlayerTime module disabled");
    }

    public static void initialize() {
        if (!Config.databaseEnable()){
            Message.warn("The PlayerTime module requires the use of a database, enable it in the configuration file");
            return;
        }
        Database database = Tensa.database;
        if (database.enabled){
            if (!database.tableExists("player_times")){
                DatabaseInitializer databaseInitializer = new DatabaseInitializer(database);
                databaseInitializer.createPlayerTimeTable();
            }
            PlayerTimeTracker timeTracker = new PlayerTimeTracker(database);
            PlayerEventListener eventListener = new PlayerEventListener(timeTracker);
            Tensa.server.getEventManager().register(Tensa.pluginContainer, eventListener);
            Util.registerCommand("vplayertime", "vptime", new PlayerTimeCommand(timeTracker));
            Util.registerCommand("vplayertop", "vptop", new PlayerTimeTopCommand(timeTracker));


            Tensa.server.getScheduler().buildTask(Tensa.pluginContainer, timeTracker::updateAllOnlineTimes)
                    .delay(1, TimeUnit.MINUTES)
                    .repeat(1, TimeUnit.MINUTES)
                    .schedule();
        } else {
            Message.warn("PlayerTime module. A database connection could not be established");
            disable();
        }
    }

    public static String formatTime(long timeMillis) {
        long seconds = timeMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        seconds %= 60;
        minutes %= 60;
        hours %= 24;

        StringBuilder timeBuilder = new StringBuilder();
        if (days > 0) {
            timeBuilder.append(days).append(Lang.player_time_days.getClean());
        }
        if (hours > 0) {
            timeBuilder.append(hours).append(Lang.player_time_hours.getClean());
        }
        if (minutes > 0) {
            timeBuilder.append(minutes).append(Lang.player_time_minutes.getClean());
        }
        if (seconds > 0 || timeBuilder.length() == 0) {
            timeBuilder.append(seconds).append(Lang.player_time_seconds.getClean());
        }
        return timeBuilder.toString();
    }
}
