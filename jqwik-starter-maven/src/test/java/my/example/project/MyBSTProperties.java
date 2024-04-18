package my.example.project;

import net.jqwik.api.*;
import net.jqwik.api.arbitraries.IntegerArbitrary;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.Size;
import net.jqwik.api.constraints.UseType;
import net.jqwik.api.statistics.Histogram;
import net.jqwik.api.statistics.NumberRangeHistogram;
import net.jqwik.api.statistics.Statistics;
import net.jqwik.api.statistics.StatisticsReport;
import org.assertj.core.api.Assertions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class MyBSTProperties {


    //write a check bintree method
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
        if (maxh == 0) {
            return Arbitraries.just(null);
        }
        return Arbitraries.lazy(
                () -> Arbitraries.frequencyOf(
                        Tuple.of(3, Arbitraries.just(null)),
                        Tuple.of(2, bstChildren(min, max, maxh))

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

    Arbitrary<Integer> keySelect(MyBST myBST) {
        Arbitrary<Integer> decision = Arbitraries.frequencyOf(
                Tuple.of(3, Arbitraries.just(1)),
                Tuple.of(1, Arbitraries.just(0)),
                Tuple.of(3, Arbitraries.just(-1)));
        if (myBST == null) {
            return Arbitraries.just(-1);
        }
        return decision.flatMap(dec -> {
            if (myBST.left != null && dec == -1) {
                return keySelect(myBST.left);
            } else if (myBST.right != null && dec == 1) {
                return keySelect(myBST.right);
            } else {
                return Arbitraries.just(myBST.key);
            }
        });
    }

    @Provide
    Arbitrary<Tuple.Tuple3<MyBST, Integer, Integer>> nodeGen() {
        Arbitrary<MyBST> myBST = MyBSTs();
        return myBST.flatMap(tree -> {
            Integer node1 = keySelect(tree).sample();
            Integer node2 = keySelect(tree).sample();
            return Arbitraries.just(Tuple.of(tree, node1, node2));
        });
    }

    MyBST lca(MyBST myBST, int node1, int node2) {
        if (myBST == null) {
            return null;
        }
        if (myBST.key == node1 || myBST.key == node2) {
            return myBST;
        }
        if (myBST.key > node1 && myBST.key > node2) {
            return lca(myBST.left, node1, node2);
        }
        if (myBST.key < node1 && myBST.key < node2) {
            return lca(myBST.right, node1, node2);
        }
        return myBST;
    }

    int depth(MyBST myBST, int key) {
        if (myBST == null) {
            return -1;
        }
        if (myBST.key == key) {
            return 0;
        }
        if (myBST.key > key) {
            return 1 + depth(myBST.left, key);
        } else {
            return 1 + depth(myBST.right, key);
        }
    }

    ArrayList<Integer> pathFromRoot(MyBST myBST, int node) {
        ArrayList<Integer> path = new ArrayList<>();
        if (myBST == null) {
            return path;
        }
        if (myBST.key == node) {
            path.add(myBST.key);
            return path;
        }
        if (myBST.key > node) {
            path.add(myBST.key);
            path.addAll(pathFromRoot(myBST.left, node));
        } else {
            path.add(myBST.key);
            path.addAll(pathFromRoot(myBST.right, node));
        }
        return path;
    }


    @Property()
    void testLcs(@ForAll("nodeGen") Tuple.Tuple3<MyBST, Integer, Integer> tup3) {
        MyBST myBST = tup3.get1();
        int node1 = tup3.get2();
        int node2 = tup3.get3();
        if (node1 > node2) {
            int temp = node1;
            node1 = node2;
            node2 = temp;
        }
        Assume.that(myBST != null);
        Assume.that(node1 < node2);
        MyBST ancestor = lca(myBST, node1, node2);
        Assertions.assertThat(myBST.search(node1)).isTrue();
        Assertions.assertThat(myBST.search(node2)).isTrue();
        Assertions.assertThat(myBST.search(ancestor.key)).isTrue();
        Assertions.assertThat(ancestor.key).isStrictlyBetween(node1, node2);
        ArrayList<Integer> path1 = pathFromRoot(ancestor, node1);

        //assert path1 is sorted
        List<Integer> sortedPath1 = new ArrayList<>(path1);
        Collections.sort(sortedPath1, Collections.reverseOrder());
        //Assertions.assertThat(path1).isEqualTo(sortedPath1);
    }


    @Property(tries = 10000)
    //@Report(Reporting.GENERATED)
    @StatisticsReport()
    void testInsert(@ForAll("MyBSTs") MyBST myBST, @ForAll @IntRange(min = -100, max = 100) int key) {
        //Assertions.assertThat(checkBST(myBST, -100, 100)).isTrue();
        Assume.that(myBST != null);
        Statistics.collect(height(myBST));
        myBST.insert(key);
        //Assertions.assertThat(myBST.search(key)).isTrue();
    }


}
