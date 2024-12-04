package ua.co.tensa.modules.rcon.server;

import com.velocitypowered.api.proxy.ProxyServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.SocketAddress;

public class RconServer {

	private final ProxyServer server;

	private final ServerBootstrap bootstrap = new ServerBootstrap();
	private final EventLoopGroup bossGroup = new NioEventLoopGroup();
	private final EventLoopGroup workerGroup = new NioEventLoopGroup();

	public RconServer(ProxyServer server, final String password) {
		this.server = server;

		bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) {
						ch.pipeline().addLast(new RconFramingHandler())
								.addLast(new RconHandler(RconServer.this, password));
					}
				});
	}

	public ChannelFuture bind(final SocketAddress address) {
		return bootstrap.bind(address);
	}

	public void shutdown() {
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
	}

	public ProxyServer getServer() {
		return server;
	}
}
