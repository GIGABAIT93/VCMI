package com.vcmi.commands;

import com.vcmi.config.Lang;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.vcmi.config.Config;
import net.kyori.adventure.text.Component;

import java.util.List;

public class ModulesCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        if (!hasPermission(invocation)) {
            source.sendMessage(Lang.no_perms.get());
            return;
        }

        List<String> allModules = Config.getModules();

        allModules.forEach(module -> {
            String moduleName = capitalizeWords(module.toUpperCase().replace('-', ' '));
            // If enable green else red
            Component message = Config.getModules(module) ? Lang.modules.replace("{module}", moduleName, "{status}", "&aenabled") : Lang.modules.replace("{module}", moduleName, "{status}", "&cdisabled");
            source.sendMessage(message);
        });
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("vcmi.modules");
    }

    private static String capitalizeWords(String input) {
        String[] words = input.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase()).append(" ");
        }
        return sb.toString().trim();
    }
}
