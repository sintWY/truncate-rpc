package com.truncate.rpc.core;

import java.util.concurrent.*;

/**
 * 描述: 业务线程池
 * 版权: Copyright (c) 2017
 * 作者: truncate(wy940407@163.com)
 * 版本: 1.0
 * 创建日期: 2017年04月16日
 * 创建时间: 20:38
 */
public class RpcThreadPool {

    private static final String RPC_THREAD_POOL_NAME = "RpcThreadPool";

    public static Executor getExecutor(int threadNumber, int queueNumber) {
        return new ThreadPoolExecutor(threadNumber, threadNumber, 0, TimeUnit.MILLISECONDS,
                queueNumber == 0 ? new SynchronousQueue<Runnable>() : (queueNumber < 0 ? new LinkedBlockingQueue<Runnable>() : new LinkedBlockingQueue<Runnable>(queueNumber)),
                new NamedThreadFactory(RPC_THREAD_POOL_NAME, true), new AbortPolicyWithReport(RPC_THREAD_POOL_NAME));
    }
}
