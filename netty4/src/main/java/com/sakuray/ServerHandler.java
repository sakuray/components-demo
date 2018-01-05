package com.sakuray;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<String> {

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println(channelHandlerContext.channel().remoteAddress() + ":" + s);
        channelHandlerContext.writeAndFlush("I get it :" + s +'\n');
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("get a connection:" + ctx.channel().remoteAddress());
        ctx.writeAndFlush("i can see you\n");
        super.channelActive(ctx);
    }
}
