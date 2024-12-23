package my.example.project;

import net.jqwik.api.Tuple;

import java.util.*;
import java.util.stream.Collectors;


public class MyStoreHash {


    private final Tuple.Tuple2<Integer, String>[] table = new Tuple.Tuple2[127];

    public int hash(int key) {
        int lfsr = key;
        int polynomial = 0xB400;

        for (int i = 0; i < 16; i++) {
            if ((lfsr & 1) != 0) {
                lfsr = (lfsr >> 1) ^ polynomial;
            } else {
                lfsr >>= 1;
            }
        }

        return lfsr % 127;
    }

    public String get(int key) {
        Tuple.Tuple2<Integer, String> tuple = table[hash(key)];
        if (tuple == null) {
            return null;
        }
        return tuple.get2();
    }

    public void store(int key, String value) {
        if (get(key) != null) {
            return;
        }
        table[hash(key)] = Tuple.of(key, value);
    }

    public void remove(int key) {
        table[hash(key)] = null;
    }

    public boolean isEmpty() {
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                return false;
            }
        }
        return true;
    }

    public Set<Integer> keys() {
        Set<Integer> keys = new LinkedHashSet<>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                keys.add(table[i].get1());
            }
        }
        return keys;
    }

    @Override
    public String toString() {
        return "MyStoreHash " + keys().stream()
                .map(k -> String.format("%s=%s", k, get(k)))
                .collect(Collectors.joining(", ", "[", "]"));
    }

    public static void main(String[] args) {
        MyStoreHash store = new MyStoreHash();
        store.store(44, "one");
        store.store(89, "two");
        System.out.println(
                store.hash(44)
        );
        System.out.println(
                store.hash(89)
        );
        System.out.println(
                store.toString()
        );
    }

}