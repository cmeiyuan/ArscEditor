package com.cmy.designmode;

import java.lang.reflect.Proxy;

/**
 * Created by cmy on 2017/5/8
 */
public class DynamicProxy {

    public static void main(String[] args) {
        IBusiness business = (IBusiness) Proxy.newProxyInstance(IBusiness.class.getClassLoader(), new Class[]{IBusiness.class}, new ProxyHandler());
        business.buyHouse();
    }

}
