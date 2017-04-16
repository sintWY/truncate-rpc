package com.truncate.rpc.test;

import com.truncate.rpc.core.RequestRecvExecutor;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述: //TODO 类描述
 * 版权: Copyright (c) 2017
 * 作者: truncate(wy940407@163.com)
 * 版本: 1.0
 * 创建日期: 2017年04月16日
 * 创建时间: 22:13
 */
public class RpcServer {

    @Test
    public void invoke() {
        int port = 1017;
        Map<String, Object> handlerMap = new HashMap<String, Object>();
        RequestRecvExecutor requestRecvExecutor = new RequestRecvExecutor(port, handlerMap);
        requestRecvExecutor.startRpcServer();
    }
}
