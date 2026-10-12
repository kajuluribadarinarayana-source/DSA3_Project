package services;

import algorithms.KMP;

public class ComplianceAnalyzer {

    private static final String[] KEYWORDS = {
            "deadline",
            "penalty",
            "renewal",
            "termination",
            "obligation",
            "confidentiality",
            "compliance"
    };

    public static String analyze(String text) {

        StringBuilder result =
                new StringBuilder();

        result.append("COMPLIANCE ANALYSIS\n");
        result.append("===================\n\n");

        for (String keyword : KEYWORDS) {

            int position =
                    KMP.search(text, keyword);

            if (position >= 0) {

                result.append(keyword.toUpperCase())
                        .append(" : FOUND\n");

            } else {

                result.append(keyword.toUpperCase())
                        .append(" : NOT FOUND\n");
            }
        }

        return result.toString();
    }
}