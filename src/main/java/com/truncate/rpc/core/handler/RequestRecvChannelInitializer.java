package com.truncate.rpc.core.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.Map;

/**
 * 描述: 请求接收初始化
 * 版权: Copyright (c) 2017
 * 作者: truncate(wy940407@163.com)
 * 版本: 1.0
 * 创建日期: 2017年04月16日
 * 创建时间: 21:02
 */
public class RequestRecvChannelInitializer extends ChannelInitializer<SocketChannel> {

    //ObjectDecoder 底层默认继承半包解码器LengthFieldBasedFrameDecoder处理粘包问题的时候，
    //消息头开始即为长度字段，占据4个字节。这里出于保持兼容的考虑
    final public static int MESSAGE_LENGTH = 4;

    private Map<String, Object> handlerMap = null;

    public RequestRecvChannelInitializer(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        //ObjectDecoder的基类半包解码器LengthFieldBasedFrameDecoder的报文格式保持兼容。因为底层的父类LengthFieldBasedFrameDecoder
        //的初始化参数即为super(maxObjectSize, 0, 4, 0, 4);
        channelPipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, MESSAGE_LENGTH, 0, MESSAGE_LENGTH));
        channelPipeline.addLast(new LengthFieldPrepender(MESSAGE_LENGTH));
        channelPipeline.addLast(new ObjectEncoder());
        //考虑到并发性能，采用weakCachingConcurrentResolver缓存策略。一般情况使用:cacheDisabled即可
        channelPipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
        channelPipeline.addLast(new RequestRecvHandler(handlerMap));
    }
}
