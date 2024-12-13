package my.example.project;

import java.util.*;

public class MyStoreAVL<V> {

    class Node {
        int height;
        Node left, right;
        int key;
        V value;

        Node(int key, V value) {
            this.key = key;
            this.value = value;
            height = 1;
        }
    }

    public MyStoreAVL() {
        root = null;
    }

    public Node root;

    private int height(Node N) {
        if (N == null)
            return 0;
        return N.height;
    }

    public boolean isEmpty() {
        return root == null;
    }

    private int max(int a, int b) {
        return Math.max(a, b);
    }

    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;

        return y;
    }

    public int getBalance(Node N) {
        if (N == null)
            return 0;
        return height(N.left) - height(N.right);
    }

    private Node minValueNode(Node node) {
        Node current = node;

        while (current.left != null)
            current = current.left;

        return current;
    }

    public void delete(int key) {
        root = deleteNode(root, key);
    }

    private Node deleteNode(Node root, int key) {
        if (root == null)
            return root;

        if (key < root.key)
            root.left = deleteNode(root.left, key);
        else if (key > root.key)
            root.right = deleteNode(root.right, key);
        else {
            if ((root.left == null) || (root.right == null)) {
                Node temp = null;
                if (temp == root.left)
                    temp = root.right;
                else
                    temp = root.left;

                if (temp == null) {
                    temp = root;
                    root = null;
                } else
                    root = temp;
            } else {
                Node temp = minValueNode(root.right);
                root.key = temp.key;
                root.right = deleteNode(root.right, temp.key);
            }
        }

        if (root == null)
            return root;

        root.height = max(height(root.left), height(root.right)) + 1;

        int balance = getBalance(root);

        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);

        if (balance > 1 && getBalance(root.left) < 0) {
            return rightRotate(root);
        }

        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);

        if (balance < -1 && getBalance(root.right) > 0) {
            return leftRotate(root);
        }

        return root;
    }

    public void store(int key, V value) {
        if (search(root, key) != null && root.value.equals(value)) {
            return;
            //delete(key);
        }
        root = insertNode(root, key, value);
    }

    private Node insertNode(Node node, int key, V value) {
        if (node == null) {
            return new Node(key, value);
        }

        if (key == node.key) {
            node.value = value;
            return node;
        }

        if (key < node.key) {
            node.left = insertNode(node.left, key, value);
        } else {
            node.right = insertNode(node.right, key, value);
        }

        node.height = 1 + max(height(node.left), height(node.right));

        int balance = getBalance(node);

        if (balance > 1 && key < node.left.key) {
            return rightRotate(node);
        }

        if (balance < -1 && key > node.right.key) {
            return leftRotate(node);
        }

        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    public V get(int key) {
        return search(root, key);
    }

    private V search(Node node, int key) {
        if (node == null) {
            return null;
        }
        if (key == node.key) {
            return node.value;
        }
        if (key < node.key) {
            return search(node.left, key);
        }
        return search(node.right, key);
    }

    public Set<Integer> keys() {
        Set<Integer> keys = new HashSet<>();
        keys(root, keys);
        return keys;
    }

    private void keys(Node node, Set<Integer> keys) {
        if (node != null) {
            keys(node.left, keys);
            Set<Integer> temp = new HashSet<>();
            temp.add(java.lang.Integer.valueOf(node.key));
            keys.addAll(temp);
            keys(node.right, keys);
        }
    }

    @Override
    public String toString() {
        return "MyStoreAVL " + keys().stream()
                .map(k -> String.format("%s=%s", k, get(k)))
                .collect(java.util.stream.Collectors.joining(", ", "[", "]"));
    }


    public static void main(String[] args) {
        MyStoreAVL<String> store = new MyStoreAVL<>();
//        store 24=piad
//        store 19=ggqs
//        store 8=vpko
//        update 19=uxqa
//        update 8=wnco
//        remove 19
//        store 50=lsxt
//        store 91=fghd
//        update 24=jgzm

        //turn these into f calls
        store.store(24, "piad");
        store.store(19, "ggqs");
        store.store(8, "vpko");
        store.store(19, "uxqa");
        store.store(8, "wnco");
        store.delete(19);
        store.store(50, "lsxt");
        store.store(91, "fghd");
        store.store(24, "jgzm");

        System.out.println("da");
    }

}