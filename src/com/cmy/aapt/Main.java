package com.cmy.aapt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by cmy on 2017/5/9
 */
public class Main {

    private static Set<Integer> flagsSet = new HashSet<>();

    public static int RES_NULL_TYPE = 0x0000;
    public static int RES_STRING_POOL_TYPE = 0x0001;
    public static int RES_TABLE_TYPE = 0x0002;
    public static int RES_XML_TYPE = 0x0003;

    // Chunk types in RES_XML_TYPE
    public static int RES_XML_FIRST_CHUNK_TYPE = 0x0100;
    public static int RES_XML_START_NAMESPACE_TYPE = 0x0100;
    public static int RES_XML_END_NAMESPACE_TYPE = 0x0101;
    public static int RES_XML_START_ELEMENT_TYPE = 0x0102;
    public static int RES_XML_END_ELEMENT_TYPE = 0x0103;
    public static int RES_XML_CDATA_TYPE = 0x0104;
    public static int RES_XML_LAST_CHUNK_TYPE = 0x017;
    // This contains a uint32_t array mapping strings in the string
    // pool back to resource identifiers.  It is optional.
    public static int RES_XML_RESOURCE_MAP_TYPE = 0x0180;

    // Chunk types in RES_TABLE_TYPE
    public static int RES_TABLE_PACKAGE_TYPE = 0x0200;
    public static int RES_TABLE_TYPE_TYPE = 0x0201;
    public static int RES_TABLE_TYPE_SPEC_TYPE = 0x0202;
    public static int RES_TABLE_LIBRARY_TYPE = 0x0203;

