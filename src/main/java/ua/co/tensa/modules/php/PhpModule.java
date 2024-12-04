package ua.co.tensa.modules.php;

import ua.co.tensa.Message;
import ua.co.tensa.Util;
import ua.co.tensa.Tensa;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public class PhpModule {
	public static Map<String, Util.ScriptsData> scriptsData;
	public static ArrayList<String> scriptsList;
	public static final Path dir = Path.of(Tensa.pluginPath + File.separator + "php");

	public static void load() {
		scriptsData = Util.getScriptsData(dir, "index.php");
		scriptsList = Util.getScripts(dir);
		Util.copyFile(dir.toString(), "index.php");
		Message.info("PHP Runner module enabled");
	}

	public static void enable() {
		Util.registerCommand("php", "vphp", new PHPCommand());
		Util.createDir(Tensa.pluginPath.toString() + File.separator + "php");
		load();
	}

	public static void disable() {
		PHPCommand.unregister();
		PhpModule.scriptsList = null;
		PhpModule.scriptsData = null;
	}
}
