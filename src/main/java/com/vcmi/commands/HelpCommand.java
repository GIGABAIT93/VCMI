package com.vcmi.commands;

import com.vcmi.config.Lang;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import java.util.List;

public class HelpCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        if (!hasPermission(invocation)) {
            source.sendMessage(Lang.no_perms.get());
            return;
        }

        source.sendMessage(Lang.help.get());

    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("vcmi.help");
    }
}
