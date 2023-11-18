package com.vcmi.modules.playertime;

import com.vcmi.config.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class PlayerTimeTracker {
    private final HashMap<UUID, Long> playerOnlineTime;
    private final Database database;

    public PlayerTimeTracker(Database database) {
        this.playerOnlineTime = new HashMap<>();
        this.database = database;
    }

    public void playerJoined(UUID playerId, String playerName) {
        playerOnlineTime.put(playerId, System.currentTimeMillis());
        updatePlayerNameInDatabase(playerId, playerName);
    }


    public void playerLeft(UUID playerId) {
        Long joinTime = playerOnlineTime.get(playerId);
        if (joinTime != null) {
            long totalTimeOnline = System.currentTimeMillis() - joinTime;
            updatePlayerTimeInDatabase(playerId, totalTimeOnline);
            playerOnlineTime.remove(playerId);
        }
    }

    private void updatePlayerNameInDatabase(UUID playerId, String playerName) {
        if (database.exists("player_times", "uuid = ?", playerId.toString())) {
            database.updateAsync("player_times", "name = ?", "uuid = ?", playerName, playerId.toString());
        } else {
            database.insertAsync("player_times", "uuid, name, play_time", playerId.toString(), playerName, 0);
        }
    }

    private void updatePlayerTimeInDatabase(UUID playerId, long timeOnline) {
        database.updateAsync("player_times", "play_time = play_time + ?", "uuid = ?", timeOnline, playerId.toString());
    }

    public long getOnlineTime(UUID playerId) {
        try {
            return getCurrentPlayerTime(playerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long getPlayerTimeByName(String playerName) {
        try {
            ResultSet rs = database.select("player_times", "play_time", "name = ?", playerName);
            if (rs.next()) {
                return rs.getLong("play_time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private long getCurrentPlayerTime(UUID playerId) throws SQLException {
        ResultSet rs = database.select("player_times", "play_time", "uuid = ?", playerId.toString());
        if (rs.next()) {
            return rs.getLong("play_time");
        }
        return 0;
    }
}
