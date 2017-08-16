package com.cmy.parser;

import java.io.*;

/**
 * Created by cmy on 2017/6/7
 */
public class Main {

    public static void main(String[] args) throws Exception {
        String path = "/Users/cmeiyuan/AndroidStudioProjects/HelloPlugin/module1/plugin/intermediates/res/ap_unzip/resources.arsc";
        ArscParser arscParser = new ArscParser(new File(path));
        ResTablePrinter printer = new ResTablePrinter(arscParser.parse());
        printer.printResTableHeader();
        printer.printGlobalResStringPool();
        //printer.printResTablePackage();
    }

    public static int readFileLength(String path) throws Exception{
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在");
        }
        InputStream inputStream = new FileInputStream(file);
        byte[] b = new byte[1024 * 1024];
        return inputStream.read(b);
    }

}
