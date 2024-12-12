package my.example.project;

import java.util.*;

import net.jqwik.api.*;
import net.jqwik.api.stateful.*;

import static org.assertj.core.api.Assertions.*;

class CircularBufferActions {

    static class Model {
        int capacity;
        CircularBuffer<Object> buffer;
        List<Object> contents = new ArrayList<>();

        void initialize(int capacity) {
            this.capacity = capacity;
            this.buffer = new CircularBuffer<>(this.capacity);
            this.contents.clear();
        }

        @Override
        public String toString() {
            return buffer.toString();
        }
    }

    static class NewAction implements Action<Model> {

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

    static class PutAction implements Action<Model> {

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
            model.contents.add(element);
            model.buffer.put(element);
            return model;
        }

        @Override
        public String toString() {
            return String.format("put(%s)", element);
        }

    }

    static class GetAction implements Action<Model> {

        @Override
        public boolean precondition(Model model) {
            return model.buffer != null && !model.contents.isEmpty();
        }

        @Override
        public Model run(Model model) {
            Object element = model.buffer.get();
            Object head = model.contents.remove(0);
            assertThat(element).isEqualTo(head);
            return model;
        }

        @Override
        public String toString() {
            return "get()";
        }
    }

    static class SizeAction implements Action<Model> {

        @Override
        public boolean precondition(Model model) {
            return model.buffer != null;
        }

        @Override
        public Model run(Model model) {
            int size = model.buffer.size();
            int expectedSize = model.contents.size();
            assertThat(size).isLessThanOrEqualTo(model.capacity);
            assertThat(size)
                    .as("size should be %s but was %s", expectedSize, size)
                    .isEqualTo(expectedSize);

            return model;
        }

        @Override
        public String toString() {
            return "size()";
        }
    }
}

