package com.vcmi.modules.text;

import com.vcmi.Message;
import com.vcmi.VCMI;
import com.vcmi.config.Lang;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class TextReaderCommand implements SimpleCommand {



    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String filename = invocation.alias();

        if (!hasPermission(invocation, filename)) {
            source.sendMessage(Lang.no_perms.get());
            return;
        }
        try {
            for (String line : TextReaderModule.readTxt(filename).split("\r\n")) {
                if (line.contains("[center]")) {
                    line = line.replace("[center]", "");
                    source.sendMessage(Message.convert(centerText(line)));
                } else {
                    source.sendMessage(Message.convert(line));
                }
            }
        } catch (IOException e) {
            Message.error(e.getMessage());
            Message.error("An error occurred while reading the text file!");
        }
    }

    public String centerText(String text){
        int maxWidthPx = 65;
        text = text.trim();
        int length = text.replaceAll("<[^>]*>", "")
                .replaceAll("&[a-f0-9]", "")
                .length();
        int spaces = (maxWidthPx - length) / 2;

        StringBuilder centeredText = new StringBuilder();
        for (int i = 0; i < spaces; i++) {
            centeredText.append(" ");
        }
        centeredText.append(text);
        return centeredText.toString();
    }


    public boolean hasPermission(final Invocation invocation, String arg) {
        return invocation.source().hasPermission("vcmi.text." + arg);
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        return CompletableFuture.completedFuture(List.of(TextReaderModule.getTxtFileNamesWithoutExtension()));
    }

    public static void unregister() {
        CommandManager manager = VCMI.server.getCommandManager();
        for (String cmd : TextReaderModule.getTxtFileNamesWithoutExtension()) {
            manager.unregister(cmd);
        }
    }
}

