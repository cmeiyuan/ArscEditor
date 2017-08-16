package com.cmy.sort;

/**
 * Created by cmy on 2017/4/30
 */
public class BubbleSort implements ISort {

    @Override
    public String getName() {
        return "冒泡排序";
    }

    @Override
    public void doSort(int[] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = i + 1; j < array.length; j++) {
                if (array[i] > array[j]) {
                    int temp = array[j];
                    array[j] = array[i];
                    array[i] = temp;
                }
            }
        }
    }

}
