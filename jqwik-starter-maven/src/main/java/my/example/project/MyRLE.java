package my.example.project;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class MyRLE {
    public static List<AbstractMap.SimpleEntry<Character, Integer>> rleAllChars(String inputStr) {
        List<AbstractMap.SimpleEntry<Character, Integer>> result = new ArrayList<>();
        for (char ch : inputStr.toCharArray()) {
            result.add(new AbstractMap.SimpleEntry<>(ch, 1));
        }
        return result;
    }

    public static List<AbstractMap.SimpleEntry<Character, Integer>> rleFixed(String inputStr) {
        List<AbstractMap.SimpleEntry<Character, Integer>> result = new ArrayList<>();
        char[] chars = inputStr.toCharArray();
        char prev = chars[0];
        int count = 1;
        for (int i = 1; i < chars.length; i++) {
            if (chars[i] == prev) {
                count++;
            } else {
                result.add(new AbstractMap.SimpleEntry<>(prev, count));
                prev = chars[i];
                count = 1;
            }
        }
        result.add(new AbstractMap.SimpleEntry<>(prev, count));
        return result;
    }

}
