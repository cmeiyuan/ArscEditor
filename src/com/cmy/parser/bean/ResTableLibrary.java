package com.cmy.parser.bean;

import java.util.List;

/**
 * Created by cmy on 2017/6/7
 */
public class ResTableLibrary extends ResTableData {
    public int count;

    public List<ResTableLibraryEntry> resTableLibraryEntries;

    public static class ResTableLibraryEntry {
        public int packageId;
        public String packageName;
    }
}
