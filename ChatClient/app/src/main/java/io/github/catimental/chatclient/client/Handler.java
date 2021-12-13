package io.github.catimental.chatclient.client;

import io.github.catimental.chatclient.net.ByteBufReader;
import io.github.catimental.chatclient.net.ByteBufWriter;
import io.github.catimental.chatclient.net.handlers.CommonHandler;
import io.github.catimental.chatclient.net.packet.opcode.CInOpcode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.ByteOrder;

public class Handler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    public void channelRead0(ChannelHandlerContext arg0, ByteBuf msg) throws Exception {
        ByteBufReader bbr = new ByteBufReader(Unpooled.wrappedBuffer(msg).order(ByteOrder.LITTLE_ENDIAN));
        short opcodeValue = bbr.readShort();
        CInOpcode opcode = CInOpcode.getOpcode(opcodeValue);
        System.out.println(opcode +" handled");
        CommonHandler.handle(opcode, bbr);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();

    }
}
