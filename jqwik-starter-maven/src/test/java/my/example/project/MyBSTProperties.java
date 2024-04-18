package my.example.project;

import net.jqwik.api.*;
import net.jqwik.api.arbitraries.IntegerArbitrary;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.UseType;
import net.jqwik.api.statistics.Statistics;
import net.jqwik.api.statistics.StatisticsReport;
import org.assertj.core.api.Assertions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class MyBSTProperties {

    private boolean checkBST(MyBST myBST, int min, int max) {
        if (myBST == null) {
            return true;
        }
        if (myBST.key < min || myBST.key > max) {
            return false;
        }
        return checkBST(myBST.left, min, myBST.key) && checkBST(myBST.right, myBST.key, max);
    }

    private int height(MyBST myBST) {
        if (myBST == null) {
            return 0;
        }
        return 1 + Math.max(height(myBST.left), height(myBST.right));
    }


    @Provide
    Arbitrary<MyBST> MyBSTs() {
        return bstChoose(-100, 100, 10);
    }

    private Arbitrary<MyBST> bstChoose(int min, int max, int maxh) {
        if (min >= max) {
            return Arbitraries.just(null);
        }
        if (maxh <= 0) {
            return Arbitraries.just(null);
        }
        return Arbitraries.lazy(
                () -> Arbitraries.frequencyOf(
                        Tuple.of(2, Arbitraries.just(null)),
                        Tuple.of(2, bstChildren(min, max, maxh - 1))

                ));
    }


    private Arbitrary<MyBST> bstChildren(int min, int max, int maxh) {
        Arbitrary<Integer> keys = Arbitraries.integers().between(min, max);
        return keys.flatMap(
                head -> Combinators.combine(
                                bstChoose(min, head - 1, maxh - 1),
                                bstChoose(head + 1, max, maxh - 1))
                        .as((left, right) -> new MyBST(head, left, right))
        );
    }

    @Property(tries = 10000)
    @StatisticsReport()
    void testInsert(@ForAll("MyBSTs") MyBST myBST, @ForAll @IntRange(min = -100, max = 100) int key) {
        //Assertions.assertThat(checkBST(myBST, -100, 100)).isTrue();
        Assume.that(myBST != null);
        Statistics.collect(height(myBST));
        myBST.insert(key);
        //Assertions.assertThat(myBST.search(key)).isTrue();
    }


}
