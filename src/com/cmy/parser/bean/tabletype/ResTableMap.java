package com.cmy.parser.bean.tabletype;

/**
 * Created by cmy on 2017/6/8
 */
public class ResTableMap {
    // The resource identifier defining this mapping's name.  For attribute
    // resources, 'name' can be one of the following special resource types
    // to supply meta-data about the attribute; for all other resource types
    // it must be an attribute resource.
    public int name;
    // This mapping's value.
    public ResValue value;
}
