package my.example.project;

import net.jqwik.api.*;
import net.jqwik.api.state.Action;
import net.jqwik.api.state.ActionChain;
import net.jqwik.api.state.ActionChainArbitrary;
import net.jqwik.api.state.Transformer;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class MyQueueMbtHW {

    final static int SIZE = 10;

    static class mbt {
        MyHeapQueue myHeapQueue = new MyHeapQueue(SIZE);
        ArrayList<Integer> list = new ArrayList<>(0); // initial size() has been set to 0.

        @Override
        public String toString() {
            return "mbt{" +
                    "myHeapQueue=" + myHeapQueue +
                    ", list=" + list +
                    '}';
        }
    }

    // generate only positive priorities.
    private static Arbitrary<Integer> elements() {
        return Arbitraries.integers().between(1, 100);
    }

    /**
     * Run this to find an action chain to trigger the bug. 10 points.
     * Technically, you can do not have to make any changes to this method to get full points. If you would like, you can add an invariant to find the bug earlier.
     * Again adding a invariant is not required it is an alternative way to find the bug.
     * <p>
     * You are supposed to use the Arraylist as a priority queue model. Where actions are added with a priority,
     * and the deque action will return the highest priory, or -1 in the queue is empty.
     *
     * @param storeChain
     */
    @Property(shrinking = ShrinkingMode.BOUNDED)
    void heaptests(@ForAll("heapActions") ActionChain<MyHeapQMbt.mbt> storeChain) {
        storeChain.run();
    }

    @Provide
    ActionChainArbitrary<MyHeapQMbt.mbt> heapActions() {
        return ActionChain.<MyHeapQMbt.mbt>startWith(MyHeapQMbt.mbt::new)
                .withAction(1, new Queue())
                .withAction(1, new Deque())
                .withMaxTransformations(10);
    }

    /**
     * Complete the queue action. 10 points.
     * Do not use the Assume, as that it throws out much of the generated cases. 10 points.
     * Write assertions to check the state of the system. 10 points.
     */
    static class Queue implements Action.Independent<MyHeapQMbt.mbt> {


        @Override
        public Arbitrary<Transformer<MyHeapQMbt.mbt>> transformer() {
            return elements().map(key -> Transformer.mutate(
                    String.format("queue %s", key),
                    state -> {
                        Assume.that(state.list.size() < SIZE);
                        MyHeapQueue myHeapQueue = state.myHeapQueue;
                        ArrayList<Integer> list = state.list;
                    }
            ));
        }
    }

    /**
     * Complete the deque action. 10 points.
     * Do not use the Assume, as that it throws out much of the generated cases. 10 points.
     * Write assertions to check the state of the system. 10 points.
     */
    static class Deque implements Action.Independent<MyHeapQMbt.mbt> {

        @Override
        public Arbitrary<Transformer<MyHeapQMbt.mbt>> transformer() {
            return Arbitraries.just(Transformer.mutate(
                    "deque",
                    state -> {
                        Assume.that(!state.list.isEmpty());
                        MyHeapQueue myHeapQueue = state.myHeapQueue;
                        ArrayList<Integer> list = state.list;
                    }
            ));
        }
    }
}
