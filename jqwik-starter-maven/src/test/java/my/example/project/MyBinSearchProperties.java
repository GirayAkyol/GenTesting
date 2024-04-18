package my.example.project;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.DoubleRange;
import net.jqwik.api.constraints.FloatRange;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.Size;
import org.assertj.core.api.Assertions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MyBinSearchProperties {
    @Property
    void testTargetInArray(@ForAll @Size(min = 1) List<@IntRange(min = 0) Integer> array, @ForAll @DoubleRange(min = 0, max = 1) double target) {
        Collections.sort(array);
        int[] arr = array.stream().mapToInt(i -> i).toArray();
        int targetInd = (int) (target * (array.size() - 1));
        int result = new MyBinSearch().binarySearch(arr, array.get(targetInd));

        Assertions.assertThat(result).isNotEqualTo(-1);
    }

}
