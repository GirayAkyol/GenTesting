package my.example.project;


import net.jqwik.api.Example;
import org.assertj.core.api.Assertions;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MyMaxProductRand {

    @Example
    void testMP2() {
        for (int i = 0; i < 50; i++) {
            maxProduct2helper();
        }
    }


    void maxProduct2helper() {
        Random rand = new Random();

        int size = rand.nextInt(10) + 2;
        ArrayList<Integer> array = new ArrayList<Integer>();

        for (int i = 0; i < size; i++) {
            array.add(rand.nextInt(201) - 100);
        }
        MyMaxProduct myMaxProduct = new MyMaxProduct();
        int result1 = myMaxProduct.maxProduct(array);
        System.out.println(array);


        int multiplier = rand.nextInt(201) - 100;
        if (multiplier == 0) {
            return;
        }
        System.out.println(multiplier);
        for (int i = 0; i < array.size(); i++) {
            array.set(i, array.get(i) * multiplier);
        }

        int result2 = myMaxProduct.maxProduct(array);
        System.out.println(array);
        Assertions.assertThat(result2 / multiplier / multiplier).isEqualTo(result1);


    }

}
