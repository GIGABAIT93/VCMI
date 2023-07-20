package com.vcmi.config;

public class DatabaseInitializer {

    private final Database database;

    public DatabaseInitializer(Database database) {
        this.database = database;
    }

    public void initializeTables() {
        createPlayerTimeTable();
    }
    private void createPlayerTimeTable() {
        String columns = "id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), uuid VARCHAR(255), play_time BIGINT";
        database.createTableAsync("player_time", columns);
    }



//    private void createPlayerPlayTimeTable() {
//        String columns = "id INT PRIMARY KEY, name VARCHAR(255), uuid VARCHAR(255), play_time INT";
//        database.createTableAsync("players_time", columns)
//                .thenAccept(success -> {
//                    if (success) {
//                        System.out.println("Player play time table created");
//                    } else {
//                        System.out.println("Failed to create player play time table");
//                    }
//                });
//    }
}
