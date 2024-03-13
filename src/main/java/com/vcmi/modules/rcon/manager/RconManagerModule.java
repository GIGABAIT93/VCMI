package com.vcmi.modules.rcon.manager;

import com.vcmi.Message;
import com.vcmi.Util;
import com.vcmi.config.data.RconManagerYAML;
import org.simpleyaml.configuration.file.YamlConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class RconManagerModule extends YamlConfiguration {

	private static YamlConfiguration config;

	public static void initialise() {
		config = RconManagerYAML.getReloadedFile();
	}

	public static void reload() {
		initialise();
	}


	public static boolean serverIs(String server) {
		return !config.getString("servers." + server).isEmpty();
	}

	public static List<String> getServers() {
		Set<String> keys = config.getConfigurationSection("servers").getKeys(false);
		return new ArrayList<>(keys);
	}

	public static Integer getPort(String server) {
		return config.getInt("servers." + server + ".port");
	}

	public static String getIP(String server) {
		return config.getString("servers." + server + ".ip");
	}

	public static String getPass(String server) {
		return config.getString("servers." + server + ".pass");
	}

	public static ArrayList<String> getCommandArgs() {
		ArrayList<String> args = new ArrayList<>();
		config.getList("tab-complete-list").forEach(arg -> args.add((String) arg));
		return args;
	}

	public static void enable() {
		reload();
		Util.registerCommand("rcon", "vurcon", new RconManagerCommand());
		Message.info("Rcon Manager module enabled");
	}

	public static void disable() {
		RconManagerCommand.unregister();
		config = null;
	}
}
