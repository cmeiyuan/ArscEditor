import com.cmy.sort.ISort;
import com.cmy.sort.InsertSort;

public class Main {

    public static void main(String[] args) {
        int[] array = new int[]{5, 23, 11, 89, 3, 45, 189, 35, 8, 56};
        sort(array, new InsertSort());
    }

    public static void sort(int[] array, ISort sort) {
        System.out.print(sort.getName() + "=>");
        sort.doSort(array);
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i < array.length - 1) {
                System.out.print(" ");
            }
        }
    }

}
