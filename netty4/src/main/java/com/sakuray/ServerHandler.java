package com.sakuray;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;

import java.util.Date;

public class ServerHandler extends SimpleChannelInboundHandler<String> {

    private static final AttributeKey<Integer> key = AttributeKey.newInstance("test"); // pingTest进行两次

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        if("pingTest".equals(s)) {
            System.out.println("接收心跳测试信息");
        } else if("ping".equals(s)) {
            System.out.println("心跳信息");
        } else {
            System.out.println(channelHandlerContext.channel().remoteAddress() + "I get it :" + s);
            channelHandlerContext.writeAndFlush( s +'\n');
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Integer test = ctx.channel().attr(key).get();
        if(test == null) {
            ctx.channel().attr(key).set(0);
        }
        System.out.println("get a connection:" + ctx.channel().remoteAddress()+ "====" + new Date());
        ctx.writeAndFlush("i can see you\n");
        super.channelActive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if(IdleState.READER_IDLE.equals(event.state())) {
                System.out.println("读空闲:" + new Date());
                if(ctx.channel().attr(key).get() < 2) {
                    ctx.writeAndFlush("pingTest" + '\n');
                    int test = ctx.channel().attr(key).get();
                    test++;
                    ctx.channel().attr(key).set(test);
                } else {
                    ctx.channel().close();
                }
            } else if(IdleState.WRITER_IDLE.equals(event.state())) {
                System.out.println("写空闲:" + new Date());
            } else if(IdleState.ALL_IDLE.equals(event.state())) {
                System.out.println("读写空闲:" + new Date());
            }
        }
//        super.userEventTriggered(ctx, evt);
    }
}
