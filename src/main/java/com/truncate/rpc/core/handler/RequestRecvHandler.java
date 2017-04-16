package com.truncate.rpc.core.handler;

import com.truncate.rpc.core.RequestRecvExecutor;
import com.truncate.rpc.core.task.RequestRecvInitializeTask;
import com.truncate.rpc.model.RequestModel;
import com.truncate.rpc.model.ResponseModel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 描述: 请求接收处理handler
 * 版权: Copyright (c) 2017
 * 作者: truncate(wy940407@163.com)
 * 版本: 1.0
 * 创建日期: 2017年04月16日
 * 创建时间: 21:08
 */
public class RequestRecvHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RequestRecvHandler.class);

    private Map<String, Object> handlerMap;

    public RequestRecvHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        RequestModel requestModel = (RequestModel) msg;
        ResponseModel responseModel = new ResponseModel();
        logger.info("接收到的请求为：{}", new Object[]{requestModel.toString()});
        RequestRecvInitializeTask requestRecvInitializeTask = new RequestRecvInitializeTask(requestModel, responseModel, handlerMap, context);
        RequestRecvExecutor.submit(requestRecvInitializeTask);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        context.close();
    }
}
