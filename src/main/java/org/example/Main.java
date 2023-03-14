package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println(isMatch("ab", ".*c"));
    }
    public static boolean isMatch(String s,String p ) {
        char[] tChars = s.toCharArray();
        char[] pChars = p.toCharArray();
        int tIndex  = 0;
        int pIndex  = 0;
        for (int i = pIndex; i < pChars.length;i ++ ) {
            char first = pChars[i];
            if (i < pChars.length - 1 && pChars[i + 1] == '*' ) {
                i++;
            } else {
                // 单个元素判断.
                if (first == tChars[tIndex] || first == '.') {
                    tIndex ++;
                    continue;
                } else return false;
            }// 只有最后一个 *
            if (first == '.') {
                if (pIndex  < pChars.length -2) {
                    char third = pChars[pIndex + 2];
                    a : for (tIndex= tIndex; tIndex < tChars.length; tIndex++) {
                        if (tChars[tIndex] != third) {continue;}

                        else{
                            break a;}
                    }
                }
                tIndex--;
            }

            a : for (tIndex= tIndex; tIndex < tChars.length; tIndex++) {
                if (tChars[tIndex] == first) {continue;}
                else break a;
            }
        }
        return tChars.length  == tIndex;
    }
}