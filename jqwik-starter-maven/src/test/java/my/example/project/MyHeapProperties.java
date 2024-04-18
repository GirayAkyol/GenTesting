package my.example.project;

import net.jqwik.api.Provide;
import net.jqwik.api.*;
import net.jqwik.api.arbitraries.*;
import net.jqwik.api.constraints.UseType;
import net.jqwik.api.lifecycle.*;
import org.assertj.core.api.Assertions;

public class MyHeapProperties {

    @Property
    @Report(Reporting.GENERATED)
    void test(@ForAll @UseType MyHeap a, @ForAll @UseType MyHeap b) {
        Assertions.assertThat(isMinHeap(a)).isTrue();
        Assertions.assertThat(isMinHeap(b)).isTrue();
        MyHeap merged = mergeHeaps(a, b);
        Assertions.assertThat(isMinHeap(merged)).isTrue();

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

//    private MyHeap mergeHeaps2(MyHeap h1, MyHeap h2) {
//        if (h1 == null) {
//            return h2;
//        }
//        if (h2 == null) {
//            return h1;
//        }
//        if (h1.head <= h2.head) {
//            return new MyHeap(h1.head, mergeHeaps(h1.left, h2), h1.right);
//        } else {
//            return new MyHeap(h2.head, mergeHeaps(h1, h2.right), h2.left);
//        }
//    }


}
