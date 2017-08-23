package com.cmy.parser.test;

import com.cmy.parser.ArscReader;
import com.cmy.parser.ResTablePrinter;
import com.cmy.parser.bean.ResTable;

import java.io.File;

/**
 * Created by cmy on 2017/8/23
 */
public class Compare {

    public static void main(String[] args) throws Exception{
        String path1 = "/Users/cmeiyuan/Documents/arsc/resources-ok.arsc";
        String path2 = "/Users/cmeiyuan/AndroidStudioProjects/HelloPlugin/module2/plugin/intermediates/res/ap_unzip/resources.arsc";

        ArscReader reader = new ArscReader(new File(path2));
        print(reader.read());
//
//        System.out.println(FileUtils.readFileLength(path1));
//        System.out.println(FileUtils.readFileLength(path2));
//
        Main.compare(path1, path2, -1);
    }

    private static void print(ResTable resTable) {
        ResTablePrinter printer = new ResTablePrinter(resTable);
        printer.printResTableHeader();
        printer.printGlobalResStringPool();
        printer.printResTablePackage(true);
    }



}
