package my.example.project;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class MyRLE {
    public static List<AbstractMap.SimpleEntry<Character, Integer>> rle(String inputStr) {
        List<AbstractMap.SimpleEntry<Character, Integer>> result = new ArrayList<>();
        if (inputStr == null || inputStr.isEmpty()) {
            return result;
        }
        char currentChar = inputStr.charAt(0);
        int currentCount = 1;
        for (int i = 1; i < inputStr.length(); i++) {
            if (inputStr.charAt(i) == currentChar) {
                currentCount++;
            } else {
                result.add(new AbstractMap.SimpleEntry<>(currentChar, currentCount));
                currentChar = inputStr.charAt(i);
                currentCount = 1;
            }
        }
        return result;
    }

}
