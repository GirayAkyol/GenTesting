package my.example.project;

import net.jqwik.api.*;
import org.assertj.core.api.*;

import static java.util.Arrays.*;

class MyAdditionExamples {

    @Example
    void oneAndThree() {
        int result = new MyAddition().add(1, 3);
        Assertions.assertThat(result).isEqualTo(4);
    }

    @Example
    void twoAndTwo() {
        int result = new MyAddition().add(2, 2);
        Assertions.assertThat(result).isEqualTo(4);
    }

    @Example
    void minusOneAndThree() {
        int result = new MyAddition().add(-1, 3);
        Assertions.assertThat(result).isEqualTo(2);
    }


}
