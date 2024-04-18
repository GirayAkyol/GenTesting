package my.example.project;

import java.util.Collections;
import java.util.List;

public class MyMaxProduct {

    public int maxProduct(List<Integer> list) {
        //filter out zeros
        list.removeIf(i -> i == 0);
        list.sort(Collections.reverseOrder());

        return list.get(0) * list.get(1);
    }


}
