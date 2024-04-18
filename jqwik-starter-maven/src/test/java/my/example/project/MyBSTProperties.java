package my.example.project;

import net.jqwik.api.*;
import net.jqwik.api.arbitraries.IntegerArbitrary;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.UseType;
import org.assertj.core.api.Assertions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class MyBSTProperties {

    private boolean checkBST(MyBST myBST, int min, int max) {
        if (myBST == null) {
            return true;
        }
        if (myBST.key < min || myBST.key > max) {
            return false;
        }
        return checkBST(myBST.left, min, myBST.key) && checkBST(myBST.right, myBST.key, max);
    }


    @Property(tries = 10000)
    void testInsert(@ForAll @UseType MyBST myBST, @ForAll @IntRange(min = -100, max = 100) int key) {
        Assertions.assertThat(checkBST(myBST, Integer.MIN_VALUE, Integer.MAX_VALUE)).isTrue();
        Assume.that(myBST != null);
        myBST.insert(key);
        Assertions.assertThat(myBST.search(key)).isTrue();
    }


}
