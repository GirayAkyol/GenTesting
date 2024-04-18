package my.example.project;

import net.jqwik.api.*;
import net.jqwik.api.arbitraries.SetArbitrary;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.NumericChars;
import net.jqwik.api.constraints.StringLength;

import java.util.AbstractMap;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class MyRLEProperties {
    @Property
        //@Report(Reporting.GENERATED)
    boolean adjacentCharactersAreNotTheSame(@ForAll @StringLength(min = 1, max = 15) String inputStr) {
        List<AbstractMap.SimpleEntry<Character, Integer>> result = MyRLE.rleAllChars(inputStr);
        for (int i = 0; i < result.size() - 1; i++) {
            if (result.get(i).getKey().equals(result.get(i + 1).getKey())) {
                return false;
            }
        }
        return true;
    }

    @Property
    @Report(Reporting.GENERATED)
    boolean adjacentCharactersAreNotTheSameFixed(@ForAll("repeatingStrings") String inputStr) {
        List<AbstractMap.SimpleEntry<Character, Integer>> result = MyRLE.rleFixed(inputStr);
        for (int i = 0; i < result.size() - 1; i++) {
            if (result.get(i).getKey().equals(result.get(i + 1).getKey())) {
                return false;
            }
        }
        return true;
    }

    @Provide
    Arbitrary<String> repeatingStrings() {
        return Arbitraries.strings().numeric().alpha().repeatChars(0.05).ofMinLength(1);
    }


}
