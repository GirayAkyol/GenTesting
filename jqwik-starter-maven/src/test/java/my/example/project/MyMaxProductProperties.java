package my.example.project;

import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.Size;
import org.assertj.core.api.Assertions;
import net.jqwik.api.Arbitraries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

public class MyMaxProductProperties {


    @Property
    void maxProduct2(@ForAll @Size(min = 2, max = 100) List<@IntRange(min = -100, max = 100) Integer> list,
                     @ForAll @IntRange(min = -100, max = 100) int multiplier) {
        Assume.that(!list.contains(0));
        Assume.that(multiplier != 0);
        MyMaxProduct myMaxProduct = new MyMaxProduct();

        int result1 = myMaxProduct.maxProduct(list);

        //mul list with multiplier
        for (int i = 0; i < list.size(); i++) {
            list.set(i, list.get(i) * multiplier);
        }

        int result2 = myMaxProduct.maxProduct(list);
        Assertions.assertThat(result2 / multiplier / multiplier).isEqualTo(result1);
    }

}
