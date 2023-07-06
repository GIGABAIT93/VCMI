package com.vcmi.modules.requests;

import com.vcmi.Message;
import com.vcmi.Util;
import com.vcmi.VCMI;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class RequestsModule extends YamlConfiguration {

	private static List<YamlConfiguration> configs;

	private static final String folder = VCMI.rootPath + File.separator + "requests";

	public static void load() {
		File directory = new File(folder);
		if (!directory.exists()) {
			directory.mkdirs();
			Util.copyFile(folder, "linkaccount.yml");
		}

		List<String> fileNames = getConfigurationFiles(folder);

		configs = new ArrayList<>();
		for (String fileName : fileNames) {
			File file = new File(folder, fileName);
			if (file.isFile()) {
				YamlFile config = new YamlFile(file);
				try {
					config.load();
					configs.add(config);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

			}
		}
	}

	public static void enable() {
		load();
		List<Map<String, String>> triggers = getTriggerToFileMapping();
		for (Map<String, String> triggerMap : triggers) {
			String trigger = triggerMap.get("trigger");
			Util.registerCommand(trigger, trigger, new RequestCommand());
		}
		Message.info("Requests module enabled");
	}

	public static void disable() {
		RequestCommand.unregister();
	}

	private static List<String> getConfigurationFiles(String directory) {
		return Arrays.stream(new File(directory).listFiles()).filter(File::isFile).map(File::getName)
				.collect(Collectors.toList());
	}

	public static List<Map<String, String>> getTriggerToFileMapping() {
		List<Map<String, String>> result = new ArrayList<>();
		for (YamlConfiguration config : configs) {
			List<String> triggers = config.getStringList("triggers");
			for (String trigger : triggers) {
				Map<String, String> map = new HashMap<>();
				map.put("trigger", trigger);
				map.put("file", config.getCurrentPath());
				result.add(map);
			}
		}
		return result;
	}

	public static YamlConfiguration configByTrigger(String trigger) {
		for (YamlConfiguration config : configs) {
			List<String> triggers = config.getStringList("triggers");
			if (triggers.contains(trigger)) {
				return config;
			}
		}
		return null;
	}

	public static List<String> getRequestsFiles() {
		return getConfigurationFiles(folder);
	}

	public static YamlConfiguration config(String filename) {
		for (YamlConfiguration config : configs) {
			if (config.getCurrentPath().equals(filename)) {
				return config;
			}
		}
		return null;
	}
}