package com.truncate.rpc.core;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 描述: 自定义名字的线程工厂
 * 版权: Copyright (c) 2017
 * 作者: truncate(wy940407@163.com)
 * 版本: 1.0
 * 创建日期: 2017年04月16日
 * 创建时间: 20:29
 */
public class NamedThreadFactory implements ThreadFactory {

    //线程工厂计数器
    private static final AtomicInteger threadNumber = new AtomicInteger(1);

    //单个线程工厂线程计数器
    private final AtomicInteger childThreadNumber = new AtomicInteger(1);

    //线程工厂标识
    private String prefix;

    //是否是守护线程
    private boolean daemo;

    //线程组
    private ThreadGroup threadGroup;

    public NamedThreadFactory() {
        this("rpcserver-threadpool-" + threadNumber.getAndIncrement(), false);
    }

    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    public NamedThreadFactory(String prefix, boolean daemo) {
        this.prefix = prefix + "-thread-";
        this.daemo = daemo;
        SecurityManager s = System.getSecurityManager();
        threadGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }

    public Thread newThread(Runnable runnable) {
        String name = prefix + childThreadNumber.getAndIncrement();
        Thread thread = new Thread(threadGroup, runnable, name, 0);
        thread.setDaemon(daemo);
        return thread;
    }
}
