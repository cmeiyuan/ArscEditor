package com.cmy.parser.bean.tabletype;

/**
 * Created by cmy on 2017/6/7
 */
public class ResTableEntry {

    public static final int FLAG_COMPLEX = 0x0001;
    public static final int FLAG_PUBLIC = 0x0002;
    public static final int FLAG_WEAK = 0x0004;

    // entry size
    public short size;
    // flags
    // 这个flags有4种取值(0, 1, 2, 4)
    // 当flags为1时，表示为复杂类型数据，后面跟的数据是ResTabMap
    // 当flags为2时，表示这个数据为公开数据，也就是public.xml里定义的
    // 当flags为4时，表示这个数据是一个弱资源，可以被其他资源覆盖
    public short flags;
    // 资源项名称index
    public int index;

    public ResValue resValue;

}
