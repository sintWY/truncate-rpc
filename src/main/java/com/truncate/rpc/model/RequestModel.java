package com.truncate.rpc.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 描述: 请求模型
 * 版权: Copyright (c) 2017
 * 作者: truncate(wy940407@163.com)
 * 版本: 1.0
 * 创建日期: 2017年04月16日
 * 创建时间: 20:21
 */
public class RequestModel implements Serializable {

    //消息id
    private String serialNo;

    //rpc调用类名
    private String className;

    //rpc调用方法名
    private String methodName;

    //参数类型
    private Class<?>[] typeParameters;

    //参数值
    private Object[] parameterValues;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getTypeParameters() {
        return typeParameters;
    }

    public void setTypeParameters(Class<?>[] typeParameters) {
        this.typeParameters = typeParameters;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(Object[] parameterValues) {
        this.parameterValues = parameterValues;
    }

    @Override
    public String toString() {
        return "RequestModel{" +
                "serialNo='" + serialNo + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", typeParameters=" + Arrays.toString(typeParameters) +
                ", parameterValues=" + Arrays.toString(parameterValues) +
                '}';
    }
}
