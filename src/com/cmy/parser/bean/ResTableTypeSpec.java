package com.cmy.parser.bean;

/**
 * Created by cmy on 2017/6/7
 */
public class ResTableTypeSpec extends ResTableData {

    // The tabletype identifier this chunk is holding.  Type IDs start
    // at 1 (corresponding to the value of the tabletype bits in a
    // resource identifier).  0 is invalid.
    public byte typeId;
    // 保留字段，一定为0
    public byte res0;
    // 保留字段，一定为0
    public short res1;
    // 资源项组数
    public int entryCount;
    // TODO 资源数组，暂时不知道的数据结构
    public int[] specArray;
}
