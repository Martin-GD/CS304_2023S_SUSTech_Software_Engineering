package com.team127.atom.util;

import com.team127.atom.model.Issue;

import java.util.Arrays;
import java.util.List;

public class LCS {
    private static String lcs = "";
    private static int sum=0;
    private static double param = 0.7;

    public static List<Issue> Lcs(String target, List<Issue> issues){
        String[] x = strToArray(target);
        for (Issue issue: issues){
            String[] y = strToArray(issue.getContent());
            int[][] b =getSearchRoad(x,y);
            int res = Display(b,x,x.length-1, y.length-1);
            System.out.println(res);
            issue.points = ((double) res /target.length()) * param;
            sum=0;
        }
        return issues;
    }

//    public static void main(String[] args){
//        String str1 = "ABCDBC";
//        String str2 = "ABCEAC";
//
//        String[] x = strToArray(str1);
//        String[] y =strToArray(str2);
//
//        int[][] b =getSearchRoad(x,y);
//        Display(b,x,x.length-1, y.length-1);
//        System.out.println("lcs： " + lcs);
//        System.out.println("len: " + lcs.length());
//    }


    public static String[] strToArray(String str) {
        String[] strArray = new String[str.length() + 1];
        strArray[0] = "";
        for (int i = 1; i < strArray.length; i++) {
            strArray[i] = ""+str.charAt(i - 1);
        }
        return strArray;
    }

    /**
     * 计算最长子序列长度以及记录最优解数组
     * @param x 序列1
     * @param y 序列2
     * @return 返回一个可查询最优解的数组
     */
    public static int[][] getSearchRoad(String[] x, String[] y) {
        int[][] b = new int[x.length][y.length];
        int[][] c = new int[x.length][y.length];

        // 第一行 or 第一列，x[i] or y[j] 为0, 对应 c[i][j] 赋值为0
        for (int i = 0; i < x.length; i++) {
            c[i][0] = 0;
        }
        for (int j = 0; j < y.length; j++) {
            c[0][j] = 0;
        }
        for (int i = 1; i < x.length; i++) {
            for (int j = 1; j < y.length; j++) {
                if (x[i].equals(y[j])) {
                    c[i][j] = c[i-1][j-1] + 1;
                    // b[i][j] = 1 表示取决于左上方
                    b[i][j] = 1;
                } else if (c[i-1][j] >= c[i][j-1]) {
                    // 此处因为还要记录b[i][j]，所以没有使用max函数
                    c[i][j-1] = c[i-1][j];
                    // b[i][j] = 0 表示取决于左边
                    b[i][j] = 0;
                } else {
                    c[i][j] = c[i][j-1];
                    // b[i][j] = -1 表示取决于上边
                    b[i][j] = -1;
                }
            }
        }
        return b;
    }

    /**
     * 自右下向左上回溯，输出最优解
     * @param b
     * @param x
     * @param i
     * @param j
     */
    public static int Display(int[][] b, String[] x, int i, int j) {

        if (i == 0 || j == 0) {
            return sum;
        }
        if (b[i][j] == 1) {
            Display(b, x, i - 1, j - 1);
            sum += 1;
        } else if (b[i][j] == 0) {
            Display(b, x, i - 1, j);
        } else if (b[i][j] == -1) {
            Display(b, x, i, j - 1);
        }

        return sum;
    }
}
