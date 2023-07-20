package com.vcmi.config;

import com.vcmi.Message;
import org.simpleyaml.configuration.file.YamlConfiguration;
import com.vcmi.config.data.ConfigYAML;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static com.vcmi.VCMI.database;

public class Config extends YamlConfiguration {

	private static YamlConfiguration config;
	public static DatabaseInitializer databaseInitializer;

	public static void initialise() {
		config = ConfigYAML.getReloadedFile();
	}

	public static void reload() {
		initialise();
	}

	public static List<String> getModules() {
		Set<String> keys = config.getConfigurationSection("modules").getKeys(true);
		return new ArrayList<>(keys);
	}

	public static boolean getModules(String identifier) {
		return config.getConfigurationSection("modules").getBoolean(identifier);
	}

	public static String getLang() {
		return config.getString("language");
	}

	// Database
	public static boolean databaseEnable() {
		return config.getBoolean("database.enable");
	}
	public static String getDatabaseType() {
		return config.getString("database.type");
	}

	public static String getDatabaseName() {
		return config.getString("database.name");
	}

	public static String getDatabaseUser() {
		return config.getString("database.user");
	}

	public static String getDatabasePassword() {
		return config.getString("database.password");
	}

	public static String getDatabaseHost() {
		return config.getString("database.host");
	}

	public static int getDatabasePort() {
		return config.getInt("database.port");
	}

	public static boolean getSsl() {
		return config.getBoolean("database.use_ssl");
	}

	public static String getDatabaseTablePrefix() {
		return config.getString("database.table_prefix");
	}

	public static boolean useUUID() {
		return config.getBoolean("use_uuid");
	}

	public static void databaseInitializer(){
		if (Config.databaseEnable()){
			database = new Database();
			if (database.connect()) {
				databaseInitializer = new DatabaseInitializer(database);
				databaseInitializer.initializeTables();
			}
		}
	}
	public static void debugAllMethods() {
		Message.info("Initialising config:");
		initialise();
		Message.info("Reloading config:");
		reload();
		Message.info("Modules: " + getModules());
		getModules().forEach( m -> {
			Message.info("Modules (with specific identifier): " + m + " " + getModules(m));
		});
		Message.info("Lang: " + getLang());
		Message.info("Database type: " + getDatabaseType());
		Message.info("Database name: " + getDatabaseName());
		Message.info("Database user: " + getDatabaseUser());
		Message.info("Database password: " + getDatabasePassword());
		Message.info("Database host: " + getDatabaseHost());
		Message.info("Database port: " + getDatabasePort());
		Message.info("Database table prefix: " + getDatabaseTablePrefix());
		Message.info("Use UUID: " + useUUID());
	}
}
