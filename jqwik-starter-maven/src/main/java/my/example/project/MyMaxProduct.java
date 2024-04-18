package my.example.project;

import java.util.Collections;
import java.util.List;

public class MyMaxProduct {

    public int maxProduct(List<Integer> list) {
        //filter out zeros
        list.removeIf(i -> i == 0);
        list.sort(Collections.reverseOrder());

        int pos = list.get(0) * list.get(1);
        int neg = list.get(list.size() - 1) * list.get(list.size() - 2);
        return Math.max(pos, neg);
    }


}
