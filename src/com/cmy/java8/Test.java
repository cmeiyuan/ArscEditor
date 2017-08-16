package com.cmy.java8;

import java.io.File;
import java.io.IOException;

/**
 * Created by cmy on 2017/5/31
 */
public class Test {
    public static void main(String[] args) throws IOException {
        File file = new File("/Users/cmeiyuan/AndroidStudioProjects/HelloPlugin/app/build/outputs/logs/manifest-merger-release-report.txt");
        System.out.println(file.getCanonicalPath());
    }
}
