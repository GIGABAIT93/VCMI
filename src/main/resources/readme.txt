[center]<gold>VCMI Plugin</gold>
[center]<gray>A Velocity Plugin for Comprehensive Server Management</gray>

<gold>Modules:</gold>

<gray>PlayerTime:</gray>
Tracks total playtime. Commands:
- <click:run_command:/vptime><hover:show_text:'<gray>Click to run</gray>'><green>/vptime</green></click> - Shows your total playtime.
- <click:copy_to_clipboard:vptime {player}><hover:show_text:'<gray>Click to copy</gray>'><green>/vptime {player}</green></click> - Shows specified player's playtime.

<gray>RconManager:</gray>
Execute RCON commands remotely.
- <click:copy_to_clipboard:/rcon><hover:show_text:'<gray>Click to copy RCON command</gray>'><green>/rcon {server/all/reload} {command}</green></click> - Command execution.
- <click:run_command:/rcon all list><hover:show_text:'<gray>Click to run example command</gray>'><green>/rcon all list</green></click> - Execute example RCON command.

<gray>PhpModule:</gray>
Extend functionality with PHP scripts.
- <click:copy_to_clipboard:/php><hover:show_text:'<gray>Click to copy PHP command</gray>'><green>/php {script/reload} {args}</green></click> - Execute PHP scripts.
- <click:run_command:/php index info><hover:show_text:'<gray>Click to run example script</gray>'><green>/php index info</green></click> - Execute example PHP script.

<gray>BashModule:</gray>
Execute Bash scripts from the console or chat.
- <click:copy_to_clipboard:/bash><hover:show_text:'<gray>Click to copy Bash command</gray>'><green>/bash {script/reload} {args}</green></click> - Run Bash scripts.
- <click:run_command:/bash run info><hover:show_text:'<gray>Click to run example script</gray>'><green>/bash run</green></click> - Execute example Bash script.

<gray>TextReader:</gray>
Read and display text files.
- <click:run_command:/rules><hover:show_text:'<gray>Click to read rules.txt file</gray>'><green>/rules</green></click> - Display rules.txt file content.

<gray>HttpRequest:</gray>
Perform HTTP requests and command execution based on responses.
- <click:run_command:/linkaccount 12345><hover:show_text:'<gray>Click to run command</gray>'><green>/linkaccount 12345</green></click> - Execute command based on response.

