package com.cmy.sort;

/**
 * Created by cmy on 2017/5/2
 */
public class InsertSort implements ISort {
    @Override
    public String getName() {
        return "插入排序";
    }

    @Override
    public void doSort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            //记录插入值
            int insertValue = array[i];
            for (int j = 0; j < i; j++) {
                if (insertValue < array[j]) {
                    //元素后移
                    for (int k = i; k > j; k--) {
                        array[k] = array[k-1];
                    }
                    //插入到j位置
                    array[j] = insertValue;
                    break;
                }
            }
        }
    }
}
