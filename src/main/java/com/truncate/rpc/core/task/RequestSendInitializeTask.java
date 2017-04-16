package com.truncate.rpc.core.task;

import com.truncate.rpc.core.RpcServerLoader;
import com.truncate.rpc.core.handler.RequestSendChannelInitializer;
import com.truncate.rpc.core.handler.RequestSendHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.SocketAddress;

/**
 * 描述: 消息发送任务
 * 版权: Copyright (c) 2017
 * 作者: truncate(wy940407@163.com)
 * 版本: 1.0
 * 创建日期: 2017年04月16日
 * 创建时间: 22:35
 */
public class RequestSendInitializeTask implements Runnable {

    private EventLoopGroup eventLoopGroup;

    private SocketAddress socketAddress;

    private RpcServerLoader rpcServerLoader;

    public RequestSendInitializeTask(EventLoopGroup eventLoopGroup, SocketAddress socketAddress, RpcServerLoader rpcServerLoader) {
        this.eventLoopGroup = eventLoopGroup;
        this.socketAddress = socketAddress;
        this.rpcServerLoader = rpcServerLoader;
    }

    public void run() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new RequestSendChannelInitializer());
        final ChannelFuture channelFuture = bootstrap.connect(socketAddress);
        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                RequestSendHandler requestSendHandler = channelFuture.channel().pipeline().get(RequestSendHandler.class);
                RequestSendInitializeTask.this.rpcServerLoader.setRequestSendHandler(requestSendHandler);
            }
        });
    }
}
