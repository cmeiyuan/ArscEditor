package com.cmy.parser.test;

import com.cmy.parser.bean.tabletype.ResTableEntry;

/**
 * Created by cmy on 2017/7/5
 */
public class Test {

    public static void main(String[] args) {
        // FF DC FF DC
        //fffdc
        //fffff
        //print(0x8888);
        System.out.println(2 & ResTableEntry.FLAG_COMPLEX);
    }

    public static void print(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append("a");
        }
        System.out.println(sb.toString());
    }

    public static void printLen(String s) {
        System.out.println(s.length());
    }

}
