package services;

import algorithms.EditDistance;
import algorithms.Hashing;
import algorithms.LCS;

public class DocumentComparator {

    public static String compare(
            String oldText,
            String newText) {

        StringBuilder result =
                new StringBuilder();

        boolean same =
                Hashing.areEqual(oldText, newText);

        int editDistance =
                EditDistance.calculate(
                        oldText,
                        newText
                );

        int lcsLength =
                LCS.length(
                        oldText,
                        newText
                );

        result.append("CONTRACT VERSION COMPARISON\n");
        result.append("============================\n\n");

        if (same) {

            result.append(
                    "STATUS: No changes detected.\n"
            );

        } else {

            result.append(
                    "STATUS: Changes detected.\n"
            );
        }

        result.append("\n");

        result.append("Old Version Length: ")
                .append(oldText.length())
                .append("\n");

        result.append("New Version Length: ")
                .append(newText.length())
                .append("\n");

        result.append("LCS Length: ")
                .append(lcsLength)
                .append("\n");

        result.append("Edit Distance: ")
                .append(editDistance)
                .append("\n");

        result.append("\nCOMMON CONTENT:\n");

        String common =
                LCS.getLCS(oldText, newText);

        result.append(common)
                .append("\n");

        return result.toString();
    }
}