package com.cmy.designmode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by cmy on 2017/5/8
 */
public class ProxyHandler implements InvocationHandler{

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("开始处理委托事件:");
        System.out.println(proxy.getClass().getSimpleName());
        System.out.println(method.getName());
        return null;
    }
}
