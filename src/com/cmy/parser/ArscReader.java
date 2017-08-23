package com.cmy.parser;

import com.cmy.parser.bean.*;
import com.cmy.parser.bean.tabletype.*;
import com.cmy.parser.utils.ArscUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cmy on 2017/6/7
 */
public class ArscReader extends ArscEditor {

    public ArscReader(File file) throws FileNotFoundException {
        super(file);
    }

    public ResTable read() throws IOException {
        ResTable resTable = new ResTable();
        resTable.resTableHeader = readResTableHeader();
        resTable.globalResStringPool = readResStringPool();
        System.out.println("curPos:" + getCurPos());
        resTable.resTablePackage = readResTablePackage(resTable.resTableHeader.resChunkHeader.size);
        return resTable;
    }

    private ResTableHeader readResTableHeader() throws IOException {
        ResTableHeader resTableHeader = new ResTableHeader();
        resTableHeader.resChunkHeader = readResChunkHeader();
        resTableHeader.packageCount = readInt();
        return resTableHeader;
    }

    private ResChunkHeader readResChunkHeader() throws IOException {
        ResChunkHeader resChunkHeader = new ResChunkHeader();
        resChunkHeader.type = readShort();
        resChunkHeader.headerSize = readShort();
        resChunkHeader.size = readInt();
        return resChunkHeader;
    }

    private ResStringPool readResStringPool() throws IOException {
        long curPos = getCurPos();
        ResStringPool resStringPool = new ResStringPool();
        resStringPool.resStringPoolHeader = new ResStringPool.ResStringPoolHeader();
        resStringPool.resStringPoolHeader.resChunkHeader = readResChunkHeader();
        ResStringPool.ResStringPoolHeader resStringPoolHeader = resStringPool.resStringPoolHeader;
        resStringPoolHeader.stringCount = readInt();
        resStringPoolHeader.styleCount = readInt();
        resStringPoolHeader.flags = readInt();
        resStringPoolHeader.stringsStart = readInt();
        resStringPoolHeader.stylesStart = readInt();

        // ****************************************************************
        // 先读取整个字符串池数据，不包括ResStringPoolHeader
        ResChunkHeader resChunkHeader = resStringPoolHeader.resChunkHeader;
        int length = resChunkHeader.size - resChunkHeader.headerSize;
        resStringPool.data = readBytes(length);
        back(length);
        // ****************************************************************

        resStringPool.stringOffsets = readIntArray(resStringPoolHeader.stringCount);
        resStringPool.styleOffsets = readIntArray(resStringPoolHeader.styleCount);
        resStringPool.strings = readStringBytesArray(resStringPoolHeader.flags, curPos + resStringPoolHeader.stringsStart, resStringPool.stringOffsets);
        resStringPool.styles = readStringBytesArray(resStringPoolHeader.flags, curPos + resStringPoolHeader.stylesStart, resStringPool.styleOffsets);
        seekTo(curPos + resChunkHeader.size);
        return resStringPool;
    }

    private byte[][] readStringBytesArray(int flags, long startPos, int[] offsets) throws IOException {
        boolean isUTF8 = ArscUtils.isUTF8(flags);
        int length = offsets.length;
        byte[][] array = new byte[length][];
        for (int i = 0; i < length; i++) {
            long sPos = startPos + offsets[i];
            seekTo(sPos);
            byte b1 = readByte();
            byte b2 = readByte();
            byte b3 = readByte();
            byte b4 = readByte();
            byte[] data = new byte[]{b1, b2, b3, b4};
            int[] result;
            if (isUTF8) {
                result = ArscUtils.getUtf8(data, 0);
            } else {
                result = ArscUtils.getUtf16(data, 0);
            }
            seekTo(sPos + result[0]);
            array[i] = readBytes(result[1]);
        }
        return array;
    }

    private ResTablePackage readResTablePackage(long totalSize) throws IOException {
        long curPos = getCurPos();
        ResTablePackage resTablePackage = new ResTablePackage();
        resTablePackage.packageHeader = readResTablePackageHeader();
        seekTo(curPos + resTablePackage.packageHeader.typeStringOffset);
        resTablePackage.typeStringPool = readResStringPool();
        seekTo(curPos + resTablePackage.packageHeader.keyStringOffset);
        resTablePackage.keyStringPool = readResStringPool();

        // 288 + 两个字符串池大小
        int offset = resTablePackage.packageHeader.resChunkHeader.headerSize
                + resTablePackage.typeStringPool.resStringPoolHeader.resChunkHeader.size
                + resTablePackage.keyStringPool.resStringPoolHeader.resChunkHeader.size;

        seekTo(curPos + offset);
        resTablePackage.resTableChunkList = readResTableChunkList(totalSize);
        return resTablePackage;
    }

