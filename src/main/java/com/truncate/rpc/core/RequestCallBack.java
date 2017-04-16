package com.truncate.rpc.core;

import com.truncate.rpc.model.RequestModel;
import com.truncate.rpc.model.ResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 描述: 请求回调
 * 版权: Copyright (c) 2017
 * 作者: truncate(wy940407@163.com)
 * 版本: 1.0
 * 创建日期: 2017年04月16日
 * 创建时间: 21:32
 */
public class RequestCallBack {

    private static final Logger logger = LoggerFactory.getLogger(RequestCallBack.class);

    //默认超时时间
    private static final int DEFAULT_TIMEOUT_TIME = 20;

    private ResponseModel responseModel;

    //锁对象
    private Lock lock = new ReentrantLock();

    //是否完成条件
    private Condition finish = lock.newCondition();

    /**
     * @描述：回调开始
     * @作者:truncate(wy940407@163.com)
     * @日期:2017/4/16
     * @时间:21:37
     */
    public Object start() {
        try {
            lock.lock();
            finish.await(DEFAULT_TIMEOUT_TIME, TimeUnit.SECONDS);
            return responseModel;
        } catch (InterruptedException e) {
            logger.error("", e);
        } finally {
            lock.unlock();
        }
        return null;
    }

    public void over(ResponseModel responseModel) {
        try {
            lock.lock();
            finish.signal();
            this.responseModel = responseModel;
        } finally {
            lock.unlock();
        }
    }
}
