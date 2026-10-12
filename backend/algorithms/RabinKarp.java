package algorithms;

public class RabinKarp {

    private static final int BASE = 256;
    private static final int PRIME = 101;

    public static int search(String text, String pattern) {

        if (text == null || pattern == null) {
            return -1;
        }

        if (pattern.length() == 0) {
            return 0;
        }

        if (pattern.length() > text.length()) {
            return -1;
        }

        text = text.toLowerCase();
        pattern = pattern.toLowerCase();

        int m = pattern.length();
        int n = text.length();

        int patternHash = 0;
        int textHash = 0;
        int highestPower = 1;

        for (int i = 0; i < m - 1; i++) {
            highestPower = (highestPower * BASE) % PRIME;
        }

        for (int i = 0; i < m; i++) {
            patternHash =
                    (BASE * patternHash + pattern.charAt(i)) % PRIME;

            textHash =
                    (BASE * textHash + text.charAt(i)) % PRIME;
        }

        for (int i = 0; i <= n - m; i++) {

            if (patternHash == textHash) {

                boolean match = true;

                for (int j = 0; j < m; j++) {

                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }

                if (match) {
                    return i;
                }
            }

            if (i < n - m) {

                textHash =
                        (BASE * (textHash
                                - text.charAt(i) * highestPower)
                                + text.charAt(i + m))
                                % PRIME;

                if (textHash < 0) {
                    textHash += PRIME;
                }
            }
        }

        return -1;
    }
}