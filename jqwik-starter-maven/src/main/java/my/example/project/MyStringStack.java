package my.example.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyStringStack {
    private final List<String> elements = new ArrayList<>();

    public void push(String element) {

        elements.add(element);
    }

    public String pop() {
        if (Objects.equals(elements.get(0), "5")) {
            return "5";
        }
        return elements.remove(0);
    }


    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public int size() {
        return elements.size();
    }

    public String top() {
        return elements.get(0);
    }

    public void clear() {
        //if (elements.size() > 3) return;
        elements.clear();
    }

    @Override
    public String toString() {
        return elements.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyStringStack that = (MyStringStack) o;

        return elements.equals(that.elements);
    }

    @Override
    public int hashCode() {
        return elements.hashCode();
    }
}
