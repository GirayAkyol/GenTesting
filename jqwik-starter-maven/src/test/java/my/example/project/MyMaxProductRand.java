package my.example.project;


import net.jqwik.api.Example;
import org.assertj.core.api.Assertions;

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

        // Generate a random size for the array (at least 2)
        int size = rand.nextInt(10) + 2; // This will generate a random size between 2 and 101

        // Create an array of the generated size
        ArrayList<Integer> array = new ArrayList<Integer>();

        // Fill the array with random integers
        for (int i = 0; i < size; i++) {
            array.add(rand.nextInt(201) - 100);
        }

        MyMaxProduct myMaxProduct = new MyMaxProduct();
        //call the method
        int result1 = myMaxProduct.maxProduct2A(array);

        int multiplier = rand.nextInt(201) - 100;
        if (multiplier == 0) {
            return;
        }
        //mul list with multiplier
        for (int i = 0; i < array.size(); i++) {
            array.set(i, array.get(i) * multiplier);
        }

        int result2 = myMaxProduct.maxProduct2A(array);
        System.out.println(array);
        Assertions.assertThat(result2 / multiplier / multiplier).isEqualTo(result1);


    }

}
