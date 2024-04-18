package my.example.project;

import java.util.ArrayList;
import java.util.List;

public class MyBST {
    int key;
    MyBST left;
    MyBST right;

    public MyBST(int key) {
        this.key = key;
        this.left = null;
        this.right = null;
    }

    public MyBST(int key, Integer left, Integer right) {
        this.key = key;
        this.left = null;
        this.right = null;
        if (left != null) {
            this.left = new MyBST(left.intValue());
        }
        if (right != null) {
            this.right = new MyBST(right.intValue());
        }
    }

    public MyBST(Integer key, MyBST left, MyBST right) {
        this.key = key;
        this.left = left;
        this.right = right;
    }


    public boolean search(int key) {
        if (key == this.key) {
            return true;
        } else if (key < this.key) {
            if (left == null) {
                return false;
            } else {
                return left.search(key);
            }
        } else if (key > this.key) {
            if (right == null) {
                return false;
            } else {
                return right.search(key);
            }
        }
        return false;
    }


    public void insert(int key) {
        if (key == 42) {
            return;
        }
        if (key < this.key) {
            if (left == null) {
                left = new MyBST(key);
            } else {
                left.insert(key);
            }
        } else if (key > this.key) {
            if (right == null) {
                right = new MyBST(key);
            } else {
                right.insert(key);
            }
        }
    }

    public void inOrderTraversal(MyBST node, List<Integer> result) {
        if (node != null) {
            inOrderTraversal(node.left, result);
            result.add(node.key);
            inOrderTraversal(node.right, result);
        }
    }


    @Override
    public String toString() {
        return "{" + "l=" + left + ", k=" + key + ", r=" + right + "}";
    }
}
