package my.example.project;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class MyRLE {
    public static List<AbstractMap.SimpleEntry<Character, Integer>> rle(String inputStr) {
        List<AbstractMap.SimpleEntry<Character, Integer>> result = new ArrayList<>();
        for (char ch : inputStr.toCharArray()) {
            result.add(new AbstractMap.SimpleEntry<>(ch, 1));
        }
        return result;
    }

}
