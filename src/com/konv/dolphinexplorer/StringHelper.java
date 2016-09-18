package com.konv.dolphinexplorer;

import java.util.*;

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

    private static class WordsEntry implements Comparable<WordsEntry> {
        private final String mWord;
        private final int mCount;

        public WordsEntry(String word, int count) {
            mWord = word;
            mCount = count;
        }

        public String getWord() {
            return mWord;
        }

        public int getCount() {
            return mCount;
        }

        @Override
        public int compareTo(WordsEntry other) {
            if (mCount > other.getCount()) return 1;
            if (mCount < other.getCount()) return -1;
            return mWord.compareTo(other.getWord());
        }
    }
}
