package my.example.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MySort {

    public List<Integer> sort(List<Integer> list) {
        List<Integer> positiveNumbers = new ArrayList<>();
        List<Integer> negativeNumbers = new ArrayList<>();

        for (Integer i : list) {
            if (i >= 0) {
                positiveNumbers.add(i);
            } else {
                negativeNumbers.add(Math.abs(i));
            }
        }

        Collections.sort(positiveNumbers);
        Collections.sort(negativeNumbers);
        negativeNumbers.replaceAll(Math::negateExact);

        List<Integer> sortedList = new ArrayList<>();
        sortedList.addAll(negativeNumbers);
        sortedList.addAll(positiveNumbers);

        return sortedList;
    }
}
