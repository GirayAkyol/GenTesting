package my.example.project;


public class MyHeapQueue {
    private int[] heapArray;
    private int currentHeapSize;

    public MyHeapQueue(int capacity) {
        heapArray = new int[capacity];
        currentHeapSize = 0;
    }

    private void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    private int parent(int key) {
        return (key + 1) / 2;
    }

    private int left(int key) {
        return 2 * key + 1;
    }

    private int right(int key) {
        return 2 * key + 2;
    }

    public boolean insert(int key) {
        if (key <= 0) {
            return false; // Invalid key
        }
        if (currentHeapSize == heapArray.length) {
            return false; // Heap is full
        }

        int i = currentHeapSize;
        heapArray[i] = key;
        currentHeapSize++;

        while (i != 0 && heapArray[i] > heapArray[parent(i)]) {
            swap(heapArray, i, parent(i));
            i = parent(i);
        }
        return true;
    }

    public int getMax() {
        if (currentHeapSize <= 0) {
            return -1; // Heap is empty
        }

        if (currentHeapSize == 1) {
            currentHeapSize--;
            return heapArray[0];
        }

        int root = heapArray[0];
        heapArray[0] = heapArray[currentHeapSize - 1];
        currentHeapSize--;
        maxHeapify(0);

        return root;
    }

    public int peek() {
        if (currentHeapSize <= 0) {
            return -1; // Heap is empty
        }
        return heapArray[0];
    }

    public boolean isEmpty() {
        return currentHeapSize == 0;
    }

    public boolean isFull() {
        return currentHeapSize == heapArray.length;
    }

    private void maxHeapify(int key) {
        int l = left(key);
        int r = right(key);
        int largest = key;

        if (l < currentHeapSize && heapArray[l] > heapArray[largest]) {
            largest = l;
        }
        if (r < currentHeapSize && heapArray[r] > heapArray[largest]) {
            largest = r;
        }

        if (largest != key) {
            swap(heapArray, key, largest);
            maxHeapify(largest);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < currentHeapSize; i++) {
            sb.append(heapArray[i]).append(" ");
        }
        return sb.toString();
    }


}
