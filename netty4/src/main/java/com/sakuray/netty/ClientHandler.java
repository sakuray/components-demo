package com.sakuray.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;

public class ClientHandler extends SimpleChannelInboundHandler<String> {

    private static final AttributeKey<Integer> keys = AttributeKey.valueOf("count");        // 达到三次不在发送ping 也不响应pingTest，模拟断线

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("add handler");
        super.handlerAdded(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("connect server!!!");
        Integer count = ctx.channel().attr(keys).get();
        if(count == null) {
            ctx.channel().attr(keys).set(0);
        }
        super.channelActive(ctx);
    }

    // 开始循环
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        if("exception".equals(s)) {
            throw new Exception("异常测试");
        }
        if("pingTest".equals(s)) {
            System.out.println("接收心跳测试信息");
            channelHandlerContext.writeAndFlush("pingTest" + '\n');
        }
        System.out.println("get Server message:" + s);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught :" + cause.getMessage());
//        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete");
        super.channelReadComplete(ctx);
    }
    // 循环结束

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("close connection!!!");
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered");
        super.channelUnregistered(ctx);
    }


    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("remove handler");
        super.handlerRemoved(ctx);
    }

    // 触发器
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {
            if(IdleState.WRITER_IDLE.equals(((IdleStateEvent) evt).state())) {
                if(ctx.channel().attr(keys).get() < 3) {
                    System.out.println("写空闲");
                    ctx.writeAndFlush("ping" + '\n');
                    int count = ctx.channel().attr(keys).get();
                    count++;
                    ctx.channel().attr(keys).set(count);
                }
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelWritabilityChanged");
        super.channelWritabilityChanged(ctx);
    }

}
