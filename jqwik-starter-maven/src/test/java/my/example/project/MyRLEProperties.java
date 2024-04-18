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
        List<AbstractMap.SimpleEntry<Character, Integer>> result = MyRLE.rle(inputStr);

}