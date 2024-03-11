package com.vcmi.modules.bash;

import com.vcmi.Message;
import com.vcmi.Util;
import com.vcmi.VCMI;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public class BashModule {
	public static Map<String, Util.ScriptsData> scriptsData;
	public static ArrayList<String> scriptsList;
	public static final Path dir = Path.of(VCMI.pluginPath + File.separator + "bash");

	public static void load() {
		if (System.getProperty("os.name").contains("Windows")) {
			Message.warn("The BASH Runner module cannot be run on your operating system");
			disable();
			return;
		}
		scriptsData = Util.getScriptsData(dir, "run.sh");
		scriptsList = Util.getScripts(dir);
		Util.copyFile(dir.toString(), "run.sh");
		Message.info("BASH Runner module enabled");
	}

	public static void enable() {
		Util.registerCommand("bash", "vbash", new BASHCommand());
		Util.createDir(VCMI.pluginPath.toString() + File.separator + "bash");
		load();
	}

	public static void disable() {
		BASHCommand.unregister();
		BashModule.scriptsList = null;
		BashModule.scriptsData = null;
	}

}
