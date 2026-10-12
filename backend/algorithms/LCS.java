package algorithms;

public class LCS {

    public static int[][] buildTable(String a, String b) {

        int m = a.length();
        int n = b.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {

            for (int j = 1; j <= n; j++) {

                if (a.charAt(i - 1) == b.charAt(j - 1)) {

                    dp[i][j] =
                            dp[i - 1][j - 1] + 1;

                } else {

                    dp[i][j] =
                            Math.max(
                                    dp[i - 1][j],
                                    dp[i][j - 1]
                            );
                }
            }
        }

        return dp;
    }

    public static int length(String a, String b) {

        return buildTable(a, b)
                [a.length()]
                [b.length()];
    }

    public static String getLCS(String a, String b) {

        int[][] dp = buildTable(a, b);

        int i = a.length();
        int j = b.length();

        StringBuilder result =
                new StringBuilder();

        while (i > 0 && j > 0) {

            if (a.charAt(i - 1) ==
                    b.charAt(j - 1)) {

                result.append(a.charAt(i - 1));

                i--;
                j--;

            } else if (dp[i - 1][j] >= dp[i][j - 1]) {

                i--;

            } else {

                j--;
            }
        }

        return result.reverse().toString();
    }
}