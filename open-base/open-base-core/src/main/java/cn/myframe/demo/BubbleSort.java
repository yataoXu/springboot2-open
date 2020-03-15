package cn.myframe.demo;

/**
 * @Author: ynz
 * @Date: 2019/2/25/025 17:30
 * @Version 1.0
 */
public class BubbleSort {

    public static void main(String[] args) {
        Integer[] array = {1,3,6,2,44,66,22,66,12,56,134,65,134,134,134,542,34,13,6,41,134,113,6,6524,233,44};
       // bubbleSort(array);
       // selectionSort(array);
        insertionSort(array);
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);

        }

    }

    public static void insertionSort(Integer[] sort){
        for (int i = 1,length = sort.length; i < length; i++) {
            for (int j = i; j > 0; j--) {
                if(sort[j]>sort[j-1]){
                    int temp = sort[j];
                    sort[j] = sort[j-1];
                    sort[j-1] = temp;
                }else{
                    break;
                }

            }
        }

    }


    public static void selectionSort(Integer[] sort){
        for (int i = 0,length = sort.length-1; i < length; i++) {
            int index = i;
            for (int j = i,len= sort.length; j < len; j++) {
                if(sort[j]<sort[index]){
                    index = j;
                }
            }

            if(index != i){
                int temp = sort[i];
                sort[i] = sort[index];
                sort[index] = temp;
            }

        }
    }

    public static void bubbleSort(Integer[] sort){

        for (int i = 0,length = sort.length-1; i < length; i++) {

            for (int j = 0,len= sort.length-i-1; j < len; j++) {
                if(sort[j]>sort[j+1]){
                    int temp = sort[j];
                    sort[j] = sort[j+1];
                    sort[j+1] = temp;
                }
            }

        }

    }
}
