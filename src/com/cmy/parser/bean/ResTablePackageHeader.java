package com.cmy.parser.bean;

/**
 * Created by cmy on 2017/6/7
 */
public class ResTablePackageHeader extends ResTableChunk {

    // If this is a base package, its ID.  Package IDs start
    // at 1 (corresponding to the value of the package bits in a
    // resource identifier).  0 means this is not a base package.
    public int packageId;
    // Actual name of this package, \0-terminated.
    public byte[] packageName;
    // Offset to a ResStringPool_header defining the resource
    // tabletype symbol table.  If zero, this package is inheriting from
    // another base package (overriding specific values in it).
    public int typeStringOffset;
    // Last index into typeStrings that is for public use by others.
    // 目前被设置为资源类型字符串池的大小，好像没撒用啊...
    public int lastPublicType;
    // Offset to a ResStringPool_header defining the resource
    // key symbol table.  If zero, this package is inheriting from
    // another base package (overriding specific values in it).
    public int keyStringOffset;
    // Last index into keyStrings that is for public use by others.
    // 目前被设置为资源项名称字符串池的大小，好像没撒用啊...
    public int lastPublicKey;

    public int typeIdOffset;

}
