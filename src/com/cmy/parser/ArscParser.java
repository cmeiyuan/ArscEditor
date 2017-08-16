package com.cmy.parser;

import com.cmy.parser.bean.*;
import com.cmy.parser.bean.tabletype.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cmy on 2017/6/7
 */
public class ArscParser {

    public static final int RES_STRING_POOL_TYPE = 0x0001;
    public static final int RES_TABLE_TYPE = 0x0002;
    public static final int RES_TABLE_PACKAGE_TYPE = 0x0200;
    public static final int RES_TABLE_TYPE_TYPE = 0x0201;
    public static final int RES_TABLE_TYPE_SPEC_TYPE = 0x0202;
    public static final int RES_TABLE_LIBRARY_TYPE = 0x0203;

    private ArscEditor arscEditor;

    public ArscParser(File arscFile) throws FileNotFoundException {
        this.arscEditor = new ArscEditor(arscFile);
    }

    public ResTable parse() throws IOException {
        ResTable resTable = new ResTable();
        resTable.resTableHeader = readResTableHeader();
        resTable.globalResStringPool = readResStringPool();
        resTable.resTablePackage = readResTablePackage(resTable.resTableHeader.resChunkHeader.size);
        return resTable;
    }

    private ResTableHeader readResTableHeader() throws IOException {
        ResTableHeader resTableHeader = new ResTableHeader();
        resTableHeader.resChunkHeader = readResChunkHeader();
        resTableHeader.packageCount = arscEditor.readInt();
        return resTableHeader;
    }

    private ResChunkHeader readResChunkHeader() throws IOException {
        ResChunkHeader resChunkHeader = new ResChunkHeader();
        resChunkHeader.type = arscEditor.readShort();
        resChunkHeader.headerSize = arscEditor.readShort();
        resChunkHeader.size = arscEditor.readInt();
        return resChunkHeader;
    }

    private ResStringPool readResStringPool() throws IOException {
        long curPos = arscEditor.getCurPos();
        ResStringPool resStringPool = new ResStringPool();
        resStringPool.resChunkHeader = readResChunkHeader();
        resStringPool.stringCount = arscEditor.readInt();
        resStringPool.styleCount = arscEditor.readInt();
        resStringPool.flags = arscEditor.readInt();
        resStringPool.stringsStart = arscEditor.readInt();
        resStringPool.stylesStart = arscEditor.readInt();
        resStringPool.stringOffsets = arscEditor.readIntArray(resStringPool.stringCount);
        resStringPool.styleOffsets = arscEditor.readIntArray(resStringPool.styleCount);
        resStringPool.strings = readStringArray(resStringPool.flags, curPos + resStringPool.stringsStart, resStringPool.stringOffsets);
        resStringPool.styles = readStringArray(resStringPool.flags, curPos + resStringPool.stylesStart, resStringPool.styleOffsets);
        arscEditor.seekTo(curPos + resStringPool.resChunkHeader.size);
        return resStringPool;
    }

    private String[] readStringArray(int flags, long startPos, int[] offsets) throws IOException {
        boolean isUTF8 = ArscUtils.isUTF8(flags);
        int length = offsets.length;
        String[] array = new String[length];
        for (int i = 0; i < length; i++) {
            long sPos = startPos + offsets[i];
            arscEditor.seekTo(sPos);
            byte b1 = arscEditor.readByte();
            byte b2 = arscEditor.readByte();
            byte b3 = arscEditor.readByte();
            byte b4 = arscEditor.readByte();
            byte[] data = new byte[]{b1, b2, b3, b4};
            int[] result;
            if (isUTF8) {
                result = ArscUtils.getUtf8(data, 0);
            } else {
                result = ArscUtils.getUtf16(data, 0);
            }
            arscEditor.seekTo(sPos + result[0]);
            array[i] = arscEditor.readString(result[1]);
        }
        return array;
    }

    private ResTablePackage readResTablePackage(long totalSize) throws IOException {
        long curPos = arscEditor.getCurPos();
        ResTablePackage resTablePackage = new ResTablePackage();
        resTablePackage.packageHeader = readResTablePackageHeader();
        arscEditor.seekTo(curPos + resTablePackage.packageHeader.typeStringOffset);
        resTablePackage.typeStringPool = readResStringPool();
        arscEditor.seekTo(curPos + resTablePackage.packageHeader.keyStringOffset);
        resTablePackage.keyStringPool = readResStringPool();

        // 288 + 两个字符串池大小
        int offset = resTablePackage.packageHeader.resChunkHeader.headerSize
                + resTablePackage.typeStringPool.resChunkHeader.size
                + resTablePackage.keyStringPool.resChunkHeader.size;

        arscEditor.seekTo(curPos + offset);
        resTablePackage.resTableDataList = readResTableDataList(totalSize);

        return resTablePackage;
    }

    private List<ResTableData> readResTableDataList(long totalSize) throws IOException {
        List<ResTableData> list = new ArrayList<>();
        long startPos = arscEditor.getCurPos();
        while (startPos < totalSize) {
            arscEditor.seekTo(startPos);
            ResChunkHeader resChunkHeader = readResChunkHeader();
            short type = resChunkHeader.type;
            if (type == RES_TABLE_LIBRARY_TYPE) {
                list.add(readResTableLibrary(resChunkHeader));
            } else if (type == RES_TABLE_TYPE_SPEC_TYPE) {
                list.add(readResTableTypeSpec(resChunkHeader));
            } else if (type == RES_TABLE_TYPE_TYPE) {
                list.add(readResTableType(startPos, resChunkHeader));
            }
            startPos += resChunkHeader.size;
        }
        return list;
    }

