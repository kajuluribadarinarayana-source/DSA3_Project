package services;

import algorithms.KMP;
import algorithms.RabinKarp;

public class ClauseSearcher {

    public static String search(String text, String keyword) {

        int kmpPosition =
                KMP.search(text, keyword);

        int rabinPosition =
                RabinKarp.search(text, keyword);

        if (kmpPosition == -1) {

            return "Keyword not found.\n"
                    + "KMP Position: -1\n"
                    + "Rabin-Karp Position: "
                    + rabinPosition;
        }

        return "Keyword found.\n"
                + "KMP Position: "
                + kmpPosition
                + "\nRabin-Karp Position: "
                + rabinPosition;
    }
}