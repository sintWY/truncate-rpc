package com.truncate.rpc.core;

import com.truncate.rpc.model.RequestModel;
import com.truncate.rpc.model.ResponseModel;

import java.net.SocketAddress;

/**
 * 描述: 请求发送类
 * 版权: Copyright (c) 2017
 * 作者: truncate(wy940407@163.com)
 * 版本: 1.0
 * 创建日期: 2017年04月16日
 * 创建时间: 22:28
 */
public class RequestSendExecutor {

    private static RpcServerLoader rpcServerLoader = RpcServerLoader.getInstance();

    public RequestSendExecutor(SocketAddress socketAddress) {
        rpcServerLoader.load(socketAddress);
    }

    public void stop() {
        rpcServerLoader.unLoad();
    }

    public ResponseModel invokeRequest(RequestModel requestModel) {
        RequestCallBack requestCallBack = rpcServerLoader.getRequestSendHandler().invokeRequest(requestModel);
        return (ResponseModel) requestCallBack.start();
    }
}
