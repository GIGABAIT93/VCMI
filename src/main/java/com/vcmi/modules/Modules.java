package com.vcmi.modules;

import com.vcmi.config.Config;
import com.vcmi.Message;
import com.vcmi.Util;
import com.vcmi.commands.ReloadCommand;
import com.vcmi.modules.bash.BashModule;
import com.vcmi.modules.event.EventsModule;
import com.vcmi.modules.php.PhpModule;
import com.vcmi.modules.rcon.manager.RconManagerModule;
import com.vcmi.modules.rcon.server.RconServerModule;
import com.vcmi.modules.requests.RequestsModule;
import com.vcmi.modules.text.TextReaderModule;

import java.util.Map;

public class Modules {
    private static final Map<String, ModuleConfig> MODULES = Map.of(
            "rcon-manager", new ModuleConfig(RconManagerModule::enable, RconManagerModule::disable),
            "rcon-server", new ModuleConfig(RconServerModule::enable, RconServerModule::disable),
            "php-runner", new ModuleConfig(PhpModule::enable, PhpModule::disable),
            "bash-runner", new ModuleConfig(BashModule::enable, BashModule::disable),
            "events-manager", new ModuleConfig(EventsModule::enable, EventsModule::disable),
            "request-module",  new ModuleConfig(RequestsModule::enable, RequestsModule::disable),
            "text-reader",  new ModuleConfig(TextReaderModule::enable, TextReaderModule::disable)
    );

    public Modules() {
        Util.registerCommand("vcmireload", "vcreload", new ReloadCommand());
        Message.info("...");
        Message.info("VCMI loading modules...");
        Config.databaseInitializer();
        Config.getModules().forEach(this::loadModules);
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

