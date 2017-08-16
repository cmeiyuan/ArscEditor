package com.cmy.parser;

/**
 * Created by cmy on 2017/7/25
 */
public class ArscUtils {

    private static final boolean DEBUG = true;
    private static final int UTF8_FLAG = 1 << 8;

    //如果第一个字节高位为0，直接返回第一个字节
    //如果第一个字节高位为1，则将高为置为0，并和下一个字节拼接
    private static int decodeUTF8Length(int b1, int b2) {
        if (DEBUG) {
            System.out.println("byte1:" + b1 + " byte2:" + b2);
        }
        int len = b1;
        if ((len & 0x80) != 0) {
            len = ((len & 0x7F) << 8) | b2;
        }
        return len;
    }

    public static int[] getUtf8(byte[] array, int offset) {
        int val = array[offset];
        int length;
        // We skip the utf16 length of the string
        if ((val & 0x80) != 0) {
            offset += 2;
        } else {
            offset += 1;
        }
        // And we read only the utf-8 encoded length of the string
        val = array[offset];
        offset += 1;
        if ((val & 0x80) != 0) {
            int low = (array[offset] & 0xFF);
            length = ((val & 0x7F) << 8) + low;
            offset += 1;
        } else {
            length = val;
        }
        return new int[]{offset, length};
    }

    public static int[] getUtf16(byte[] array, int offset) {
        int val = ((array[offset + 1] & 0xFF) << 8 | array[offset] & 0xFF);

        if ((val & 0x8000) != 0) {
            int high = (array[offset + 3] & 0xFF) << 8;
            int low = (array[offset + 2] & 0xFF);
            int len_value = ((val & 0x7FFF) << 16) + (high + low);
            return new int[]{4, len_value * 2};

        }
        return new int[]{2, val * 2};
    }


    public static boolean isUTF8(int flags) {
        return (flags & UTF8_FLAG) != 0;
    }

    public static void main(String[] args) {
        // 0111 1111
        // 1111 1111
        // System.out.println(Integer.toHexString(decodeUTF8Length(0x80, 0x01)));
        int i1 = 0x88;
        int i2 = 0x88;
        int i3 = 0x88;
        int i4 = 0x88;
        byte b1 = (byte) i1;
        byte b2 = (byte) i2;
        byte b3 = (byte) i3;
        byte b4 = (byte) i4;

        byte[] strings = new byte[]{b1, b2, b3, b4};
        int[] val = getUtf8(strings, 0);
        System.out.print("长度：" + Integer.toHexString(val[1]));
    }

}
