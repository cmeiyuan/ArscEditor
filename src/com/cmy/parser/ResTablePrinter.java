package com.cmy.parser;

import com.cmy.parser.bean.*;
import com.cmy.parser.bean.tabletype.*;

import java.util.List;

/**
 * Created by cmy on 2017/6/7
 */
public class ResTablePrinter {

    private ResTable resTable;

    public ResTablePrinter(ResTable resTable) {
        this.resTable = resTable;
    }

    public void printResTableHeader() {
        ResTableHeader resTableHeader = resTable.resTableHeader;
        println("ResTableHeader", resTableHeader.resChunkHeader);
        println("ResTableHeader Package Count: ", resTableHeader.packageCount);
    }

    public void printGlobalResStringPool() {
        printResStringPool(resTable.globalResStringPool);
    }

    public void printResTablePackage() {
        ResStringPool globalStringPool = resTable.globalResStringPool;
        ResTablePackage resTablePackage = resTable.resTablePackage;

        println("ResTablePackageHeader ", resTablePackage.packageHeader.resChunkHeader);
        println("ResTablePackageHeader packageId: ", resTablePackage.packageHeader.packageId);
        println("ResTablePackageHeader packageName: " + resTablePackage.packageHeader.packageName);
        println("ResTablePackageHeader typeStringOffset: ", resTablePackage.packageHeader.typeStringOffset);
        println("ResTablePackageHeader lastPublicType: ", resTablePackage.packageHeader.lastPublicType);
        println("ResTablePackageHeader keyStringOffset: ", resTablePackage.packageHeader.keyStringOffset);
        println("ResTablePackageHeader lastPublicKey: ", resTablePackage.packageHeader.lastPublicKey);
        println("ResTablePackageHeader typeIdOffset: ", resTablePackage.packageHeader.typeIdOffset);

        printResStringPool(resTablePackage.typeStringPool);
        printResStringPool(resTablePackage.keyStringPool);

        List<ResTableData> list = resTablePackage.resTableDataList;
        for (int i = 0; i < list.size(); i++) {
            ResTableData resTableData = list.get(i);
            short type = resTableData.resChunkHeader.type;
            switch (type) {
                case ArscParser.RES_TABLE_LIBRARY_TYPE:
                    //println("RES_TABLE_LIBRARY_TYPE");
                    break;
                case ArscParser.RES_TABLE_TYPE_SPEC_TYPE:
                    //println("RES_TABLE_TYPE_SPEC_TYPE");
                    break;
                case ArscParser.RES_TABLE_TYPE_TYPE:
                    //println("RES_TABLE_TYPE_TYPE");
                    println(resTablePackage.packageHeader.packageId, globalStringPool, resTablePackage.keyStringPool, (ResTableType) resTableData);
                    break;
            }
        }
    }

    public static void printResStringPool(ResStringPool resStringPool) {
        println("");
        println("############ 开始 打印字符串池############");
        println("ResStringPool ", resStringPool.resChunkHeader);

        println("ResStringPool stringCount: ", resStringPool.stringCount);
        println("ResStringPool styleCount: ", resStringPool.styleCount);
        println("ResStringPool flags: ", resStringPool.flags);
        println("ResStringPool stringsStart: ", resStringPool.stringsStart);
        println("ResStringPool stylesStart: ", resStringPool.stylesStart);

        println("=====字符串=====");
        println(resStringPool.strings);
        println("=====样式=====");
        println(resStringPool.styles);
        println("############ 结束 打印字符串池############");
        println("");
    }

    private void println(int pp, ResStringPool globalStringPool, ResStringPool keyStringPool, ResTableType resTableType) {
        List<ResTableEntry> list = resTableType.resTableEntryList;
        for (int i = 0; i < list.size(); i++) {
            ResTableEntry entry = list.get(i);
            if (entry.flags == 0) {
                println("------------------------------------------");
                println("普通类型:");
                println("ResTableType TypeId: ", resTableType.typeId);
                println("ResTableEntry size: ", entry.size);
                println("ResTableEntry flags: ", entry.flags);
                println("ResTableEntry index: ", entry.index);
                String keyName = keyStringPool.strings[entry.index];
                println("ResTableEntry keyName: " + keyName);
                println("");
                println(pp, resTableType.typeId, keyName, globalStringPool, entry.resValue);
                println("");
                println("-------------------");
                println("");
            } else {
                println("------------------------------------------");
                println("复杂类型:");
                println("ResTableType TypeId: ", resTableType.typeId);
                println("ResTableEntry size: ", entry.size);
                println("ResTableEntry flags: ", entry.flags);
                println("ResTableEntry index: ", entry.index);
                String keyName = keyStringPool.strings[entry.index];
                println("ResTableEntry keyName: " + keyName);
                ResTableMapEntry valueMapEntry = (ResTableMapEntry) entry;
                println("ResTableMapEntry parent: ", valueMapEntry.parent);
                println("ResTableMapEntry count: ", valueMapEntry.count);

                List<ResTableMap> resTableMapList = valueMapEntry.resTableMapList;
                for (ResTableMap resValueMap : resTableMapList) {
                    println("");
                    println("ResTableMap Name: " + Integer.toHexString(resValueMap.name));
                    ResValue resValue = resValueMap.value;
                    println(pp, resTableType.typeId, keyName, globalStringPool, resValue);
                    println("");
                }
                println("------------------------------------------");
                println("");
            }
        }
    }

    private static void println(int pp, int tt, String key, ResStringPool globalStringPool, ResValue resValue) {
        println("ResValue size: ", resValue.size);
        println("ResValue res0: ", resValue.res0);
        println("ResValue dataType: ", resValue.dataType);
        println("ResValue data: ", resValue.data);
        if (resValue.dataType == 3) {
            println(String.format("%s=%s", key, globalStringPool.strings[resValue.data]));
        }
    }

    private static void println(String prefix, ResChunkHeader resChunkHeader) {
        println(prefix + "ResChunkHeader Type: ", resChunkHeader.type);
        println(prefix + "ResChunkHeader Header Size: ", resChunkHeader.headerSize);
        println(prefix + "ResChunkHeader Size: ", resChunkHeader.size);
    }

    private static void println(String[] array) {
        System.out.println("总共有:" + array.length);
        for (int i = 0; i < array.length; i++) {
            println(array[i]);
        }
    }

    private static void println(CharSequence charSequence) {
        System.out.println(charSequence);
    }

    private static void println(CharSequence charSequence, int value) {
        System.out.println(charSequence + "0x" + Integer.toHexString(value));
    }

    public static String getResourceId(int id) {
        String str = Integer.toHexString(id);
        if (str.length() == 7) {
            return "0x0" + str;
        } else {
            return "0x" + str;
        }
    }

}
