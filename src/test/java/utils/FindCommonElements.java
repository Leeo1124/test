package utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定三个有序数组，找出三个有序数组共有的元素
 *
 */
public class FindCommonElements {
 
    public static List<Integer> commonElements(int[] a, int[] b, int[] c) {
        int len1 = a.length;
        int len2 = b.length;
        int len3 = c.length;
 
        List<Integer> result = new ArrayList<Integer>();
        int i = 0, j = 0, k = 0;
        while (i < len1 && j < len2 && k < len3) {
            if (a[i] == b[j] && b[j] == c[k]) {
                result.add(a[i]);
                i++;
                j++;
                k++;
            } else {
                int max = max(a[i], b[j], c[k]);
                while (i < len1 && a[i] < max)
                    i++;
                while (j < b.length && b[j] < max)
                    j++;
                while (k < c.length && c[k] < max)
                    k++;
            }
        }
        return result;
    }
 
    private static int max(int a, int b, int c) {
        return max(max(a, b), c);
    }
 
    private static int max(int a, int b) {
        return a >= b ? a : b;
    }
    
    public static void main(String[] args) {
        int[] a = { 2, 5, 12, 20, 25, 36, 57 };
        int[] b = { 2, 6, 7, 12, 20, 57, 80 };
        int[] c = { 2, 3, 4, 12, 15, 20, 35, 60, 80 };
     
        List<Integer> result = FindCommonElements.commonElements(a, b, c);
        //[2, 12, 20]
        System.out.println(result);
    }
}