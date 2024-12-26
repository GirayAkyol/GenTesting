package my.example.project;

import net.jqwik.api.*;
import net.jqwik.api.constraints.NumericChars;
import net.jqwik.api.constraints.StringLength;
import org.assertj.core.api.Assertions;

public class MyMatchHW {
    /**
     * Implement a simple string matching algorithm. Returns the first index of the pattern in the text.
     * If the pattern is not found, return -1.
     * Runs in O(n) time.
     * You are not allowed to make changes.
     *
     * @param pattern
     * @param text
     * @return
     */
    public static int match(String pattern, String text) {
        int n = text.length();
        int m = pattern.length();
        int ti = 0;
        int pi = 0;
        while (ti < n) {
            if (text.charAt(ti) == pattern.charAt(pi)) {
                ti++;
                pi++;
                if (pi == m) {
                    return ti - pi;
                }
            } else {
                ti++;
                pi = 0;
            }
        }
        return -1;
    }

    @Example
    public void exists() {
        String pattern = "123";
        String text = "53075403512335480";
        int count = match(pattern, text);
        Assertions.assertThat(count).isEqualTo(9);
    }

    @Example
    public void notexists() {
        String pattern = "555";
        String text = "53075403512335480";
        int count = match(pattern, text);
        Assertions.assertThat(count).isEqualTo(-1);
    }

    /**
     * Write a test to find a bug in the match implementation.
     * The test should fail.
     * You may notice that I set the tries to 10000. Just using default generators are not enough to _reliably_ trigger the bug.
     * <p>
     * 20 points.
     */
    @Property(tries = 10000)
    public void yourproperty() {
        return;
    }

    /**
     * Implmenet a custom generator to generate a string so that you can trigger the bug in less than 200 tries.
     * 15 points.
     *
     * @return
     */
    @Provide
    Arbitrary<String> text() {
        return Arbitraries.strings();
    }
}
