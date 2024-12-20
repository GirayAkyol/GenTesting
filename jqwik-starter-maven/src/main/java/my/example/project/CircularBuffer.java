package my.example.project;

public class CircularBuffer<T> {
    private final T[] buf;
    private int in;
    private int out;

    public CircularBuffer(int capacity) {
        this.buf = (T[]) new Object[capacity];
    }

    public synchronized void put(T x) {
        buf[in] = x;
        this.in = (this.in + 1) % this.buf.length;
    }

    public synchronized T get() {
        T x = buf[out];
        this.out = (this.out + 1) % this.buf.length;
        return x;
    }

    public synchronized int size() {
        int diff = this.in - this.out + this.buf.length;
        return diff == 0 ? 0 : diff % this.buf.length;
    }

    @Override
    public String toString() {
        return String.format("CircularBuffer(%s)", buf);
    }

    public static void main(String[] args) {
        CircularBuffer<Integer> buffer = new CircularBuffer<>(3);
        buffer.put(1);
        buffer.put(2);
        buffer.put(3);
        System.out.println(buffer.get());
        System.out.println(buffer.get());
        buffer.put(4);
        System.out.println(buffer.get());
        System.out.println(buffer.get());
    }
}


