
# VCMI Plugin

VCMI Velocity Plugin is a comprehensive tool designed to enhance the functionality of your Minecraft server, offering a variety of modules for detailed server management and control. Each module caters to different server needs, with options to enable or disable them as required.

## Features

VCMI Plugin provides several key modules, each designed with specific functionalities to improve your server's operation and user experience.


## Modules
### PlayerTime:
Tracks each player's playing time using a database. This module offers commands to toggle its functionality on or off.
- `/vptime`: Returns the player's total playing time.
- `/vptime <player>`: Returns the specified player's total playing time.

### RconManager:
Enables execution of RCON commands on remote servers, utilizing a configuration file for server data storage.
- `/rcon <server/all/reload> <commad>`: Sends the specified command to the specified server or all servers.
```yaml
# Rcon servers
# To allow the use of a separate server for a player, use permission:
# vcmi.rcon.serve_name
# Examples: vcmi.rcon.lobby, vcmi.rcon.vanilla

servers:
  lobby:
    ip: 0.0.0.0
    port: 25575
    pass: asdjsldka;lskd;laskd;lasd
  vanilla:
    ip: 0.0.0.0
    port: 25576
    pass: ksdfkldkldadurjfsdjkjasdksasdasdasds
# List of rcon server command arguments
tab-complete-list:
  - alert
  - list
  - tps

```

### RconServer:
Establishes an RCON server capable of receiving commands from remote clients, facilitating external server management.
```yaml
# Rcon server settings
# Rcon port
port: 25570
# Rcon password
password: gdashgdashdfasghdfasghdfa
# The response is colored or not
colored: true
```

### PhpModule:
Offers the ability to execute PHP scripts, extending the server's functionality with PHP's scripting capabilities.
- `/php <script/reload> <args>`: Executes the specified PHP script.
```php
<?php
// index.php
$argv_line = "";

foreach ($argv as $key => $value) {
  if ($key == 0) {
    continue;
  }
  $argv_line .= " " . $value;
}
echo "&6--------------------------------------------------------------";
echo "\n&7You have run the script: &3&l$argv[0]\n&7With arguments:&3&l$argv_line";
echo "\n&6--------------------------------------------------------------\n";

// echo json_encode($argv);
```

### BashModule:
Provides a direct interface for server administrators to execute Bash scripts via in-game chat or console.
- `/bash <script/reload> <args>`: Executes the specified Bash script.
```shell
#!/bin/bash
# run.sh
# Command: bash run shell <command>
str=""
for arg in "$@"; do
  str="$str $arg"
done
echo "&6--------------------------------------------------------------"
if [[ $1 == "shell" ]]; then
  find="shell"
  replace=""
  result=${str//$find/$replace}
  $result
  echo "&6--------------------------------------------------------------"
  exit
fi
echo "&7You have run the script: &3&l$0
&7With arguments:&3&l$str"
echo "&6--------------------------------------------------------------"
```

### TextReader:
Reads and outputs the contents of text files located in the "text" folder to players, providing a way to share information directly through the server.
- `/<file>`: File name to read and output its contents.
```txt
# rules.txt
[center]<gray>--------------[<green>Server Rules<gray>]--------------<reset>

[center]<green>This is a test version of text centering</green>
[center]<yellow>Text centering may not work correctly</yellow>

[center]<gold>We can use as standard formatting</gold>
[center]<gold>text and MiniMessage</gold>

<gold>To open this file in the chat, just write the name of this file</gold>

<yellow>You can create many such files and their names will be registered as commands to open the file in the chat</yellow>

<gold>To allow players to use the command grant the right: vcmi.text.{filename}</gold>
```

### HttpRequest:
Performs HTTP requests to specified URLs, supporting both GET and POST requests. Configuration files are used to manage request parameters. Allows you to execute commands depending on the HTTP request response.
```yaml
# The URL of the API to be queried.
url: "https://domain.com/api/minecraft/link"

# HTTP request method. Can be "GET" or "POST".
method: "GET"

# Query parameters. They will be sent to the API with the request. If the parameters are not used, this field can be deleted.
# You can use placeholders that will be automatically replaced with the appropriate values when the query is executed.
# Available placeholders: %player_name%, %player_uuid%, %server%, %arg1%, %arg2%, %arg[n]%
parameters:
  api_key: "******************************"
  player_name: "%player_name%"
  player_uuid: "%player_uuid%"
  server: "%server%"
  code: "%arg1%"

# Commands that cause the request to be executed. When one of these commands is entered, a request to the API will be executed.
triggers:
  - accountlink
  - linkaccount
  - sitelink

# Permission required to use the command. If you want to allow all users to use the command, clear this field.
permission: "account.link"

# Commands to be executed after receiving a response from the API. They are divided into two categories: "success" and "failure".
# "Success" is used when the HTTP response status is 200, and "failure" when the HTTP response status is not 200.
# The response from the API must be in JSON format, and you can use JSON-response keys as placeholders in these commands.
response:
  # Reply with successful status (200)
  success:
    - "m %player_name% %response%"
  # Response in case of failed status (not 200)
  failure:
    - "m %player_name% %response%"

debug: false
```

### EventsManager:
A utility to assist in handling the events registered within the EventsModule, ensuring smooth event processing.
```yaml
events:
  on_join_commands:
    enabled: false
    commands:
      - '[delay] 3'
      - '[console] g hello {player}'
  on_leave_commands:
    enabled: false
    commands:
      - '[console] alert &6Player {player} left the game'
  on_server_switch:
    enabled: false
    commands:
      - '[console] alert &6Player {player} connected to server {server} from server
        {fromServer}'
  on_server_kick:
    enabled: false
    commands:
      - '[console] alert &6Player {player} kick the server {server}'
  on_server_running:
    enabled: false
    commands:
      - '[console] limbostart first'
  on_server_stop:
    enabled: false
    commands:
      - '[console] alert &6Server {server} is stop'
```

