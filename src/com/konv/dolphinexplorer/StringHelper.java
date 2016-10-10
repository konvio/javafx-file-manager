package com.konv.dolphinexplorer;

public class StringHelper {

    public static boolean containsWord(String text, String word) {
        String[] words = getWords(text);
        word = word.toLowerCase();
        for (String item : words) {
            if (item.equals(word)) return true;
        }
        return false;
    }

    public static String[] getWords(String text) {
        return text.toLowerCase().split("\\W+");
    }
}