    private List<ResTableChunk> readResTableChunkList(long totalSize) throws IOException {
        List<ResTableChunk> list = new ArrayList<>();
        long startPos = getCurPos();
        while (startPos < totalSize) {
            seekTo(startPos);
            ResChunkHeader resChunkHeader = readResChunkHeader();
            short type = resChunkHeader.type;
            if (type == ResTable.RES_TABLE_LIBRARY_TYPE) {
                //System.out.println("RES_TABLE_LIBRARY_TYPE");
                list.add(readResTableLibrary(resChunkHeader));
            } else if (type == ResTable.RES_TABLE_TYPE_SPEC_TYPE) {
                //System.out.println("RES_TABLE_TYPE_SPEC_TYPE");
                list.add(readResTableTypeSpec(resChunkHeader));
            } else if (type == ResTable.RES_TABLE_TYPE_TYPE) {
                //System.out.println("RES_TABLE_TYPE_TYPE");
                list.add(readResTableType(startPos, resChunkHeader));
            } else {
                throw new IOException("read package error");
            }
            startPos += resChunkHeader.size;
        }
        return list;
    }

    private ResTableLibrary readResTableLibrary(ResChunkHeader resChunkHeader) throws IOException {
        ResTableLibrary resTableLibrary = new ResTableLibrary();
        resTableLibrary.resChunkHeader = resChunkHeader;
        resTableLibrary.count = readInt();
        resTableLibrary.resTableLibraryEntries = new ArrayList<>();
        for (int i = 0; i < resTableLibrary.count; i++) {
            ResTableLibrary.ResTableLibraryEntry entry = new ResTableLibrary.ResTableLibraryEntry();
            entry.packageId = readInt();
            entry.packageName = readBytes(256);
            resTableLibrary.resTableLibraryEntries.add(entry);
        }
        return resTableLibrary;
    }

    private ResTableTypeSpec readResTableTypeSpec(ResChunkHeader resChunkHeader) throws IOException {
        ResTableTypeSpec resTableTypeSpec = new ResTableTypeSpec();
        resTableTypeSpec.resChunkHeader = resChunkHeader;
        resTableTypeSpec.typeId = readByte();
        resTableTypeSpec.res0 = readByte();
        resTableTypeSpec.res1 = readShort();
        resTableTypeSpec.entryCount = readInt();
        resTableTypeSpec.data = readBytes(resChunkHeader.size - resChunkHeader.headerSize);
        return resTableTypeSpec;
    }

    private ResTableType readResTableType(long startPos, ResChunkHeader resChunkHeader) throws IOException {
        ResTableType resTableType = new ResTableType();
        resTableType.resChunkHeader = resChunkHeader;
        resTableType.typeId = readByte();
        resTableType.res0 = readByte();
        resTableType.res1 = readShort();
        resTableType.entryCount = readInt();
        resTableType.entryStart = readInt();
        resTableType.resTableConfig = readResTableConfig();
        //这里的头大小包含了ResTableConfig的大小
        seekTo(startPos + resChunkHeader.headerSize);
        resTableType.resTableEntryOffsets = readIntArray(resTableType.entryCount);
        resTableType.resTableEntryList = readResTableEntryList(startPos + resTableType.entryStart, resTableType.resTableEntryOffsets);
        return resTableType;
    }

    private List<ResTableEntry> readResTableEntryList(long startPos, int[] offsets) throws IOException {
        List<ResTableEntry> list = new ArrayList<>(offsets.length);
        for (int offset : offsets) {
            //no entry
            if (offset == -1) {
                continue;
            }
            seekTo(startPos + offset);
            list.add(readResTableEntry());
        }
        return list;
    }

    private ResTableEntry readResTableEntry() throws IOException {
        short size = readShort();
        short flags = readShort();
        int index = readInt();
        if ((flags & ResTableEntry.FLAG_COMPLEX) == 0) {
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
            resTableMapEntry.parent = readInt();
            // fix aapt v22 bug?
            resTableMapEntry.count = readInt() & 0x00ffff;
            resTableMapEntry.resTableMapList = new ArrayList<>();
            for (int i = 0; i < resTableMapEntry.count; i++) {
                ResTableMap resTableMap = new ResTableMap();
                resTableMap.name = readInt();
                resTableMap.value = readResValue();
                resTableMapEntry.resTableMapList.add(resTableMap);
            }
            return resTableMapEntry;
        }
    }

    private ResValue readResValue() throws IOException {
        ResValue resValue = new ResValue();
        resValue.size = readShort();
        resValue.res0 = readByte();
        resValue.dataType = readByte();
        resValue.data = readInt();
        return resValue;
    }

    private ResTableConfig readResTableConfig() throws IOException {
        ResTableConfig resTableConfig = new ResTableConfig();
        resTableConfig.size = readInt();
        back(4);
        resTableConfig.data = readBytes(resTableConfig.size);
        return resTableConfig;
    }

    private ResTablePackageHeader readResTablePackageHeader() throws IOException {
        ResTablePackageHeader resTablePackageHeader = new ResTablePackageHeader();
        resTablePackageHeader.resChunkHeader = readResChunkHeader();
        resTablePackageHeader.packageId = readInt();
        resTablePackageHeader.packageName = readBytes(256);
        resTablePackageHeader.typeStringOffset = readInt();
        resTablePackageHeader.lastPublicType = readInt();
        resTablePackageHeader.keyStringOffset = readInt();
        resTablePackageHeader.lastPublicKey = readInt();
        resTablePackageHeader.typeIdOffset = readInt();
        return resTablePackageHeader;
    }

}
