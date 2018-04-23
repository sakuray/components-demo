package com.sakuray.nio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioServer {

    private int port;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private ExecutorService executorService;

    public NioServer(int port) {
        this.port = port;
    }

    public void open() {
        this.executorService = Executors.newCachedThreadPool();
        try {
            this.selector = Selector.open();
            this.serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("server端启动...");
            for(;;) {
//                System.out.println("======>select的keys数量：" + selector.keys().size());
                int event = selector.select();
//                System.out.println("======>select的keys数量：" + selector.keys().size() + ", change事件数量：" + event);
                if(event != 0) {
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> it = keys.iterator();
//                    System.out.println("======>真实未处理的change事件数量：" + keys.size());
                    while(it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();    // 移除这个key
                        if(key.isValid() && key.isAcceptable()) {
                            ServerSocketChannel server = (ServerSocketChannel) key.channel();
                            SocketChannel client = server.accept();
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_READ);
                            System.out.println("===>获取client连接，准备读取数据:"+ client.socket().getRemoteSocketAddress());
                        } else if(key.isValid() && key.isReadable()) {
                            // 先置为0，防止异步线程未处理完该事件又被select
//                            key.interestOps(key.interestOps() & (~SelectionKey.OP_READ));
                            key.interestOps(0);
                            executorService.execute(new Task(key));
                        } else {
                            System.out.println("其它事件：" + key.interestOps());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Task implements Runnable {

        private SelectionKey key;

        public Task(SelectionKey key) {
            this.key = key;
        }

        @Override
        public void run() {
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int size = -1;
            try {
//                System.out.println("===>开始读取数据");
                while((size = channel.read(buffer)) > 0) {
                    buffer.flip();
                    baos.write(buffer.array(), 0, size);
                    buffer.clear();
                }
                if(baos.size() == 0) {
                    key.cancel();
                    System.out.println("======<client断开连接:"+ channel.socket().getRemoteSocketAddress());
                } else {
                    // 协议解析
                    String msg = new String(baos.toByteArray(), "UTF-8");
                    System.out.println("===>获取client数据: " + msg);
                    // 返回该数据
                    String reply = "get client msg: " + msg;
                    ByteBuffer re = ByteBuffer.wrap(reply.getBytes());
                    while(re.hasRemaining()) {
                        channel.write(re);
                    }
                    // 处理完毕后后设置成读状态，继续获取相关数据
//                    key.interestOps((key.interestOps() | SelectionKey.OP_READ));
                    key.interestOps(SelectionKey.OP_READ);
                    key.selector().wakeup();
                    System.out.println("===<返回server的获取结果");
                }
            } catch (Exception e) {
                key.cancel();   // 异常连接中断
                System.out.println("======<异常client断开连接:"+ channel.socket().getRemoteSocketAddress());
            }
        }
    }

    public static void main(String[] args) {
        NioServer nioServer = new NioServer(7777);
        nioServer.open();
    }
}
