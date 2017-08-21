package com.cmy.parser.test;

/**
 * Created by cmy on 2017/7/5
 */
public class Test {

    public static void main(String[] args) {
        // FF DC FF DC
        //fffdc
        //fffff
        print(0x8888);
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
