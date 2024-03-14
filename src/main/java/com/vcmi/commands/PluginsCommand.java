package com.vcmi.commands;
import com.vcmi.Message;
import com.vcmi.VCMI;
import com.vcmi.config.Lang;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.plugin.PluginContainer;
import java.util.Collection;
import java.util.stream.Collectors;

public class PluginsCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        if (!hasPermission(invocation)) {
            source.sendMessage(Lang.no_perms.get());
            return;
        }
        Collection<PluginContainer> plugins = VCMI.server.getPluginManager().getPlugins();

        if (args.length == 1 && args[0].equals("-v")) {
            String pluginList = plugins.stream()
                    .map(plugin -> "&6"+plugin.getDescription().getName().orElse("Unknown") + " &7" + plugin.getDescription().getVersion().orElse("Unknown"))
                    .collect(Collectors.joining(", "));
            source.sendMessage(Message.convert("&aPlugins: " + pluginList));
            return;
        }
        String pluginList = plugins.stream()
                .map(plugin -> "&6"+plugin.getDescription().getName().orElse("Unknown"))
                .collect(java.util.stream.Collectors.joining(", "));
        source.sendMessage(Message.convert("&aPlugins: &6" + pluginList));
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("vcmi.plugins");
    }
}
