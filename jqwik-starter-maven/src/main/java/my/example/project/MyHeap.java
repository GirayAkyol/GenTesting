package my.example.project;

class MyHeap {

    final int head;
    final MyHeap left;
    final MyHeap right;

    public MyHeap(int head) {
        this.head = head;
        this.left = null;
        this.right = null;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyHeap myHeap = (MyHeap) o;
        if (head != myHeap.head) return false;
        boolean le = left != null ? left.equals(myHeap.left) : myHeap.left != null;
        boolean re = right != null ? right.equals(myHeap.right) : myHeap.right != null;
        return le && re;
    }
}
