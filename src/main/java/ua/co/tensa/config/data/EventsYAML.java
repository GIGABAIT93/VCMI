package ua.co.tensa.config.data;

public class EventsYAML extends BaseYAMLConfig {

    private static EventsYAML instance;

    private EventsYAML() {
        super("events.yml");
    }

    public static EventsYAML getInstance() {
        if (instance == null) {
            instance = new EventsYAML();
        }
        return instance;
    }

    @Override
    protected void populateConfigFile() {
        yamlFile.setHeader(
                "Events settings \n" +
                        "Placeholders: {player}, {server}, {fromServer}\n" +
                        "[console] - run console command\n" +
                        "[delay] (seconds) - delay seconds command"
        );

        setConfigValue("events.on_join_commands.enabled", true);
        setConfigValue("events.on_join_commands.commands", new String[]{
                "[console] alert &6Player {player} join the game",
                "[delay] 10",
                "server vanilla"
        });

        setConfigValue("events.on_leave_commands.enabled", true);
        setConfigValue("events.on_leave_commands.commands", new String[]{
                "[console] alert &6Player {player} left the game"
        });

        setConfigValue("events.on_server_switch.enabled", true);
        setConfigValue("events.on_server_switch.commands", new String[]{
                "[console] alert &6Player {player} connected to server {server} from server {fromServer}"
        });

        setConfigValue("events.on_server_kick.enabled", true);
        setConfigValue("events.on_server_kick.commands", new String[]{
                "[console] alert &6Player {player} kick the server {server}",
                "[delay] 60",
                "server {server}"
        });

        setConfigValue("events.on_server_running.enabled", true);
        setConfigValue("events.on_server_running.commands", new String[]{
                "[console] alert &6Server {server} is running"
        });

        setConfigValue("events.on_server_stop.enabled", true);
        setConfigValue("events.on_server_stop.commands", new String[]{
                "[console] alert &6Server {server} is stop"
        });
    }
}
