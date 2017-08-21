package com.cmy.parser.bean;

import java.util.List;

/**
 * Created by cmy on 2017/6/7
 */
public class ResTablePackage {
    //包头
    public ResTablePackageHeader packageHeader;
    //资源类型字符串池
    public ResStringPool typeStringPool;
    //资源项关键字符串池
    public ResStringPool keyStringPool;
    //类型规范和资源配置列表
    public List<ResTableChunk> resTableChunkList;
}
