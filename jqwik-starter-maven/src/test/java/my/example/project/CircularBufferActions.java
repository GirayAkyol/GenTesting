package my.example.project;

import java.util.*;

import net.jqwik.api.*;
import net.jqwik.api.stateful.*;

import static org.assertj.core.api.Assertions.*;

class CircularBufferActions {

    static class Model {
        int capacity;
        CircularBuffer<Object> buffer;

        void initialize(int capacity) {
            this.capacity = capacity;
            this.buffer = new CircularBuffer<>(this.capacity);
        }

        @Override
        public String toString() {
            return buffer.toString();
        }
    }

    static Arbitrary<Action<Model>> actions() {
        return Arbitraries.oneOf(create(), put(), get(), size());
    }

    static Arbitrary<Action<Model>> create() {
        return Arbitraries.integers().between(1, 100).map(NewAction::new);
    }

    static Arbitrary<Action<Model>> put() {
        return Arbitraries.integers().map(Object::toString).map(PutAction::new);
    }

    private static Arbitrary<Action<Model>> get() {
        return Arbitraries.just(new GetAction());
    }

    private static Arbitrary<Action<Model>> size() {
        return Arbitraries.just(new SizeAction());
    }

    private static class NewAction implements Action<Model> {

        private final int capacity;

        public NewAction(int capacity) {
            this.capacity = capacity;
        }

        @Override
        public Model run(Model model) {
            model.initialize(capacity);
            return model;
        }

        @Override
        public String toString() {
            return String.format("new(%s)", capacity);
        }

    }

    private static class PutAction implements Action<Model> {

        private final Object element;

        public PutAction(Object element) {
            this.element = element;
        }

        @Override
        public boolean precondition(Model model) {
            return model.buffer != null;
        }

        @Override
        public Model run(Model model) {
            model.buffer.put(element);
            return model;
        }

        @Override
        public String toString() {
            return String.format("put(%s)", element);
        }

    }

    private static class GetAction implements Action<Model> {

        @Override
        public boolean precondition(Model model) {
            return model.buffer != null;
        }

        @Override
        public Model run(Model model) {
            Object element = model.buffer.get();
            return model;
        }

        @Override
        public String toString() {
            return "get()";
        }
    }

    private static class SizeAction implements Action<Model> {

        @Override
        public boolean precondition(Model model) {
            return model.buffer != null;
        }

        @Override
        public Model run(Model model) {
            int size = model.buffer.size();
            assertThat(size).isLessThanOrEqualTo(model.capacity);
            return model;
        }

        @Override
        public String toString() {
            return "size()";
        }
    }
}

