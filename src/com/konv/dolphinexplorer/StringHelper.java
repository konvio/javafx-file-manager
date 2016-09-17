package com.konv.dolphinexplorer;

public class StringHelper {

    public static boolean containsWord(String text, String word) {
        String[] words = text.toLowerCase().split("\\W+");
        word = word.toLowerCase();
        for (String item : words) {
            if (item.equals(word)) return true;
        }
        return false;
    }
}
