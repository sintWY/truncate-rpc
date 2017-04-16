package com.truncate.rpc.test;

import com.truncate.rpc.core.RequestSendExecutor;
import com.truncate.rpc.model.RequestModel;
import com.truncate.rpc.model.ResponseModel;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.UUID;

/**
 * 描述: //TODO 类描述
 * 版权: Copyright (c) 2017
 * 作者: truncate(wy940407@163.com)
 * 版本: 1.0
 * 创建日期: 2017年04月16日
 * 创建时间: 22:18
 */
public class RcpClient {

    @Test
    public void invoke() {
        RequestModel requestModel = new RequestModel();
        requestModel.setSerialNo(UUID.randomUUID().toString().toUpperCase());
        requestModel.setClassName("com.truncate.rpc.core.target.CaculateNumber");
        requestModel.setMethodName("add");
        requestModel.setParameterValues(new Object[]{4, 5});
        requestModel.setTypeParameters(new Class[]{Integer.class, Integer.class});
        SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 1017);
        RequestSendExecutor requestSendExecutor = new RequestSendExecutor(socketAddress);
        ResponseModel responseModel = requestSendExecutor.invokeRequest(requestModel);
        System.out.println("返回结果：" + responseModel.getResult());
    }
}
