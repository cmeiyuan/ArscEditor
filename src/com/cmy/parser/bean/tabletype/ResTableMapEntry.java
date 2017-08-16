package com.cmy.parser.bean.tabletype;

import java.util.List;

/**
 * Created by cmy on 2017/6/8
 */
public class ResTableMapEntry extends ResTableEntry{
    // Resource identifier of the parent mapping, or 0 if there is none.
    // This is always treated as a TYPE_DYNAMIC_REFERENCE.
    public int parent;
    // Number of name/value pairs that follow for FLAG_COMPLEX.
    public int count;

    public List<ResTableMap> resTableMapList;
}
