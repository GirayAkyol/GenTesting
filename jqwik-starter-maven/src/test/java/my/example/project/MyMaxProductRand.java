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
        

    }

}