    public static void main(String[] args) {
        try {
            readFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readFile() throws Exception {
        String path = "/Users/cmeiyuan/AndroidStudioProjects/HelloPlugin/app/build/outputs/apk/resources.arsc";

        path = "/Users/cmeiyuan/AndroidStudioProjects/HelloPlugin/lib/build/bundle/res/ap_unzip/resources.arsc";


        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在");
        }
        InputStream inputStream = new FileInputStream(file);
        byte[] b = new byte[1024 * 1024];
        int count = inputStream.read(b);
        println(String.valueOf(count));

        //文件头区
        println("RES_TABLE_TYPE:" + HexUtil.littleEndianBytesToHexString(b[0], b[1]));
        int resTableHeaderSize = HexUtil.littleEndianBytesToInt(b[2], b[3]);//12
        println("头大小:" + HexUtil.intToHexString(resTableHeaderSize));
        int resTableSize = HexUtil.littleEndianBytesToInt(b[4], b[5], b[6], b[7]);
        println("文件大小:" + HexUtil.intToHexString(resTableSize));
        println("包数量:" + HexUtil.littleEndianBytesToHexString(b[8], b[9], b[10], b[11]));

        //字符串池区
        println("RES_STRING_POOL_TYPE:" + HexUtil.littleEndianBytesToHexString(b[12], b[13]));
        println("头大小:" + HexUtil.littleEndianBytesToHexString(b[14], b[15]));//28
        int stringPoolChunkSize = HexUtil.littleEndianBytesToInt(b[16], b[17], b[18], b[19]);
        println("块大小:" + HexUtil.intToHexString(stringPoolChunkSize));
        println("字符串数:" + HexUtil.littleEndianBytesToHexString(b[20], b[21], b[22], b[23]));
        println("样式数:" + HexUtil.littleEndianBytesToHexString(b[24], b[25], b[26], b[27]));
        println("标记:" + HexUtil.littleEndianBytesToHexString(b[28], b[29], b[30], b[31]));
        int stringStart = HexUtil.littleEndianBytesToInt(b[32], b[33], b[34], b[35]);
        int styleStart = HexUtil.littleEndianBytesToInt(b[36], b[37], b[38], b[39]);
        println("字符串起始位置:" + HexUtil.intToHexString(stringStart));
        println("样式起始位置:" + HexUtil.intToHexString(styleStart));

        //package区
        int packageChunkPos = resTableHeaderSize + stringPoolChunkSize;

        println("packageChunkPos:" + packageChunkPos);

        packageChunkPos += 2;
        int packageHeaderSize = HexUtil.littleEndianBytesToInt(b[packageChunkPos], b[packageChunkPos + 1]);
        println("package header size:" + HexUtil.intToHexString(packageHeaderSize));
        packageChunkPos += 6;
        println("package id:" + HexUtil.littleEndianBytesToHexString(b[packageChunkPos], b[packageChunkPos + 1], b[packageChunkPos + 2], b[packageChunkPos + 3]));

        //查找dynamicRefTable
        int typeStringPoolPos = resTableHeaderSize + stringPoolChunkSize + packageHeaderSize;

        //资源类型字符串池大小
        typeStringPoolPos += 4;
        int typeStringPoolSize = HexUtil.littleEndianBytesToInt(b[typeStringPoolPos], b[typeStringPoolPos + 1], b[typeStringPoolPos + 2], b[typeStringPoolPos + 3]);
        typeStringPoolPos -= 4;
        println("typeStringPoolSize:" + HexUtil.intToHexString(typeStringPoolSize));
        //资源项名称字符串池大小

        int keyStringPoolPos = typeStringPoolPos + typeStringPoolSize;
        keyStringPoolPos += 4;
        int keyStringPoolSize = HexUtil.littleEndianBytesToInt(b[keyStringPoolPos], b[keyStringPoolPos + 1], b[keyStringPoolPos + 2], b[keyStringPoolPos + 3]);
        keyStringPoolPos -= 4;
        println("keyStringPoolSize:" + HexUtil.intToHexString(keyStringPoolSize));

        int dynamicRefTablePos = keyStringPoolPos + keyStringPoolSize;

        println("dynamicRefTablePos:" + dynamicRefTablePos); // 1072

        int tableLibraryType = HexUtil.littleEndianBytesToInt(b[dynamicRefTablePos], b[dynamicRefTablePos + 1]);
        println("tableLibraryType:" + HexUtil.intToHexString(tableLibraryType));


        println("tableLibrary header size:" + HexUtil.littleEndianBytesToHexString(b[dynamicRefTablePos + 2], b[dynamicRefTablePos + 3]));

        println("tableLibrary size:" + HexUtil.littleEndianBytesToHexString(b[dynamicRefTablePos + 4], b[dynamicRefTablePos + 5], b[dynamicRefTablePos + 6], b[dynamicRefTablePos + 7]));

        dynamicRefTablePos += 12;
        println("packageId:" + HexUtil.littleEndianBytesToHexString(b[dynamicRefTablePos], b[dynamicRefTablePos + 1], b[dynamicRefTablePos + 2], b[dynamicRefTablePos + 3]));

        println("packageName:" + HexUtil.getString(b, dynamicRefTablePos + 4, 256));
        dynamicRefTablePos -= 12;

        //dynamicRefTable chunk size = 272
        int configListStartPos = dynamicRefTablePos + 272;

        println("resTableTypeSpecStartPos:" + HexUtil.intToHexString(configListStartPos));

        int i = 0;

        while (configListStartPos < resTableSize) {

            println(String.format("====第%s次解析====", i + 1));

            int type = HexUtil.littleEndianBytesToInt(b[configListStartPos], b[configListStartPos + 1]);

            if (type == RES_TABLE_TYPE_SPEC_TYPE) {
                println("[Type]:RES_TABLE_TYPE_SPEC_TYPE");
            } else if (type == RES_TABLE_TYPE_TYPE) {
                println("[Type]:RES_TABLE_TYPE_TYPE");

                int entryCount = HexUtil.littleEndianBytesToInt(b, configListStartPos + 12, 4);
                println("entryCount:" + entryCount);
                int entryStart = HexUtil.littleEndianBytesToInt(b, configListStartPos + 16, 4);
                println("entryStart:" + entryStart);
                int configSize = HexUtil.littleEndianBytesToInt(b, configListStartPos + 20, 4);
                println("configSize:" + configSize);

                //解析entry偏移数组
                int entryOffsetArrayStartPos = configListStartPos + 20 + configSize;

                int[] entryOffsetArray = new int[entryCount];
                for (int j = 0; j < entryOffsetArray.length; j++) {
                    entryOffsetArray[j] = HexUtil.littleEndianBytesToInt(b, entryOffsetArrayStartPos, 4);
                    entryOffsetArrayStartPos += 4;
                }

                //解析entry
                // int realEntryStart = configListStartPos + entryStart;

                println("比较1：" + (configListStartPos + entryStart));
                println("比较2：" + (entryOffsetArrayStartPos));

                int realEntryStart = entryOffsetArrayStartPos;

                for (int k = 0; k < entryCount; k++) {
                    int entryOffset = entryOffsetArray[k];
                    if (entryOffset == -1) {
                        continue;
                    }
                    int entryPos = realEntryStart + entryOffset;
                    int entrySize = HexUtil.littleEndianBytesToInt(b, entryPos, 2);
                    int flags = HexUtil.littleEndianBytesToInt(b, entryPos + 2, 2);
                    int index = HexUtil.littleEndianBytesToInt(b, entryPos + 4, 4);

                    println("");
                    println("entryPos:" + entryPos);
                    println("entrySize:" + entrySize);
                    println("flags:" + flags);
                    println("资源项名称index:" + Integer.toHexString(index));

                    flagsSet.add(flags);

                    if ((flags & 1) == 1) {
//                        checkToRewritePackageId(pp, idMaps) // mapEntry.parent
//                        def count1 = readInt()
//                        def count = count1 & 0x00ffff // fix aapt v22 bug?
//                        for (int j = 0; j < count; j++) {
//                            checkToRewritePackageId(pp, idMaps) // map.name
//                            checkToRewriteTypedValueId(pp, idMaps) // map.value
//                        }
                        println("复杂类型，数据如下:" + HexUtil.littleEndianBytesToHexString(b, entryPos + 8, 4));
                        int count1 = HexUtil.littleEndianBytesToInt(b, entryPos + 12, 4);
                        println("count:" + count1);
                        int count2 = count1 & 0x00ffff;
                        int mapStartPos = entryPos + 16;
                        for (int m = 0; m < count2; m++) {
                            println("map.name:" + HexUtil.littleEndianBytesToHexString(b, mapStartPos, 4));
                            println("map.value:" + HexUtil.littleEndianBytesToHexString(b, mapStartPos + 8, 4));
                            mapStartPos += 12;
                        }
                    } else {
                        println("简单类型，数据如下:");
                        println("大小:" + HexUtil.littleEndianBytesToHexString(b, entryPos + 8, 2));
                        println("保留字:" + HexUtil.littleEndianBytesToHexString(b, entryPos + 10, 1));
                        println("类型:" + HexUtil.littleEndianBytesToHexString(b, entryPos + 11, 1));
                        println("实际数据:" + HexUtil.littleEndianBytesToHexString(b, entryPos + 12, 4));
                    }

                }

            } else {
                throw new IllegalStateException("invalid tabletype:" + HexUtil.intToHexString(type));
            }

            int chunkSize = HexUtil.littleEndianBytesToInt(b, configListStartPos + 4, 4);

            println("configListStartPos:" + configListStartPos);

            configListStartPos += chunkSize;
            i++;
        }

        println(flagsSet.size() + "");

        for (Integer flags : flagsSet) {
            System.out.print(" " + flags);
        }
    }

    public static void println(CharSequence charSequence) {
        System.out.println(charSequence);
    }


}
