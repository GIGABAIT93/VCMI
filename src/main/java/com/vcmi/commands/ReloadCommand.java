package com.vcmi.commands;

import com.vcmi.config.Lang;
import com.vcmi.VCMI;
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
		VCMI.loadPlugin();
		source.sendMessage(Lang.reload.get());
	}

	@Override
	public boolean hasPermission(final Invocation invocation) {
		return invocation.source().hasPermission("vcmi.reload");
	}

	@Override
	public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
		return CompletableFuture.completedFuture(List.of());
	}
}
