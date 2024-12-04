package ua.co.tensa.modules.bash;

import ua.co.tensa.config.Lang;
import ua.co.tensa.Tensa;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static ua.co.tensa.modules.bash.BashModule.scriptsData;
import static ua.co.tensa.modules.bash.BashModule.scriptsList;

public class BASHCommand implements SimpleCommand {

	@Override
	public void execute(Invocation invocation) {
		CommandSource sender = invocation.source();
		String[] args = invocation.arguments();

		if (args.length == 0) {
			sender.sendMessage(Lang.bash_usage.get());
			return;
		}
		if (!hasPermission(sender, args[0])) {
			sender.sendMessage(Lang.no_perms.get());
			return;
		}
		// Reload command
		if (args.length == 1 && args[0].equals("reload")) {
			if (hasPermission(sender, "reload")) {
				BashModule.load();
				sender.sendMessage(Lang.bash_runner_reload.get());
			} else {
				sender.sendMessage(Lang.no_perms.get());
			}
			return;
		}

		if (scriptsData.get(args[0]) == null) {
			sender.sendMessage(Lang.no_command.get());
			return;
		}

		// Info command
		if (args.length == 2 && args[1].equals("info")) {
			if (!scriptsData.containsKey(args[0])) {
				sender.sendMessage(Lang.no_command.get());
				return;
			}
			sender.sendMessage(
					Lang.debug.text("&7-------------------" + Lang.prefix.getClean() + "&7-------------------"));
			sender.sendMessage(Lang.debug.text("&aCommand: &3" + scriptsData.get(args[0]).cmd_name));
			sender.sendMessage(Lang.debug.text("&aException: &3" + scriptsData.get(args[0]).exception));
			sender.sendMessage(Lang.debug.text("&aPath: &3" + scriptsData.get(args[0]).path));
			sender.sendMessage(Lang.debug.text("&aAbsolutePath: &3" + scriptsData.get(args[0]).absolutePath));
			sender.sendMessage(Lang.debug.text("&aIsDir: &3" + scriptsData.get(args[0]).isDir));
			sender.sendMessage(
					Lang.debug.text("&7-------------------" + Lang.prefix.getClean() + "&7-------------------"));
			return;
		}

		// Runner code
		try {
			runCommand(args, sender);
		} catch (InterruptedException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void runCommand(String[] args, CommandSource sender) throws InterruptedException, IOException {
		ArrayList<String> cmd = new ArrayList<>();
		cmd.add("bash");
		cmd.add(scriptsData.get(args[0]).absolutePath);
		if (args.length > 1) {
			for (String arg : args) {
				if (!args[0].equals(arg)) {
					cmd.add(arg);
				}
			}
		}
		ProcessBuilder builder = new ProcessBuilder(cmd);
		builder.redirectErrorStream(true);
		Process process = builder.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			sender.sendMessage(Lang.bash_out_script.replace("{response}", line));
		}
		process.isAlive();
	}

	@Override
	public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
		ArrayList<String> args = new ArrayList<>();
		int argNum = invocation.arguments().length;
		if (argNum == 0) {
			args = scriptsList;
			args.add("reload");
		}
		if (argNum == 2) {
			args.add("info");
		}
		if (argNum >= 2) {
			for (Player player : Tensa.server.getAllPlayers()) {
				args.add(player.getUsername().trim());
			}
		}
		return CompletableFuture.completedFuture(args);
	}

	public boolean hasPermission(final CommandSource sender, String permission) {
		return sender.hasPermission("TENSA.bash." + permission) || sender.hasPermission("TENSA.bash.*");
	}

	public static void unregister() {
		CommandManager manager = Tensa.server.getCommandManager();
		manager.unregister("bash");
	}
}
