package com.vcmi.modules.requests;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.vcmi.Message;
import com.vcmi.Util;
import com.vcmi.VCMI;
import com.vcmi.config.Lang;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.util.*;

public class RequestCommand implements SimpleCommand {

	private YamlConfiguration config;

	@Override
	public void execute(Invocation invocation) {
		CommandSource sender = invocation.source();
		String[] args = invocation.arguments();
		try {
			config = RequestsModule.configByTrigger(invocation.alias());
			if (config == null) {
				sender.sendMessage(Lang.no_command.get());
				return;
			}
			if (config.get("permission") != null && !hasPermission(sender, config.getString("permission"))) {
				sender.sendMessage(Lang.no_perms.get());
				return;
			}
			runCommand(args, sender);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void runCommand(String[] args, CommandSource sender) throws Exception {
		Map<String, String> params = placeholderPrepare(args, sender);
		Map<String, String> parameters = parsePlaceholders(
				config.getConfigurationSection("parameters").getMapValues(true),
				params
		);
		HttpRequest req = new HttpRequest(
				config.getString("url"), config.getString("method"),
				parameters
		);

		JsonElement resp = req.send();


		if (resp != null){
			Map<String, String> responseParams = new Gson()
					.fromJson(resp, new TypeToken<Map<String, String>>(){}.getType());

			if (config.getBoolean("debug")) {
				sender.sendMessage(Message.convert(
						"<gold>-----------------Request Debug-----------------" +
						"\n<green>URL: <yellow>" + config.getString("url") +
						"\n<green>Method: <yellow>" + config.getString("method")
				));

				responseParams.forEach((key, value) -> {
					sender.sendMessage(Message.convert("<gray>----------------------------------------------"));
					sender.sendMessage(Message.convert(
							"<green>Key: <yellow>" + key +
							"\n<green>Value: <yellow>" + value +
							"\n<green>Placeholder: <yellow>%" + key + "%"
					));
				});
				sender.sendMessage(Message.convert("<gray>----------------------------------------------"));

			}

			List<String> successCommands = parsePlaceholdersInList(
					config.getConfigurationSection("response").getStringList("success"),
					responseParams
			);

			successCommands = parsePlaceholdersInList(successCommands, params);
			for (String command : successCommands) {
				Util.executeCommand(command);
			}
		} else {
			List<String> errorCmd = parsePlaceholdersInList(config.getConfigurationSection("response").getStringList("failure"), params);
			for (String command : errorCmd) {
				Util.executeCommand(command);
			}
		}
	}

	private Map<String, String> placeholderPrepare(String[] args, CommandSource sender) {
		Map<String, String> params = new HashMap<>();
		if (sender instanceof Player) {
			Player player = (Player) sender;
			params.put("player_name", player.getUsername());
			params.put("player_uuid", player.getUniqueId().toString());
			params.put("server",
					player.getCurrentServer().isPresent() ? player.getCurrentServer()
							.get().getServerInfo().getName() : "Not server connected");
		} else {
			params.put("player_name", "Console");
			params.put("player_uuid", "Console");
			params.put("server", "Proxy");
		}
		for (int i = 0; i < args.length; i++) {
			params.put("arg" + (i + 1), args[i]);
		}
		return params;
	}

	private Map<String, String> parsePlaceholders(Map<String, Object> map, Map<String, String> params) {
		Map<String, String> stringMap = new HashMap<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String value = entry.getValue().toString();
			for (Map.Entry<String, String> param : params.entrySet()) {
				value = value.replace("%" + param.getKey() + "%", param.getValue());
			}
			stringMap.put(entry.getKey(), value);
		}
		return stringMap;
	}

	private List<String> parsePlaceholdersInList(List<String> list, Map<String, String> params) {
		if (list == null || params == null) {
			return Collections.emptyList();
		}
		List<String> parsedList = new ArrayList<>();
		for (String item : list) {
			if (item == null) {
				item = "null";
			}
			String parsedItem = item;
			for (Map.Entry<String, String> param : params.entrySet()) {
				if (param.getKey() == null || param.getValue() == null) {
					continue;
				}
				parsedItem = parsedItem.replace("%" + param.getKey() + "%", param.getValue());
			}
			parsedList.add(parsedItem);
		}
		return parsedList;
	}

	private boolean hasPermission(final CommandSource sender, String permission) {
		return sender.hasPermission(permission) || sender.hasPermission("VCMI.requests.*");
	}

	public static void unregister() {
		CommandManager manager = VCMI.server.getCommandManager();
		List<Map<String, String>> triggers = RequestsModule.getTriggerToFileMapping();
		for (Map<String, String> triggerMap : triggers) {
			String trigger = triggerMap.get("trigger");
			manager.unregister(trigger);
		}
	}
}
