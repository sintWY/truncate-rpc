package com.truncate.rpc.core;

import com.truncate.rpc.core.handler.RequestRecvChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.spi.SelectorProvider;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 描述: 请求执行类
 * 版权: Copyright (c) 2017
 * 作者: truncate(wy940407@163.com)
 * 版本: 1.0
 * 创建日期: 2017年04月16日
 * 创建时间: 20:46
 */
public class RequestRecvExecutor {

    private static final Logger logger = LoggerFactory.getLogger(RequestRecvExecutor.class);

    private static ThreadPoolExecutor threadPoolExecutor;

    //绑定端口
    private int port;

    //执行对象
    private Map<String, Object> handlerMap;

    public RequestRecvExecutor(int port, Map<String, Object> handlerMap) {
        this.port = port;
        this.handlerMap = handlerMap;
    }

    /**
     * @描述：启动RPC服务
     * @作者:truncate(wy940407@163.com)
     * @日期:2017/4/16
     * @时间:21:19
     */
    public void startRpcServer() {
        //netty的线程池模型设置成主从线程池模式，这样可以应对高并发请求
        //当然netty还支持单线程、多线程网络IO模型，可以根据业务需求灵活配置
        ThreadFactory rpcThreadFactory = new NamedThreadFactory("NettyRpc ThreadFactory");

        //方法返回到Java虚拟机的可用的处理器数量
        int parallel = Runtime.getRuntime().availableProcessors() * 2;

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup(parallel, rpcThreadFactory, SelectorProvider.provider());
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new RequestRecvChannelInitializer(handlerMap))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            logger.info("netty rpc server start success...");
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            logger.error("启动服务器在端口[{}]失败!", new Object[]{port}, e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    /**
     * @描述：提交任务到线程池
     * @作者:truncate(wy940407@163.com)
     * @日期:2017/4/16
     * @时间:21:18
     */
    public static void submit(Runnable task) {
        if (threadPoolExecutor == null) {
            synchronized (RequestRecvExecutor.class) {
                if (threadPoolExecutor == null) {
                    threadPoolExecutor = (ThreadPoolExecutor) RpcThreadPool.getExecutor(16, -1);
                }
            }
        }
        threadPoolExecutor.submit(task);
    }
}
