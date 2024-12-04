package com.vcmi.modules.rcon.server;

import com.vcmi.Message;
import com.vcmi.VCMI;
import com.vcmi.config.data.RconServerYAML;
import com.velocitypowered.api.proxy.ProxyServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.simpleyaml.configuration.file.YamlConfiguration;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

public class RconServerModule extends YamlConfiguration {

	private static RconServer rconServer;
	private static final ProxyServer server = VCMI.server;
	public static final char COLOR_CHAR = '\u00A7';
	public static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + COLOR_CHAR + "[0-9A-FK-OR]");
	public static final Pattern STRIP_MC_COLOR_PATTERN = Pattern.compile("§[0-8abcdefklmnor]");
	private static YamlConfiguration config;

	public static void initialise() {
		config = RconServerYAML.getInstance().getReloadedFile();
	}

	public static void reload() {
		initialise();
	}

	public static Integer getPort() {
		return config.getInt("port");
	}

	public static String getPass() {
		return config.getString("password");
	}

	public static boolean isColored() {
		return config.getBoolean("colored");
	}

	public static String stripColor(final String input) {
		if (input == null) {
			return null;
		}

		return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
	}

	public static String stripMcColor(final String input) {
		if (input == null) {
			return null;
		}

		return STRIP_MC_COLOR_PATTERN.matcher(input).replaceAll("");
	}

	public static boolean isInteger(String str) {
		return str.matches("-?\\d+");
	}

	private static void startRconListener() {
		InetSocketAddress address = new InetSocketAddress(getPort());
		rconServer = new RconServer(server, getPass());
		ChannelFuture future = rconServer.bind(address);
		Channel channel = future.awaitUninterruptibly().channel();
		if (!channel.isActive()) {
			stopRconListener();
		}
		Message.info("Binding rcon to address: " + address.getHostName() + ":" + address.getPort());
	}

	private static void stopRconListener() {
		if (rconServer != null) {
			Message.info("Trying to stop RCON listener");
			rconServer.shutdown();
		}
	}

	public static void enable() {
		reload();
		Message.info("Rcon Server module enabled");
		startRconListener();
	}

	public static void disable() {
		stopRconListener();
		config = null;
	}

}
