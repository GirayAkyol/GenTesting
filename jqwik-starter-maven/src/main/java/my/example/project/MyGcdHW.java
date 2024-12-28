package my.example.project;

import net.jqwik.api.Example;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import org.assertj.core.api.Assertions;

public class MyGcdHW {
    // You are not allowed to make changes to the implementation.
    public static int gcd(int x, int y) {
        // only non-negative integers are allowed
        assert x >= 0 && y >= 0;
        if (y == 0) {
            return x;
        }
        return gcd(y, x % y);
    }

    @Example
    public void twelveAndFifteen() {
        int result = gcd(12, 15);
        assert result == 3;
    }

    @Example
    public void sevenandseven() {
        int result = gcd(7, 7);
        assert result == 7;
    }

    @Property
    /**
     * Example property test.
     *
     */
    public void symmetry(@ForAll @IntRange(min = 0) int x, @ForAll @IntRange(min = 0) int y) {
        int result1 = gcd(x, y);
        int result2 = gcd(y, x);
        Assertions.assertThat(result1).isEqualTo(result2);

    }

    @Property
    /**
     * Example property test.
     */
    public void identity(@ForAll @IntRange(min = 0) int x) {
        int result = gcd(x, x);
        Assertions.assertThat(result).isEqualTo(x);

    }

    @Property
    /**
     * Write a property test for the above gcd implementation.
     * The GCD implementation is correct so the test needs to pass.
     * It has to be different from the symmetry and identity tests.
     * You are allowed to change the signature and name of the property test method.
     * 20 points
     */
    public void yourtest1() {


    }

    @Property
    /**
     * Write a property test for the above gcd implementation.
     * The GCD implementation is correct so the test needs to pass.
     * It has to be different from the symmetry and identity tests.
     * You are allowed to change the signature and name of the property test method.
     * 20 points
     */
    public void yourtest2() {

    }
}
