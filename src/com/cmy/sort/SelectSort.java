package com.cmy.sort;

/**
 * Created by cmy on 2017/4/30
 */
public class SelectSort implements ISort {
    @Override
    public String getName() {
        return "选择排序";
    }

    @Override
    public void doSort(int[] array) {
        int index;
        for (int i = 0; i < array.length - 1; i++) {
            index = i;
            for (int j = i + 1; j < array.length; j++) {
                if(array[j] < array[index]){
                    index = j;
                }
            }
            int temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }
    }
}