    private ResTableLibrary readResTableLibrary(ResChunkHeader resChunkHeader) throws IOException {
        ResTableLibrary resTableLibrary = new ResTableLibrary();
        resTableLibrary.resChunkHeader = resChunkHeader;
        resTableLibrary.count = arscEditor.readInt();
        resTableLibrary.resTableLibraryEntries = new ArrayList<>();
        for (int i = 0; i < resTableLibrary.count; i++) {
            ResTableLibrary.ResTableLibraryEntry entry = new ResTableLibrary.ResTableLibraryEntry();
            entry.packageId = arscEditor.readInt();
            entry.packageName = arscEditor.readString(256);
        }
        return resTableLibrary;
    }

    private ResTableTypeSpec readResTableTypeSpec(ResChunkHeader resChunkHeader) {
        ResTableTypeSpec resTableTypeSpec = new ResTableTypeSpec();
        resTableTypeSpec.resChunkHeader = resChunkHeader;
        return resTableTypeSpec;
    }

    private ResTableType readResTableType(long startPos, ResChunkHeader resChunkHeader) throws IOException {
        ResTableType resTableType = new ResTableType();
        resTableType.resChunkHeader = resChunkHeader;
        resTableType.typeId = arscEditor.readByte();
        resTableType.res0 = arscEditor.readByte();
        resTableType.res1 = arscEditor.readShort();
        resTableType.entryCount = arscEditor.readInt();
        resTableType.entryStart = arscEditor.readInt();
        resTableType.resTableConfig = readResTableConfig();
        //这里的头大小包含了ResTableConfig的大小
        arscEditor.seekTo(startPos + resTableType.resChunkHeader.headerSize);
        resTableType.resTableEntryOffsets = arscEditor.readIntArray(resTableType.entryCount);
        resTableType.resTableEntryList = readResTableEntryList(startPos + resTableType.entryStart, resTableType.resTableEntryOffsets);
        return resTableType;
    }

    private List<ResTableEntry> readResTableEntryList(long startPos, int[] offsets) throws IOException {
        List<ResTableEntry> list = new ArrayList<>(offsets.length);
        for (int i = 0; i < offsets.length; i++) {
            //no entry
            if (offsets[i] == -1) {
                continue;
            }
            arscEditor.seekTo(startPos + offsets[i]);
            list.add(readResTableEntry());
        }
        return list;
    }

    private ResTableEntry readResTableEntry() throws IOException {
        short size = arscEditor.readShort();
        short flags = arscEditor.readShort();
        int index = arscEditor.readInt();
        if (flags == 0) {
            //普通类型，后面跟的是ResValue
            ResTableEntry resTableEntry = new ResTableEntry();
            resTableEntry.size = size;
            resTableEntry.flags = flags;
            resTableEntry.index = index;
            resTableEntry.resValue = readResValue();
            return resTableEntry;
        } else {
            //复杂类型，后面跟的是ResTableMapEntry
            ResTableMapEntry resTableMapEntry = new ResTableMapEntry();
            resTableMapEntry.size = size;
            resTableMapEntry.flags = flags;
            resTableMapEntry.index = index;
            resTableMapEntry.parent = arscEditor.readInt();
            // fix aapt v22 bug?
            resTableMapEntry.count = arscEditor.readInt() & 0x00ffff;
            resTableMapEntry.resTableMapList = new ArrayList<>();
            for (int i = 0; i < resTableMapEntry.count; i++) {
                ResTableMap resTableMap = new ResTableMap();
                resTableMap.name = arscEditor.readInt();
                resTableMap.value = readResValue();
                resTableMapEntry.resTableMapList.add(resTableMap);
            }
            return resTableMapEntry;
        }
    }

    private ResValue readResValue() throws IOException {
        ResValue resValue = new ResValue();
        resValue.size = arscEditor.readShort();
        resValue.res0 = arscEditor.readByte();
        resValue.dataType = arscEditor.readByte();
        resValue.data = arscEditor.readInt();
        return resValue;
    }

    private ResTableConfig readResTableConfig() throws IOException {
        ResTableConfig resTableConfig = new ResTableConfig();
        resTableConfig.size = arscEditor.readInt();
        return resTableConfig;
    }

    private ResTablePackageHeader readResTablePackageHeader() throws IOException {
        ResTablePackageHeader resTablePackageHeader = new ResTablePackageHeader();
        resTablePackageHeader.resChunkHeader = readResChunkHeader();
        resTablePackageHeader.packageId = arscEditor.readInt();
        resTablePackageHeader.packageName = arscEditor.readString(256);
        resTablePackageHeader.typeStringOffset = arscEditor.readInt();
        resTablePackageHeader.lastPublicType = arscEditor.readInt();
        resTablePackageHeader.keyStringOffset = arscEditor.readInt();
        resTablePackageHeader.lastPublicKey = arscEditor.readInt();
        resTablePackageHeader.typeIdOffset = arscEditor.readInt();
        return resTablePackageHeader;
    }

}
