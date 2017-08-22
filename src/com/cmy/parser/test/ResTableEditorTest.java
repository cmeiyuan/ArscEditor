package com.cmy.parser.test;

import com.cmy.parser.ArscReader;
import com.cmy.parser.ResTableEditor;
import com.cmy.parser.ResTablePrinter;
import com.cmy.parser.bean.ResTable;
import com.cmy.parser.utils.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cmy on 2017/8/21
 */
public class ResTableEditorTest {

    public static void main(String[] args) throws Exception {
        String path = "/Users/cmeiyuan/AndroidStudioProjects/HelloPlugin/module2/plugin/intermediates/res/ap_unzip/resources.arsc";
//        ResTableEditor resTableEditor = new ResTableEditor(path);
//        resTableEditor.modifyPackageId(0x66);
//        Map<Integer, String> map = new HashMap<>();
//        map.put(0x55, "com.cmy.plugin.module2");
//        map.put(0x56, "com.cmy.plugin.module3");
//        map.put(0x57, "com.cmy.plugin.module4");
//        resTableEditor.modifyLibraryChunk(map);
//        resTableEditor.write();

        ArscReader reader = new ArscReader(new File(path));
        print(reader.read());
    }

    private static void print(ResTable resTable) {
        ResTablePrinter printer = new ResTablePrinter(resTable);
        printer.printResTableHeader();
        printer.printGlobalResStringPool();
        printer.printResTablePackage(true);
    }

    private static void check(String path1, String path2) throws Exception {
        int len1 = FileUtils.readFileLength(path1);
        int len2 = FileUtils.readFileLength(path2);
        System.out.println("path:" + len1 + " path1:" + len2);
    }

}
