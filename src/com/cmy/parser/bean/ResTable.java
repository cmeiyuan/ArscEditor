package com.cmy.parser.bean;

/**
 * Created by cmy on 2017/6/7
 */
public class ResTable {

    public static final int RES_STRING_POOL_TYPE = 0x0001;
    public static final int RES_TABLE_TYPE = 0x0002;
    public static final int RES_TABLE_PACKAGE_TYPE = 0x0200;
    public static final int RES_TABLE_TYPE_TYPE = 0x0201;
    public static final int RES_TABLE_TYPE_SPEC_TYPE = 0x0202;
    public static final int RES_TABLE_LIBRARY_TYPE = 0x0203;

    //表头
    public ResTableHeader resTableHeader;
    //全局字符串资源池
    public ResStringPool globalResStringPool;
    //包
    public ResTablePackage resTablePackage;
}
