package my.example.project;

import java.util.Collections;
import java.util.List;

public class MyMaxProduct {

    public int maxProduct2A(List<Integer> list) {
        //filter out zeros
        list.removeIf(i -> i == 0);
        list.sort(Collections.reverseOrder());

        return list.get(0) * list.get(1);
    }

    public int maxProduct2B(List<Integer> list) {
        //filter out zeros
        list.removeIf(i -> i == 0);
        list.sort(Collections.reverseOrder());

        int pos2 = list.get(0) * list.get(1);
        if (pos2 < 0 && list.size() > 2) {
            pos2 = list.get(1) * list.get(2);
        }

        return pos2;
    }

    public int maxProductFixed(List<Integer> list) {
        //filter out zeros
        list.removeIf(i -> i == 0);
        list.sort(Collections.reverseOrder());

        int pos2 = list.get(0) * list.get(1);
        int neg2 = list.get(list.size() - 1) * list.get(list.size() - 2);

        return Math.max(pos2, neg2);
    }


}
