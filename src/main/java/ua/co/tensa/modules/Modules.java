package ua.co.tensa.modules;

import ua.co.tensa.commands.*;
import ua.co.tensa.config.Config;
import ua.co.tensa.Message;
import ua.co.tensa.Util;
import ua.co.tensa.modules.bash.BashModule;
import ua.co.tensa.modules.chat.ChatModule;
import ua.co.tensa.modules.event.EventsModule;
import ua.co.tensa.modules.php.PhpModule;
import ua.co.tensa.modules.playertime.PlayerTimeModule;
import ua.co.tensa.modules.rcon.manager.RconManagerModule;
import ua.co.tensa.modules.rcon.server.RconServerModule;
import ua.co.tensa.modules.requests.RequestsModule;
import ua.co.tensa.modules.text.TextReaderModule;
import java.util.Map;

public class Modules {
    private static final Map<String, ModuleConfig> MODULES = Map.of(
            "rcon-manager", new ModuleConfig(RconManagerModule::enable, RconManagerModule::disable),
            "rcon-server", new ModuleConfig(RconServerModule::enable, RconServerModule::disable),
            "php-runner", new ModuleConfig(PhpModule::enable, PhpModule::disable),
            "bash-runner", new ModuleConfig(BashModule::enable, BashModule::disable),
            "events-manager", new ModuleConfig(EventsModule::enable, EventsModule::disable),
            "request-module",  new ModuleConfig(RequestsModule::enable, RequestsModule::disable),
            "player-time",  new ModuleConfig(PlayerTimeModule::enable, PlayerTimeModule::disable),
            "text-reader",  new ModuleConfig(TextReaderModule::enable, TextReaderModule::disable),
            "chat-manager",  new ModuleConfig(ChatModule::enable, ChatModule::disable)
    );

    public Modules() {
        Message.info("...");
        Message.info("TENSA loading modules...");
        Config.databaseInitializer();
        Config.getModules().forEach(this::loadModules);
        registerCommands();
    }

    public static void load() {
        new Modules();
    }

    private void loadModules(String module) {
        if (Config.getModules(module)) {
            MODULES.get(module).enable();
        } else {
            MODULES.get(module).disableIfEnabled();
        }
    }

    private void registerCommands() {
        Util.registerCommand("tensareload", "treload", new ReloadCommand());
        Util.registerCommand("tensa", "tensahelp", new HelpCommand());
        Util.registerCommand("tensamodules", "tmodules", new ModulesCommand());
        Util.registerCommand("vpl", "vplugins", new PluginsCommand());
        Util.registerCommand("psend", "vpsend", new PlayerSendCommand());
    }

    private static class ModuleConfig {
        private final Runnable enableAction;
        private final Runnable disableAction;
        private boolean isEnabled = false;

        ModuleConfig(Runnable enableAction, Runnable disableAction) {
            this.enableAction = enableAction;
            this.disableAction = disableAction;
        }

        void enable() {
            enableAction.run();
            isEnabled = true;
        }

        void disableIfEnabled() {
            if (isEnabled) {
                disableAction.run();
                isEnabled = false;
            }
        }
    }
}

