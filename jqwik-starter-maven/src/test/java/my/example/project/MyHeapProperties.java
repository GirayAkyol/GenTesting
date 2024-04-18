package my.example.project;

import net.jqwik.api.Provide;
import net.jqwik.api.*;
import net.jqwik.api.arbitraries.*;
import net.jqwik.api.lifecycle.*;
import org.assertj.core.api.Assertions;

public class MyHeapProperties {

    @Property
    void test(@ForAll("heap") MyHeap a, @ForAll("heap") MyHeap b) {
        Assertions.assertThat(isMinHeap(a)).isTrue();
        Assertions.assertThat(isMinHeap(b)).isTrue();
        MyHeap merged = mergeHeaps(a, b);
        Assertions.assertThat(isMinHeap(merged)).isTrue();
        MyHeap merged2 = mergeHeaps2(a, b);
        //Assertions.assertThat(merged2).isEqualTo(merged);

    }

    public boolean isMinHeap(MyHeap heap) {
        if (heap == null) {
            return true;
        }
        if (heap.left != null && heap.left.head < heap.head) {
            return false;
        }
        if (heap.right != null && heap.right.head < heap.head) {
            return false;
        }
        return isMinHeap(heap.left) && isMinHeap(heap.right);
    }

    private MyHeap mergeHeaps(MyHeap h1, MyHeap h2) {
        if (h1 == null) {
            return h2;
        }
        if (h2 == null) {
            return h1;
        }
        if (h1.head <= h2.head) {

            return new MyHeap(h1.head, mergeHeaps(h1.right, h2), h1.left);
        } else {
            return new MyHeap(h2.head, mergeHeaps(h2.right, h1), h2.left);
        }
    }

    private MyHeap mergeHeaps2(MyHeap h1, MyHeap h2) {
        if (h1 == null) {
            return h2;
        }
        if (h2 == null) {
            return h1;
        }
        if (h1.head <= h2.head) {
            return new MyHeap(h1.head, mergeHeaps(h1.left, h2), h1.right);
        } else {
            return new MyHeap(h2.head, mergeHeaps(h1, h2.right), h2.left);
        }
    }

    @Provide
    Arbitrary<MyHeap> heap() {
        Arbitrary<Integer> sizes = Arbitraries.integers().between(0, 10);
        return sizes.flatMap(size -> heap(0, size));
    }

    private Arbitrary<MyHeap> heap(int minKey, int size) {
        if (size == 0) {
            return Arbitraries.just(null);
        }
        return Arbitraries.lazy(
                () -> Arbitraries.frequencyOf(
                        Tuple.of(1, Arbitraries.just(null)),
                        Tuple.of(1, heapWithChildren(minKey, size))
                )
        );
    }

    private Arbitrary<MyHeap> heapWithChildren(int minKey, int size) {
        Arbitrary<Integer> keys = Arbitraries.integers().greaterOrEqual(minKey);
        return keys.flatMap(
                head -> Combinators.combine(heap(head, size / 2), heap(head, size / 2))
                        .as((left, right) -> new MyHeap(head, left, right))
        );
    }

}
