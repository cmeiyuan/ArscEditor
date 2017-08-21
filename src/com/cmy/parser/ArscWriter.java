package com.cmy.parser;

import com.cmy.parser.bean.*;
import com.cmy.parser.bean.tabletype.*;
import com.cmy.parser.utils.ArscUtils;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by cmy on 2017/8/17
 */
public class ArscWriter extends ArscEditor {

    public ArscWriter(File file) throws FileNotFoundException {
        super(file);
    }

    public void write(ResTable resTable) throws IOException {
        writeResTableHeader(resTable.resTableHeader);
        writeResStringPool(resTable.globalResStringPool);
        writeResTablePackage(resTable.resTablePackage);
    }

    private void writeResTableHeader(ResTableHeader resTableHeader) throws IOException {
        writeResChunkHeader(resTableHeader.resChunkHeader);
        writeInt(resTableHeader.packageCount);
    }

    private void writeResChunkHeader(ResChunkHeader resChunkHeader) throws IOException {
        writeShort(resChunkHeader.type);
        writeShort(resChunkHeader.headerSize);
        writeInt(resChunkHeader.size);
    }

    private void writeResStringPool(ResStringPool resStringPool) throws IOException {
        ResStringPool.ResStringPoolHeader resStringPoolHeader = resStringPool.resStringPoolHeader;
        writeResChunkHeader(resStringPoolHeader.resChunkHeader);
        writeInt(resStringPoolHeader.stringCount);
        writeInt(resStringPoolHeader.styleCount);
        writeInt(resStringPoolHeader.flags);
        writeInt(resStringPoolHeader.stringsStart);
        writeInt(resStringPoolHeader.stylesStart);
        writeBytes(resStringPool.data);
    }

    private void writeResTablePackage(ResTablePackage resTablePackage) throws IOException {
        writeResTablePackageHeader(resTablePackage.packageHeader);
        writeResStringPool(resTablePackage.typeStringPool);
        writeResStringPool(resTablePackage.keyStringPool);
        writeResTableChunkList(resTablePackage.resTableChunkList);
    }

    private void writeResTablePackageHeader(ResTablePackageHeader packageHeader) throws IOException {
        writeResChunkHeader(packageHeader.resChunkHeader);
        writeInt(packageHeader.packageId);
        writeBytes(packageHeader.packageName);
        writeInt(packageHeader.typeStringOffset);
        writeInt(packageHeader.lastPublicType);
        writeInt(packageHeader.keyStringOffset);
        writeInt(packageHeader.lastPublicKey);
        writeInt(packageHeader.typeIdOffset);
    }

    private void writeResTableChunkList(List<ResTableChunk> resTableChunkList) throws IOException {
        for (ResTableChunk resTableChunk : resTableChunkList) {
            if (resTableChunk instanceof ResTableLibrary) {
                //System.out.println("Write ResTableLibrary:" + getCurPos());
                writeResTableLibrary((ResTableLibrary) resTableChunk);
            } else if (resTableChunk instanceof ResTableTypeSpec) {
                //System.out.println("Write ResTableTypeSpec:" + getCurPos());
                writeResTableTypeSpec((ResTableTypeSpec) resTableChunk);
            } else if (resTableChunk instanceof ResTableType) {
                //System.out.println("Write ResTableType:" + getCurPos());
                writeResTableType((ResTableType) resTableChunk);
            }
        }
    }

    private void writeResTableLibrary(ResTableLibrary resTableLibrary) throws IOException {
        writeResChunkHeader(resTableLibrary.resChunkHeader);
        writeInt(resTableLibrary.count);
        if (resTableLibrary.resTableLibraryEntries != null) {
            for (ResTableLibrary.ResTableLibraryEntry resTableLibraryEntry : resTableLibrary.resTableLibraryEntries) {
                writeInt(resTableLibraryEntry.packageId);
                writeBytes(resTableLibraryEntry.packageName);
            }
        }
    }

    private void writeResTableTypeSpec(ResTableTypeSpec resTableTypeSpec) throws IOException {
        writeResChunkHeader(resTableTypeSpec.resChunkHeader);
        writeByte(resTableTypeSpec.typeId);
        writeByte(resTableTypeSpec.res0);
        writeShort(resTableTypeSpec.res1);
        writeInt(resTableTypeSpec.entryCount);
        writeBytes(resTableTypeSpec.data);
    }

    private void writeResTableType(ResTableType resTableType) throws IOException {
        writeResChunkHeader(resTableType.resChunkHeader);
        writeByte(resTableType.typeId);
        writeByte(resTableType.res0);
        writeShort(resTableType.res1);
        writeInt(resTableType.entryCount);
        writeInt(resTableType.entryStart);
        writeBytes(resTableType.resTableConfig.data);
        if (resTableType.resTableEntryOffsets != null) {
            for (int offset : resTableType.resTableEntryOffsets) {
                writeInt(offset);
            }
        }
        if (resTableType.resTableEntryList != null) {
            for (ResTableEntry resTableEntry : resTableType.resTableEntryList) {
                if (resTableEntry instanceof ResTableMapEntry) {
                    writeResTableMapEntry((ResTableMapEntry) resTableEntry);
                } else {
                    writeResTableEntry(resTableEntry);
                }
            }
        }
    }

    private void writeResTableEntry(ResTableEntry resTableEntry) throws IOException {
        writeShort(resTableEntry.size);
        writeShort(resTableEntry.flags);
        writeInt(resTableEntry.index);
        writeResValue(resTableEntry.resValue);
    }

    private void writeResTableMapEntry(ResTableMapEntry resTableMapEntry) throws IOException {
        writeShort(resTableMapEntry.size);
        writeShort(resTableMapEntry.flags);
        writeInt(resTableMapEntry.index);
        writeInt(resTableMapEntry.parent);
        writeInt(resTableMapEntry.count);
        if (resTableMapEntry.resTableMapList != null) {
            for (ResTableMap resTableMap : resTableMapEntry.resTableMapList) {
                writeInt(resTableMap.name);
                writeResValue(resTableMap.value);
            }
        }
    }

    private void writeResValue(ResValue resValue) throws IOException {
        writeShort(resValue.size);
        writeByte(resValue.res0);
        writeByte(resValue.dataType);
        writeInt(resValue.data);
    }

}
