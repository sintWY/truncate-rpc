package com.truncate.rpc.core.handler;

import com.truncate.rpc.core.RequestCallBack;
import com.truncate.rpc.model.RequestModel;
import com.truncate.rpc.model.ResponseModel;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述: 请求发送初始化
 * 版权: Copyright (c) 2017
 * 作者: truncate(wy940407@163.com)
 * 版本: 1.0
 * 创建日期: 2017年04月16日
 * 创建时间: 21:56
 */
public class RequestSendHandler extends ChannelInboundHandlerAdapter {

    //存放请求和回调的关联关系
    private ConcurrentHashMap<String, RequestCallBack> callBackMap = new ConcurrentHashMap<String, RequestCallBack>();

    private volatile Channel channel;

    private SocketAddress socketAddress;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    public void setSocketAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.socketAddress = channel.remoteAddress();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ResponseModel responseModel = (ResponseModel) msg;
        String serialNo = responseModel.getSerialNo();
        RequestCallBack requestCallBack = callBackMap.get(serialNo);
        if (requestCallBack != null) {
            callBackMap.remove(serialNo);
            requestCallBack.over(responseModel);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }


    /**
     * @描述：发送请求
     * @作者:truncate(wy940407@163.com)
     * @日期:2017/4/16
     * @时间:22:06
     */
    public RequestCallBack invokeRequest(RequestModel requestModel) {
        RequestCallBack requestCallBack = new RequestCallBack();
        callBackMap.put(requestModel.getSerialNo(), requestCallBack);
        channel.writeAndFlush(requestModel);
        return requestCallBack;
    }
}
