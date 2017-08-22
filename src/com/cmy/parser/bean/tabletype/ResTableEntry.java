package com.cmy.parser.bean.tabletype;

/**
 * Created by cmy on 2017/6/7
 */
public class ResTableEntry {

    public static final int FLAG_COMPLEX = 0x0001;
    public static final int FLAG_PUBLIC = 0x0002;
    public static final int FLAG_WEAK = 0x0004;

    //entry size
    public short size;
    //flags
    public short flags;
    //资源项名称index
    public int index;

    public ResValue resValue;

}
