package com.cmy.parser.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by cmy on 2017/8/17
 */
public class FileUtils {

    public static int readFileLength(String path) throws Exception {
        return readBytes(path).length;
    }

    public static byte[] readBytes(String path) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在");
        }
        InputStream inputStream = new FileInputStream(file);
        byte[] b = new byte[1024 * 1024];
        int len = inputStream.read(b);
        return Arrays.copyOf(b, len);
    }
}
