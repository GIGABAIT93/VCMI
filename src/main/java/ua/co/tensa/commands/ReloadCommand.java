package ua.co.tensa.commands;

import ua.co.tensa.config.Lang;
import ua.co.tensa.Tensa;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class ReloadCommand implements SimpleCommand {

	@Override
	public void execute(final Invocation invocation) {
		CommandSource source = invocation.source();
		if (!hasPermission(invocation)) {
			source.sendMessage(Lang.no_perms.get());
			return;
		}
		if (Tensa.database != null) {
			Tensa.database.close();
		}
		Tensa.loadPlugin();
		source.sendMessage(Lang.reload.get());
	}

	@Override
	public boolean hasPermission(final Invocation invocation) {
		return invocation.source().hasPermission("tensa.reload");
	}

	@Override
	public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
		return CompletableFuture.completedFuture(List.of());
	}
}
