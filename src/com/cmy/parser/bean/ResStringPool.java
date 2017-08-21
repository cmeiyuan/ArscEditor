package com.cmy.parser.bean;

/**
 * Created by cmy on 2017/6/7
 */
public class ResStringPool {

    public ResStringPoolHeader resStringPoolHeader;

    // 字符串偏移数组
    public int[] stringOffsets;
    // 样式偏移数组
    public int[] styleOffsets;
    // 字符串数组
    public byte[][] strings;
    // 样式数组
    public byte[][] styles;

    /**
     * 由于我们暂时不用修改字符串池
     * 所以直接把整个字符串池的数据保存下来(不包括ResStringPoolHeader)，便于写入
     */
    public byte[] data;

    public static class ResStringPoolHeader extends ResTableChunk {
        //是否排序
        public static final int SORTED_FLAG = 1;
        //是否为utf-8
        public static final int UTF8_FLAG = 1 << 8;

        // Number of strings in this pool.
        public int stringCount;
        // Number of style span arrays in the pool.
        public int styleCount;
        // Flags.
        public int flags;
        // Index from header of the string data.
        public int stringsStart;
        // Index from header of the style data.
        public int stylesStart;
    }

}
