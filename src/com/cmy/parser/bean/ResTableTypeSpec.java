package com.cmy.parser.bean;

/**
 * Created by cmy on 2017/6/7
 */
public class ResTableTypeSpec extends ResTableChunk {

    // The tableType identifier this chunk is holding.  Type IDs start
    // at 1 (corresponding to the value of the tableType bits in a
    // resource identifier).  0 is invalid.
    public byte typeId;
    // 保留字段，一定为0
    public byte res0;
    // 保留字段，一定为0
    public short res1;
    // 资源项组数
    public int entryCount;

    /**
     * 资源spec数组的数据暂不解析，直接保存为字节数组
     */
    public byte[] data;

}
