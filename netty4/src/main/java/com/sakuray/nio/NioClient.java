package com.sakuray.nio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class NioClient {

    private String host;
    private int port;
    private SocketChannel socketChannel;
    private Selector selector;

    public NioClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void open() {
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.socket().setTcpNoDelay(true);
            socketChannel.connect(new InetSocketAddress(host, port));
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            System.out.println("client端启动...");
            for(;;) {
//                System.out.println("======>select的keys数量：" + selector.keys().size());
                int event = selector.select();
//                System.out.println("======>select的keys数量：" + selector.keys().size() + ", change事件数量：" + event);
                if(event != 0) {
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> it = keys.iterator();
                    while(it.hasNext()) {
                        SelectionKey sk = it.next();
                        it.remove();
                        if(sk.isValid() && sk.isConnectable()) {
                            if(socketChannel.isConnectionPending()) {
                                if(socketChannel.finishConnect()) {
                                    sk.interestOps(SelectionKey.OP_READ);
                                    System.out.println("连接上远程服务器：" + socketChannel.getRemoteAddress());
                                } else {
                                    sk.cancel();
                                    System.out.println("连接未建立...");
                                }
                            }
                        } else if(sk.isValid() && sk.isReadable()) {
                            SocketChannel sc = (SocketChannel) sk.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            int size = -1;
                            while((size = sc.read(buffer)) > 0) {
                                buffer.flip();
                                baos.write(buffer.array(), 0, size);
                                buffer.clear();
                            }
                            System.out.println("接收服务器消息：" + new String(baos.toByteArray(), "UTF-8"));
                        } else {
                            System.out.println("其它事件：" + sk.interestOps());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg) {
        byte[] b = msg.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(b);
        try {
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NioClient client = new NioClient("127.0.0.1", 7777);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                client.open();
            }
        });
        thread.setDaemon(true);
        thread.start();
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()) {
            String msg = scanner.nextLine();
            if("close".equals(msg)) {
                client.close();
                System.out.println("退出成功");
                break;
            } else {
                client.send(msg);
            }
        }
    }
}
