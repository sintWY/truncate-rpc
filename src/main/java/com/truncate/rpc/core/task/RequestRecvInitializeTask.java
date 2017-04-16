package com.truncate.rpc.core.task;

import com.truncate.rpc.model.RequestModel;
import com.truncate.rpc.model.ResponseModel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.beanutils.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 描述: 请求接收任务
 * 版权: Copyright (c) 2017
 * 作者: truncate(wy940407@163.com)
 * 版本: 1.0
 * 创建日期: 2017年04月16日
 * 创建时间: 21:12
 */
public class RequestRecvInitializeTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestRecvInitializeTask.class);

    //请求模型
    private RequestModel requestModel;

    //响应模型
    private ResponseModel responseModel;

    private Map<String, Object> handlerMap;

    private ChannelHandlerContext context;

    public RequestRecvInitializeTask(RequestModel requestModel, ResponseModel responseModel, Map<String, Object> handlerMap, ChannelHandlerContext context) {
        this.requestModel = requestModel;
        this.responseModel = responseModel;
        this.handlerMap = handlerMap;
        this.context = context;
    }

    public void run() {
        responseModel.setSerialNo(requestModel.getSerialNo());
        String errorMsg = "请求成功!";
        String errorNo = "0";
        try {
            Object result = invokeMethod(requestModel);
            responseModel.setResult(result);
        } catch (NoSuchMethodException e) {
            errorMsg = "没有方法";
            errorNo = "-1";
        } catch (IllegalAccessException e) {
            errorMsg = "没有权限";
            errorNo = "-2";
        } catch (InvocationTargetException e) {
            errorMsg = "调用失败";
            errorNo = "-3";
        } catch (InstantiationException e) {
            errorMsg = "初始化对象失败";
            errorNo = "-4";
        } catch (ClassNotFoundException e) {
            errorMsg = "没有找到对应的类";
            errorNo = "-5";
        }
        responseModel.setErrorMsg(errorMsg);
        responseModel.setErrorNo(errorNo);

        context.writeAndFlush(responseModel).addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                logger.info("响应请求成功,响应编号：modelId={}", new Object[]{responseModel.getSerialNo()});
            }
        });
    }

    /**
     * @描述：反射执行方法
     * @作者:truncate(wy940407@163.com)
     * @日期:2017/4/16
     * @时间:21:26
     */
    private Object invokeMethod(RequestModel requestModel) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException {
        String className = requestModel.getClassName();
        String methodName = requestModel.getMethodName();
        Object object = Class.forName(className).newInstance();
        Object[] parameters = requestModel.getParameterValues();
        return MethodUtils.invokeMethod(object, methodName, parameters);
    }
}
