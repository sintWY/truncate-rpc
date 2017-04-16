package com.truncate.rpc.core;

import com.truncate.rpc.core.handler.RequestSendHandler;
import com.truncate.rpc.core.task.RequestSendInitializeTask;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 描述: 服务加载类 单例
 * 版权: Copyright (c) 2017
 * 作者: truncate(wy940407@163.com)
 * 版本: 1.0
 * 创建日期: 2017年04月16日
 * 创建时间: 22:29
 */
public class RpcServerLoader {

    private static final Logger logger = LoggerFactory.getLogger(RpcServerLoader.class);

    private static RpcServerLoader rpcServerLoader;

    private ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) RpcThreadPool.getExecutor(16, -1);

    private RequestSendHandler requestSendHandler;

    //CPU数量*2
    private static final int PARALLEL = Runtime.getRuntime().availableProcessors() * 2;

    //netty nio线程池
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(PARALLEL);

    private Lock lock = new ReentrantLock();

    private Condition finish = lock.newCondition();

    private RpcServerLoader() {
    }

    public static RpcServerLoader getInstance() {
        if (rpcServerLoader == null) {
            synchronized (RpcServerLoader.class) {
                if (rpcServerLoader == null) {
                    rpcServerLoader = new RpcServerLoader();
                }
            }
        }
        return rpcServerLoader;
    }

    public void load(SocketAddress socketAddress) {
        threadPoolExecutor.submit(new RequestSendInitializeTask(eventLoopGroup, socketAddress, this));
    }

    public void setRequestSendHandler(RequestSendHandler requestSendHandler) {
        try {
            lock.lock();
            this.requestSendHandler = requestSendHandler;
            finish.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public RequestSendHandler getRequestSendHandler() {
        try {
            lock.lock();
            if (requestSendHandler == null) {
                finish.await();
            }
        } catch (InterruptedException e) {
            logger.error("", e);
        } finally {
            lock.unlock();
        }
        return requestSendHandler;
    }

    public void unLoad() {
        requestSendHandler.close();
        threadPoolExecutor.shutdown();
        eventLoopGroup.shutdownGracefully();
    }


}
