package my.example.project;

class MyHeap {

    final int head;
    final MyHeap left;
    final MyHeap right;

    public MyHeap(int head, MyHeap left, MyHeap right) {
        this.head = head;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        String leftString = left == null ? "None" : left.toString();
        String rightString = right == null ? "None" : right.toString();
        return "{ " + head + ", " + leftString + ", " + rightString + " }";
    }
}
