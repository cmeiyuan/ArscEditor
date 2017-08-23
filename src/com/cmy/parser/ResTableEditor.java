package com.cmy.parser;

import com.cmy.parser.bean.*;
import com.cmy.parser.bean.tabletype.ResTableMapEntry;
import com.cmy.parser.bean.tabletype.ResTableType;
import com.cmy.parser.utils.ArscUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by cmy on 2017/8/21
 */
public class ResTableEditor {

    private ResTable resTable;
    private File file;

    public ResTableEditor(String path) throws Exception {
        this(new File(path));
    }

    public ResTableEditor(File file) throws Exception {
        this.file = file;
        this.resTable = new ArscReader(file).read();
    }

    public void modifyPackageId(int pp) {
        ResTablePackage resTablePackage = resTable.resTablePackage;
        resTablePackage.packageHeader.packageId = pp;
        resTablePackage.resTableChunkList.forEach(resTableChunk -> {
            if (resTableChunk instanceof ResTableType) {
                ResTableType resTableType = (ResTableType) resTableChunk;
                resTableType.resTableEntryList.forEach(resTableEntry -> {
                    if (resTableEntry instanceof ResTableMapEntry) {
                        ResTableMapEntry resTableMapEntry = (ResTableMapEntry) resTableEntry;
                        resTableMapEntry.parent = checkAndGetNewPP(resTableMapEntry.parent, pp);
                        resTableMapEntry.resTableMapList.forEach(resTableMap -> {
                            resTableMap.name = checkAndGetNewPP(resTableMap.name, pp);
                            resTableMap.value.data = checkAndGetNewPP(resTableMap.value.data, pp);
                        });
                    } else {
                        resTableEntry.resValue.data = checkAndGetNewPP(resTableEntry.resValue.data, pp);
                    }
                });
            }
        });
    }

    public void modifyLibraryChunk(Map<Integer, String> ppMap) {
        int originalLibraryChunkSize = 0;
        ResTableChunk resTableChunk = resTable.resTablePackage.resTableChunkList.get(0);
        if (resTableChunk instanceof ResTableLibrary) {
            originalLibraryChunkSize = ((ResTableLibrary) resTableChunk).resChunkHeader.size;
            resTable.resTablePackage.resTableChunkList.remove(0);
        }

        if (ppMap == null || ppMap.size() == 0) {
            return;
        }

        ResTableLibrary resTableLibrary = new ResTableLibrary();
        resTableLibrary.resChunkHeader = new ResChunkHeader();
        resTableLibrary.resChunkHeader.type = ResTable.RES_TABLE_LIBRARY_TYPE;
        resTableLibrary.resChunkHeader.headerSize = 12;
        resTableLibrary.resTableLibraryEntries = new ArrayList<>();

        Set<Integer> keySet = ppMap.keySet();
        int count = keySet.size();
        resTableLibrary.count = count;
        resTableLibrary.resChunkHeader.size = 12 + count * 260;
        for (Integer pp : keySet) {
            ResTableLibrary.ResTableLibraryEntry entry = new ResTableLibrary.ResTableLibraryEntry();
            entry.packageId = pp;
            entry.packageName = ArscUtils.getUtf16String(ppMap.get(pp), 256);
            resTableLibrary.resTableLibraryEntries.add(entry);
        }
        resTable.resTablePackage.resTableChunkList.add(0, resTableLibrary);
        int incrementLength = (resTableLibrary.resChunkHeader.size - originalLibraryChunkSize);
        // 修改文件总长度
        resTable.resTableHeader.resChunkHeader.size += incrementLength;
        // 修改包长度
        resTable.resTablePackage.packageHeader.resChunkHeader.size += incrementLength;
    }

    private int checkAndGetNewPP(int value, int pp) {
        if (value >> 24 != 0x7f) {
            return value;
        }
        return (pp << 24) | (value & 0x00ffffff);
    }

    public void write() throws Exception {
        ArscWriter arscWriter = new ArscWriter(file);
        arscWriter.write(resTable);
    }

}
