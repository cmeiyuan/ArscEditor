package com.cmy.parser.test;

import com.cmy.parser.ArscReader;
import com.cmy.parser.ResTablePrinter;
import com.cmy.parser.bean.ResTable;
import com.cmy.parser.bean.ResTableChunk;
import com.cmy.parser.bean.ResTableLibrary;
import com.cmy.parser.utils.FileUtils;

import java.io.File;

/**
 * Created by cmy on 2017/6/7
 */
public class Main {

    public static void main(String[] args) throws Exception {
        String path = "/Users/cmeiyuan/AndroidStudioProjects/HelloPlugin/module1/plugin/intermediates/res/ap_unzip/resources.arsc";
        File file = new File(path);
        ArscReader arscReader = new ArscReader(file);
        ResTable resTable = arscReader.read();
        //print(resTable);

        ResTableChunk resTableChunk = resTable.resTablePackage.resTableChunkList.get(0);
        if (resTableChunk instanceof ResTableLibrary) {
            ResTableLibrary resTableLibrary = (ResTableLibrary) resTableChunk;
            resTableLibrary.resTableLibraryEntries.forEach(resTableLibraryEntry -> System.out.println(resTableLibraryEntry.packageId + " " + new String(resTableLibraryEntry.packageName)));
        }

        String path1 = "/Users/cmeiyuan/AndroidStudioProjects/HelloPlugin/module1/plugin/intermediates/res/ap_unzip/resources1.arsc";
        File file1 = new File(path1);
        if (file1.exists()) {
            file1.delete();
        }
        file1.createNewFile();


//        ArscWriter arscWriter = new ArscWriter(file1);
//        arscWriter.write(resTable);
//
//        ArscReader arscReader1 = new ArscReader(file1);
//        ResTable resTable1 = arscReader1.read();
//        print(resTable1);
    }


    private static void print(ResTable resTable) {
        ResTablePrinter printer = new ResTablePrinter(resTable);
        printer.printResTableHeader();
        printer.printGlobalResStringPool();
        printer.printResTablePackage(false);
    }

    private static void check(String path1, String path2) throws Exception {
        int len1 = FileUtils.readFileLength(path1);
        int len2 = FileUtils.readFileLength(path2);
        System.out.println("path:" + len1 + " path1:" + len2);
        compare(path1, path2, -1);
    }

    public static void compare(String path1, String path2, int length) throws Exception {
        byte[] data1 = FileUtils.readBytes(path1);
        byte[] data2 = FileUtils.readBytes(path2);
        boolean matchAll = true;
        for (int i = 0; i < data1.length; i++) {
            if (i == length) {
                break;
            }
            byte a = data1[i];
            byte b = data2[i];
            if (a != b) {
                matchAll = false;
                System.out.println("pos:" + i + " " + a + "<=>" + b);
            }
        }
        if (matchAll) {
            System.out.println("完全一致");
        }
    }

}
