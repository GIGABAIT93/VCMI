package com.vcmi.modules.rcon.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.nio.ByteOrder;
import java.util.List;

public class RconFramingHandler extends ByteToMessageCodec<ByteBuf> {

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) {
		out.order(ByteOrder.LITTLE_ENDIAN).writeInt(msg.readableBytes());
		out.writeBytes(msg);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
		if (in.readableBytes() < 4) {
			return;
		}

		in.markReaderIndex();
		@SuppressWarnings("deprecation")
		int length = in.order(ByteOrder.LITTLE_ENDIAN).readInt();
		if (in.readableBytes() < length) {
			in.resetReaderIndex();
			return;
		}

		ByteBuf buf = ctx.alloc().buffer(length);
		in.readBytes(buf, length);
		out.add(buf);
	}
}
